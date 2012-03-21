package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class ClassifiedResultFile{
    
    static final String[] IXMAS_ACTIONS = {"nothing", "check watch", "cross arms", "scratch head", "sit down", "get up", "turn around", "walk towards", "wave", "punch", "kick",  "point at", "pick up", "throw over head", "throw from bottom up"};

    public String fileName;
    public String lineToParse;
    public Vector actionsInCurrentParsedLine;
    public Estimation estimationForCurrentClassifiedResultFile; 
    public String actor;

    public ClassifiedResultFile(String fn){
	fileName = fn;
	actor = stripActorName();
	actionsInCurrentParsedLine = new Vector();
	estimationForCurrentClassifiedResultFile = new Estimation(actor);
    }



    public void analyzeClassifiedResultFile(){

	try{
	    FileInputStream fstream = new FileInputStream(fileName); //TO_FIX: path to files cannot be hard-coded
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    
	    if((strLine = br.readLine()) != null)
		estimationForCurrentClassifiedResultFile.setFirstActionInEstimation();
	    
	    while ((strLine = br.readLine()) != null && isAnActionLine(strLine))   {
		
		lineToParse = strLine;
		
		// @DEBUGGING
		System.out.println("Line to parse: "+lineToParse);
		// @DEBUGGING
		
		cleanUpActionsInCurrentParsedLine();
		actionsInCurrentParsedLine = stripActionsFromLineToParse();
		
		printOutActionList(actionsInCurrentParsedLine);
		
		estimationForCurrentClassifiedResultFile.newActionInEstimation(actionsInCurrentParsedLine);
	    }
	    estimationForCurrentClassifiedResultFile.setLastActionInEstimation();
	    in.close();
	} catch (Exception e){
	    System.err.println("Error: " + e.getMessage());
	}
	
    }
    public boolean isAnActionLine(String strLine){
	if(strLine.indexOf("Accuracy") <0)
	    return true;
	else
	    return false;
	
    }
    public void cleanUpActionsInCurrentParsedLine(){
	actionsInCurrentParsedLine = new Vector();
    }
	    

    public void setLineToParse(String ltp){
	lineToParse = ltp;
    }
    
    public String getLineToParse( ){
	return lineToParse;
    }

    public Vector getActionsFromLine(){
	return actionsInCurrentParsedLine;
    }
    public void setActionsFromLine(){

	int start =0;
	int end=lineToParse.indexOf(" ", 0);
	
	if(lineToParse.indexOf("a") <0){ //To get rid of the accuracy message at the end of the file. The three sentences contains letter 'a'
	    while(end !=-1){
		actionsInCurrentParsedLine.addElement(Integer.parseInt(lineToParse.substring(start, end)));
		start = end+1; 
		end = lineToParse.indexOf(" ", start);
	    }
	}
    }
    public String stripActorName(){
	return fileName.substring(fileName.lastIndexOf("_")+1,fileName.length());   
    }
    public Vector stripActionsFromLineToParse(){
	int start =0;
	int end=lineToParse.indexOf(" ", 0);
	
	
	if(lineToParse.indexOf("a") <0){ //To get rid of the accuracy message at the end of the file. The three sentences contains letter 'a'
	    while(end !=-1){
		Action tempAction = new Action(IXMAS_ACTIONS[Integer.parseInt(lineToParse.substring(start, end))], actor);
		actionsInCurrentParsedLine.add(tempAction);
		
		start = end+1; 
		end = lineToParse.indexOf(" ", start);
	    }
	}
	

	return actionsInCurrentParsedLine;
    }
    public void printOutActionList(Vector actionListToPrint){
	for(int iter=0; iter< actionListToPrint.size(); iter++){
	    // @DEBUGGING
	    System.out.println(((Action)actionListToPrint.elementAt(iter)).actionName+" ");
	    // @DEBUGGING
	}
	
	System.out.println("\n");
    }

    public void printOutBelievesInEstimation(){

	for(int iter=0; iter< estimationForCurrentClassifiedResultFile.consideredBelieves.size(); iter++){
	    
	    Believe tempBelieve = ((Believe) estimationForCurrentClassifiedResultFile.consideredBelieves.elementAt(iter));
	    System.out.println("*********** PRINTING BELIEVE: "+tempBelieve.believeName+"************");

	    String contentOfBelieve = tempBelieve.sfObject.sendToScone("(list-context-contents (lookup-element {"+tempBelieve.believeName +"}))");
	    nicelyFormatContentOfBelieve(contentOfBelieve);
	    System.out.println("\n");
	    
	}
	
    }
      // @DEBUGGING
    public void nicelyFormatContentOfBelieve(String contentOfBelieve){
	int start = contentOfBelieve.indexOf("{events:");
	int end = start;
	String element = null;
	Vector contents = new Vector();	
	while(start > -1){
	    start += "{events:".length();
	    end = contentOfBelieve.indexOf('(',start);
	    element = contentOfBelieve.substring(start, end);
	    contents.add(element);
	    start = contentOfBelieve.indexOf("{events:", end);	    
	}
	System.out.println( "\n\n\n");
	if(contents.size()>0){
	    for(int i=contents.size()-1; i>=0; i--)
		System.out.println(contents.elementAt(i));
	}
    }
    // @DEBUGGING

}