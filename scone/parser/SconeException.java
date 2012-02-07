package parser;

/**
 * An exception thrown when and error is returned by the Scone server.
 * 
 * @author tisganit
 *
 */
public class SconeException extends Exception {

	public SconeException() {
		super();
	}
	
	public SconeException(String errorMessage) {
		super(errorMessage);
	}
	
	public SconeException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}
	
	public SconeException(Throwable cause) {
		super(cause);
	}
}
