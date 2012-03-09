package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class ActionParsing 
{
    static final String server = "localhost";
    static final int port=5000;
    static final String[] IXMAS_ACTIONS = {"Nothing", "check watch", "cross arms", "scratch head", "sit down", "get up", "turn around", "walk towards", "wave", "punch", "kick",  "point at", "pick up", "throw over head", "throw from bottom up"};
    
    // TO FIX: this has to be deleted
    public int believeRound;    // It counts the number of beliefs held at any time
    
    public Vector believes;
    
    public int levelOfInheritance;
    public Expectation expectations;
    public ArrayList listOfBlockedBelieves;
    public Vector currentExpectations;
    public String previousAction;
    public boolean FOLLOWING_SINGLE_EXPECTATION;    
    public ActionParsing(){
	believes = new Vector();
	believeRound = 0;
	levelOfInheritance=0;
	expectations = new Expectation();
	listOfBlockedBelieves = new ArrayList();
	currentExpectations = new Vector();
	FOLLOWING_SINGLE_EXPECTATION = false;
    }
    
    
    public Vector getParsedActions(String actionList){
	Vector splittedActions = new Vector();
	int start =0;
	int end=actionList.indexOf(" ", 0);
	
	if(actionList.indexOf("a") <0){ //To get rid of the accuracy message at the end of the file. The three sentences contains letter 'a'
	    while(end !=-1){
		splittedActions.addElement(Integer.parseInt(actionList.substring(start, end)));
		 start = end+1; 
		 end = actionList.indexOf(" ", start);
	    }
	}
	return splittedActions;
    }


    /* When the first action is walking this means that the actor is entering the room
    // Let's describe the explicit knowledge involved in the first action being walk
    // First thing that need to be done is creating a node for the appearing actor */
    public void assertFirstActionInFirstBelieve(String actor, Vector splittedActions){
	
	boolean WALKED_DETECTED = false;
	String actionToAssert;
	// Then, let's state that the first action of the believe is that of walking"
	for(int i=0; i<splittedActions.size(); i++)
	    if(((Integer)splittedActions.elementAt(i))==7)
		WALKED_DETECTED=true;
	
	if(!WALKED_DETECTED){
	    // If the first action is different from walking, the system will fix that manually
	    System.out.println("The first action should be that of the actor entering the room\n");
	    System.out.println("The system has fixed the error by stating that fact into the KB\n");
	}
	
	actionToAssert = IXMAS_ACTIONS[7]; //actionToAssert = "walk"

	sendToScone("(in-context (lookup-element {general}))");
	// These assertions go to the common contexts
	sendToScone("(new-indv {"+actor+"} {person})");
	sendToScone("(new-statement {"+actor+"} {is in} {test room})");
	
	// We create and activate a new context
	//newBelieve(actor);
	System.out.println("Create the main context");
	sendToScone("(new-context {believe 0 for "+actor+"})");
	believeRound++;
	sendToScone("(in-context (lookup-element {believe 0 for "+actor +"}))");
	believes.add("believe 0 for "+actor);

	// We assert the new fact the just created and activated new context
	sendToScone("(new-event-indv {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actionToAssert+"})");
	//sendToScone("(new-event-indv {"+actor+" "+actionToAssert +"} {"+actionToAssert+"})");
	sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actor+"})");
	//sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+"} {"+actor+"})");
	previousAction= actionToAssert;
	// Just in case, we return to the common context
	// TO FIX: Do we really need that?
	//sendToScone("(in-context (lookup-element {general}))");
    }
    
    public String getPoI(String action){
	String queryToGetPoI = sendToScone("(incoming-a-wires (lookup-element {"+action+"}))");
	// ({events:punch has point of interest in arm (0-1512)}
	// the required action starts after the 'has point of interest in' word and ends before the '(' symbol
	
	int startIndexOfPoI = queryToGetPoI.indexOf("has point of interest in");
	int endIndexOfPoI = startIndexOfPoI;
	
	if(startIndexOfPoI > -1){
	    startIndexOfPoI+=25; // We add 9 to jump just after the 'has point of interest in' word 
	    endIndexOfPoI= queryToGetPoI.indexOf('(',startIndexOfPoI)-1; 
	}		    	    
	return queryToGetPoI.substring(startIndexOfPoI, endIndexOfPoI);
    }

    public Map getRankedPointsOfInterest(Vector splittedActions){
	Map PoI = new HashMap();
	String retrievedPoI;
	for(int i=0; i<splittedActions.size(); i++){
	    retrievedPoI = getPoI(IXMAS_ACTIONS[((Integer)splittedActions.elementAt(i))]);
	    // @DEBUGGING
	    // System.out.println("Point of interest for action "+ IXMAS_ACTIONS[((Integer)splittedActions.elementAt(i))] +" is "+retrievedPoI +"\n" );
	    // @DEBUGGING

	    if(PoI.containsKey(retrievedPoI))
		{
		    int newTimes = ((Integer)PoI.get(retrievedPoI)) +1;
		    // @DEBUGGING
		    // System.out.println("Repeated appearence for "+ IXMAS_ACTIONS[((Integer)splittedActions.elementAt(i))] + " times "+ newTimes);
		    // @DEBUGGING
		    
		    PoI.put(retrievedPoI,newTimes);
		    
		}
	    else
		PoI.put(retrievedPoI, 1);
	}
	return PoI;
    }
    
    public String getHighestRankedPoI(Map PoI){
	Iterator it = PoI.entrySet().iterator();
	int max = 0;
	String highestRanked = "nil";
	while (it.hasNext()) {
	    Map.Entry pairs = (Map.Entry)it.next();
	    // @DEBUGGING
	    // System.out.println("Number of appearances for "+(String)pairs.getKey()+" " +(Integer)pairs.getValue());
	    // System.out.println("So far, maximun number of appearences: "+max);
	    // @DEBUGGING
	    if(((Integer)pairs.getValue()) > max){
	
		highestRanked = (String)pairs.getKey();
		max =((Integer)pairs.getValue()) ;
		
	    }
	    it.remove(); // avoids a ConcurrentModificationExcept
	}
	return highestRanked;
	
    }
    
    public String getActorName(String fileName){
	return fileName.substring(fileName.lastIndexOf("_")+1,fileName.length());   
    }
    
    public int newBelieve(String actor){
	String believeName = "believe "+believeRound+ " for "+actor;

	System.out.println("Create "+believeName);
	
	//sendToScone("(in-context (lookup-element {general}))");
	int index =levelOfInheritance-1;
	String parentBelieve =  "believe "+index+ " for "+actor;
	
	sendToScone("(in-context (lookup-element {general}))");

	// Create the new context

	sendToScone("(new-context {"+believeName+"})");
	sendToScone("(new-is-a {"+believeName+"}  (lookup-element{"+parentBelieve+"}))");

	// Activate the new context

	sendToScone("(in-context (lookup-element {"+believeName+"}))");
	
	// Assert a new element to the Vector which is tracking the created contexts
	believes.add(believeName);
	
	return believeRound++;
	
    }
    
    //TOFIX
    public void getBelieve(String believeName, String action){
	sendToScone("(in-context (lookup-element {"+believeName+"}))");	
    }
    
    public void activateBelieve(String believeName){
	sendToScone("(in-context (lookup-element {"+believeName+"}))");	
    }
    
    public void addActionToBelieve(String believeName, String actionToAssert, String actor){
	
	
	sendToScone("(in-context {"+believeName+"})");	
	
	// We assert the new fact the just created and activated new context
	
	// @DEBUGGING
	System.out.println("(new-event-indv {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actionToAssert+"})");
	// @DEBUGGING
	
	
	sendToScone("(new-event-indv {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actionToAssert+"})");
	//sendToScone("(new-event-indv {"+actor+" "+actionToAssert+"} {"+actionToAssert+"})");
	sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actor+"})");
	//sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+"} {"+actor+"})");
	
	previousAction= actionToAssert;
	// Just in case, we return to the common context
	// TO FIX: Do we really need that?
	//sendToScone("(in-context (lookup-element {general}))");
    }
    public void readActionsFromFile(String fileName){
	
	int actionNumber, splittedActionIterator;


	try{
	    FileInputStream fstream = new FileInputStream(fileName); //TO_FIX: path to files cannot be hard-coded
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    boolean FIRST_ACTION= true; //use to check whenever first action is not walk

	    
	    // Extract name of the actor out of the file name. Recall that the last word of the file is the actor name
	    String actor = getActorName(fileName);
	    String believeName;
	    Vector actionsToCome = new Vector();

	    // Read File Line By Line
	    // Each line is a level of context inheritance
	    while ((strLine = br.readLine()) != null)   {
		

		// Iterator that tracks the number of actions that are in each of the lines of the file
		splittedActionIterator = 0;

		// @DEBUGGING
		System.out.println("=================================");
		System.out.println("     SPLITTING ACTIONS           ");
		System.out.println("=================================");
		// @DEBUGGING		
		
		// All the actions that hold 50% of the probability given by the classifier are analyzed
		Vector splittedActions = getParsedActions(strLine);

		// @DEBUGGING
		System.out.println("Stage "+levelOfInheritance+" actions considered probable: "+splittedActions);
		// @DEBUGGING
		
		// @DEBUGGING
		System.out.println("=================================");
		System.out.println("     RAKING POINTS OF INTERESTS  ");
		System.out.println("=================================");
		// @DEBUGGING		
		
		// Count the different points of interest. How many actions have the hands as PoI, or how many others have the legs
		Map PoI = getRankedPointsOfInterest(splittedActions);
		
		// Rank the PoI so that we can assume that the highest in the rank the more likely to be involved in the real performed action
		String bestRatedPoI = getHighestRankedPoI(PoI);
		
		// @DEBUGGING
		System.out.println("Best rated point of interest for the considered actions " +splittedActions +" is: " + bestRatedPoI);
		// @DEBUGGING

		// @DEBUGGING
		System.out.println("=================================");
		System.out.println("     ANALYZE EACH OF THE ACTIONS ");
		System.out.println("=================================");
		// @DEBUGGING		
		
		next_line:{
		// Do for each action
		for(splittedActionIterator=0; splittedActionIterator < splittedActions.size(); splittedActionIterator++){
		    if(FOLLOWING_SINGLE_EXPECTATION){
			if(actionsToCome.size()>0 && currentExpectations.size()>0){
			// Check whether any of the splittedActions matches the expected action
			
			// @DEBUGGING
			System.out.println("Check whether the expected action from expectation matches any of the classifier's outputs\n");
			// @DEBUGGING
			actionsToCome = expectations.getActionsToCome((String)currentExpectations.elementAt(0));
			for(int iterator =0; iterator < splittedActions.size(); iterator++){
			    
			    // @DEBUGGING
			    System.out.println("First action to come "+(String)actionsToCome.elementAt(0));
			    System.out.println("Comparing with "+  IXMAS_ACTIONS[((Integer)splittedActions.elementAt(iterator))]);

			    // @DEBUGGING
			    
			    
			    // if( IXMAS_ACTIONS[((Integer)splittedActions.elementAt(iterator))] == ((String)actionsToCome.elementAt(0))){
			    if( ((String)actionsToCome.elementAt(0)).indexOf(IXMAS_ACTIONS[((Integer)splittedActions.elementAt(iterator))]) > -1){
				addActionToBelieve("believe "+ 0 + " for "+actor,((String)actionsToCome.elementAt(0)) , actor);
				
				// @DEBUGGING
				System.out.println("Expected action found at position "+iterator+"\n");
				// @DEBUGGING
				
				
				actionsToCome.remove(((String)actionsToCome.elementAt(0)));
				break next_line;
			    }
			    //TO FIX: else  is there any similar action
			    
			} // End for loop cheecking if the expected action is in the splitted list
			} // If actionToCome>0
			else
			    FOLLOWING_SINGLE_EXPECTATION = false;
		    }
		    
		    else{
			
			actionNumber = (Integer)splittedActions.elementAt(splittedActionIterator); // Action given by the name
			String actionToAssert = IXMAS_ACTIONS[actionNumber]; // Same action given as string
			
			
			// @DEBUGGING
			System.out.println("For action "+actionToAssert+": ");
			// @DEBUGGING
			
			// Existing believes??
			// Is there any belive in which that action can be asserted or maybe we have to create a new believe
			if(believes.size()==0){ //if no new believes have already been created
			    
			    // @DEBUGGING
			    System.out.println("1. Create a new believe --main believe--");
			    // @DEBUGGING
			    
			    // The importance of the first action requires a special treatement. We expect it to be the walk towards action
			    assertFirstActionInFirstBelieve(actor, splittedActions);
			    
			    // @DEBUGGING
			    System.out.println("2. Assert walk towards as a the first action");
			    // @DEBUGGING
			    
			    // @DEBUGGING
			    System.out.println("Frist action (walk towards) asserted in first believe");
			    System.out.println("Ignore the following actions and steps into the second action");
			    // @DEBUGGING
			    
			    // We abandom the analysis of the first actions because we are only interested in the walk towards
			    break;
			    
			}
			else{ //believe contexts have already been created
			    // Does the action fit in this context?
			    

			    // Only the one whose PoI matches the highest ranked one is going to be asserted to the oldest available believe
			    // The importance of a believe (in the sense of context) depend on the position it hold in the believe chain
			    // Most important believe is believe 0, then believe 1, and so on so forth
			    // Consistant actions will be asserted to the corresponding believe grounded in the ranked PoI and the believe order
			    
			    Vector consistentBelieves = getConsistentBelieves(actionToAssert, actor);
			    
			    if(consistentBelieves.size()>0){  // If so, the
				
				// @DEBUGGING
				System.out.println("Checking whether there is any active expectation\n");
				// @DEBUGGING
				
				Vector expectationForCurrentAction = expectations.getActiveExpectations(actionToAssert);
				
				// @DEBUGGING
				System.out.println("Current Expectations: "+expectationForCurrentAction);
				// @DEBUGGING
				
				
				// @DEBUGGING
				System.out.println("FOLLOWING_SINGLE_EXPECTATION = false\n ");
				// @DEBUGGING
				
				// Does any match previous active expectations?
				currentExpectations = expectations.getCommonSetOfExpectations(currentExpectations, expectationForCurrentAction);
				
				// @DEBUGGING
				System.out.println("Get common set of expecations = "+ currentExpectations +"\n");
				// @DEBUGGING
				
				
				// Is it a single expectation?
				if(currentExpectations.size()==1){
				    
				    // @DEBUGGING
				    System.out.println("Just one expectation\n");
				    // @DEBUGGING
				    
				    
				    FOLLOWING_SINGLE_EXPECTATION = true;
				    addActionToBelieve("believe "+ 0 + " for "+actor, actionToAssert, actor);
				    Vector temp = expectations.getActionsToCome(((String)currentExpectations.elementAt(0)));
				    boolean ERASED_ACTION_TO_ASSERT=false;
				    
				    for(int iterator = temp.size()-1; iterator>=0; iterator--){
					if(((String)temp.elementAt(iterator)) != actionToAssert)
					    actionsToCome.add(((String)temp.elementAt(iterator)));
					else{
					    if(!ERASED_ACTION_TO_ASSERT)
						ERASED_ACTION_TO_ASSERT=true;
					    else
						actionsToCome.add(((String)temp.elementAt(iterator)));
					}
					
				    }
				    
				    actionsToCome.remove(actionToAssert);
				    if(actionInExpectation(actionsToCome, splittedActions, actor, actionToAssert)){
					// @DEBUGGING
					System.out.println("action found in expectation\n");
					// @DEBUGGING
					
					break next_line;
				    }
				    // @DEBUGGING
				    System.out.println("Action to assert: "+actionToAssert+" Actions to come: "+actionsToCome+ " \n");
				    // @DEBUGGING
				    break;
				}//end if single expectation
				else{
				    
				    String foundAction;
				    
				    if(expectationForCurrentAction.size()>0 && actionInExpectation(actionsToCome, splittedActions, actor, actionToAssert)){
					System.out.println("Hola\n");
					break next_line;
				
				    }
				
				    actionsToCome = new Vector();
				    // @DEBUGGING
				    System.out.println("Recalculating actions to come: ");
				    // @DEBUGGING
				    				    
				    for(int expectationIt=0; expectationIt< currentExpectations.size();expectationIt++){
					Vector severalExpectations = new Vector();
					Vector temp = expectations.getActionsToCome(((String)currentExpectations.elementAt(expectationIt)));
					boolean ERASED_ACTION_TO_ASSERT=false;
					
					for(int iterator = temp.size()-1; iterator>=0; iterator--){
					    if(((String)temp.elementAt(iterator)) != actionToAssert)
						severalExpectations.add(((String)temp.elementAt(iterator)));
					    else{
						if(!ERASED_ACTION_TO_ASSERT)
						    ERASED_ACTION_TO_ASSERT=true;
						else
						    severalExpectations.add(((String)temp.elementAt(iterator)));
					    }
					}
					actionsToCome.add(severalExpectations);

				    }
				    
				    if(actionInExpectation(actionsToCome, splittedActions, actor, actionToAssert)){
				
					// @DEBUGGING
					System.out.println("Action found in multimple expectation\n");
					// @DEBUGGING	
					break next_line;
				    }
				    
				    //assertActionToAppropriateBelieve(consistentBelieves, actionToAssert, actor, bestRatedPoI);
				}// End else single expectation
				
								
			    } // End of if constisten action
			    else{
				// If we get to this point is beacuse the action has not been consistant with any of the existing context
				
				System.out.println("Action "+ actionToAssert + " was not consistant with any of the existing contexts");
				System.out.println("Action "+ actionToAssert + " is discarded");
			    }
			    // If we get here it is because we have already asserted the action into a consistent context
			    // we can then leave the inner loop that is checking all the believes looking for a consistent one
			    
			} // END FOR LOOP believes
			System.out.println("\n");
			
			/* CHECK LATER ABOUT HOW TO HANDLE THE EXPECTATIONS
			   String expectations = getExpectations(believe);
			   if(expectations!="null") //if there are any expectations regarding current believe?
			   assertExpectations(expectations);
			   
			*/
		    } // End of the if following single expectation
		} // END FOR 
		}
		System.out.println("Level of inheritance "+levelOfInheritance+"\n");
		
		// @DEBUGGING
		listBelieves(actor);
		// @DEBUGGING

		levelOfInheritance++;
		

	    } // END WHILE
	    
	    in.close();
	} catch (Exception e){//Catch exception if any //END TRY
	    System.err.println("Error: " + e.getMessage());
	}
    }

    public int getIndexOfAction(String actionToAssert, Vector splittedActions){
	for(int iterator =0; iterator< splittedActions.size(); iterator++){
	    if(actionToAssert == IXMAS_ACTIONS[(Integer)splittedActions.elementAt(iterator)])

	       return iterator;

	    
	}
	return 0;
	
    }

    public boolean actionInExpectation(Vector actionsToCome, Vector splittedActions, String actor, String actionToAssert){
	// More than actions in Vector? Is this a Vector of Vectors?
	// check if the action to assert is in all the expectations
	Vector actionsInATC = new Vector();
	String currentAct;
	int actionsToComeSize = actionsToCome.size();
	System.out.println("Qué hay en actionstocome "+actionsToCome);
	System.out.println("Qué hay en splitted "+splittedActions);
	while(actionsToCome.size()>0 && ((String)actionsToCome.getClass().getName()).indexOf("Vector")>-1){
	    next_expectation: {

	    for(int splittedIt=getIndexOfAction(actionToAssert, splittedActions); splittedIt<splittedActions.size(); splittedIt++){
		currentAct = IXMAS_ACTIONS[(Integer)splittedActions.elementAt(splittedIt)];
		actionsInATC = (Vector)actionsToCome.elementAt(actionsToCome.size()-1);
		System.out.println("Prueba "+ actionsInATC);
		for(int vectorIt=0; vectorIt < actionsInATC.size(); vectorIt++){
		    // @DEBUGGING
		    System.out.println("Exploring: "+ actionsInATC.elementAt(vectorIt)+" Action to assert "+ currentAct);
		    System.out.println("Checking action: "+((String)actionsInATC.elementAt(0))+" "+actionsInATC);
		    // @DEBUGGING
		    if(((String)actionsInATC.elementAt(vectorIt)).indexOf(currentAct) > -1){
			System.out.println("Encontrado\n");
			if(vectorIt>0){
			    if(!checkPreviouslyAssertedAction((String)actionsInATC.elementAt(vectorIt-1))){
				
				// @DEBUGGING
				System.out.println("Previous actions did not take place \n");
				// @DEBUGGING
				actionsInATC.remove(((String)actionsInATC.elementAt(0)));
				
				break next_expectation;
			    }
			}
			
			addActionToBelieve("believe "+ 0 + " for "+actor,((String)actionsInATC.elementAt(0)) , actor);
			// @DEBUGGING
			System.out.println("Expected action found at position "+vectorIt+"\n");
			System.out.println("Add action: "+((String)actionsInATC.elementAt(0)));
			// @DEBUGGING
			actionsInATC.remove(((String)actionsInATC.elementAt(0)));
			return true;
		    }// End of if
		    //TO FIX: else  is there any similar action
		    
		} // End of for actionsInATC
		if(actionsToCome.size()>0){

		    currentExpectations.remove(actionsToCome.size()-1);


		    if(currentExpectations.size()==1){
			FOLLOWING_SINGLE_EXPECTATION=true;
			actionsInATC = new Vector();
			actionsInATC = expectations.getActionsToCome((String)currentExpectations.elementAt(0));
		    }
		}
	    }
	    }

	}
	return false;
	
    }    
    
    public boolean checkPreviouslyAssertedAction(String action){
	System.out.println("Current: "+ previousAction);
	if(action==previousAction)
	    return true;
	else 
	    return false;
    }
    
    public void assertActionToAppropriateBelieve(Vector consistentBelieves, String actionToAssert, String actor, String bestRatedPoI){
	
	// Retrieve its PoI
	String currentActionPoI = getPoI(actionToAssert);
	  	
	// @DEBUGGING
	// System.out.println("The point of interest for the action: "+actionToAssert+" is "+currentActionPoI+"\n");
	// System.out.println("Checking consistency for the action: "+actionToAssert);
	// @DEBUGGING
	
	for(int consistentBelievesIterator=0; consistentBelievesIterator<consistentBelieves.size(); consistentBelievesIterator++){
	    String believeName = ((String)consistentBelieves.elementAt(consistentBelievesIterator));//"believe"+believeItera+"for"+actor; 
	    
	    //Check whether the PoI matches the highest ranked
	    if(currentActionPoI == bestRatedPoI && !listOfBlockedBelieves.contains(0)){ 
		
		//if so, it is considered candidate for being asserted to the current believe
		// This action goes to the main believe. Lowest number (zero) is the main believe
		addActionToBelieve("believe "+ 0 + " for "+actor, actionToAssert, actor);
		
		// @DEBUGGING
		System.out.println("Assert action to "+"believe "+ 0 + " for "+actor);
		// @DEBUGGING
		listOfBlockedBelieves.add(0);
		break;
		
	    }
	    else{ 
		
		// If action is consistant with the current believe, we assert the action
		
		if(!listOfBlockedBelieves.contains(consistentBelievesIterator)){
		    System.out.println("We are at this point with action "+actionToAssert);
		    addActionToBelieve(believeName, actionToAssert, actor);
		    listOfBlockedBelieves.add(consistentBelievesIterator);
		    
		    // @DEBUGGING
		    System.out.println("Assert action to "+believeName);
		    // @DEBUGGING
		}
		else{ //if it is already blocked is because this is the second round exploring believes
		    if(consistentBelievesIterator == believes.size()-1){ // a new believe has to be created
			// @DEBUGGING
			// System.out.println("Before creatining a new believe");
			// @DEBUGGING
			newBelieve(actor);
			believeName = ((String)believes.elementAt(consistentBelievesIterator+1));//because of the just created believe
			
			// @DEBUGGING
			// System.out.println("Before adding action to believe");
			// @DEBUGGING
			
			addActionToBelieve(believeName, actionToAssert, actor);
			listOfBlockedBelieves.add(consistentBelievesIterator+1);
			
			// @DEBUGGING
			System.out.println("Assert action to "+believeName);
			// @DEBUGGING
			break;
		    } //end if consistentBelievesIterator
		} // End of else the believe was blocked
	    }// End the PoI was not the highest rated
	} // End for loop for each existing believe 
    }

    public void listBelieves(String actor) {
	
	for(int i=0; i<believeRound-1; i++){
	    System.out.println("\n\n Listado de contenido del contexto believe "+i);
	    sendToScone("(list-context-contents (lookup-element {believe "+i +" for "+actor+"}))");
	    
	}
    }

    // TO FIX! check consistency with all the existing believes

    public Vector getConsistentBelieves( String actionToAssert, String actor){
	Vector consistentBelieves = new Vector();
	int believeIterator=believes.size();
	
	while(believeIterator > 0){
	    believeIterator--;
	    if(checkActionConsistency(((String)believes.elementAt(believeIterator)), actionToAssert,actor))
		{
		    consistentBelieves.add(((String)believes.elementAt(believeIterator)));
		    break;
		}
	    
	}
	return consistentBelieves;
	
    }

    public boolean checkActionConsistency(String believeName, String actionToAssert, String actor){
	
	// First thing to be done is to activate the context
	activateBelieve(believeName);
	boolean FAILED_CONSISTENCY = false;
	// For example, throw over head requires an object to be thrown. Has the actor first picked up the object to throw?
	// If so, the action is said to be consisten. On the contrary, the action is said to be inconsistant and therefore ignored
	// So first thing to check is whether the action to assert has any "involvements"

	String queryToCheckConsistency = sendToScone("(incoming-a-wires (lookup-element {"+actionToAssert+"}))");
	// ({events:punch requires walk (0-1806)} 
	// the required action starts after the 'requires' word and ends before the '(' symbol
	
	int startIndexOfAction = queryToCheckConsistency.indexOf("requires");
	int endIndexOfAction = startIndexOfAction;
	String requiredAction;
	if(startIndexOfAction > -1){
	    startIndexOfAction+=9; // We add 9 to jump just after the 'requires' word 
	    endIndexOfAction= queryToCheckConsistency.indexOf('(',startIndexOfAction )-1; 
	    requiredAction = queryToCheckConsistency.substring(startIndexOfAction, endIndexOfAction);

	    // At this point we know if there is any requirement.
	    // Now, let's determine whether or not the requirement is satisfy
	    // We are retrieving the parent wire for the element. If there is not element it is because the requirement is not satisfied
	    int previousLevelOfInheritance=levelOfInheritance;
	    while(previousLevelOfInheritance > 0){
		previousLevelOfInheritance--;
		String consistencyCheckedResult = sendToScone("(parent-wire (lookup-element {"+actor+" "+requiredAction+" at t"+ previousLevelOfInheritance+"}))");
		//String consistencyCheckedResult = sendToScone("(parent-wire (lookup-element {"+actor+" "+requiredAction+"}))");
		if(consistencyCheckedResult.indexOf("SCONE ERROR")>-1){
		    System.out.println("Action "+ actionToAssert +" required "+requiredAction+" to have previously taken place and it has not.");
		    FAILED_CONSISTENCY = true;
		}
		else {

		    // @DEBUGGINGç
		    System.out.println("Found a previous requirement for consistency \n");
		    // @DEBUGGINGç
		    
		    return true;
		}

		
	    }
	    if(FAILED_CONSISTENCY)
		return false;
	    else
		return true;
	}
	else //if we get there it is because there are not requirements stated for the action surveyed
	    return true;
    }

    public String getBelieve(int believeRound, String actionToAssert){
	return "TOFIX";
    }

    public String getExpectations(String believe){
	return "TOFIX";
    }

    public void assertExpectations(String expectations){

    }

    public String sendToScone(String query){
	String result = "nil";
	
	try{
	    SconeClientXML sc = new SconeClientXML(server, port);
	    try{
		result = sc.evalLisp(query);
	    }catch(Exception e){
		result = e.toString();
		System.out.println(e);
	    }
	    sc.close();
	}catch(Exception e){
	    result = e.toString();
	    System.out.println(e);
	}
	System.out.println(result);
	return result;
    }
    public List getListFrom(String answer){
	List result = new ArrayList();
	int index = 0;
	while((index = answer.indexOf('{', index))>0){
	    result.add(answer.substring(index, answer.indexOf('}', index)+1));
	    answer = answer.substring(answer.indexOf('}', index)+1);
	}
	return result;
    }

    public static void main(String[] args) {
	
	if (args.length > 0) {
	    try {
		// Process all files under directory results
		File resultsDirectory = new File(args[0]);
		String[] filesInDirectory = resultsDirectory.list();
		
		if(filesInDirectory==null)
		    System.out.println("Directory "+args[0]+" does not exist\n");
		else{
		    for(int i=0; i<filesInDirectory.length; i++){
			String fileName = filesInDirectory[i];
			if(fileName.indexOf("classified_result_0_400_hof_300_") != -1){
			    // Parsing files
			    ActionParsing actionParsingObject = new ActionParsing();
			    
			    // Check whether the provided file path ends in the / 
			    if(args[0].lastIndexOf("/")!= args[0].length()){
				System.out.println("Parsing file: "+ fileName +"\n");
				actionParsingObject.readActionsFromFile(resultsDirectory+"/"+fileName);
			    }
			    else{
				System.out.println("\nParsing file: "+ fileName+"\n");
				actionParsingObject.readActionsFromFile(resultsDirectory+fileName);
				
			    }
			    actionParsingObject.sendToScone("(list-context-contents (lookup-element {believe 0 for actor1}))");
			}
			
		    }
		}
	    }catch (NumberFormatException e) {
		System.err.println("The directory for the result files to parse must be provided as argument");
		System.exit(1);
		
	    }


	    }

	    /*
	    java.util.Date date= new java.util.Date();
	Timestamp contextTimeStamp = new Timestamp(date.getTime());
	ActionParsing actionParsingObject = new ActionParsing();
	actionParsingObject.sendToScone("(new-event-indv {actor 13 walk at "+contextTimeStamp+"} {walk})");
	actionParsingObject.checkActionConsistency("punch", "actor 13", contextTimeStamp);
	ActionParsing actionParsingObject = new ActionParsing();
	Vector v = new Vector();
	v.addElement("punch");
	
	v.addElement("sit down");

	Map result = actionParsingObject.getRankedPointsOfInterest(v);*/
	


	
    }
}