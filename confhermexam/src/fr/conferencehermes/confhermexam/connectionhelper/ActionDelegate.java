package fr.conferencehermes.confhermexam.connectionhelper;


/**
 * Provides callback methods for activities to render or to act on ready
 * situations
 * 
 * @author aabraham
 * 
 */
public interface ActionDelegate {

	// --------------------- Service Response Delegates -------------//

	public void didFinishRequestProcessing();

	public void didFailRequestProcessing();

}
