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
    
    public void readActionsFromFile(String fileName){

	int actionNumber;
	
	try{
	    FileInputStream fstream = new FileInputStream(fileName); //TO_FIX: path to files cannot be hard-coded
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    boolean FIRST_ACTION= true; //use to check whenever first action is not walk

	    // Extract name of the actor out of the file name. Recall that the last word of the file is the actor name
	    String actor = fileName.substring(fileName.lastIndexOf("_")+1,fileName.length());
	    
	    //Read File Line By Line
	    while ((strLine = br.readLine()) != null)   {
		int start =0;
		int end=strLine.indexOf(" ", 0);
		
		if(strLine.indexOf("a") <0){ //To get rid of the accuracy message at the end of the file. The three sentences contains letter 'a'
		    // For each line, a new context is created
		    java.util.Date date= new java.util.Date();
		    Timestamp contextTimeStamp = new Timestamp(date.getTime());
		    sendToScone("(in-context {general})");
		    sendToScone("(in-context (new-context {"+contextTimeStamp +"}))");
		    
		    
		    while(end !=-1){
			actionNumber= Integer.parseInt(strLine.substring(start, end));
			String actionToAssert = IXMAS_ACTIONS[actionNumber];
			// When the first action is walking that means that the actor in entering the room
			if(actionNumber==7 && FIRST_ACTION){
			    // Let's describe the explicit knowledge involved in the first action being walk
			    // First thing that need to be done is creating a node for the appearing actor

			    // TO-FIX: capturing errors
			    sendToScone("(new-indv {"+actor+"} {person})");
			    sendToScone("(new-statement {"+actor+"} {is in} {test room})");
			    FIRST_ACTION = false;
			    			    
			}


			sendToScone("(new-event-indv {"+actor+" "+actionToAssert+" at "+contextTimeStamp +"} {"+actionToAssert+"})");
			sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert+" at "+contextTimeStamp +"} {"+actor+"})");
			start = end+1; 
			end = strLine.indexOf(" ", start);
		    }
		}
		// If at this point FIRST_ACTION is still false, we should correct that manually
		if(FIRST_ACTION){
		    System.out.println("The first action should be that of the actor entering the room\n");
		    
		    // TO-FIX: capturing errors
		    sendToScone("(new-indv {"+actor+"} {person})");
		    sendToScone("(new-statement {"+actor+"} {is in} {test room})");
		    FIRST_ACTION = false;
		    
		}
		    

		System.out.println("\n");
	    }
	    
	    in.close();
	}catch (Exception e){//Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	}
    }

    public String sendToScone(String query){
	String result = "nil";
	
	try{
	    SconeClientXML sc = new SconeClientXML(server, port);
	    try{
		result = sc.evalLisp(query);
	    }catch(Exception e){
		System.out.println(e);
	    }
	    sc.close();
	}catch(Exception e){
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
			}
		
		    }
		}
	    }catch (NumberFormatException e) {
		System.err.println("The directory for the result files to parse must be provided as argument");
		System.exit(1);
		
	    }


	}
    }
}