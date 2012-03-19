package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class Believe
{
    public String believeName;
    public Vector composingActions;
    public Action previousAction;
    public int levelOfInheritance;
    public int activatedExpectations;
    public SconeFacade sfObject;
    public int currentTimeInstant;
    public Vector expectationsForActionsInBelieve;

    public Believe(int loi){
	activatedExpectations=0;
	composingActions = new Vector();
	previousAction = new Action();
	levelOfInheritance = loi;
	sfObject = new SconeFacade();
	expectationsForActionsInBelieve = new Vector();

    }

    public Believe(String bn, int loi){
	activatedExpectations=0;
	believeName = bn;
	composingActions = new Vector();
	previousAction = new Action();
	levelOfInheritance = loi;
	sfObject = new SconeFacade();
	expectationsForActionsInBelieve = new Vector();

    }
    public Vector getActionsInBelieve(){
	
	return composingActions;
    }


    public void  setActionInBelieveAtCurrentTimeInstant(Action actionToAssert, int cti){
	currentTimeInstant = cti;
	String actor = actionToAssert.actor;
	sfObject.sendToScone("(in-context {"+believeName+"})");	

	// @DEBUGGING
	System.out.println("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	// @DEBUGGING
	
	sfObject.sendToScone("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	sfObject.sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actor+"})");
	previousAction= actionToAssert;
	composingActions.add(actionToAssert);

    }


    public void setFirstActionInMainBelieve(String actor){
	
	// @DEBUGGING
	System.out.println("Setting first action in main believe");
	// @DEBUGGING
	
	currentTimeInstant =  0;

	Action actionToAssert = new Action("walk towards", actor);

	// @DEBUGGING
	System.out.println("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	// @DEBUGGING
	
	sfObject.sendToScone("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	sfObject.sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert.actionName+" at t0} {"+actor+"})");
	
	previousAction= actionToAssert;
	
	// @DEBUGGING
	System.out.println("Assign action to assert to previous action");
	// @DEBUGGING
	
	composingActions.add(actionToAssert);

	// @DEBUGGING
	System.out.println("Add action to assert to composing actions \n");
	// @DEBUGGING
	
    }
    
    public boolean doesActionExistInBelieveAtCurrentTimeInstant(Action requiredAction, int cti){
	currentTimeInstant = cti;
	
	for(int decreaseCurrentTimeInstant=0;  decreaseCurrentTimeInstant < currentTimeInstant;  decreaseCurrentTimeInstant++){
	    // @DEBUGGING
	    System.out.println("Checking string: (parent-wire (lookup-element {"+requiredAction.actor+" "+requiredAction.actionName+" at t"+decreaseCurrentTimeInstant +"}))");
	    System.out.println("Value of decrease current time instant: "+decreaseCurrentTimeInstant);
	    // @DEBUGGING
	
	    String checkActionAtTimeInstant = sfObject.sendToScone("(parent-wire (lookup-element {"+requiredAction.actor+" "+requiredAction.actionName+" at t"+decreaseCurrentTimeInstant +"}))");
	    
	    if(!sfObject.isAnErrorMessage(checkActionAtTimeInstant))
	    	return true;
	    
	}
	
	return false;
	
	
    }
    
    public Vector getExpectationsInvolvingActionsInCurrentBelieve(){

	// @DEBUGGING
	System.out.println("Get expectations involving actions in current believe with composing actions: ");
	// @DEBUGGING	
	printOutActionList(composingActions);
	for(int composingActionsIterator=0; composingActionsIterator< composingActions.size(); composingActionsIterator++){
	    Action currentAction = ((Action)composingActions.elementAt(composingActionsIterator));
	    
	    Vector expectationsForCurrentAction = currentAction.getExpectationsOfAction();
	    
	    // @DEBUGGING
	    System.out.println("Expectation list for action: "+currentAction.actionName);
	    // @DEBUGGING	
	    
	    printOutExpectationList(expectationsForCurrentAction);
	    addExpectationsToExistingSetOfExpectations(expectationsForCurrentAction);
	}
	return expectationsForActionsInBelieve;
    }

    public void addExpectationsToExistingSetOfExpectations(Vector expectationsForCurrentAction){
	for(int expectationsForCurrentActionIterator=0; expectationsForCurrentActionIterator<expectationsForCurrentAction.size(); expectationsForCurrentActionIterator++)
	    {
		Expectation currentExpectationElement = ((Expectation)expectationsForCurrentAction.elementAt(expectationsForCurrentActionIterator));
		
		if(!expectationExistInExpectationsForActionsInBelieve(currentExpectationElement))
		    expectationsForActionsInBelieve.add(((Expectation)expectationsForCurrentAction.elementAt(expectationsForCurrentActionIterator)));
		
	    }
    }
    public boolean expectationExistInExpectationsForActionsInBelieve(Expectation currentExpectationElement){
	for(int it = 0; it < expectationsForActionsInBelieve.size(); it++){
	    Expectation tempExp = ((Expectation)expectationsForActionsInBelieve.elementAt(it));

	    if(tempExp.expectationName.equals(currentExpectationElement.expectationName))
		return true;

	}
		
	return false;
    }
   
    public void incrementNumberOfActivatedExpectations(){
	activatedExpectations++;
    }
    
    public void printOutExpectationList(Vector expectationListToPrint){
	for(int iter=0; iter< expectationListToPrint.size(); iter++){
	    // @DEBUGGING
	    System.out.println(((Expectation)expectationListToPrint.elementAt(iter)).expectationName+" ");
	    // @DEBUGGING
	}
	
	System.out.println("\n");
    }
   public void printOutActionList(Vector actionListToPrint){
	for(int iter=0; iter< actionListToPrint.size(); iter++){
	    // @DEBUGGING
	    System.out.println(((Action)actionListToPrint.elementAt(iter)).actionName+" ");
	    // @DEBUGGING
	}
	
	System.out.println("\n");
    }
}