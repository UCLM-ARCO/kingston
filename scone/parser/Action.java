package parser;

import java.lang.String;
import java.util.*;
import java.io.*;


public class Action{

    public String actor;
    public String pointOfInterest;
    public String actionName;
    public SconeFacade sfObject;
  
    public Action requiredAction;
    
    public Action(String actionNameInputArg, String actorNameInputArg){
	actor = actorNameInputArg;
	actionName = actionNameInputArg;

	// @DEBUGGING
	System.out.println("At Action constructor with arguments "+actor+" "+actionName);
	// @DEBUGGING

	sfObject = new SconeFacade();
	setPoI();
	setRequiredAction();
	requiredAction.actor = actor;
		
    }

    public Action(String actionNameInputArg){
	sfObject = new SconeFacade();
	actionName = actionNameInputArg;

    }
    public Action(){
	sfObject = new SconeFacade();

    }

    public Vector getExpectationsOfAction(){
	String retrievedExpectations = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
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

    public void getRequiredAction(){
	String checkExistingRequirements = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
	requiredAction = new Action(sfObject.stripRequiredAction(checkExistingRequirements), actor);
    }
    
    public void setPoI(){
	
	// @DEBUGGING
	System.out.println("(incoming-a-wires (lookup-element {"+actionName+"}))");
	// @DEBUGGING
	String queryToGetPoI = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
	
	// @DEBUGGING
	System.out.println("Seting up the PoI: "+queryToGetPoI+"\n");
	// @DEBUGGING
	pointOfInterest = sfObject.stripPoI(queryToGetPoI);
    }

    public void setRequiredAction(){
	
	// @DEBUGGING
	System.out.println("(incoming-a-wires (lookup-element {"+actionName+"}))");
	// @DEBUGGING
	String queryToGetRequiredAction = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
	requiredAction = new Action(sfObject.stripRequiredAction(queryToGetRequiredAction));	
	requiredAction.actor = actor;
	// @DEBUGGING
	System.out.println("Seting up the required action: "+requiredAction.actionName+"\n");
	// @DEBUGGING

    }
    
    public boolean equals(Action actionToCheck){
	if(actionName.equals(actionToCheck.actionName))
	    return true;
	else
	    return false;
    }

    
    public boolean isActionTheFirstActionToComeInExpectation(Expectation currentExpectation){

	currentExpectation.setActionsToCome(this);
	
	if(equals(((Action)currentExpectation.actionsToCome.elementAt(0))))
	    return true;
	else
	    return false;

    }
    
    public boolean hasRequiredAction(){
	if(requiredAction.actionName !=null)
	    return true;
	else
	    return false;
    }


}