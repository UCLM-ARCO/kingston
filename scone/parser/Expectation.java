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
	actionsToCome = new Vector();
	sfObject = new SconeFacade();
    }
    
    public Expectation(){
	sfObject = new SconeFacade();
	
    }
    public Vector getActionsInExpectations(){
	
	// @DEBUGGING
	System.out.println("List all the actions involved in the expectation\n");
	System.out.println("(show-all-x-of-y  {has expectation} {"+expectationName+"})");
	// @DEBUGGING
	
	String actionsInvolvedInExpectation = sfObject.sendToScone("(list-all-x-of-y  {has expectation} {"+expectationName+"})");
	String separator = "{events:";
	
	int startOfSeparator = actionsInvolvedInExpectation.indexOf(separator);
	int endOfSeparator =  actionsInvolvedInExpectation.indexOf('}');
	
	while(startOfSeparator > -1){
	    startOfSeparator +=  separator.length();
	    actionsInExpectation.add(actionsInvolvedInExpectation.substring(startOfSeparator, endOfSeparator));
	    startOfSeparator = actionsInvolvedInExpectation.indexOf(separator, endOfSeparator);
	    endOfSeparator = actionsInvolvedInExpectation.indexOf('}', startOfSeparator);
	}
	return actionsInExpectation;
    }

    public void setActionsToCome(Action currentAction){
	
	actionsToCome =  actionsInExpectation;

	for(int actionsToComeIterator = 0; actionsToComeIterator < actionsToCome.size(); actionsToComeIterator++){
	    if(!((Action)actionsToCome.elementAt(actionsToComeIterator)).equals(currentAction))
		actionsToCome.remove(((Action)actionsToCome.elementAt(actionsToComeIterator)));
	} 
    }
    

}

