;;; ACTION FILE

;; getExpectationsOfAction method
(incoming-a-wires (lookup-element {"actionName"}))

;; getRequiredAction method
(incoming-a-wires (lookup-element {"actionName"}))

;; setPoI method
(incoming-a-wires (lookup-element {"actionName"}))

;; setRequiredAction method
(incoming-a-wires (lookup-element {"actionName"}))


;;; ACTIONPARSING FILE

;; assertFirstActionInFirstBelieve method
(in-context {general})
(new-indv {"actor"} {person})
(new-statement {"actor"} {is in} {test room})
(new-context {believe 0 for "actor"})
(in-context {believe 0 for "actor"})
(new-is-a {"actor" "actionToAssert" at t"levelOfInheritance"} {"actionToAssert"})
(x-is-the-y-of-z {"actor"} {agent} {"actor" "actionToAssert" at "levelOfInheritance"})

;; getPoI method
(incoming-a-wires (lookup-element {"action"}))

;; newBelieve method
(in-context {general})
(new-context {"believeName"})
(new-is-a {"believeName"} {"parentBelieve"})
(in-context {"believeName"})

;; getBelieve method
(in-context {"believeName"})

;; activateBelieve method
(in-context {"believeName"})

;; addActionToBelieve method
(in-context {"believeName"})
(new-is-a {"actor" "actionToAssert" at t"levelOfInheritance"} {"actionToAssert"})
(x-is-the-y-of-z {"actor"} {agent} {"actor" "actionToAssert" at t"levelOfInheritance"})

;; listBelieves method
(list-context-contents (lookup-element {believe "i" for "actor"}))

;; checkActionConsistency method
(incoming-a-wires (lookup-element {"actionToAssert"})
(parent-wire (lookup-element {"actor" "requiredAction" at t"previousLevelOfInheritance}))

;; main method
(list-context-contents (lookup-element {believe 0 for actor1}))


;;; BELIEVE FILE

;; setActionInBelieveAtCurrentTimeInstant method
(in-context {"believeName"})
(new-is-a {"actor" "actionToAssert at t"currentTimeInstant} {"actionToAssert"}))
(x-is-the-y-of-z {"actor"} {agent} {"actorName" "actionToAssert" at t"currentTimeInstant"})

;; setFirstActionInMainBelieve method
(new-is-a {"actor" "actionToAssert" at t"currentTimeInstant"} {"actionToAssert"})
(x-is-the-y-of-z {"actor"} {agent} {"actor" "actionToAssert" at t0})

;; setLastActionInMainBelieve method
(new-is-a {"actor" "actionToAssert" at t"currentTimeInstant"} {"actionToAssert"})
(x-is-the-y-of-z {"actor"} {agent} {"actor" "actionToAssert" at t0})

;; removeLastAction method
(in-context {believe 0 for "actor"})
(remove-element {"lastAction"})

;; doesActionExistInBelieveAtCurrentTimeInstant method
(list-context-contents *context*)

;; activateBelieve method
(in-context {"believeName"})


;;; CSAR FILE 
;; This file haven't calls to Scone


;;; CLASSIFIEDRESULT FILE

;; printOutBelievesInEstimation method
(list-context-contents (lookup-element {"believe"}))


;;; ESTIMATION FILE

;; createMainBelieve method
(in-context {general})
(new-indv {"actor"} {person})
(new-statement {"actor"} {is in} {test room})
(new-context {believe 0 for "actor"})
(in-context {believe 0 for "actor"})

;; activateMainBelieve method
(in-context {"currentBelieve"})

;; activateNextBelieve method
(in-context {"currentBelieve"})

;; createNewBelieveInEstimation method
(in-context {general})
(new-context {"believeName"})

;; setCurrentBelieve method
(in-context {"believeName"})

;; deleteCurrentActionIfInconsistent method
(list-context-contents (lookup-element {"believe"}))


;;; EXPECTATION FILE

;; getActionsInExpectations method
(list-all-x-of-y  {has expectation} {"expectationName"})
