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

    

    public Estimation(String actorName){
	actor = actorName;
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
	
	// @DEBUGGING
	System.out.println("Asserting new action in estimation at time instant: "+ timeInstant);
	// @DEBUGGING
	
	cleanUpConsideredActions();
	assignNewConsideredActions(actionsInCPL);
	
	filterInconsistentActions();
	
	reorderActionsBasedOnPoI();

	// @DEBUGGING
	System.out.println("Bug at assert considered actions to corresponding believes\n\n");
	// @DEBUGGING
	assertConsideredActionsToCorrespondingBelieves();
    }
    public void cleanUpConsideredActions(){
	consideredActions = new Vector();
    }
    public void assignNewConsideredActions(Vector actionsInCPL){
	consideredActions = actionsInCPL;
    }
    
    public void setFirstActionInEstimation(){
	
	// @DEBUGGING
	System.out.println("Setting first action in estimation at time instant: "+ timeInstant);
	// @DEBUGGING
	
	mainBelieve = new Believe(0);
	currentBelieve = mainBelieve;
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
	consideredBelieves.add(new Believe("believe 0 for "+actor, 0));

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
	
	consideredBelieves.add(new Believe(believeName,consideredBelieves.size()));

	
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
	Vector expectationForCurrentAction  = currentAction.getExpectationsOfAction();
	
	for(int expectationForCurrentActionIterator=0;expectationForCurrentActionIterator<expectationForCurrentAction.size(); expectationForCurrentActionIterator++){
	    if(!isPresentInConsideredExpectations((String)expectationForCurrentAction.elementAt(expectationForCurrentActionIterator))){
		// @DEBUGGING
		System.out.println("Expectation "+ ((String)expectationForCurrentAction.elementAt(expectationForCurrentActionIterator))+" not yet considered in considered expectations");
		// @DEBUGGING	
		consideredExpectations.add(new Expectation((String)expectationForCurrentAction.elementAt(expectationForCurrentActionIterator)));
	    }

	}

    }
    
    public boolean isPresentInConsideredExpectations(String expectationName){
	Expectation tempExp = new Expectation(expectationName);
	if(consideredExpectations.indexOf(tempExp)>0)
	    return true;
	else
	    return false;
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
	// @DEBUGGING
	System.out.println("expectations of actions in current believe: "+markExpectationsOfActionsInCurrentBelieve.size());
	printOutExpectationList(markExpectationsOfActionsInCurrentBelieve);
	// @DEBUGGING
	System.out.println("expectations of current actions: ");	
	Vector markExpectationsOfCurrentAction = currentAction.getExpectationsOfAction();
	// @DEBUGGING

	printOutExpectationList(markExpectationsOfCurrentAction);
	// @DEBUGGING
	
	Vector intersetMarkedExpectations = getIntersectionOfMarkedSet(markExpectationsOfActionsInCurrentBelieve, markExpectationsOfCurrentAction);
	// @DEBUGGING
	System.out.println("Intersection: ");
	printOutExpectationList(intersetMarkedExpectations);
	// @DEBUGGING
	

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
	    if(currentAction.hasRequiredAction())
		deleteCurrentActionIfInconsistent();	    
	}
	
    }

    public boolean deleteCurrentActionIfInconsistent(){
	Action deleteIfNotFoundInBelieve;
	// @DEBUGGING
	System.out.println("Number of considered believes "+consideredBelieves.size());
	// @DEBUGGING

	for(int consideredBelievesIterator=0; consideredBelievesIterator< consideredBelieves.size(); consideredBelievesIterator++){
	    currentBelieve = ((Believe)consideredBelieves.elementAt(consideredBelievesIterator));
	    if(!currentBelieve.doesActionExistInBelieveAtCurrentTimeInstant(currentAction.requiredAction, timeInstant)){
		consideredActions.remove(currentAction);
		return true;
	    }
	}
	return false;

    }

    public void reorderActionsBasedOnPoI(){
	Vector reorderListOfConsideredActions = new Vector();
	Map rankedListOfPoI = rankPoIOfConsideredActions();


	for(int mapIterator = 0; mapIterator< rankedListOfPoI.size(); mapIterator++){
	    String highestRankedPoI = getHighestRankedPoI(rankedListOfPoI);
	    
	    for(int consideredActionsIterator=0; consideredActionsIterator < consideredActions.size(); consideredActionsIterator++){
		Action tempAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
		if(tempAction.pointOfInterest.equals(highestRankedPoI)){
		    reorderListOfConsideredActions.add(tempAction);
		}
	    }
	    rankedListOfPoI.put(highestRankedPoI, -1);
	   
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
	Map rankedListCopy = rankedListOfPoI;
	Iterator it = rankedListCopy.entrySet().iterator();
	int max = 0;
	String highestRanked = "nil";
	while (it.hasNext()) {
	    Map.Entry pairs = (Map.Entry)it.next();
	    if(((Integer)pairs.getValue()) > max){
		highestRanked = (String)pairs.getKey();
		max =((Integer)pairs.getValue()) ;
	    }
	}
	
	return highestRanked;
    }
    
    public void assertConsideredActionsToCorrespondingBelieves(){
	
	//currentAction = ((Action)consideredActions.elementAt(0));
	//currentBelieve = ((Believe)consideredBelieves.elementAt(0));	
	//setConsideredExpectations();
	
	if(isThereAnActiveExpectation()){
	    assertConsideredActionsFollowingTheActiveExpectation();
	}
	
	else{
	    
	    if(isThereAnActiveExpectationInConsideredBelieves()){
		checkIfInterchangeOfBelieveIsRequired();
		assertConsideredActionsFollowingTheActiveExpectation();
	    }
	    else{
		assertFirstActionInLineToMainBelieve();	
		assertRemainingActionsInLineToRemainingBelieves();
	       
	    }
	}
	
       	
    }
    public void assertFirstActionInLineToMainBelieve(){
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));	
	currentAction = ((Action)consideredActions.elementAt(0));
	currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);

	//mainBelieve = currentBelieve;
	//removeAssertedActionFromConsideredActions(currentAction);
	//removeBelieveContainingCurrentAction(currentBelieve);
    }

    public void assertRemainingActionsInLineToRemainingBelieves(){
	
	for(int consideredActionsIterator=1; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){  
	    activateNextBelieve();
	    
	    currentAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	    //removeAssertedActionFromConsideredActions(currentAction);
	    //removeBelieveContainingCurrentAction(currentBelieve);
	    
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
	// @DEBUGGING
	System.out.println("Get intersection of marked set "+a_marked);
	// @DEBUGGING
	if(a_marked==null)
	    return b_marked;
	else{
	    if(b_marked==null)
		return a_marked;
	    else{
		int minNumberOfElementsInSet = getSmallestNumberOfElementsInSet(a_marked, b_marked);
		
		for(int iterator=0; iterator < minNumberOfElementsInSet; iterator++){
		    if(a_marked.indexOf(b_marked.elementAt(iterator))>0)
			intersectedSet.add(b_marked.elementAt(iterator));
		    
		}
		
		return intersectedSet;
	    }

	}
    }
    public int  getSmallestNumberOfElementsInSet(Vector a_marked, Vector b_marked){
	// @DEBUGGING
	System.out.println("Get smallest number of elements "+a_marked.size()+ " "+b_marked.size());
	// @DEBUGGING
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

    public void printOutActionList(Vector actionListToPrint){
	for(int iter=0; iter< actionListToPrint.size(); iter++){
	    // @DEBUGGING
	    System.out.println(((Action)actionListToPrint.elementAt(iter)).actionName+" ");
	    // @DEBUGGING
	}
	
	System.out.println("\n");
    }

   public void printOutExpectationList(Vector actionListToPrint){
	for(int iter=0; iter< actionListToPrint.size(); iter++){
	    // @DEBUGGING
	    System.out.println(((Expectation)actionListToPrint.elementAt(iter)).expectationName+" ");
	    // @DEBUGGING
	}
	
	System.out.println("\n");
    }

}