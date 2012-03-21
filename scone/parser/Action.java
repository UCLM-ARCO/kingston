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
	    
	    if(retrievedExpectations.indexOf(retrievedExpectations.substring(startOfExpectation, endOfExpectation)) < startOfExpectation){
		// @DEBUGGING
		System.out.println("Nombre de expectation: "+ retrievedExpectations.substring(startOfExpectation, endOfExpectation)+" ya ha aparecido\n\n");
		// @DEBUGGING
	    }
	    else
		expectations.add(new Expectation(retrievedExpectations.substring(startOfExpectation, endOfExpectation)));
	    startOfExpectation = retrievedExpectations.indexOf(separator, endOfExpectation);
	    endOfExpectation =  retrievedExpectations.indexOf('(', startOfExpectation)-1;
	    
	}
	
	
	return expectations;
    }

    public void getRequiredAction(){
	String checkExistingRequirements = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
	requiredAction = new Action(sfObject.stripRequiredAction(checkExistingRequirements), actor);
    }
    
    public void setPoI(){
	
	String queryToGetPoI = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
	
	pointOfInterest = sfObject.stripPoI(queryToGetPoI);
    }

    public void setRequiredAction(){
	
	String queryToGetRequiredAction = sfObject.sendToScone("(incoming-a-wires (lookup-element {"+actionName+"}))");
	requiredAction = new Action(sfObject.stripRequiredAction(queryToGetRequiredAction));	
	requiredAction.actor = actor;
	
    }
    
    public boolean equals(Action actionToCheck){
	if(actionName.equals(actionToCheck.actionName))
	    return true;
	else
	    return false;
    }
    
    public boolean isActionTheFirstActionToComeInExpectation(Expectation currentExpectation){
	if(currentExpectation.includesAction(this)){
	    currentExpectation.setActionsToCome(this);
	    // @DEBUGGING
	    System.out.println("Hola \n");
	    // @DEBUGGING
	    if(equals(((Action)currentExpectation.actionsToCome.elementAt(0))))
		return true;
	}
	return false;

    }
    
    public boolean hasRequiredAction(){
	if(requiredAction.actionName !=null)
	    return true;
	else
	    return false;
    }


}