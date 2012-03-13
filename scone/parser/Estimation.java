package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class Estimation{

    public String actor;
    public int timeInstant;
    public Believe currentBelieve;
    public Action currentAction;
    public Expectation currentExpectation;
    public Believe mainBelieve;
    public Vector consideredBelieves;
    public Vector consideredActions;
    public Vector consideredExpectations;
    public SconeFacade sfObject;

    

    public Estimation(String actor){
	timeInstant = 0;
	consideredBelieves = new Vector();
	consideredActions = new Vector();
	consideredExpectations = new Vector();
	currentBelieve = null;
	currentAction = null;
	currentExpectation = null;
	sfObject = new SconeFacade();	
    }

    
    public void newActionInEstimation(Vector actionsInCPL){
	timeInstant++;
	consideredActions = actionsInCPL;
	filterInconsistentActions();
	reorderActionsBasedOnPoI();
	assertConsideredActionsToCorrespondingBelieves();
    }
    
    public void setFirstActionInEstimation(){
	mainBelieve = new Believe(0);
	currentBelieve = new Believe(0);
	setMainBelieve();
	mainBelieve.setFirstActionInMainBelieve(actor);
    }
    public Believe getMainBelieve(){
	return mainBelieve;
    }

    public void setMainBelieve(){
	
	// We create and activate a new context
	//newBelieve(actor);
	System.out.println("Create the main context");
	sfObject.sendToScone("(new-indv {"+actor+"} {person})");
	sfObject.sendToScone("(new-statement {"+actor+"} {is in} {test room})");
	sfObject.sendToScone("(new-context {believe 0 for "+actor+"})");
	sfObject.sendToScone("(in-context (lookup-element {believe 0 for "+actor +"}))");
	consideredBelieves.add("believe 0 for "+actor);

    }
    
    public Vector getBelievesInEstimation(){
	return consideredBelieves;
    }
    
    public void activateNextBelieve(){
	if(currentBelieve.levelOfInheritance == (consideredBelieves.size()-1)){
	    createNewBelieveInEstimation();
	    currentBelieve = ((Believe)consideredBelieves.elementAt(consideredBelieves.size()-1));
	}
	else
	    currentBelieve = ((Believe)consideredBelieves.elementAt(currentBelieve.levelOfInheritance+1));
	
    }

    public void createNewBelieveInEstimation(){
	String believeName = "believe "+consideredBelieves.size()+ " for "+actor;
	// @DEBUGGING
	System.out.println("Created: "+believeName);
	// @DEBUGGING
	
	sfObject.sendToScone("(in-context (lookup-element {general}))");
	sfObject.sendToScone("(new-context {"+believeName+"})");
	
	consideredBelieves.add(believeName);
	
    }


    public Believe getCurrentBelieve(){
	return currentBelieve;
    }
    
    public void setCurrentBelieve(String believeName){
	// Activate the new context
	sfObject.sendToScone("(in-context (lookup-element {"+believeName+"}))");
	currentBelieve.believeName = believeName;
	
    }

    public void setConsideredExpectations(){
	Vector expectationForCurrentAction = new Vector();
	expectationForCurrentAction = currentAction.getExpectationsOfAction();
	Vector commonExpectations = new Vector();
	
	commonExpectations = consideredExpectations;
	if(consideredExpectations.size()>0){
	    for(int consideredExpectationsIterator =0; consideredExpectationsIterator< consideredExpectations.size(); consideredExpectationsIterator++){
		for(int expectationForCurrentActionIt=0; expectationForCurrentActionIt < expectationForCurrentAction.size(); expectationForCurrentActionIt++){
		    System.out.println("Checking expectations: "+ ((Expectation)consideredExpectations.elementAt(consideredExpectationsIterator)).expectationName+ " "+  ((Expectation)expectationForCurrentAction.elementAt(expectationForCurrentActionIt)).expectationName);
		    if((((Expectation)consideredExpectations.elementAt(consideredExpectationsIterator)).expectationName).equals(((Expectation)expectationForCurrentAction.elementAt(expectationForCurrentActionIt)).expectationName))
			commonExpectations.add(((Expectation)consideredExpectations.elementAt(consideredExpectationsIterator)));
		    
		}
	    }
	    consideredExpectations = commonExpectations;
	}
	else
	    consideredExpectations =  expectationForCurrentAction;
	
    }
    


    public void setActiveExpectation(Expectation ae){
	if(consideredExpectations.size()==1)
	    currentExpectation = ((Expectation)consideredExpectations.elementAt(0));
    }

    public boolean isThereAnActiveExpectation(){
	if(currentExpectation!=null)
	    return true;
	else {
	    if(consideredExpectations.size()==1){
		currentExpectation = ((Expectation)consideredExpectations.elementAt(0));
		return true;
	    }
	    else		
		return false;
	}
    }
    
    // This function discard those actions out of the consideredActions that are inconsistent
    public void filterInconsistentActions(){
	for(int consideredActionsIterator=0; consideredActionsIterator < consideredActions.size(); consideredActionsIterator++){
	    currentAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    currentAction.getRequiredAction();
	    deleteCurrentActionIfInconsistent();	    
	} 
	
    }

    public void deleteCurrentActionIfInconsistent(){
	Action deleteIfNotFoundInBelieve;

	for(int consideredBelievesIterator=0; consideredBelievesIterator< consideredBelieves.size(); consideredBelievesIterator++){
	    currentBelieve = ((Believe)consideredBelieves.elementAt(consideredBelievesIterator));
	    if(currentBelieve.doesActionExistInBelieveAtCurrentTimeInstant(currentAction.requiredAction, timeInstant)){
		// @DEBUGGING
		System.out.println("Found a previous requirement for consistency \n");
		// @DEBUGGING
		deleteIfNotFoundInBelieve = null;
	    }
	    else{
		// @DEBUGGING
		System.out.println("Action "+ currentAction.actionName +" requires "+currentAction.requiredAction.actionName+" and it hasn't been found");
		// @DEBUGGING
		deleteIfNotFoundInBelieve = currentAction;
	    }
	    
	}
	consideredActions.remove(currentAction);
    }

    public void reorderActionsBasedOnPoI(){
	Vector reorderListOfConsideredActions = new Vector();
	Map rankedListOfPoI = rankPoIOfConsideredActions();
	Iterator rankedListOfPoIIterator = rankedListOfPoI.entrySet().iterator();
	while(rankedListOfPoIIterator.hasNext()){
	    String highestRankedActionName = getHighestRankedPoI(rankedListOfPoI);
	    reorderListOfConsideredActions.add(new Action(actor, highestRankedActionName));
	    rankedListOfPoI.remove(highestRankedActionName);
	}
	consideredActions = reorderListOfConsideredActions;
    }

    public Map rankPoIOfConsideredActions(){
	Map poiOfConsideredActions = new HashMap();
	int timesInConsideredActions=0;
	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){
	    currentAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    if(poiOfConsideredActions.containsKey(currentAction.pointOfInterest)){
		timesInConsideredActions = ((Integer)poiOfConsideredActions.get(currentAction.pointOfInterest))+1;
		poiOfConsideredActions.put(currentAction.pointOfInterest, timesInConsideredActions);
	    }
	    else
		poiOfConsideredActions.put(currentAction.pointOfInterest, 1);
	}
	return poiOfConsideredActions;
    }

    public String getHighestRankedPoI(Map rankedListOfPoI){
	Iterator it = rankedListOfPoI.entrySet().iterator();
	int max = 0;
	String highestRanked = "nil";
	while (it.hasNext()) {
	    Map.Entry pairs = (Map.Entry)it.next();
	    if(((Integer)pairs.getValue()) > max){
		highestRanked = (String)pairs.getKey();
		max =((Integer)pairs.getValue()) ;
	    }
	    it.remove();
	}
	return highestRanked;
    }
    
    public void assertConsideredActionsToCorrespondingBelieves(){
	
	currentAction = ((Action)consideredActions.elementAt(0));
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));	
	setConsideredExpectations();
	
	if(isThereAnActiveExpectation())
	    assertConsideredActionsFollowingTheActiveExpectation();
	
	else{
	    assertFirstActionInLineToMainBelieve();
	    assertRemainingActionsInLineToRemainingBelieves();
	}
		
    }
    public void assertFirstActionInLineToMainBelieve(){
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));	
	currentAction = ((Action)consideredActions.elementAt(0));
	currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	removeAssertedActionFromConsideredActions(currentAction);

    }

    public void assertRemainingActionsInLineToRemainingBelieves(){
	
	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){
	    activateNextBelieve();
	    currentAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	    
	}
	    
	
    }
    public void assertConsideredActionsFollowingTheActiveExpectation(){
	Action firstActionInActionsToCome = ((Action)currentExpectation.actionsToCome.elementAt(0));
	
	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){
	    Action actionInConsideredActions =  ((Action)consideredActions.elementAt(consideredActionsIterator));
	    if(actionInConsideredActions.isActionTheFirstActionToComeInExpectation(currentExpectation)){
		firstActionInActionsToCome = ((Action)consideredActions.elementAt(consideredActionsIterator));
		removeAssertedActionFromConsideredActions(firstActionInActionsToCome);
	    }
	}
	currentBelieve.setActionInBelieveAtCurrentTimeInstant(firstActionInActionsToCome, timeInstant);
    }

    public void removeAssertedActionFromConsideredActions(Action actionToRemove){
	consideredActions.remove(actionToRemove);
    }
}