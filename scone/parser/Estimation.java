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
    public Vector backUpConsideredBelieves;
    public Vector consideredActions;
    public Vector consideredExpectations;
    public SconeFacade sfObject;

    

    public Estimation(String actorName){
	actor = actorName;
	timeInstant = 0;
	consideredBelieves = new Vector();
	consideredActions = new Vector();
	consideredExpectations = new Vector();
	backUpConsideredBelieves = new Vector();
	currentBelieve = null;
	currentAction = null;
	currentExpectation = null;
	sfObject = new SconeFacade();	
    }

    
    public void newActionInEstimation(Vector actionsInCPL){
	timeInstant++;

	// @DEBUGGING
	System.out.println("Asserting new action in estimation at time instant: "+ timeInstant);
	// @DEBUGGING
	
	consideredActions = actionsInCPL;

	// @DEBUGGING
	System.out.println("Considering actions: "+ consideredActions);
	// @DEBUGGING
	
	filterInconsistentActions();

	// @DEBUGGING
	System.out.println("Considered actions after having filter inconsistent actions: "+ consideredActions);
	// @DEBUGGING
	
	reorderActionsBasedOnPoI();

	// @DEBUGGING
	System.out.println("Considered actions after having reorder them based on PoI: "+ consideredActions);
	// @DEBUGGING

	assertConsideredActionsToCorrespondingBelieves();
    }
    
    public void setFirstActionInEstimation(){
	
	// @DEBUGGING
	System.out.println("Setting first action in estimation at time instant: "+ timeInstant);
	// @DEBUGGING
	
	mainBelieve = new Believe(0);
	currentBelieve = new Believe(0);
	createMainBelieve();
	mainBelieve.setFirstActionInMainBelieve(actor);
    }
 
    public void createMainBelieve(){
	
	// @DEBUGGING
	System.out.println("Create the main context");
	// @DEBUGGING

	sfObject.sendToScone("(new-indv {"+actor+"} {person})");
	
	// @DEBUGGING
	System.out.println("Actor asserted");
	// @DEBUGGING
	
	sfObject.sendToScone("(new-statement {"+actor+"} {is in} {test room})");
	sfObject.sendToScone("(new-context {believe 0 for "+actor+"})");
	sfObject.sendToScone("(in-context (lookup-element {believe 0 for "+actor +"}))");
	consideredBelieves.add("believe 0 for "+actor);

    }
    
    public Vector getBelievesInEstimation(){
	return consideredBelieves;
    }
    
    public void activateNextBelieve(){
	if(currentBelieve.levelOfInheritance == (backUpConsideredBelieves.size()-1)){
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
	else 
	    return false;
	
    }

    public boolean isThereAnActiveExpectationInConsideredBelieves(){
	
	Vector markExpectationsOfActionsInCurrentBelieve = currentBelieve.getExpectationsInvolvingActionsInCurrentBelieve();
	Vector markExpectationsOfCurrentAction = currentAction.getExpectationsOfAction();
	Vector intersetMarkedExpectations = getIntersectionOfMarkedSet(markExpectationsOfActionsInCurrentBelieve, markExpectationsOfCurrentAction);
	if(intersetMarkedExpectations.size() ==1){
	    currentExpectation = ((Expectation)intersetMarkedExpectations.elementAt(0));
	    currentBelieve.incrementNumberOfActivatedExpectations();
	    return true;
	}
	else
	    return false;
	
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
	    reorderListOfConsideredActions.add(new Action(highestRankedActionName, actor));
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
	
	// @DEBUGGING
	System.out.println("Asserting considered actions to corresponding believes\n");
	// @DEBUGGING
	
	makeBackupCopyOfConsideredBelieves();
	
	currentAction = ((Action)consideredActions.elementAt(0));
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));	
	//setConsideredExpectations();
	
	if(isThereAnActiveExpectation()){
	    assertConsideredActionsFollowingTheActiveExpectation();
	}
	
	else{
	    //setConsideredExpectations();
	    if(isThereAnActiveExpectationInConsideredBelieves()){
		checkIfInterchangeOfBelieveIsRequired();
		assertConsideredActionsFollowingTheActiveExpectation();
	    }
	    else{
		assertFirstActionInLineToMainBelieve();
		assertRemainingActionsInLineToRemainingBelieves();
	    }
	}
	
	restoreBackupCopyOfConsideredBelieves();
	
    }
    public void assertFirstActionInLineToMainBelieve(){
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));	
	currentAction = ((Action)consideredActions.elementAt(0));
	currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	mainBelieve = currentBelieve;
	removeAssertedActionFromConsideredActions(currentAction);
	removeBelieveContainingCurrentAction(currentBelieve);

    }

    public void assertRemainingActionsInLineToRemainingBelieves(){
	
	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){
	    activateNextBelieve();
	    currentAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	    removeAssertedActionFromConsideredActions(currentAction);
	    removeBelieveContainingCurrentAction(currentBelieve);
	    
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
	removeBelieveContainingCurrentAction(currentBelieve);
    }

    public void removeAssertedActionFromConsideredActions(Action actionToRemove){
	consideredActions.remove(actionToRemove);
    }
    public void removeBelieveContainingCurrentAction(Believe believeToRemove){
	consideredBelieves.remove(believeToRemove);
    }
    

    public Vector getIntersectionOfMarkedSet(Vector a_marked, Vector b_marked){
	Vector intersectedSet = new Vector();
	int minNumberOfElementsInSet = getSmallestNumberOfElementsInSet(a_marked, b_marked);
	for(int iterator=0; iterator < minNumberOfElementsInSet; iterator++){
	    if(a_marked.indexOf(b_marked.elementAt(iterator))>0)
		intersectedSet.add(b_marked.elementAt(iterator));
	    
	}
	return intersectedSet;

	
    }
    public int  getSmallestNumberOfElementsInSet(Vector a_marked, Vector b_marked){
	if(a_marked.size()>b_marked.size())
	    return b_marked.size();
	else
	    return a_marked.size();
    }

    
    public void checkIfInterchangeOfBelieveIsRequired(){
	for(int consideredBelievesIterator=0; consideredBelievesIterator< consideredBelieves.size(); consideredBelievesIterator++){
	    Believe tempBel = ((Believe)consideredBelieves.elementAt(consideredBelievesIterator));
	    if(tempBel.activatedExpectations==2)
		mainBelieve = tempBel;
	}
    }
	    
    public void makeBackupCopyOfConsideredBelieves(){
	backUpConsideredBelieves = consideredBelieves;
    }

    public void	restoreBackupCopyOfConsideredBelieves(){
	consideredBelieves = backUpConsideredBelieves;

    }


}