package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class CSAR
{
    public String filePath;
    public File directoryName;
    public Vector filesInDirectory;
    public SconeFacade sfObject;
    
    public CSAR(String pathToDir){
	filePath = pathToDir;
	directoryName = new File(pathToDir);
	filesInDirectory = new Vector(Arrays.asList(directoryName.list()));
	removeFilesFromList();
	sfObject = new SconeFacade();

    }

    // This function adds the "/" at the end of the path if the symbol hasn't be provided
    public String getFilePath(String fileName){
	// Check whether the provided file path ends in the / 
	if(filePath.lastIndexOf("/")!= filePath.length())
	    return directoryName+"/"+fileName;
	else
	    return directoryName+fileName;
	    
    }

    // This function removes those files from dir that are not "classified result"
    public void removeFilesFromList(){
	Vector filesToRemove = new Vector();
	for(int i=0; i<filesInDirectory.size(); i++){
	    String fileName = filesInDirectory.elementAt(i).toString();

	    if(fileName.indexOf("classified_result_0_400_hof_300_") ==-1)		
		filesToRemove.add(fileName);
	    
	}
	for(int i=0; i<filesToRemove.size(); i++)
	    filesInDirectory.remove(filesToRemove.elementAt(i));

    }

    public static void main(String[] args) {
	
	if (args.length > 0) {
	    CSAR csarObject = new CSAR(args[0]);
	    try {
		// Process all files under directory results
		if(csarObject.filesInDirectory==null)
		    System.out.println("Directory "+args[0]+" does not exist\n");
		else{
		    for(int i=0; i<csarObject.filesInDirectory.size(); i++){
			String fileName = csarObject.filesInDirectory.elementAt(i).toString();
			System.out.println("Parsing file: "+ fileName +"\n");
			ClassifiedResultFile crfObject = new ClassifiedResultFile(csarObject.getFilePath(fileName));
			// @DEBUGGING
			System.out.println("Parsing file: "+ csarObject.getFilePath(fileName) +"\n");
			// @DEBUGGING
			
			crfObject.analyzeClassifiedResultFile();

			// @DEBUGGING
			System.out.println("*************************************************");
			System.out.println("Content of the considered believes");
			System.out.println("*************************************************");
			// @DEBUGGING
			
			crfObject.printOutBelievesInEstimation();

		    }
		    
		}
	    }catch (NumberFormatException e) {
		System.err.println("The directory for the result files to parse must be provided as argument");
		System.exit(1);
		
	    }
	    
	}
    }
  
    
}