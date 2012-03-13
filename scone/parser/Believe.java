package parser;

import java.lang.String;
import java.util.*;
import java.io.*;

public class Believe
{
    public String believeName;
    public Vector composingActions;
    public Action previousAction;
    public int levelOfInheritance;
    public SconeFacade sfObject;
    public int currentTimeInstant;

    public Believe(int loi){
	composingActions = new Vector();
	previousAction = new Action();
	levelOfInheritance = loi;
	sfObject = new SconeFacade();

    }

    public Believe(String bn, int loi){

	believeName = bn;
	composingActions = new Vector();
	previousAction = new Action();
	levelOfInheritance = loi;
    }

    public Vector getActionsInBelieve(){
	
	return composingActions;
    }


    public void  setActionInBelieveAtCurrentTimeInstant(Action actionToAssert, int cti){
	currentTimeInstant = cti;
	String actor = actionToAssert.actor;
	sfObject.sendToScone("(in-context {"+believeName+"})");	

	// @DEBUGGING
	System.out.println("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	// @DEBUGGING
	
	sfObject.sendToScone("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert.actionName+"})");
	sfObject.sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actor+"})");
	previousAction= actionToAssert;

    }


    public void setFirstActionInMainBelieve(String actor){
	currentTimeInstant =  0;

	Action actionToAssert = new Action(actor, "walk towards");

	// We assert the new fact the just created and activated new context
	sfObject.sendToScone("(new-event-indv {"+actor+" "+actionToAssert.actionName+" at t"+currentTimeInstant +"} {"+actionToAssert+"})");
	sfObject.sendToScone("(the-x-of-y-is-z {agent} {"+actor+" "+actionToAssert.actionName+" at t0} {"+actor+"})");
	previousAction= actionToAssert;
    }
    
    public boolean doesActionExistInBelieveAtCurrentTimeInstant(Action requiredAction, int cti){
	currentTimeInstant = cti;
	for(int decreaseCurrentTimeInstant=0;  decreaseCurrentTimeInstant <4;  decreaseCurrentTimeInstant++){
	    String checkActionAtTimeInstant = sfObject.sendToScone("(parent-wire (lookup-element {"+requiredAction.actor+" "+requiredAction.actionName+" at t"+ currentTimeInstant+"}))");
	    if(!sfObject.isAnErrorMessage(checkActionAtTimeInstant))
		return true;
	}
	
	return false;
	
	
    }
}