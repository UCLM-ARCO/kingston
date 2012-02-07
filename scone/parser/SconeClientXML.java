package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.log4j.Logger;

//import edu.cmu.scone.edit.FocusSearchResult;
//import edu.cmu.scone.edit.Highlight;



/**
 * 
 * 
 * @author Vasco Pedro, changes by Tim Isganitis and Alicia Tribble
 * @date 9/13/05
 * @date 12/01/09 Changed made by Maria J. Santofimia
 * 
 * Changes:
 * 
 * 28 Feb: (akt) Added a function to strip the namespaces from element names:
 * removeSconeNamespaces().  
 *          
 * 
 * This is essentially the client code that Vasco wrote with a few minor
 * changes. The functions return Java data structures instead of strings of xml.
 * Exceptions are thrown in error cases. Instead of reading in the connection
 * parameters from a file, they are specified in the constructor. A few
 * functions have been added, and some names changed.
 * 
 */
public class SconeClientXML implements SconeClient {

	private static final Logger log = Logger.getLogger(SconeClientXML.class);

	private static final String SERVER_PROMPT = "[PROMPT]";
	private static final String NEWLINE = "\n";

	// Server Info:
	private String serverName;
	private int portNum;
	
	// Server Mechanics:
	private Socket SconeSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	/* ***** CONSTRUCTORS ***** */
	
	/**
	 * Establishes a connection with a Scone server on the designated machine
	 * that is listening on the specified port. If the client fails to connect
	 * to the server it throws a SconeClientException specifying the error
	 * encountered.
	 * 
	 * @param serverName
	 *            The name of the machine on which a Scone server is running
	 * @param portNum
	 *            The port on which the Scone server is listening on that
	 *            machine
	 */
	public SconeClientXML(String serverName, int portNum) throws SconeClientException {
		this.serverName = serverName;
		this.portNum = portNum;
		
		log.debug("Connecting to " + serverName + ":" + portNum);
		try {
			SconeSocket = new Socket(serverName, portNum);
			out = new PrintWriter(SconeSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(SconeSocket.getInputStream()));
		} catch (UnknownHostException e) {
			String errMsg = "Could not find host: " + serverName;
			log.debug(errMsg);
			throw new SconeClientException(errMsg, e);
		} catch (IOException e) {
			String errMsg = "Could not establish I/O for the connection to: " + serverName; 
			log.debug(errMsg);
			throw new SconeClientException(errMsg, e);
		}
		try {
			// Read remaining finger response
			String line = in.readLine();
			while(!line.equals("[PROMPT]")) {
				log.debug(line);
				// Read next line
				line = in.readLine();
			}
		} catch (Exception e) {
			log.debug(e);
			throw new SconeClientException(e);
		}
		log.debug("Connection Established");
	}

	/* ***** METHODS ***** */
	
	/**
	 * Sends a disconnect message to the server and closes the connection and
	 * all readers and writers associated with the connection.
	 *  
	 */
	public synchronized void close() {
		try {
			out.println("<request><disconnect/></request>");
			// Read response from socket
			String line = in.readLine();
			log.debug(line);
		} catch (Exception e) {
			log.error(e);
		}
		try {
			out.close();
			in.close();
			SconeSocket.close();
		} catch (Exception e) {
			log.error(e);
		}
		log.debug("Disconnected from " + serverName + ":" + portNum);
	}

	public String evalLisp(String lispExp) throws SconeException, SconeClientException {
		String XMLEvalLisp = "<eval-lisp>" + lispExp + "</eval-lisp>";
		return sendReceive(XMLEvalLisp);
	}

	/**
	 * Given an XML formatted lisp command, wraps the command so it will be
	 * understood by the SCONE server, sends the command and returns the result.
	 * 
	 * TODO Eventually we will want to be passing DOMs instead of strings, but
	 * this can wait.
	 * 
	 * @param XMLFuncCall
	 * @return
	 */
	private synchronized String sendReceive(String XMLFuncCall) throws SconeException, SconeClientException {
		String XMLRequest = "<request session='ABC' password='XYZ'>"
				+ XMLFuncCall + "</request>";
		
		// The Scone server will only read until it finds a newline, at which
		// point it attempts to parse and execute what it has received.  We
		// must therefore remove all newlines from the xml <request> that we
		// are sending.
		XMLRequest = XMLRequest.replace(NEWLINE, " ");

		log.debug("Sending : " + XMLRequest);
		String result = "";
		try {
			out.println(XMLRequest);

			// Read response from socket
			String line = in.readLine();
			while (!line.equals(SERVER_PROMPT)) {
				result += line;
				// Read next line
				line = in.readLine();
			}
		} catch (Exception e) {
			log.debug(e);
			throw new SconeClientException(e);
		}
		/*log.debug("Received: " + result);
		if(result.contains("ERROR")) {
			throw new SconeException(result);
		}
		result = CleanSconeNamespaces(result);
		log.debug("Cleaned: "+ result);*/
		return result;
	}

	private String CleanSconeNamespaces(String str){
		String res;
		res = str.replaceAll("[a-z]+:", "");
		res = res.replaceAll("\\([0-9]+\\)", "");
		
		return res;
	}
	
	
}
