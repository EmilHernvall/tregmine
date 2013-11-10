package info.tregmine.web;

import java.util.List;
import java.io.PrintWriter;

import org.eclipse.jetty.server.Request;

import org.bukkit.Server;
import org.bukkit.OfflinePlayer;

import org.json.JSONWriter;
import org.json.JSONException;

import info.tregmine.Tregmine;
import info.tregmine.WebHandler;
import info.tregmine.api.TregminePlayer;

public class PlayerListAction implements WebHandler.Action
{
    public static class Factory implements WebHandler.ActionFactory
    {
        public Factory()
        {
        }

        @Override
        public String getName()
        {
            return "/playerlist";
        }

        @Override
        public WebHandler.Action createAction(Request request)
        {
            return new PlayerListAction();
        }
    }

    private List<TregminePlayer> players;

    public PlayerListAction()
    {
    }

    @Override
    public void queryGameState(Tregmine tregmine)
    {
        players = tregmine.getOnlinePlayers();
    }

    @Override
    public void generateResponse(PrintWriter writer)
    throws WebHandler.WebException
    {
        try {
            JSONWriter json = new JSONWriter(writer);
            json.array();
            for (TregminePlayer player : players) {
                json.object()
                    .key("id").value(player.getId())
                    .key("name").value(player.getName())
                    .key("rank").value(player.getRank().toString())
                    .key("softwarned").value(player.hasFlag(TregminePlayer.Flags.SOFTWARNED))
                    .key("hardwarned").value(player.hasFlag(TregminePlayer.Flags.HARDWARNED))
                    .key("hidden").value(player.hasFlag(TregminePlayer.Flags.INVISIBLE))
                    .key("playtime").value(player.getTimeOnline())
                    .endObject();
            }
            json.endArray();

            writer.close();
        }
        catch (JSONException e) {
            throw new WebHandler.WebException(e);
        }
    }
}

