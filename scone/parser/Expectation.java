package parser;

import java.lang.String;
import java.util.*;
import java.io.*;


public class Expectation
{
    static final String server = "localhost";
    static final int port=5000;
    public Vector activeExpectations;
    
    public Expectation(String expectationName){
	sendToScone(expectationName);
	activeExpectations = new Vector();
    }

    public Expectation(){
	activeExpectations = new Vector();
     }

    public Vector parseExpectations(String actionToAssert){
	String retrievedExpectations = sendToScone("(incoming-a-wires (lookup-element {"+actionToAssert+"}))");
	Vector expectations = new Vector();
	
	// Now parse the response to separate each of the provided expectations
	String separator = "is the has expectation of ";
	
	int startOfExpectation = retrievedExpectations.indexOf(separator);//+ separator.length();
	int endOfExpectation =  retrievedExpectations.indexOf('(', startOfExpectation)-1;
	
	while(startOfExpectation > -1){
	    // @DEBUGGING
	    startOfExpectation += separator.length();
	    System.out.println("Inside: "+retrievedExpectations.substring(startOfExpectation, endOfExpectation));
	    // @DEBUGGING
	    
	    expectations.add(retrievedExpectations.substring(startOfExpectation, endOfExpectation));
	    startOfExpectation = retrievedExpectations.indexOf(separator, endOfExpectation);
	    endOfExpectation =  retrievedExpectations.indexOf('(', startOfExpectation)-1;
	    
	}
	
	// @DEBUGGING
	System.out.println("After parsing expectations: "+expectations+"\n");
	// @DEBUGGING
	
	
	return expectations;
    }
    
    public Vector getActiveExpectations(String actionToAssert){
	return parseExpectations(actionToAssert);
    }

    public Vector getExpectations(){
	Vector markedExpectations = new Vector();
	Vector a_MarkedExpectations = new Vector();
	Vector b_MarkedExpectations = new Vector();

	return markedExpectations;
    }

    public Vector getCommonSetOfExpectations(Vector currentExpectations, Vector expectationForCurrentAction){
	Vector commonExpectations = new Vector();
	commonExpectations = currentExpectations;
	if(currentExpectations.size()>0){
	    for(int currentExpectationsIterator =0; currentExpectationsIterator< currentExpectations.size(); currentExpectationsIterator++){
		for(int expectationForCurrentActionIt=0; expectationForCurrentActionIt < expectationForCurrentAction.size(); expectationForCurrentActionIt++){
		    System.out.println("Checking expectations: "+ ((String)currentExpectations.elementAt(currentExpectationsIterator))+ " "+  ((String)expectationForCurrentAction.elementAt(expectationForCurrentActionIt)));
		    if(((String)currentExpectations.elementAt(currentExpectationsIterator)) == ((String)expectationForCurrentAction.elementAt(expectationForCurrentActionIt)))
			commonExpectations.add(((String)currentExpectations.elementAt(currentExpectationsIterator)));
		    
		}
	    }
	    
	    return commonExpectations;
	}
	else
	    return expectationForCurrentAction;
	
    }


    public Vector getActionsToCome(String expectationName){
	
	// @DEBUGGING
	System.out.println("List all the actions involved in the expectation\n");
	System.out.println("(show-all-x-of-y  {has expectation} {"+expectationName+"})");
	
	// @DEBUGGING
	String actionsInvolvedInExpectation = sendToScone("(list-all-x-of-y  {has expectation} {"+expectationName+"})");
	return parseAction(actionsInvolvedInExpectation);
    }

    public Vector parseAction(String actionsInvolvedInExpectation){
	Vector parsedActions = new Vector();
	String separator = "{events:";

	int startOfSeparator = actionsInvolvedInExpectation.indexOf(separator);
	int endOfSeparator =  actionsInvolvedInExpectation.indexOf('}');
	while(startOfSeparator > -1){
	    startOfSeparator +=  separator.length();
	    parsedActions.add(actionsInvolvedInExpectation.substring(startOfSeparator, endOfSeparator));
	    startOfSeparator = actionsInvolvedInExpectation.indexOf(separator, endOfSeparator);
	    endOfSeparator = actionsInvolvedInExpectation.indexOf('}', startOfSeparator);
	}
	return parsedActions;
    }

    
    public String sendToScone(String query){
	String result = "nil";
	
	try{
	    SconeClientXML sc = new SconeClientXML(server, port);
	    try{
		result = sc.evalLisp(query);
	    }catch(Exception e){
		result = e.toString();
		System.out.println(e);
	    }
	    sc.close();
	}catch(Exception e){
	    result = e.toString();
	    System.out.println(e);
	}
	System.out.println(result);
	return result;
    }
    
}

