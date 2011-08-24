package info.tregmine.client.playerlist;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("playerlist")
public interface PlayerListService extends RemoteService {
	String greetServer(String names) throws IllegalArgumentException;
}
