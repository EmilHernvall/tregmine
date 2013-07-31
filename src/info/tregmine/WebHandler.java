package info.tregmine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import info.tregmine.api.util.Base64;

public class WebHandler extends AbstractHandler implements Runnable
{
    public static class WebException extends Exception
    {
        private int responseCode;

        public WebException(String message, int responseCode)
        {
            super(message);

            this.responseCode = responseCode;
        }

        public WebException(Throwable t)
        {
            super(t);

            this.responseCode = 500;
        }


        public int getResponseCode() { return responseCode; }
    }

    public interface ActionFactory
    {
        public String getName();
        public Action createAction(Request request) throws WebException;
    }

    public interface Action
    {
        public void queryGameState(Tregmine tregmine);
        public void generateResponse(PrintWriter writer) throws WebException;
    }

    private Tregmine tregmine;

    private Map<String, ActionFactory> actions;

    private Queue<Action> queue;

    private String key;

    public WebHandler(Tregmine tregmine, String key)
    {
        this.tregmine = tregmine;

        this.actions = new HashMap<String, ActionFactory>();
        this.queue = new ConcurrentLinkedQueue<Action>();

        this.key = key;
    }

    public void addAction(ActionFactory factory)
    {
        actions.put(factory.getName(), factory);
    }

    private static String hmac(String key, String msg)
    throws NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] result = mac.doFinal(msg.getBytes());

        return Base64.encode(result);
    }

    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        //System.out.println("Servlet path: " + baseRequest.getServletPath());
        //System.out.println("Path info: " + baseRequest.getPathInfo());

        baseRequest.setHandled(true);
        response.setContentType("application/json;charset=utf-8");

        String signingStr = request.getPathInfo();
        if (request.getQueryString() != null) {
            signingStr += "?" + request.getQueryString();
        }

        try {
            System.out.println(signingStr);
            String auth = hmac(key, signingStr);
            String authCmp = request.getHeader("Authorization");
            // TODO: Possible timing sidechannel?
            if (!auth.equals(authCmp)) {
                Tregmine.LOGGER.info("Web: " + signingStr + " FAILED");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else {
                Tregmine.LOGGER.info("Web: " + signingStr + " OK");
            }
        }
        catch (NoSuchAlgorithmException e) {
            throw new ServletException(e);
        }
        catch (InvalidKeyException e) {
            throw new ServletException(e);
        }

        // Look up appropriate action factory for this request
        try {
            ActionFactory factory = actions.get(baseRequest.getPathInfo());
            if (factory == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Create an action, and add it to work queue
            Action action = factory.createAction(baseRequest);
            queue.offer(action);

            // Wait for bukkit to process it
            synchronized (action) {
                action.wait();
            }

            // Prepare response
            response.setStatus(HttpServletResponse.SC_OK);

            // Generate response
            PrintWriter writer = response.getWriter();
            action.generateResponse(writer);
        }
        catch (InterruptedException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (WebException e) {
            response.setStatus(e.getResponseCode());
        }
        catch (Throwable e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void run()
    {
        Action action = null;
        while ((action = queue.poll()) != null) {
            synchronized (action) {
                action.queryGameState(tregmine);
                action.notify();
            }
        }
    }
}
