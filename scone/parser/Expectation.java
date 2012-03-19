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
	    // @DEBUGGING
	    System.out.println("Asserting action: "+ actionsInvolvedInExpectation.substring(startOfSeparator, endOfSeparator));
	    // @DEBUGGING

	    actionsInExpectation.add(new Action(actionsInvolvedInExpectation.substring(startOfSeparator, endOfSeparator)));
	    startOfSeparator = actionsInvolvedInExpectation.indexOf(separator, endOfSeparator);
	    endOfSeparator = actionsInvolvedInExpectation.indexOf('}', startOfSeparator);
	}
	return actionsInExpectation;
    }

    public void setActionsToCome(Action currentAction){
		
	actionsToCome = getActionsInExpectations();
	if(isActionInExpectation(currentAction)){
	    for(int actionsToComeIterator = 0; actionsToComeIterator < actionsToCome.size(); actionsToComeIterator++){
		if(!(((Action)actionsToCome.elementAt(actionsToComeIterator)).actionName.equals(currentAction.actionName)))
		    actionsToCome.remove(((Action)actionsToCome.elementAt(actionsToComeIterator)));
	    } 
	}
	     // @DEBUGGING
	else{
	    System.out.println("Action: "+currentAction.actionName+" is not in current expectation");
	}
	     // @DEBUGGING

		    
    }

    public boolean isActionInExpectation(Action currentAction){
	actionsInExpectation = getActionsInExpectations();
	
	for(int i=0; i < actionsInExpectation.size(); i++)
	    if(((Action)actionsInExpectation.elementAt(i)).actionName.equals(currentAction.actionName))
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
    

}

