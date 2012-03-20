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

	// @DEBUGGING
	System.out.println("******************* START OF FILTERING *************************\n");
	// @DEBUGGING	
	filterInconsistentActions();
	
	// @DEBUGGING
	System.out.println("******************* END OF FILTERING *************************\n");
	// @DEBUGGING	
	// @DEBUGGING
	System.out.println("******************* START OF REORDERING BASED ON POIS *************************\n");
	// @DEBUGGING	
	reorderActionsBasedOnPoI();
	// @DEBUGGING
	System.out.println("******************* END OF REORDERING BASED ON POIS *************************\n");
	// @DEBUGGING	
	// @DEBUGGING
	System.out.println("******************* START OF ASSERT ACTION TO CORRESPONDING BELIVE *************************\n");
	// @DEBUGGING	
	assertConsideredActionsToCorrespondingBelieves();
	// @DEBUGGING
	System.out.println("******************* END OF ASSERT ACTION TO CORRESPONDING BELIVE *************************\n");
	// @DEBUGGING	
	
    }
    public void cleanUpConsideredActions(){
	consideredActions = new Vector();
    }
    public void assignNewConsideredActions(Vector actionsInCPL){
	consideredActions = actionsInCPL;
    }
    
    public void setFirstActionInEstimation(){
	
	createMainBelieve();
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));
	mainBelieve = currentBelieve;
	
	// @DEBUGGING
	System.out.println("Setting first action in Believe: "+currentBelieve.believeName+" estimation at time instant: "+ timeInstant);
	// @DEBUGGING
	
	currentBelieve.setFirstActionInMainBelieve(actor);
    }

    public void setLastActionInEstimation(){
	
	activateMainBelieve();
	
	// @DEBUGGING
	System.out.println("Setting last action in Believe: "+currentBelieve.believeName+" estimation at time instant: "+ timeInstant);
	// @DEBUGGING
	
	currentBelieve.setLastActionInMainBelieve(actor, timeInstant);
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
    public void activateMainBelieve(){
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));
	sfObject.sendToScone("(in-context {"+currentBelieve.believeName+"})");
    }
    
    public void activateNextBelieve(){
		
	// @DEBUGGING
	System.out.println("Activating next believe. Current level of inheritance: "+ currentBelieve.levelOfInheritance );
	// @DEBUGGING
	if(currentBelieve.levelOfInheritance == (consideredBelieves.size()-1)){
	    createNewBelieveInEstimation();
	    currentBelieve = ((Believe)consideredBelieves.elementAt(consideredBelieves.size()-1));
	    
	}
	else{
	    currentBelieve = ((Believe)consideredBelieves.elementAt(currentBelieve.levelOfInheritance+1));
	    sfObject.sendToScone("(in-context {"+currentBelieve.believeName+"})");

	}
	
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

    //TO-FIX :: Ahora solo chequea el current believe. It must check all the believes in consideredBelieves

    public boolean isThereAnActiveExpectationInConsideredBelieves(){
	
	// @DEBUGGING
	System.out.println("Current Believe: "+currentBelieve.believeName);
	System.out.println("Actions composing the current believe: ");
	currentBelieve.printOutActionList(currentBelieve.composingActions);
	// @DEBUGGING
	
	
	Vector markExpectationsOfActionsInCurrentBelieve = currentBelieve.getExpectationsInvolvingActionsInCurrentBelieve();
		
	// @DEBUGGING
	System.out.println("expectations of actions in current believe: "+markExpectationsOfActionsInCurrentBelieve.size());
	printOutExpectationList(markExpectationsOfActionsInCurrentBelieve);
	System.out.println("expectations of current actions: ");	
	// @DEBUGGING
	
	
	Vector markExpectationsOfCurrentAction = currentAction.getExpectationsOfAction();

	//Vector intersetMarkedExpectations = getIntersectionOfMarkedSet(markExpectationsOfActionsInCurrentBelieve, markExpectationsOfCurrentAction);
	consideredExpectations = new Vector();
	consideredExpectations = getIntersectionOfMarkedSet(markExpectationsOfActionsInCurrentBelieve, markExpectationsOfCurrentAction);
	// @DEBUGGING
	System.out.println("Intersection: ");
	printOutExpectationList(consideredExpectations);
	// @DEBUGGING
	

	if(consideredExpectations.size() ==1){
	    currentExpectation = ((Expectation)consideredExpectations.elementAt(0));
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
	    // @DEBUGGING
	    System.out.println("Checking Action: "+ currentAction.actionName+" that requires: "+ currentAction.requiredAction.actionName);
	    // @DEBUGGING
	    if(currentAction.hasRequiredAction())
		deleteCurrentActionIfInconsistent();	    
	}
	
    }

    public boolean deleteCurrentActionIfInconsistent(){
	Action deleteIfNotFoundInBelieve;
	
	currentBelieve = ((Believe)consideredBelieves.elementAt(0));
	currentBelieve.activateBelieve();
	sfObject.sendToScone("(list-context-contents (lookup-element {"+currentBelieve.believeName +"}))");
	
	if(currentBelieve.doesActionExistInBelieveAtCurrentTimeInstant(currentAction.requiredAction, timeInstant))
	    return true;
	
	consideredActions.remove(currentAction);
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
	activateMainBelieve();
	currentAction = ((Action)consideredActions.elementAt(0));

	// @DEBUGGING
	System.out.println("Print out considered actions: ");
	printOutActionList(consideredActions);
	System.out.println("Print out considered believes: ");
	printOutBelieveList(consideredBelieves);
	System.out.println("Print out current action: "+currentAction.actionName);
	System.out.println("Print out current believe: "+currentBelieve.believeName);
	// @DEBUGGING

	if(isThereAnActiveExpectation()){
	    assertNextActionToComeBasedOnTheActiveExpectation();
	    activateNextBelieve();

	}
	
	else{
	    
	    if(isThereAnActiveExpectationInConsideredBelieves()){
		checkIfInterchangeOfBelieveIsRequired();
		// @DEBUGGING
		System.out.println("Let's assert considered actions following the active expectation\n\n");
		// @DEBUGGING
		
		assertConsideredActionsFollowingTheActiveExpectation();
		activateNextBelieve();

	
	    }
	}
	// @DEBUGGING
	System.out.println("Assert the rest of actions to the remainder believes\n");
	// @DEBUGGING
	//reorderConsideredActionsBasedOnConsideredExpectations();
	assertFirstActionInLineToNextActiveBelieve();	
	activateNextBelieve();
	assertRemainingActionsInLineToRemainingBelieves();
	       	
    }
    public boolean assertNextActionToComeBasedOnTheActiveExpectation(){
		
	if(!currentExpectation.isStillActive()){
	    // @DEBUGGING
	    System.out.println("No quedan acciones en actionsToCome\n");
	    // @DEBUGGING
	    currentExpectation = null;
	    assertFirstActionInLineToNextActiveBelieve();	
	    return false;
	}

	// @DEBUGGING
	System.out.println("Primero vamos a chequear las ActionsToCome:");
	printOutActionList(currentExpectation.actionsToCome);
	System.out.println("Las acciones que tenemos en este time instant son:");
	printOutActionList(consideredActions);
	// @DEBUGGING

	
	Action firstActionToAssertInActionsToCome =  ((Action)currentExpectation.actionsToCome.elementAt(0));
	
	// @DEBUGGING
	System.out.println("The active expectation is: "+ currentExpectation.expectationName);
	System.out.println("The next action in expectation is: "+ firstActionToAssertInActionsToCome.actionName);
	// @DEBUGGING

	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){
	    Action tempAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    // @DEBUGGING
	    System.out.println("Action in considered actions "+ tempAction.actionName);
	    // @DEBUGGING
	    if(currentExpectation.isActionInActionsToCome(tempAction) && firstActionToAssertInActionsToCome.actionName.equals(tempAction.actionName)){	
		// @DEBUGGING
		System.out.println("Assert expected action: "+ firstActionToAssertInActionsToCome.actionName+" believe: "+ currentBelieve.believeName);
		// @DEBUGGING
		currentBelieve.setActionInBelieveAtCurrentTimeInstant(tempAction, timeInstant);
		currentExpectation.actionsToCome.remove(((Action)currentExpectation.actionsToCome.elementAt(0)));
		removeAssertedActionFromConsideredActions(tempAction);
		//removeBelieveContainingCurrentAction(currentBelieve);
		return true;

	    }
	}
	
	// @DEBUGGING
	System.out.println("Action: "+  firstActionToAssertInActionsToCome.actionName +" is not in considered action list. No worries, we force it\n");
	// @DEBUGGING
	Action tempAction = new Action(firstActionToAssertInActionsToCome.actionName, actor);
	currentBelieve.setActionInBelieveAtCurrentTimeInstant(tempAction, timeInstant);
	currentExpectation.actionsToCome.remove(((Action)currentExpectation.actionsToCome.elementAt(0)));
	
	return false;
	
    }

    
    public void reorderConsideredActionsBasedOnConsideredExpectations(){
	// @DEBUGGING
	System.out.println("List of expectations considered here");
	// @DEBUGGING
	currentBelieve.printOutExpectationList(consideredExpectations);
	
    }
    public void assertFirstActionInLineToNextActiveBelieve(){
	
	currentAction =	currentBelieve.chooseActionToAvoideRepetitions(consideredActions);
	currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	removeAssertedActionFromConsideredActions(currentAction);
	
    }
    
    public void assertRemainingActionsInLineToRemainingBelieves(){
	
	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){  

	    currentAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    currentBelieve.setActionInBelieveAtCurrentTimeInstant(currentAction, timeInstant);
	    activateNextBelieve();
	    
	}
	    
	
    }
    public boolean assertConsideredActionsFollowingTheActiveExpectation(){

	Action actionInConsideredActions =  currentAction;
	currentExpectation.setActionsToCome(actionInConsideredActions);


	    
	Action firstActionToAssertInActionsToCome =  ((Action)currentExpectation.actionsToCome.elementAt(0));
	
	// @DEBUGGING
	System.out.println("Inside assert considered actions following the active expectation");
	System.out.println("The active expectation is: "+ currentExpectation.expectationName);
	System.out.println("The next action in expectation is: "+ firstActionToAssertInActionsToCome.actionName);
	// @DEBUGGING

	for(int consideredActionsIterator=0; consideredActionsIterator< consideredActions.size(); consideredActionsIterator++){
	    Action tempAction = ((Action)consideredActions.elementAt(consideredActionsIterator));
	    // @DEBUGGING
	    System.out.println("Action in considered actions "+ tempAction.actionName);
	    // @DEBUGGING
	    if(currentExpectation.isActionInExpectation(tempAction) && firstActionToAssertInActionsToCome.actionName.equals(tempAction.actionName)){	
		// @DEBUGGING
		System.out.println("Assert expected action: "+ firstActionToAssertInActionsToCome.actionName+" believe: "+ currentBelieve.believeName);
		// @DEBUGGING
		currentBelieve.setActionInBelieveAtCurrentTimeInstant(tempAction, timeInstant);
		currentExpectation.actionsToCome.remove(((Action)currentExpectation.actionsToCome.elementAt(0)));
		removeAssertedActionFromConsideredActions(tempAction);
		//removeBelieveContainingCurrentAction(currentBelieve);
		return true;

	    }
	}
	
	// @DEBUGGING
	System.out.println("Action: "+  firstActionToAssertInActionsToCome.actionName +" is not in considered action list. No worries, we force it\n");
	// @DEBUGGING
	currentExpectation.actionsToCome.remove(((Action)currentExpectation.actionsToCome.elementAt(0)));
	removeAssertedActionFromConsideredActions(firstActionToAssertInActionsToCome);
	//removeBelieveContainingCurrentAction(currentBelieve);
	return false;
	       
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
	System.out.println("Get intersection of marked set ");
	System.out.println("a_marked: ");
	printOutExpectationList(a_marked);

	System.out.println("b_marked: ");
	printOutExpectationList(b_marked);
	// @DEBUGGING

	if(a_marked==null)
	    return b_marked;
	else{
	    if(b_marked==null)
		return a_marked;
	    else{
		int minNumberOfElementsInSet = getSmallestNumberOfElementsInSet(a_marked, b_marked);
		// @DEBUGGING
		System.out.println("Smallest set has "+minNumberOfElementsInSet+" elements\n");
		// @DEBUGGING
		if(a_marked.size()==minNumberOfElementsInSet){
		    for(int iterator=0; iterator < minNumberOfElementsInSet; iterator++)
			if(((Expectation)a_marked.elementAt(iterator)).isInExpectationSet(b_marked))
			    intersectedSet.add(b_marked.elementAt(iterator));  
		}
		else{
		    for(int iterator=0; iterator < minNumberOfElementsInSet; iterator++)
			if(((Expectation)b_marked.elementAt(iterator)).isInExpectationSet(a_marked))
			    intersectedSet.add(b_marked.elementAt(iterator));

		}

	    }
	    
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
	// @DEBUGGING
	System.out.println("Checking whether believes have to be interchanged\n");
	// @DEBUGGING
	
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
      public void printOutBelieveList(Vector believeListToPrint){
	for(int iter=0; iter< believeListToPrint.size(); iter++){
	    // @DEBUGGING
	    System.out.println(((Believe)believeListToPrint.elementAt(iter)).believeName+" ");
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