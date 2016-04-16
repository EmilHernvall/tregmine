package info.tregmine.web;

import static org.bukkit.ChatColor.AQUA;

import java.io.PrintWriter;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.eclipse.jetty.server.Request;
import org.json.JSONException;
import org.json.JSONWriter;

import info.tregmine.Tregmine;
import info.tregmine.WebHandler;
import info.tregmine.api.Notification;
import info.tregmine.api.PlayerReport;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerReportDAO;

public class PushNotificationAction implements WebHandler.Action
{
    public static class Factory implements WebHandler.ActionFactory
    {
        public Factory()
        {
        }

        @Override
        public String getName()
        {
            return "/push";
        }

        @Override
        public WebHandler.Action createAction(Request request)
        throws WebHandler.WebException
        {
            try {
            	int pushtofrom = Integer.parseInt(request.getParameter("pushTo"));
            	int pushfromfrom = Integer.parseInt(request.getParameter("pushFrom"));
                String typefrom = request.getParameter("type");
                return new PushNotificationAction(pushtofrom, pushfromfrom, typefrom);
            }
            catch (NullPointerException e) {
                throw new WebHandler.WebException(e);
            }
            catch (NumberFormatException e) {
                throw new WebHandler.WebException(e);
            }
        }
    }

    private int pushto;
    private int pushfrom;
    private String type;

    private boolean status;
    private String error;

    public PushNotificationAction(int pushtoget, int pushfromget, String typeget)
    {
        this.pushto = pushtoget;
        this.pushfrom = pushfromget;
        this.type = typeget;

        this.status = true;
        this.error = null;
    }

    @Override
    public void queryGameState(Tregmine tregmine)
    {
        TregminePlayer subject = tregmine.getPlayer(pushto);
        if (subject == null) {
            status = false;
            this.error = "Player is offline";
            return;
        }

        TregminePlayer issuer = tregmine.getPlayer(pushfrom);
        if (issuer == null) {
            issuer = tregmine.getPlayerOffline(pushfrom);
        }
        if(type == "mail"){
        	subject.sendMessage("%internal%" + ChatColor.AQUA + "You got an e-mail from " + issuer.getName() + "! Type /mail read to view it.");
        	subject.sendNotification(Notification.MAIL);
            Tregmine.LOGGER.info(subject.getChatName() + " got an e-mail from " + issuer.getName());
        }
        
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
