package parser;

import java.lang.String;
import java.util.*;
import java.io.*;
import java.util.Date;
import java.sql.Timestamp;

public class ActionParsing 
{
    static final String server = "localhost";
    static final int port=5000;
    static final String[] IXMAS_ACTIONS = {"Nothing", "check watch", "cross arms", "scratch head", "sit down", "get up", "turn around", "walk towards", "wave", "punch", "kick",  "point at", "pick up", "throw over head", "throw from bottom up"};
    
    // TO FIX: this has to be deleted
    public int believeRound;    // It counts the number of beliefs held at any time
    
    public Vector believes;
    
    int levelOfInheritance;

    public ActionParsing(){
	believes = new Vector();
	believeRound = 0;
	levelOfInheritance=0;
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
    public void assertFirstActionInFirstBelieve(String actor, Vector splittedActions, Timestamp contextTimeStamp){
	
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
    
    public void addActionToBelieve(String believeName, String actionToAssert, String actor, Timestamp contextTimeStamp){
	
	sendToScone("(in-context {"+believeName+"})");	
	
	// We assert the new fact the just created and activated new context
	sendToScone("(new-event-indv {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actionToAssert+"})");
	//sendToScone("(new-event-indv {"+actor+" "+actionToAssert+"} {"+actionToAssert+"})");
	sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+" at t"+levelOfInheritance +"} {"+actor+"})");
	//sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+"} {"+actor+"})");
	
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
	    // Read File Line By Line
	    // Each line is a level of context inheritance
	    while ((strLine = br.readLine()) != null)   {
		
		ArrayList listOfBlockedBelieves = new ArrayList();

		// Iterator that tracks the number of actions that are in each of the lines of the file
		splittedActionIterator = 0;
		
		// Get the timestamp simulating the timestamp holded by a real-time system
		java.util.Date date= new java.util.Date();
		Timestamp contextTimeStamp = new Timestamp(date.getTime());
		
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


		// Do for each action
		for(splittedActionIterator=0; splittedActionIterator < splittedActions.size(); splittedActionIterator++){
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
			assertFirstActionInFirstBelieve(actor, splittedActions,  contextTimeStamp);
			
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
		
			
			
			Vector consistentBelieves = getConsistentBelieves(actionToAssert, actor, contextTimeStamp);
			
			if(consistentBelieves.size()>0){  // If so, the
			    	
			    // Retrieve its PoI
			    String currentActionPoI = getPoI(actionToAssert);
			    
			    // @DEBUGGING
			    System.out.println("The point of interest for the action: "+actionToAssert+" is "+currentActionPoI+"\n");
			    System.out.println("Checking consistency for the action: "+actionToAssert);
			    // @DEBUGGING
			    
			    for(int consistentBelievesIterator=0; consistentBelievesIterator<consistentBelieves.size(); consistentBelievesIterator++){
				believeName = ((String)consistentBelieves.elementAt(consistentBelievesIterator));//"believe"+believeIterator+" for"+actor; 
				
				//Check whether the PoI matches the highest ranked
				if(currentActionPoI == bestRatedPoI && !listOfBlockedBelieves.contains(0)){ 
				    
				    //if so, it is considered candidate for being asserted to the current believe
				    // This action goes to the main believe. Lowest number (zero) is the main believe
				    addActionToBelieve("believe "+ 0 + " for "+actor, actionToAssert, actor, contextTimeStamp);
				    
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
					addActionToBelieve(believeName, actionToAssert, actor, contextTimeStamp);
					listOfBlockedBelieves.add(consistentBelievesIterator);
					
					// @DEBUGGING
					System.out.println("Assert action to "+believeName);
					// @DEBUGGING
					break;
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

					    addActionToBelieve(believeName, actionToAssert, actor, contextTimeStamp);
					    listOfBlockedBelieves.add(consistentBelievesIterator+1);
					    
					    // @DEBUGGING
					    System.out.println("Assert action to "+believeName);
					    // @DEBUGGING
					    break;
					}
				    }
				}
			    }

			    
			}
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
		} // END FOR 
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
    

    public void listBelieves(String actor) {
	
	for(int i=0; i<believeRound-1; i++){
	    System.out.println("\n\n Listado de contenido del contexto believe "+i);
	    sendToScone("(list-context-contents (lookup-element {believe "+i +" for "+actor+"}))");
	    
	}
    }

    // TO FIX! check consistency with all the existing believes

    public Vector getConsistentBelieves( String actionToAssert, String actor, Timestamp contextTimeStamp){
	Vector consistentBelieves = new Vector();
	int believeIterator=believes.size();
	
	while(believeIterator > 0){
	    believeIterator--;
	    if(checkActionConsistency(((String)believes.elementAt(believeIterator)), actionToAssert,actor,contextTimeStamp))
		{
		    consistentBelieves.add(((String)believes.elementAt(believeIterator)));
		    break;
		}
	    
	}
	return consistentBelieves;
	
    }

    public boolean checkActionConsistency(String believeName, String actionToAssert, String actor, Timestamp contextTimeStamp){
	
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