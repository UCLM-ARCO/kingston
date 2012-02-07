package  parser;

import java.util.Vector;

//import edu.cmu.scone.edit.FocusSearchResult;
//import edu.cmu.scone.edit.Highlight;



/**
 * Definition of the interface between the SCONE viewer GUI and the actual
 * implementation of communication with the SCONE server.
 * 
 * @author Tim Isganitis (tisganit@cs.cmu.edu)
 * @date 12/01/09 changed made by Maria J. Santofimia
 */
public interface SconeClient {

	public static final int PLAIN_TEXT = 0;
	public static final int SCONE_INAME = 1;

	/**
	 * This method must terminate the connection with the server and clean up
	 * any data structures used by the SconeClient.  It is expected that the
	 * SconeClient object will be deleted after this call.
	 * 
	 */
	public void close();

	/**
	 * Sends a request to the Scone server to evaluate the given LISP
	 * expression. Returns whatever result is produced by the server.
	 * 
	 * @param lispExp
	 *            A well-formatted lisp expression
	 * @return The result of the expression evaluated by the server.
	 * @throws SconeException
	 * @throws SconeClientException
	 */
	public String evalLisp(String lispExp) throws SconeException, SconeClientException;
	
	/**
	 * Returns the types of searches that produce a <focus-element> result that
	 * can be perfomed by the server. Such searches are run using the
	 * focusSearch method by supplying the name returned by this method as the
	 * reqType argument.
	 * 
	 * @return The names of the search types.
	 * @throws SconeException
	 * @throws SconeClientException
	 */
    //	public Vector<String> getFocusSearchTypes() throws SconeException, SconeClientException;	
	
	/**
	 * Runs a search for the given element and returns a result that can be used
	 * to build a list view and a graph view.
	 * 
	 * @param iname
	 *            The name of the element to search for.
	 * @param inameType
	 *            The type of this name. Can either be PLAIN_TEXT or a
	 *            SCONE_INAME.
	 * @param reqType
	 *            The type of search to perform. The valid type names for a
	 *            given server can be retrieved with the getFocusSearchTypes
	 *            method.
	 * @return
	 * @throws SconeException
	 * @throws SconeClientException
	 */
    //	public FocusSearchResult focusSearch(String iname, int inameType, String reqType) throws SconeException, SconeClientException;
	
	/**
	 * Sends a string of text to the server. The server identifies words that
	 * correspond to SCONE elements and returns (offset, length) annotations
	 * (see Highlight class definition).
	 * 
	 * @param text
	 *            The text to search.
	 * @return The set of annotations of identified Scone elements.
	 * @throws SconeException
	 * @throws SconeClientException
	 */
    //	public Vector<Highlight> highlightSconeElements(String text) throws SconeException, SconeClientException;
}
