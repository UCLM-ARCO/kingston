package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class ClassifiedResultFile{
    

    public String fileName;
    public String lineToParse;
    public Vector actionsInCurrentParsedLine;
    public Estimation estimationForCurrentClassifiedResultFile; 

    public ClassifiedResultFile(String fn){
	fileName = fn;
	actionsInCurrentParsedLine = new Vector();
	estimationForCurrentClassifiedResultFile = new Estimation(stripActorName());
    }



    public void analyzeClassifiedResultFile(){

	try{
	    FileInputStream fstream = new FileInputStream(fileName); //TO_FIX: path to files cannot be hard-coded
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    // Read File Line By Line
	    // Each line is a level of context inheritance
	    
	    // Parse the first line to force first action to be "walking towards"
	    if((strLine = br.readLine()) != null)
		estimationForCurrentClassifiedResultFile.setFirstActionInEstimation();
	    
	    // Parse the rest of lines of the the classified result file
	    while ((strLine = br.readLine()) != null)   {
		lineToParse = strLine;
		actionsInCurrentParsedLine = stripActionsFromLineToParse();
		estimationForCurrentClassifiedResultFile.setConsideredActions(actionsInCurrentParsedLine);
	    }// END WHILE
	    
	    in.close();
	} catch (Exception e){//Catch exception if any //END TRY
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
		actionsInCurrentParsedLine.addElement(Integer.parseInt(lineToParse.substring(start, end)));
		start = end+1; 
		end = lineToParse.indexOf(" ", start);
	    }
	}
	return actionsInCurrentParsedLine;
    }
}