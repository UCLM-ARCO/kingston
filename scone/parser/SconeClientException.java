package parser;

/**
 * An exception thrown when and error occurs in the Scone Client.
 * 
 * @author tisganit
 *
 */
public class SconeClientException extends Exception {

	public SconeClientException() {
		super();
	}
	
	public SconeClientException(String errorMessage) {
		super(errorMessage);
	}
	
	public SconeClientException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}
	
	public SconeClientException(Throwable cause) {
		super(cause);
	}
}
