package info.tregmine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class WebHandler extends AbstractHandler implements Runnable
{
    public class WebException extends Exception
    {
        private int responseCode;

        public WebException(String message, int responseCode)
        {
            super(message);

            this.responseCode = responseCode;
        }

        public int getResponseCode() { return responseCode; }
    }

    public interface ActionFactory
    {
        public String getName();
        public Action createAction(Request request);
    }

    public interface Action
    {
        public void queryGameState(Tregmine tregmine);
        public void generateResponse(PrintWriter writer) throws WebException;
    }

    private Tregmine tregmine;

    private Map<String, ActionFactory> actions;

    private Queue<Action> queue;

    public WebHandler(Tregmine tregmine)
    {
        this.tregmine = tregmine;

        this.actions = new HashMap<String, ActionFactory>();
        this.queue = new ConcurrentLinkedQueue<Action>();
    }

    public void addAction(ActionFactory factory)
    {
        actions.put(factory.getName(), factory);
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
        response.setContentType("text/html;charset=utf-8");

        // Look up appropriate action factory for this request
        ActionFactory factory = actions.get(baseRequest.getPathInfo());
        if (factory == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
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
