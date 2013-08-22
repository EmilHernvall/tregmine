package info.tregmine.web;

import java.io.PrintWriter;
import java.util.Random;
import java.util.Map;

import org.eclipse.jetty.server.Request;

import org.bukkit.Server;

import org.json.JSONWriter;
import org.json.JSONException;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.WebHandler;

public class AuthAction implements WebHandler.Action
{
    public static class Factory implements WebHandler.ActionFactory
    {
        public Factory()
        {
        }

        @Override
        public String getName()
        {
            return "/auth";
        }

        @Override
        public WebHandler.Action createAction(Request request)
        throws WebHandler.WebException
        {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                return new AuthAction(id);
            }
            catch (NullPointerException e) {
                throw new WebHandler.WebException(e);
            }
            catch (NumberFormatException e) {
                throw new WebHandler.WebException(e);
            }
        }
    }

    private int playerId;
    private boolean found = false;
    private String token = null;

    public AuthAction(int playerId)
    {
        this.playerId = playerId;
    }

    private String generateToken()
    {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 32; i++) {
            buffer.append(String.format("%02X", rand.nextInt(0xFF)));
        }

        return buffer.toString();
    }

    @Override
    public void queryGameState(Tregmine tregmine)
    {
        Map<String, TregminePlayer> authTokens = tregmine.getAuthTokens();

        // look for existing tokens
        for (Map.Entry<String, TregminePlayer> entry : authTokens.entrySet()) {
            String currentToken = entry.getKey();
            TregminePlayer currentPlayer = entry.getValue();
            if (currentPlayer.getId() == playerId) {
                token = currentToken;
                found = true;
                Tregmine.LOGGER.info("Restoring token " + token + " to " +
                                     currentPlayer.getChatName());
                return;
            }
        }

        // otherwise, retrieve player from db...
        TregminePlayer player = tregmine.getPlayerOffline(playerId);
        if (player == null) {
            found = false;
            return;
        }

        // and generate a new token
        token = generateToken();
        authTokens.put(token, player);
        found = true;

        Tregmine.LOGGER.info("Assigned token " + token + " to " + player.getChatName());
    }

    @Override
    public void generateResponse(PrintWriter writer)
    throws WebHandler.WebException
    {
        try {
            JSONWriter json = new JSONWriter(writer);

            if (found) {
                json.object()
                    .key("found").value(found)
                    .key("token").value(token)
                    .endObject();
            } else {
                json.object()
                    .key("found").value(found)
                    .endObject();
            }

            writer.close();
        }
        catch (JSONException e) {
            throw new WebHandler.WebException(e);
        }
    }
}
