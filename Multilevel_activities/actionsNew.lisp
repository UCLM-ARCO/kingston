;;;*******************************************************
;;;** --------------------------------------------------**
;;;**  RELEVANT ACTIONS INVOLVED IN THE ANALYZED VIDEOS **
;;;** This file defines and describes some of the       **
;;;** actions that might take place in the context of a **
;;;** room. 					        **
;;;** REQUIRES: move.lisp			        **
;;;** --------------------------------------------------**
;;;*******************************************************


(in-context {general})

;;; Some roles required for the following events and actions
(new-type-role {name} {person} {thing})
(new-type-role {person location} {person} {place})
(new-type-role {object location} {thing} {place})
(new-type-role {body part} {person} {thing})
(new-indv-role {arm} {person} {body part} :n 2)
(new-type {left arm} {arm})	 
(new-type {right arm} {arm})	
(new-indv-role {leg} {person} {body part} :n 2)
(new-type {left leg} {leg})	 
(new-type {right leg} {leg})	
(new-type {left foot} {foot})	 
(new-type {right foot} {foot})
(new-type-role {head} {person} {body part} :n 1)
(new-type-role {hand} {person} {body part} :n 2)
(new-type {left hand} {hand})	 
(new-type {right hand} {hand})	
(new-type-role {feet} {person} {body part} :n 1)
(new-type-role {torso} {person} {body part} :n 1)
(new-type-role {kness} {person} {body part} :n 2)
(new-type-role {fist} {person} {body part} :n 2)
(new-type-role {hand finger} {person} {body part} :n 10)
(new-type-role {both hands} {person} {body part} :n 1)
(new-type {position} {place})

(new-type {clock} {thing})


;; RELATIONS APPLIED TO ACTIONS
(new-relation {has point of interest in} 
		:a-inst-of {event}
		:b-inst-of {body part})

(new-relation {is located at}
		:a-inst-of {thing}
		:b-inst-of {location})

(new-relation {has position}
		:a-inst-of {thing}
		:b-inst-of {position})

(new-relation {is opposite action of}
		:a-inst-of {event}
		:b-inst-of {event}
		:symmetric t) 
	      
(new-relation {performs the action of}
		:a-inst-of {animated object}
		:b-inst-of {event})
	       
(new-relation {is performed by}
		:a-inst-of {event}
		:b-inst-of {animated object})



;; DEFINITIONS DESCRIBING THE ACTION OF CHECKING THE TIME IN A WATCH (ACTION 1 FROM IXMAS)
(in-context {general})
(new-relation {is aware of}
		:a-inst-of {person}
		:b-inst-of {thing})

(new-relation {moves body part} 
		:a-inst-of {person}
		:b-inst-of {body part})

(new-action-type {reach}
		:agent-type {person}
		:object-type {thing})

(new-action-type {reach clock}
		:agent-type {person}
		:object-type {clock})
	      
(new-is-a {reach clock} {reach})	      
     
(new-relation {see clock}
		:a-inst-of {person}
		:b-inst-of {object})
	      
(new-action-type {check time} 
		:agent-type {person}
		:object-type {time})	      			      
			
(new-context {check time BC} {general})
(new-is-a {check time BC} {before context})
(x-is-the-y-of-z {check time BC} {before context} {check time})

(new-context {check time AC} {general})
(new-is-a {check time AC} {after context})
(x-is-the-y-of-z {check time AC} {after context} {check time})

(new-type {checking person} {person})
(x-is-the-y-of-z {checking person} {agent-type} {check time})	

(in-context {check time BC})
(new-not-statement {checking person} {moves body part} {arm})
(new-not-statement {checking person} {performs the action of} {reach clock})
(new-not-statement {checking person} {performs the action of} {see clock})
(new-not-statement {checking person} {is aware of} {check time})

(in-context {check time AC})
(new-statement {checking person} {moves body part} {arm})
(new-statement {checking person} {performs the action of} {reach clock})
(new-statement {checking person} {performs the action of} {see clock})
(new-statement {checking person} {is aware of} {check time})

