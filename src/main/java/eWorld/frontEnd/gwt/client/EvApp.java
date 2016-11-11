package eWorld.frontEnd.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EvApp implements EntryPoint {

	/** globally used for server requests */
	public static final DataCache REQ = new DataCache();
	
	/** globally used to show some info */
	public static final EvInfoPanel INFO = new EvInfoPanel();
	
	
	private EvUserInterface eWorld = new EvUserInterface();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    
	    // Associate the EvWorld widget with the HTML host page.
	    RootPanel.get("eWorld").add(eWorld);
	    
	}
	
}
