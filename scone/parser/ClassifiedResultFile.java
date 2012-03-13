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
	    
	    while ((strLine = br.readLine()) != null)   {
		
		// @DEBUGGING
		System.out.println("\nParsing next line. ");
		// @DEBUGGING

		lineToParse = strLine;
		
		// @DEBUGGING
		System.out.println("Line to parse: "+lineToParse);
		// @DEBUGGING
		
		actionsInCurrentParsedLine = stripActionsFromLineToParse();
		
		
		// @DEBUGGING
		System.out.println("Actions in current parsed line: "+ actionsInCurrentParsedLine);
		// @DEBUGGING

		estimationForCurrentClassifiedResultFile.newActionInEstimation(actionsInCurrentParsedLine);
	    }
	    
	    in.close();
	} catch (Exception e){
	    System.err.println("Error: " + e.getMessage());
	}
	
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
		// @DEBUGGING
		System.out.println("\nParsed number: " +IXMAS_ACTIONS[Integer.parseInt(lineToParse.substring(start, end))] +"  "+ actor);
		// @DEBUGGING
	
		Action tempAction = new Action(IXMAS_ACTIONS[Integer.parseInt(lineToParse.substring(start, end))], actor);
		actionsInCurrentParsedLine.add(tempAction);
	
		start = end+1; 
		end = lineToParse.indexOf(" ", start);
	    }
	}

	// @DEBUGGING
	System.out.println("\nBefore parsing actions to text: " +actionsInCurrentParsedLine );
	// @DEBUGGING
	

	return actionsInCurrentParsedLine;
    }
}