package parser;

import java.lang.String;
import java.util.*;
import java.io.*;


public class Expectation
{
    public String expectationName;
    public Vector actionsInExpectation;
    public Vector actionsToCome;
    public SconeFacade sfObject;

    public Expectation(String en){
	expectationName = en;

	actionsInExpectation = new Vector();
	//actionsInExpectation = getActionsInExpectations();
       
	actionsToCome = new Vector();
	//actionsToCome =  getActionsInExpectations();

	sfObject = new SconeFacade();
    }
    
    public Expectation(){
	sfObject = new SconeFacade();
	actionsInExpectation = new Vector();
	//actionsInExpectation = getActionsInExpectations();
	
	actionsToCome = new Vector();
	//actionsToCome =  getActionsInExpectations();
		
    }
    public Vector getActionsInExpectations(){
	
	// @DEBUGGING
	System.out.println("List all the actions involved in the expectation\n");
	System.out.println("(show-all-x-of-y  {has expectation} {"+expectationName+"})");
	// @DEBUGGING

	String actionsInvolvedInExpectation = sfObject.sendToScone("(list-all-x-of-y  {has expectation} {"+expectationName+"})");
	// @DEBUGGING
	System.out.println("Actions involved in expectation: "+actionsInvolvedInExpectation);
	// @DEBUGGING
	
	actionsInExpectation = new Vector();
	String separator = "{events:";
	
	int startOfSeparator = actionsInvolvedInExpectation.indexOf(separator);
	int endOfSeparator =  actionsInvolvedInExpectation.indexOf('}');
	
	while(startOfSeparator > -1){
	    startOfSeparator +=  separator.length();
	    actionsInExpectation.add(new Action(actionsInvolvedInExpectation.substring(startOfSeparator, endOfSeparator)));
	    startOfSeparator = actionsInvolvedInExpectation.indexOf(separator, endOfSeparator);
	    endOfSeparator = actionsInvolvedInExpectation.indexOf('}', startOfSeparator);
	}
	return actionsInExpectation;
    }

    public boolean setActionsToCome(Action currentAction){
		
	///actionsToCome
	Vector tempActions  = getActionsInExpectations();

	// @DEBUGGING
	System.out.println("Previous action was: "+ currentAction.actionName);
	// @DEBUGGING

	for(int actionsToComeIterator = tempActions.size(); actionsToComeIterator > 0; actionsToComeIterator--){
	    if(!(((Action)tempActions.elementAt(actionsToComeIterator-1)).actionName.equals(currentAction.actionName))){
		// @DEBUGGING
		System.out.println("Action already occurred: "+ ((Action)tempActions.elementAt(actionsToComeIterator-1)).actionName);
		// @DEBUGGING
		tempActions.remove(((Action)tempActions.elementAt(actionsToComeIterator-1)));
	    }
	    else{
		// @DEBUGGING
		System.out.println("Found the starting point at action: "+ ((Action)tempActions.elementAt(actionsToComeIterator-1)).actionName);
		// @DEBUGGING
		
		for(int i = tempActions.size(); i > 0; i--)
		    actionsToCome.add(tempActions.elementAt(i-1));

		return true;
	    }
	    
	} 
	return false;
	
    }

    public boolean isActionInExpectation(Action currentAction){
	actionsInExpectation = getActionsInExpectations();
	
	for(int i=0; i < actionsInExpectation.size(); i++)
	    if(((Action)actionsInExpectation.elementAt(i)).actionName.equals(currentAction.actionName))
		return true;
	return false;
    }
    

    public boolean isActionInActionsToCome(Action currentAction){
	
	for(int i=0; i < actionsToCome.size(); i++)
	    if(((Action)actionsToCome.elementAt(i)).actionName.equals(currentAction.actionName))
		return true;
	return false;
    }
    
    public boolean equalsTo(Expectation scdExpectation){
	if(expectationName.equals(scdExpectation.expectationName))
	    return true;
	else
	    return false;
    }
    public boolean isInExpectationSet(Vector expectationSet){
	for(int i = 0; i < expectationSet.size(); i++)
	    if(((Expectation)expectationSet.elementAt(i)).equalsTo(this))
		return true;
	return false;
    }
    
    public boolean includesAction(Action actionToCheck){
	for(int i=0; i < actionsInExpectation.size(); i++)
	    if(((Action)actionsInExpectation.elementAt(i)).actionName.equals(actionToCheck.actionName))
		return true;
	return false;

    }
    public boolean isStillActive(){
	if(actionsToCome.size()>0)
	    return true;
	
	else
	    return false;
	
    }

}

