package info.tregmine.web;

import java.util.Set;
import java.io.PrintWriter;

import org.eclipse.jetty.server.Request;

import org.bukkit.Server;
import org.bukkit.OfflinePlayer;

import info.tregmine.Tregmine;
import info.tregmine.WebHandler;

public class OperatorListAction implements WebHandler.Action
{
    public static class Factory implements WebHandler.ActionFactory
    {
        public Factory()
        {
        }

        @Override
        public String getName()
        {
            return "/oplist";
        }

        @Override
        public WebHandler.Action createAction(Request request)
        {
            return new OperatorListAction();
        }
    }

    private Set<OfflinePlayer> ops;

    public OperatorListAction()
    {
    }

    @Override
    public void queryGameState(Tregmine tregmine)
    {
        Server server = tregmine.getServer();
        ops = server.getOperators();
    }

    @Override
    public void generateResponse(PrintWriter writer)
    throws WebHandler.WebException
    {
        for (OfflinePlayer op : ops) {
            writer.println(op.getName() + "<br />");
        }
    }
}