(in-context {general})
(new-statement {check time} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF CROSSING ARMS (ACTION 2 FROM IXMAS)
(in-context {general})
(new-relation {is in direct contact to}
		:a-inst-of {thing}
		:b-inst-of {thing})

(new-relation {approaches}
		:a-inst-of {thing}
		:b-inst-of {thing})

(new-relation {is closed to}
		:a-inst-of {thing}
		:b-inst-of {thing}
		:symmetric t)
 
(new-relation {has crossed}
		:a-inst-of {thing}
		:b-inst-of {thing})
	      
(new-relation {has reached body part}
		:a-inst-of {person}
		:b-inst-of {body part})
	               
(new-action-type {cross arms} 
		:agent-type {person})
		 
(new-type {crossing arms person} {person})
(x-is-the-y-of-z {crossing arms person} {agent-type} {cross arms})	
(new-type {crosser} {crossing arms person})
(new-type {arms to cross} {arm})	 
(new-indv-role {left arm to cross} {arm} {crossing arms person})
(new-indv-role {right arm to cross} {arm} {crossing arms person})	

(new-context {cross arms BC} {general})
(new-is-a {cross arms BC} {before context})
(x-is-the-y-of-z {cross arms BC} {before context} {cross arms})

(new-context {cross arms AC} {general})
(new-is-a {cross arms AC} {after context})
(x-is-the-y-of-z {cross arms AC} {after context} {cross arms})

(in-context {cross arms BC})
(new-not-statement {crossing arms person} {has reached body part} {right arm})
(new-not-statement {crossing arms person} {has reached body part} {left arm})
(new-not-statement {crossing arms person} {moves body part} {right arm})
(new-not-statement {crossing arms person} {moves body part} {left arm})
(new-not-statement {crossing arms person} {has crossed}  {arms to cross})
(new-statement {left arm} {approaches} {right arm})
(new-statement {right arm} {approaches} {left arm})
(new-statement {left arm} {is closed to} {right arm})

(in-context {cross arms AC})
(new-statement {crossing arms person} {has reached body part} {right arm})
(new-statement {crossing arms person} {has reached body part} {left arm})
(new-statement {crossing arms person} {moves body part} {right arm})
(new-statement {crossing arms person} {moves body part} {left arm})
(new-statement {crossing arms person}  {has crossed}  {arms to cross})
(new-statement {left arm} {is in direct contact to} {right arm})

(in-context {general})
(new-statement {cross arms} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF SCRATCHING HEAD (ACTION 3 FROM IXMAS)
(in-context {general})     
(new-relation {put hand in body part}
		:a-inst-of {person}
		:b-inst-of {body part})
     
(new-relation {has scratched}
		:a-inst-of {animated object}
		:b-inst-of {object})	      	      	      	      

(new-action-type {scratch head} 
		:agent-type {animated object}
		:object-type {head})

(new-action-type {reach head}
		:agent-type {person}
		:object-type {head})

(new-action-type {reach hand}
		:agent-type {person}
		:object-type {hand})	      
	      
(new-is-a {reach head} {reach})		
(new-is-a {reach hand} {reach})		  
		 
(new-type {scratcher person} {person})
(x-is-the-y-of-z {scratcher person} {agent-type} {scratch head})
(new-type {scratcher hand} {hand})
(x-is-the-y-of-z {scratcher hand} {hand} {scratcher person})
 
(new-context {scratch head BC} {general})
(new-is-a {scratch head BC} {before context})
(x-is-the-y-of-z {scratch head BC} {before context} {scratch head})

(new-context {scratch head AC} {general})
(new-is-a {scratch head AC} {after context})
(x-is-the-y-of-z {scratch head AC} {after context} {scratch head})

(in-context {scratch head BC})
(new-not-statement {scratcher person} {performs the action of} {reach head})
(new-not-statement {scratcher person} {performs the action of} {reach hand})
(new-not-statement {scratcher person} {moves body part} {hand})
(new-not-statement {scratcher person} {put hand in body part} {head})
(new-statement {scratcher hand} {approaches} {scratch head})
(new-not-statement {scratcher hand} {is in direct contact to} {scratch head})

(in-context {scratch head AC})
(new-statement {scratcher person} {performs the action of} {reach head})
(new-statement {scratcher person} {performs the action of} {reach hand})
(new-statement {scratcher person} {moves body part} {hand})
(new-statement {scratcher person} {put hand in body part} {head})
(new-statement {scratcher hand} {is in direct contact to} {scratch head})

(in-context {general})
(new-statement {scratch head} {has point of interest in} {arm}) 
(new-statement {scratch head} {has point of interest in} {head}) 



;; DEFINITIONS DESCRIBING THE ACTION OF SITTING DOWN (ACTION 4 FROM IXMAS)
(in-context {general})
(new-relation {rests upon}
		:a-inst-of {thing}
		:b-inst-of {thing})

(new-relation {stands upon}
		:a-inst-of {person}
		:b-inst-of {body part})
	      
(new-relation {turn body part}
		:a-inst-of {person}
		:b-inst-of {body part})
	      
(new-relation {bend body part}
		:a-inst-of {person}
		:b-inst-of {body part})
    	      	      	      	          
(new-action-type {sit down on} 
		:agent-type {person}
		:object-type {object})
	
(new-type {sitting person} {person})			
(x-is-the-y-of-z {sitting person} {agent-type} {sit down on})		      

(new-type {surface} {thing})
(new-type {sitting surface} {surface})
		
(new-context {sit down on BC} {general})
(new-is-a {sit down on BC} {before context})
(x-is-the-y-of-z {sit down on BC} {before context} {sit down on})

(new-context {sit down on AC} {general})
(new-is-a {sit down on AC} {after context})
(x-is-the-y-of-z {sit down on AC} {after context} {sit down on})

(in-context {sit down on BC})
(new-not-statement {sitting person} {approaches} {object})
(new-not-statement {sitting person} {turn body part} {body part})
(new-not-statement {sitting person} {bend body part} {kness})
(new-not-statement {sitting person} {bend body part} {torso})
(new-not-statement {sitting person} {rests upon} {sitting surface})
(new-statement {sitting person} {stands upon} {feet})
(new-not-statement {sitting person} {is in direct contact to} {sitting surface})

(in-context {sit down on AC})
(new-statement {sitting person} {approaches} {object})
(new-statement {sitting person} {turn body part} {body part})
(new-statement {sitting person} {bend body part} {kness})
(new-statement {sitting person} {bend body part} {torso})
(new-statement {sitting person} {rests upon} {sitting surface})
(new-not-statement {sitting person} {stands upon} {feet})
(new-statement {sitting person} {is in direct contact to} {sitting surface})

(in-context {general})
(new-statement {sit down on} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF GETTING UP (ACTION 5 FROM IXMAS)
(in-context {general})
(new-relation {stretch body part}
		:a-inst-of {person}
		:b-inst-of {body part})
	      	       	            
(new-action-type {get up} 
		:agent-type {person}
		:object-type {object})

(new-type {standing up person} {person})
(x-is-the-y-of-z {standing up person} {agent-type} {get up})
(new-type {resting surface} {surface})

(new-context {get up BC} {general})
(new-is-a {get up BC} {before context})
(x-is-the-y-of-z {get up BC} {before context} {get up})

(new-context {get up AC} {general})
(new-is-a {get up AC} {after context})
(x-is-the-y-of-z {get up AC} {after context} {get up})

(in-context {get up BC})
(new-statement {standing up person} {performs the action of} {sit down on})
(new-not-statement {standing up person} {stretch body part} {kness})
(new-not-statement {standing up person} {stretch body part} {torso})
(new-statement {standing up person} {rests upon} {resting surface})
(new-statement {standing up person} {stands upon} {resting surface})
(new-statement {standing up person} {is in direct contact to} {resting surface})

(in-context {get up AC})
(new-statement {standing up person} {stretch body part} {kness})
(new-statement {standing up person} {stretch body part} {torso})
(new-not-statement {standing up person} {rests upon} {resting surface})
(new-not-statement {standing up person} {stands upon} {feet})
(new-not-statement {standing up person} {is in direct contact to} {resting surface})

(in-context {general})
(new-statement {get up} {is opposite action of} {sit down on})
(new-statement {get up} {has point of interest in} {leg})



;; DEFINITIONS DESCRIBING THE ACTION OF WALKING (ACTION 7 FROM IXMAS)
(in-context {general})   
(new-relation {put front body part}
		:a-inst-of {event}
		:b-inst-of {body part})
      	      	      	      	      
(new-action-type {walk} 
		:agent-type {person})
		 
(new-type {walking person} {person})
(x-is-the-y-of-z {walking person} {agent-type} {walk})
		 
(new-context {walk BC} {general})
(new-is-a {walk BC} {before context})
(x-is-the-y-of-z {walk BC} {before context} {walk})

(new-context {walk AC} {general})
(new-is-a {walk AC} {after context})
(x-is-the-y-of-z {walk AC} {after context} {walk})

(in-context {walk BC})
(new-not-eq {origin} {destination})
(new-not-statement {walking person} {moves body part} {right leg})
(new-not-statement {walking person} {moves body part} {left leg})
(new-not-statement {walking person} {moves body part} {right foot})
(new-not-statement {walking person} {moves body part} {left foot})
(new-not-statement {walking person} {put front body part} {right foot})
(new-not-statement {walking person} {put front body part} {left foot})
(new-statement {walking person} {is located at} {origin})
(new-not-statement {walking person} {is located at} {destination})

(in-context {walk AC})
(new-not-eq {origin} {destination})
(new-statement {walking person} {moves body part} {right leg})
(new-statement {walking person} {moves body part} {left leg})
(new-statement {walking person} {moves body part} {right foot})
(new-statement {walking person} {moves body part} {left foot})
(new-statement {walking person} {put front body part} {right foot})
(new-statement {walking person} {put front body part} {left foot})
(new-statement {walking person} {is located at} {destination})
(new-not-statement {walking person} {is located at} {origin})

(in-context {general})
(new-statement {walk} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF TURNING AROUND (ACTION 6 FROM IXMAS)
(in-context {general})
(new-action-type {turn around} 
		:agent-type {person})
		 
(new-type {turning person} {person})
(x-is-the-y-of-z {turning person} {agent-type} {turn around})
		 
(new-context {turn around BC} {general})
(new-is-a {turn around BC} {before context})
(x-is-the-y-of-z {turn around BC} {before context} {turn around})

(new-context {turn around AC} {general})
(new-is-a {turn around AC} {after context})
(x-is-the-y-of-z {turn around AC} {after context} {turn around})

(in-context {turn around BC})
(new-eq {origin} {destination})
(new-not-statement {turning person} {moves body part} {right leg})
(new-not-statement {turning person} {moves body part} {left leg})
(new-not-statement {turning person} {moves body part} {torso})
(new-statement {turning person} {is located at} {destination})

(in-context {turn around AC})
(new-eq {origin} {destination})
(new-statement {turning person} {moves body part} {right leg})
(new-statement {turning person} {moves body part} {left leg})
(new-statement {turning person} {moves body part} {torso})
(new-statement {turning person} {is located at} {origin})
(new-statement {turning person} {is located at} {destination})
(new-statement {turning person} {performs the action of} {walk})
(new-statement {turning person} {is located at} {destination})

(in-context {general})
(new-statement {turn around} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF WAVING (ACTION 8 FROM IXMAS)
(in-context {general})
(new-action-type {reach object}
		:agent-type {event}
		:object-type {object})

(new-is-a {reach object} {reach})	      

(new-action-type {wave} 
		:agent-type {person}
		:object-type {thing})
		 
(new-type {waving person} {person})
(x-is-the-y-of-z {waving person} {agent-type} {wave})
(new-type {waving hand} {hand})
		 
(new-context {wave BC} {general})
(new-is-a {wave BC} {before context})
(x-is-the-y-of-z {wave BC} {before context} {wave})

(new-context {wave AC} {general})
(new-is-a {wave AC} {after context})
(x-is-the-y-of-z {wave AC} {after context} {wave})

(in-context {wave BC})
(new-not-statement {waving person} {performs the action of} {reach object})
(new-not-statement {waving person} {moves body part} {torso})
(new-not-statement {waving person} {moves body part} {arm})
(new-not-statement {waving person} {moves body part} {hand})
(new-not-statement {waving hand} {performs the action of} {move})

(in-context {wave AC})
(new-statement {waving person} {performs the action of} {reach object})
(new-statement {waving person} {moves body part} {torso})
(new-statement {waving person} {moves body part} {arm})
(new-statement {waving person} {moves body part} {hand})
(new-statement {waving hand} {is in direct contact to} {torso})
	      
(in-context {general})
(new-statement {wave} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF PUNCHING (ACTION 9 FROM IXMAS)	      
(in-context {general})
(new-relation {has hit}
		:a-inst-of {thing}
		:b-inst-of {thing})  

(in-context {general})
(new-action-type {punch} 
		:agent-type {person}
		:object-type {object})
		 
(new-type {punching person} {person})
(x-is-the-y-of-z {punching person} {agent-type} {punch})
(new-type {punching fist} {fist})
(new-type {punched object} {object})
		 
(new-context {punch BC} {general})
(new-is-a {punch BC} {before context})
(x-is-the-y-of-z {punch BC} {before context} {punch})

(new-context {punch AC} {general})
(new-is-a {punch AC} {after context})
(x-is-the-y-of-z {punch AC} {after context} {punch})

(in-context {punch BC})
(new-not-statement {punching person} {moves body part} {right arm})
(new-not-statement {punching person} {moves body part} {left arm})
(new-not-statement {punching fist} {is in direct contact to} {punched object})
(new-statement {punching person} {approaches} {punched object})
(new-statement {punching person} {is closed to} {punched object})

(in-context {punch AC})
(new-statement {punching person} {moves body part} {right arm})
(new-statement {punching person} {moves body part} {left arm})
(new-statement {punching person} {has hit} {punched object})

(in-context {general})
(new-statement {punch} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF KICKING (ACTION 10 FROM IXMAS)
(in-context {general})
(new-action-type {kick} 
		 :agent-type {person}
		 :object-type {object})
		 
(new-type {kicking person} {person})		 
(x-is-the-y-of-z {kicking person} {agent-type} {kick})
(new-type {kicked object} {thing})	
(new-type {kicking foot} {foot})

(new-context {kick BC} {general})
(new-is-a {kick BC} {before context})
(x-is-the-y-of-z {kick BC} {before context} {kick})

(new-context {kick AC} {general})
(new-is-a {kick AC} {after context})
(x-is-the-y-of-z {kick AC} {after context} {kick})

(in-context {kick BC})
(new-not-statement {kicking person} {performs the action of} {reach object})
(new-not-statement {kicking person} {moves body part} {hand})
(new-not-statement {kicking foot} {is in direct contact to} {kicked object})
(new-statement {kicking person} {approaches} {kicked object})
(new-statement {kicking person} {is closed to} {kicked object})

(in-context {kick AC})
(new-statement {kicking person} {performs the action of} {reach object})
(new-statement {kicking person} {moves body part} {hand})
(new-statement {kicking person} {has hit} {kicked object})

(in-context {general})
(new-statement {kick} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF POINTING (ACTION 11 FROM IXMAS)
(in-context {general})
(new-relation {focuses attention on}
	      :a-inst-of {person}
	      :b-inst-of {thing})

(new-relation {points at}
	      :a-inst-of {person}
	      :b-inst-of {location})
	      
(new-action-type {find}
		:agent-type {person}
		:object-type {object})
		
(new-is-a {find} {action})			      
    	       
(new-action-type {point} 
		 :agent-type {person}
		 :object-type {object})
		
(new-type {pointing person} {person})		 
(x-is-the-y-of-z {pointing person} {agent-type} {point})
(new-type {pointed object} {thing})
(new-type {pointing finger} {hand})
(new-indv-role {pointed object location} {pointed object} {location})

(new-context {point BC} {general})
(new-is-a {point BC} {before context})
(x-is-the-y-of-z {point BC} {before context} {point})

(new-context {point AC} {general})
(new-is-a {point AC} {after context})
(x-is-the-y-of-z {point AC} {after context} {point})

(in-context {point BC})
(new-not-statement {pointing person} {moves body part} {arm})
(new-not-statement {pointing person} {performs the action of} {find})
(new-not-statement {pointing person} {moves body part} {hand finger})
(new-not-statement {pointing person} {focuses attention on} {pointed object})
(new-not-statement {pointing finger} {points at} {pointed object location})

(in-context {point AC})
(new-statement {pointing person} {performs the action of} {reach object})
(new-statement {pointing person} {moves body part} {arm})
(new-statement {pointing person} {performs the action of} {find})
(new-statement {pointing person} {moves body part} {hand finger})
(new-statement {pointing person} {focuses attention on} {pointed object})
(new-statement {pointing finger} {points at} {pointed object location})

(in-context {general})
(new-statement {point} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF PICKING UP (ACTION 12 FROM IXMAS)
(in-context {general})
(new-relation {is hold by}
	      :a-inst-of {thing}
	      :b-inst-of {person})
	      
(new-action-type {pick up} 
		 :agent-type {person}
		 :object-type {object})
		
(new-type {picking person} {person})		 
(x-is-the-y-of-z {picking person} {agent-type} {pick up})
(new-type {picked object} {thing})
(new-indv-role {picking person location} {picking person} {location})
(new-indv-role {picked object location} {picked object} {location})

(new-indv-role {picked object origin} {picked object} {location})
(new-indv-role {picked object end location} {picked object} {location})
(new-type {picking hand} {thing})

(new-context {pick up BC} {general})
(new-is-a {pick up BC} {before context})
(x-is-the-y-of-z {pick up BC} {before context} {pick up})

(new-context {pick up AC} {general})
(new-is-a {pick up AC} {after context})
(x-is-the-y-of-z {pick up AC} {after context} {pick up})

(in-context {pick up BC})
(new-not-statement {picking person} {performs the action of} {reach object})
(new-not-statement {picking person} {moves body part} {torso})
(new-not-statement {picking person} {moves body part} {hand})	
(new-not-eq {picking person location} {picked object location})
(x-is-the-y-of-z {picked object origin} {picked object location} {picked object})
(new-statement {picking person} {approaches} {picked object location})
(new-not-statement {picked object} {is hold by} {picking hand})  

(in-context {pick up AC})
(new-statement {picking person} {performs the action of} {reach object})
(new-statement {picking person} {moves body part} {torso})
(new-statement {picking person} {moves body part} {hand})	
(new-eq {picking person location} {picked object location})
(new-statement {picking person} {is in direct contact to} {picked object})
(x-is-the-y-of-z {picked object end location} {picked object location} {picked object})
(new-eq {picked object end location} {picking person location})
(x-is-the-y-of-z {picked object location} {picking person location} {picking person})
(new-statement {picked object} {is hold by} {picking hand})

(in-context {general})
(new-statement {pick up} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF THROWING OVER HEAD (ACTION 13 FROM IXMAS)
(in-context {general})
(new-relation {stands over}
	      :a-inst-of {thing}
	      :b-inst-of {thing})
     
(new-relation {put hand over body part}
	      :a-inst-of {person}
	      :b-inst-of {body part})	      
	      
(new-action-type {throw over head} 
		 :agent-type {person}
		 :object-type {object})
		 
(new-type {throwing person} {person})
(x-is-the-y-of-z {throwing person} {agent-type} {throw over head})
(new-type {thrown object} {thing})
(new-indv-role {throwing person location} {throwing person} {location})
(new-indv-role {thrown object location} {thrown object} {location})
(new-indv-role {thrown object origin} {thrown object} {location})
(new-indv-role {thrown object end location} {thrown object} {location})
(new-not-eq {thrown object origin} {thrown object end location})
(new-type {throwing hands} {thing})
(new-indv-role {throwing person head} {throwing person} {head})

(new-context {throw over head BC} {general})
(new-is-a {throw over head BC} {before context})
(x-is-the-y-of-z {throw over head BC} {before context} {throw over head})

(new-context {throw over head AC} {general})
(new-is-a {throw over head AC} {after context})
(x-is-the-y-of-z {throw over head AC} {after context} {throw over head})

(in-context {throw over head BC})
(new-statement {throwing person} {performs the action of} {reach object})
(new-statement {throwing person} {moves body part} {right arm})
(new-statement {throwing person} {moves body part} {left arm})
(new-statement {throwing person} {put hand over body part} {head})		
(new-eq {throwing person location} {thrown object location})
(x-is-the-y-of-z {thrown object origin} {thrown object location} {thrown object})
(new-statement {throwing person} {is in direct contact to} {thrown object location})
(new-statement {thrown object} {is hold by} {throwing hands})
(new-statement {throwing hands} {stands over} {throwing person head})
(new-statement {thrown object} {rests upon} {throwing hands})
(new-statement {thrown object} {is in direct contact to} {throwing hands})
    
(in-context {throw over head AC})
(new-not-eq {throwing person location} {thrown object location})
(new-not-statement {throwing person} {is in direct contact to} {thrown object location})
(new-not-statement {thrown object} {is hold by} {throwing hands})  

(in-context {general})
(new-statement {throw over head} {has point of interest in} {arm})  
