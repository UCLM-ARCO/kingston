package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class SconeFacade {
    static final String server = "localhost";
    static final int port=5000;

    
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

    public String stripRequiredAction(String queryResult){
	int startIndexOfAction = queryResult.indexOf("requires");
	int endIndexOfAction = startIndexOfAction;
	String requiredAction = null;

	if(startIndexOfAction > -1){
	    startIndexOfAction+= "requires".length();
	    endIndexOfAction= queryResult.indexOf('(',startIndexOfAction )-1; 
	    requiredAction = queryResult.substring(startIndexOfAction, endIndexOfAction);
	}
	return requiredAction;
    }

    public boolean isAnErrorMessage(String message){
	if(message.indexOf("SCONE ERROR")>-1)
	    return true;
	else
	    return false;
	
    }

    public String stripPoI(String queryResult){
	int startIndexOfPoI = queryResult.indexOf("has point of interest in");
	int endIndexOfPoI = startIndexOfPoI;
	
	if(startIndexOfPoI > -1){
	    startIndexOfPoI+="has point of interest in".length();
	    endIndexOfPoI= queryResult.indexOf('(',startIndexOfPoI)-1; 
	}		    	    
	return queryResult.substring(startIndexOfPoI, endIndexOfPoI);
    }
}