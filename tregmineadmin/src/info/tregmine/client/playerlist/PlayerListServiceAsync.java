package info.tregmine.client.playerlist;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerListServiceAsync {
	void greetServer(String names, AsyncCallback<String> asyncCallback)
			throws IllegalArgumentException;
}
