package info.tregmine.web;

import static org.bukkit.ChatColor.AQUA;

import java.io.PrintWriter;

import org.bukkit.Server;
import org.eclipse.jetty.server.Request;
import org.json.*;

import info.tregmine.*;
import info.tregmine.api.TregminePlayer;

public class PlayerReloadInventoryAction implements WebHandler.Action
{
    public static class Factory implements WebHandler.ActionFactory
    {
        public Factory()
        {
        }

        @Override
        public String getName()
        {
            return "/reloadinventory";
        }

        @Override
        public WebHandler.Action createAction(Request request)
        throws WebHandler.WebException
        {
            try {
                int subjectId = Integer.parseInt(request.getParameter("subjectId"));
                int issuerId = Integer.parseInt(request.getParameter("issuerId"));
                return new PlayerReloadInventoryAction(subjectId, issuerId);
            }
            catch (NullPointerException e) {
                throw new WebHandler.WebException(e);
            }
            catch (NumberFormatException e) {
                throw new WebHandler.WebException(e);
            }
        }
    }

    private int subjectId;
    private int issuerId;

    private boolean status;
    private String error;

    public PlayerReloadInventoryAction(int subjectId, int issuerId)
    {
        this.subjectId = subjectId;
        this.issuerId = issuerId;

        this.status = true;
        this.error = null;
    }

    @Override
    public void queryGameState(Tregmine tregmine)
    {
        TregminePlayer subject = tregmine.getPlayer(subjectId);
        if (subject == null) {
            status = false;
            error = "Subject not found or not online.";
            return;
        }

        TregminePlayer issuer = tregmine.getPlayerOffline(issuerId);

        if (subject.isOnline()) {
            subject.loadInventory(subject.getCurrentInventory(), false);
        }

        Tregmine.LOGGER.info(subject.getRealName() + "'s inventory was reloaded by " +
                             issuer.getRealName() + " (from web)");

        Server server = tregmine.getServer();
        server.broadcastMessage(issuer.getRealName() + AQUA + " reloaded "
                + subject.getRealName() + AQUA + "'s inventory");
    }

    @Override
    public void generateResponse(PrintWriter writer)
    throws WebHandler.WebException
    {
        try {
            JSONWriter json = new JSONWriter(writer);
            json.object()
                .key("status").value(status ? "ok" : "error")
                .key("error").value(error)
                .endObject();

            writer.close();
        }
        catch (JSONException e) {
            throw new WebHandler.WebException(e);
        }
    }
}
