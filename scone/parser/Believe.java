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
    public void setLastActionInMainBelieve(String actor, int cti){
	removeLastAction(actor);
	Action actionToAssert = new Action("walk towards", actor);

	// @DEBUGGING
	System.out.println("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	// @DEBUGGING
	
	sfObject.sendToScone("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	sfObject.sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert.actionName+" at t"+cti+"} {"+actor+"})");
	
	
    }
    public void removeLastAction(String actor){
	Action lastAction = ((Action)composingActions.elementAt(composingActions.size()-1));
	sfObject.sendToScone("(in-context {believe 0 for "+actor+"})");
	sfObject.sendToScone("(remove-element (lookup-element {"+lastAction.actionName+"}))");
    }
    public boolean doesActionExistInBelieveAtCurrentTimeInstant(Action requiredAction, int cti){
	currentTimeInstant = cti;
	int minTime = 0;
	if(cti>3)
	    minTime = cti -3;
	for(int decreaseCurrentTimeInstant=currentTimeInstant-1;  decreaseCurrentTimeInstant >minTime;  decreaseCurrentTimeInstant--){
	    // @DEBUGGING
	    System.out.println("Checked if action happened at time instant: "+decreaseCurrentTimeInstant);
	    // @DEBUGGING
	    String checkActionAtTimeInstant = sfObject.sendToScone("(list-context-contents *context*)");
	    if(checkActionAtTimeInstant.indexOf(requiredAction.actionName+" at t"+decreaseCurrentTimeInstant)>0)
	    	return true;
	}
	
	return false;
	
	
    }
    
    public Vector getExpectationsInvolvingActionsInCurrentBelieve(){
	
	// @DEBUGGING
	System.out.println("Get expectations involving actions in current believe: "+believeName+" with composing actions: ");
	// @DEBUGGING	
	printOutActionList(composingActions);
	//	for(int composingActionsIterator=0; composingActionsIterator< composingActions.size(); composingActionsIterator++){
	
	cleanUpExpectationList();
	int oneLevelsDown =0;
	if(composingActions.size()>1)
	    oneLevelsDown =composingActions.size()-1;
	
	for(int composingActionsIterator=composingActions.size(); composingActionsIterator>oneLevelsDown; composingActionsIterator--){
	    Action currentAction = ((Action)composingActions.elementAt(composingActionsIterator-1));
	    
	    Vector expectationsForCurrentAction = currentAction.getExpectationsOfAction();
	    
	    // @DEBUGGING
	    System.out.println("Expectation list for action: "+currentAction.actionName);
	    // @DEBUGGING	
	    
	    printOutExpectationList(expectationsForCurrentAction);
	    addExpectationsToExistingSetOfExpectations(expectationsForCurrentAction);
	}
	return expectationsForActionsInBelieve;
    }
    public void cleanUpExpectationList(){
	expectationsForActionsInBelieve = new Vector();
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

    public void activateBelieve(){
	sfObject.sendToScone("(in-context {"+believeName+"})");
    }

    public Action chooseActionToAvoideRepetitions(Vector consideredActions){
	Action currentAction = ((Action)consideredActions.elementAt(0));
	if(composingActions.size()>1 && !currentAction.actionName.equals(((Action)composingActions.elementAt(composingActions.size()-1)).actionName))
	    return currentAction;
	else{
	    if(composingActions.size()>1 && consideredActions.size()>1){
		// @DEBUGGING
		System.out.println("Accion repetida. Cambiamos a la siguiente en la lista");
		// @DEBUGGING
		return ((Action)consideredActions.elementAt(1));
	    }
	    else
		return currentAction;
	}
	
    }
}