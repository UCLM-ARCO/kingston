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
    public Vector ixmasActionsInVector;
    public Vector contents;

    public ClassifiedResultFile(String fn){
	fileName = fn;
	actor = stripActorName();
	actionsInCurrentParsedLine = new Vector();
	estimationForCurrentClassifiedResultFile = new Estimation(actor);
	ixmasActionsInVector = new Vector();
	for(int i=0; i<15; i++){
	    ixmasActionsInVector.add(IXMAS_ACTIONS[i]);
	    
		// @DEBUGGING
	    System.out.println("Action "+i+" "+ ixmasActionsInVector.elementAt(i));
		// @DEBUGGING
		
	}
	contents = new Vector();	
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
	    if(tempBelieve.believeName.indexOf("believe 0")>-1){
		BufferedWriter writerToFile = createFileToWriteResults();
		nicelyFormatContentOfBelieveToAFile(contentOfBelieve, writerToFile);
		try{
		    writerToFile.close();
		    
		}catch (Exception e){//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		}
		
	    }
	    else
		nicelyFormatContentOfBelieveToStandardOutput(contentOfBelieve);
	    
	    System.out.println("\n");
	    
	}

    }
      // @DEBUGGING
    public BufferedWriter createFileToWriteResults(){
	
	try{
	    FileWriter fstream = new FileWriter("classificationResults/"+actor+".txt");
	    return new BufferedWriter(fstream);
	}catch (Exception e){//Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	    return null;
	}
    }
    
    public void nicelyFormatContentOfBelieveToAFile(String contentOfBelieve, BufferedWriter writerToFile){
	int start = contentOfBelieve.indexOf("{events:");
	int end = start;
	String element = null;
		
	while(start > -1){
	    String beginingOfString = "{events:"+actor+" is the agent of "+actor+" ";
	    start += beginingOfString.length();
	    end = contentOfBelieve.indexOf(" at t", start);
	    element = contentOfBelieve.substring(start, end);
	    contents.add(element);
	    start = contentOfBelieve.indexOf("{events:", end);	    
	}
	
	try{
	    if(contents.size()>0){
		for(int i=contents.size()-1; i>=0; i--){
		    System.out.println(contents.elementAt(i));
		    writerToFile.write(ixmasActionsInVector.indexOf(contents.elementAt(i))+"\n");
		}
	    }
	    writeLineWithNewClassificationResults(writerToFile);
	    writeLineWithOriginalClassificationResults(writerToFile);
	
	}catch (Exception e){//Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	}
    }
    // @DEBUGGING


    public void nicelyFormatContentOfBelieveToStandardOutput(String contentOfBelieve){
	int start = contentOfBelieve.indexOf("{events:");
	int end = start;
	String element = null;

	while(start > -1){
	    String beginingOfString = "{events:"+actor+" is the agent of "+actor+" ";
	    start += beginingOfString.length();
	    end = contentOfBelieve.indexOf("at t", start);
	    element = contentOfBelieve.substring(start, end);
	    contents.add(element);
	    start = contentOfBelieve.indexOf("{events:", end);	    
	}
	
	try{
	    if(contents.size()>0){
		for(int i=contents.size()-1; i>=0; i--){
		    System.out.println(contents.elementAt(i));
		    //System.out.println(contents.elementAt(i)+"\n");
		}
	    }
	}catch (Exception e){//Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	}
    }
    public void writeLineWithOriginalClassificationResults(BufferedWriter writerToFile){
	try{
	    
	    FileInputStream fstream = new FileInputStream(fileName); //TO_FIX: path to files cannot be hard-coded
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    while ((strLine = br.readLine()) != null && strLine.indexOf("Accuracy")<0);
	    
	    writerToFile.write("\n");
	    writerToFile.write(strLine);
	    in.close();
	}catch (Exception e){//Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	}
	
    }
    public void writeLineWithNewClassificationResults(BufferedWriter writerToFile){
	try{
	    String pathToTruth = "/home/mjsantof/repo/mariajose.santofimia/kingston/BoW/videos/truth/"+actor+"_frame_segmented.txt";
	    System.out.println("Path to file: "+pathToTruth);
	    FileInputStream fstream = new FileInputStream(pathToTruth); //TO_FIX: path to files cannot be hard-coded
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    int iterator = 0;
	    int sumOfIncorrectActions=0;
	    while ((strLine = br.readLine()) != null && iterator<contents.size() ){
		String act = stripAction(strLine);
		
		System.out.println(act+" ");
		if(Integer.parseInt(act) == ixmasActionsInVector.indexOf(contents.elementAt(iterator)))
		    sumOfIncorrectActions++;
		iterator++;
	    }
	    float result = (((float)(iterator-sumOfIncorrectActions))/((float)iterator))*100;
	    writerToFile.write("Accuracy after reasoning = "+result +"% ("+(iterator-sumOfIncorrectActions)+"/"+iterator+") (classification)");
	    in.close();



	}catch (Exception e){//Catch exception if any
	    System.err.println("Error: " + e.getMessage());
	}
		
    }
    public String stripAction(String lineToParse){
	
	int firstWhiteSpace =lineToParse.indexOf(' ');
	int secondWhiteSpace=lineToParse.indexOf(' ', firstWhiteSpace+1);
	return lineToParse.substring(secondWhiteSpace+1);
	
    }
}