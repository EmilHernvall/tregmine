package info.tregmine.client;


import info.tregmine.client.playerlist.PlayerListService;
import info.tregmine.client.playerlist.PlayerListServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
//import com.google.gwt.event.dom.client.KeyUpEvent;
//import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;


public class Tregmine_info implements EntryPoint {

	private final PlayerListServiceAsync greetingService = GWT.create(PlayerListService.class);
	//	final MultiWordSuggestOracle playerlist = new MultiWordSuggestOracle();
	final MultiWordSuggestOracle playerlist = new MultiWordSuggestOracle();  

	public void onModuleLoad() {

		HorizontalPanel Hplayers = new HorizontalPanel();
		FlowPanel playerslist = new FlowPanel();
		playerslist.setHeight("600px");
		final SuggestBox playerbox = new SuggestBox(playerlist);
		final HTML text = new HTML("Hi");


		playerbox.setFocus(true);
		playerbox.setText("Enter name here");
		playerbox.setLimit(15);


		playerbox.addKeyDownHandler(new KeyDownHandler(){
			public void onKeyDown(KeyDownEvent event) {
				//				playerlist.clear();


				greetingService.greetServer(playerbox.getText(), new com.google.gwt.user.client.rpc.AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());	
					}

					
					public void onSuccess(String result) {
						text.setHTML( result.replace(",", "<br/>") );
					}
				});
				playerbox.showSuggestionList();
			}
		});



		playerbox.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				//                Window.alert("Changed");
			}
		});

		FlowPanel playersinfo = new FlowPanel();
		playersinfo.setHeight("600px");


		Hplayers.setBorderWidth(10);
		Hplayers.add(playerslist);
		playerslist.add(playerbox);
		playerslist.add(text);



		TabPanel tp = new TabPanel();
		tp.setAnimationEnabled(true);
		tp.setSize("100%", "100%");


		tp.add(Hplayers, "Players");

		tp.add(new HTML("Warps"), "Warps");
		tp.add(new HTML("Baz"), "Chat log");
		tp.add(new HTML("settings"), "Server settings");
		tp.add(new HTML("log"), "Server log");
		tp.add(new HTML("Baz"), "Plugins");
		tp.add(new HTML("other"), "Other");





		// Show the 'bar' tab initially.
		tp.selectTab(0);

		// Add it to the root panel.
		RootPanel.get().add(tp);
	}
}
