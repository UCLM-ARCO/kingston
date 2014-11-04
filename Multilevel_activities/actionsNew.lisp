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
(in-namespace "events" :include "common")
;;; This file necessarily contains some forward references.
(setq *defer-unknown-connections* t)

;;; Some roles required for the following events and actions
(new-type-role {name} {person} {thing})
(new-type-role {personLocation} {person} {place})
(new-type-role {objectLocation} {thing} {place})
(new-type-role {body part} {person} {thing})
(new-indv-role {arm} {person} {body part} :n 2)
(new-indv-role {leg} {person} {body part} :n 2)



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

(new-action-type {check time} 
		:agent-type {person}
		:object-type {time})
		
(new-relation {move arm} 
	      :a-inst-of {event}
	      :b-inst-of {arm})

(new-relation {reach clock}
	      :a-inst-of {event}
	      :b-inst-of {object})

(new-relation {see clock}
	      :a-inst-of {event}
	      :b-inst-of {object})		
		
(new-type {checking person} {person})
(new-is-a {checking person} {person})
			
(new-context {check time BC} {general})
(new-is-a {check time BC} {before context})
(x-is-the-y-of-z {check time BC} {before context} {check time})

(new-context {check time AC} {general})
(new-is-a {check time AC} {after context})
(x-is-the-y-of-z {check time AC} {after context} {check time})

(new-indv {person 1} {person})
(new-type {action person} {action})
(x-is-the-y-of-z {person 1} {action person} {check time})		

(in-context {check time BC})
(new-not-statement {person 1} {move arm} {check time})
(new-not-statement {person 1} {reach clock} {check time})
(new-not-statement {person 1} {see clock} {check time})
(new-not-statement {checking person} {is aware of} {check time})

(in-context {check time AC})
(new-statement {person 1} {move arm} {check time})
(new-statement {person 1} {reach clock} {check time})
(new-statement {person 1} {see clock} {check time})
(new-statement {checking person} {is aware of} {check time})

(new-statement {check time} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF CROSSING ARMS (ACTION 2 FROM IXMAS)
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
	      
(new-relation {reach right arm}
	      :a-inst-of {event}
	      :b-inst-of {arm})
	      
(new-relation {reach left arm}
	      :a-inst-of {event}
	      :b-inst-of {arm})
	      
(new-relation {move right arm}
	      :a-inst-of {event}
	      :b-inst-of {arm})
	      
(new-relation {move left arm}
	      :a-inst-of {event}
	      :b-inst-of {arm})
	      
(new-relation {get crosser arms}
	      :a-inst-of {event}
	      :b-inst-of {arm})	    
	       	      	      	      	      
(in-context {general})
(new-action-type {cross arms} 
		 :agent-type {person})
		 
(new-type {crossing arms person} {person})	 
(new-is-a {crossing arms person} {person})
(new-type {crosser} {crossing arms person})
(new-is-a {crosser} {crossing arms person})
(new-type {arms to cross} {arm})	 
(new-is-a {arms to cross} {arm})
(new-type {left arm} {arm})	 
(new-is-a {left arm} {arm})
(new-type {right arm} {arm})	 
(new-is-a {right arm} {arm})	 

(new-context {cross arms BC} {general})
(new-is-a {cross arms BC} {before context})
(x-is-the-y-of-z {cross arms BC} {before context} {cross arms})

(new-context {cross arms AC} {general})
(new-is-a {cross arms AC} {after context})
(x-is-the-y-of-z {cross arms AC} {after context} {cross arms})

(x-is-the-y-of-z {person 1} {action person} {cross arms})

(in-context {cross arms BC})
(new-not-statement {person 1} {reach right arm} {cross arms})
(new-not-statement {person 1} {reach left arm} {cross arms})
(new-not-statement {person 1} {move right arm} {cross arms})
(new-not-statement {person 1} {move left arm} {cross arms})
(new-not-statement {person 1} {get crosser arms} {cross arms})
(new-not-statement {crossing arms person}  {has crossed}  {arms to cross})
(new-statement {left arm} {approaches} {right arm})
(new-statement {right arm} {approaches} {left arm})
(new-statement {left arm} {is closed to} {right arm})

(in-context {cross arms AC})
(new-statement {person 1} {reach right arm} {cross arms})
(new-statement {person 1} {reach left arm} {cross arms})
(new-statement {person 1} {move right arm} {cross arms})
(new-statement {person 1} {move left arm} {cross arms})
(new-statement {person 1} {get crosser arms} {cross arms})
(new-statement {crossing arms person}  {has crossed}  {arms to cross})
(new-statement {left arm} {is in direct contact to} {right arm})

(new-statement {cross arms} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF SCRATCHING HEAD (ACTION 3 FROM IXMAS)
(new-type-role {head} {person} {body part} :n 1)
(new-type-role {hand} {person} {body part} :n 2)

(new-relation {reach head}
	      :a-inst-of {event}
	      :b-inst-of {head})
	      
(new-relation {reach hand}
	      :a-inst-of {event}
	      :b-inst-of {head})

(new-relation {move hand}
	      :a-inst-of {event}
	      :b-inst-of {hand})
	      
(new-relation {put hand in head}
	      :a-inst-of {event}
	      :b-inst-of {event})
	      
(new-relation {has scratch}
	      :a-inst-of {animated object}
	      :b-inst-of {object})	      	      	      	      

(in-context {general})
(new-action-type {scratch head} 
		 :agent-type {animated object})
		 
(new-type {scratcher hand} {hand})
(new-is-a {scratcher hand} {hand})
 
(new-context {scratch head BC} {general})
(new-is-a {scratch head BC} {before context})
(x-is-the-y-of-z {scratch head BC} {before context} {scratch head})

(new-context {scratch head AC} {general})
(new-is-a {scratch head AC} {after context})
(x-is-the-y-of-z {scratch head AC} {after context} {scratch head})

(x-is-the-y-of-z {person 1} {action person} {scratch head})

(in-context {scratch head BC})
(new-not-statement {person 1} {reach head} {scratch head})
(new-not-statement {person 1} {reach hand} {scratch head})
(new-not-statement {person 1} {move hand} {scratch head})
(new-not-statement {person 1} {put hand in head} {scratch head})
(new-statement {scratcher hand} {approaches} {scratch head})
(new-not-statement {scratcher hand} {is in direct contact to} {scratch head})

(in-context {scratch head AC})
(new-statement {person 1} {reach head} {scratch head})
(new-statement {person 1} {reach hand} {scratch head})
(new-statement {person 1} {move hand} {scratch head})
(new-statement {person 1} {put hand in head} {scratch head})
(new-statement {scratcher hand} {is in direct contact to} {scratch head})

(new-statement {scratch head} {has point of interest in} {arm}) 
(new-statement {scratch head} {has point of interest in} {head}) 



;; DEFINITIONS DESCRIBING THE ACTION OF SITTING DOWN (ACTION 4 FROM IXMAS)
(new-type-role {feet} {person} {body part} :n 1)

(new-relation {rests upon}
	      :a-inst-of {thing}
	      :b-inst-of {thing})

(new-relation {stands upon}
	      :a-inst-of {person}
	      :b-inst-of {body part})
	      
(new-relation {reach chair}
	      :a-inst-of {person}
	      :b-inst-of {object})
	      
(new-relation {turn body}
	      :a-inst-of {person}
	      :b-inst-of {body part})
	      
(new-relation {bend knees}
	      :a-inst-of {person}
	      :b-inst-of {body part})
	      
(new-relation {bend torso}
	      :a-inst-of {person}
	      :b-inst-of {body part})
	      
(new-relation {put chair}
	      :a-inst-of {person}
	      :b-inst-of {object})	      	      	      	      	      
(in-context {general})	      
(new-action-type {sit down} 
		:agent-type {person}
		:object-type {object})
	
(new-type {sitting person} {person})			
(new-is-a {sitting person} {person})
(new-type {surface} {thing})
(new-is-a {surface} {thing})
(new-type {sitting surface} {surface})
(new-is-a {sitting surface} {surface})
		
(new-context {sit down BC} {general})
(new-is-a {sit down BC} {before context})
(x-is-the-y-of-z {sit down BC} {before context} {sit down})

(new-context {sit down AC} {general})
(new-is-a {sit down AC} {after context})
(x-is-the-y-of-z {sit down AC} {after context} {sit down})

(x-is-the-y-of-z {person 1} {action person} {sit down})		      

(in-context {sit down BC})
(new-not-statement {person 1} {reach chair} {sit down})
(new-not-statement {person 1} {turn body} {sit down})
(new-not-statement {person 1} {bend knees} {sit down})
(new-not-statement {person 1} {bend torso} {sit down})
(new-not-statement {person 1} {put chair} {sit down})
(new-not-statement {sitting person} {rests upon} {sitting surface})
(new-statement {sitting person} {stands upon} {feet})
(new-not-statement {sitting person} {is in direct contact to} {sitting surface})

(in-context {sit down AC})
(new-statement {person 1} {reach chair} {sit down})
(new-statement {person 1} {turn body} {sit down})
(new-statement {person 1} {bend knees} {sit down})
(new-statement {person 1} {bend torso} {sit down})
(new-statement {person 1} {put chair} {sit down})
(new-statement {sitting person} {rests upon} {sitting surface})
(new-not-statement {sitting person} {stands upon} {feet})
(new-statement {sitting person} {is in direct contact to} {sitting surface})

(new-statement {sit down} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF GETTING UP (ACTION 5 FROM IXMAS)
(new-relation {stretch kness}
	      :a-inst-of {person}
	      :b-inst-of {object})
	      
(new-relation {stretch torso}
	      :a-inst-of {person}
	      :b-inst-of {object})
	      
(new-relation {stand up}
	      :a-inst-of {person}
	      :b-inst-of {object})
	       	            
(in-context {general})
(new-action-type {get up} 
		 :agent-type {person}
		 :object-type {object})

(new-type {standing up person} {person})
(new-is-a {standing up person} {person})
(new-type {resting surface} {surface})
(new-is-a {resting surface} {surface})

(new-context {get up BC} {general})
(new-is-a {get up BC} {before context})
(x-is-the-y-of-z {get up BC} {before context} {get up})

(new-context {get up AC} {general})
(new-is-a {get up AC} {after context})
(x-is-the-y-of-z {get up AC} {after context} {get up})

(x-is-the-y-of-z {person 1} {action person} {get up})

(in-context {get up BC})
(new-not-statement {person 1} {put chair} {get up})
(new-not-statement {person 1} {stretch kness} {get up})
(new-not-statement {person 1} {stretch torso} {get up})
(new-not-statement {person 1} {stand up} {get up})
(new-statement {standing up person} {rests upon} {resting surface})
(new-statement {standing up person} {stands upon} {resting surface})
(new-statement {standing up person} {is in direct contact to} {resting surface})

(in-context {get up AC})
(new-statement {person 1} {put chair} {get up})
(new-statement {person 1} {stretch kness} {get up})
(new-statement {person 1} {stretch torso} {get up})
(new-statement {person 1} {stand up} {get up})
(new-not-statement {standing up person} {rests upon} {resting surface})
(new-not-statement {standing up person} {stands upon} {feet})
(new-not-statement {standing up person} {is in direct contact to} {resting surface})

(new-statement {get up} {is opposite action of} {sit down})
(new-statement {get up} {has point of interest in} {leg})



;; DEFINITIONS DESCRIBING THE ACTION OF WALKING (ACTION 7 FROM IXMAS)
(new-relation {move right leg}
	      :a-inst-of {event}
	      :b-inst-of {leg})
	      
(new-relation {move left leg}
	      :a-inst-of {event}
	      :b-inst-of {leg})
	      
(new-relation {move right foot}
	      :a-inst-of {event}
	      :b-inst-of {foot})
	      
(new-relation {move left foot}
	      :a-inst-of {event}
	      :b-inst-of {foot})
	      
(new-relation {put front right foot}
	      :a-inst-of {event}
	      :b-inst-of {foot})
	      
(new-relation {put front left foot}
	      :a-inst-of {event}
	      :b-inst-of {foot})	      	      	      	      	      

(in-context {general})
(new-action-type {walk} 
		 :agent-type {person})
		 
(new-type {walking person} {person})
(new-is-a {walking person} {person})
		 
(new-context {walk BC} {general})
(new-is-a {walk BC} {before context})
(x-is-the-y-of-z {walk BC} {before context} {walk})

(new-context {walk AC} {general})
(new-is-a {walk AC} {after context})
(x-is-the-y-of-z {walk AC} {after context} {walk})

;(new-indv {person 1} {person})
(x-is-the-y-of-z {person 1} {action person} {walk})

(in-context {walk BC})
(new-not-eq {origin} {destination})
(new-not-statement {person 1} {move right leg} {walk})
(new-not-statement {person 1} {move left leg} {walk})
(new-not-statement {person 1} {move right foot} {walk})
(new-not-statement {person 1} {move left foot} {walk})
(new-not-statement {person 1} {put front right foot} {walk})
(new-not-statement {person 1} {put front left foot} {walk})
(new-statement {walking person} {is located at} {origin})
(new-not-statement {walking person} {is located at} {destination})

(in-context {walk AC})
(new-not-eq {origin} {destination})
(new-statement {person 1} {move right leg} {walk})
(new-statement {person 1} {move left leg} {walk})
(new-statement {person 1} {move right foot} {walk})
(new-statement {person 1} {move left foot} {walk})
(new-statement {person 1} {put front right foot} {walk})
(new-statement {person 1} {put front left foot} {walk})
(new-statement {walking person} {is located at} {destination})
(new-not-statement {walking person} {is located at} {origin})

(new-statement {walk} {has point of interest in} {leg}) 

;;FLUENTS NEED TO BE ADDED IN ORDER TO DESCRIBE THE WALKING ACTION



;; DEFINITIONS DESCRIBING THE ACTION OF TURNING AROUND (ACTION 6 FROM IXMAS)
(new-type-role {torso} {person} {body part} :n 1)

(new-relation {move torso}
	      :a-inst-of {event}
	      :b-inst-of {torso})

(in-context {general})
(new-action-type {turn around} 
		 :agent-type {person})
		 
(new-type {turning person} {person})
(new-is-a {turning person} {person})
		 
(new-context {turn around BC} {general})
(new-is-a {turn around BC} {before context})
(x-is-the-y-of-z {turn around BC} {before context} {turn around})

(new-context {turn around AC} {general})
(new-is-a {turn around AC} {after context})
(x-is-the-y-of-z {turn around AC} {after context} {turn around})

(x-is-the-y-of-z {person 1} {action person} {turn around})

(in-context {turn around BC})
;(new-eq {origin} {destination})
(new-not-statement {person 1} {move right leg} {turn around})
(new-not-statement {person 1} {move left leg} {turn around})
(new-not-statement {person 1} {move torso} {turn around})
(new-statement {turning person} {is located at} {origin})
(new-statement {turning person} {is located at} {destination})

(in-context {turn around AC})
;(new-eq {origin} {destination})
(new-statement {person 1} {move right leg} {turn around})
(new-statement {person 1} {move left leg} {turn around})
(new-statement {person 1} {move torso} {turn around})
(new-statement {turning person} {is located at} {origin})
(new-statement {turning person} {is located at} {destination})

(new-statement {turn around} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF WAVING (ACTION 8 FROM IXMAS)
(new-relation {reach object}
	      :a-inst-of {event}
	      :b-inst-of {object})
	      
(in-context {general})
(new-action-type {wave} 
		 :agent-type {person}
		 :object-type {object})
		 
;(new-type {waving person} {person})
;(new-is-a {waving person} {person})
(new-type {waving hand} {hand})
(new-is-a {waving hand} {hand})
		 
(new-context {wave BC} {general})
(new-is-a {wave BC} {before context})
(x-is-the-y-of-z {wave BC} {before context} {wave})

(new-context {wave AC} {general})
(new-is-a {wave AC} {after context})
(x-is-the-y-of-z {wave AC} {after context} {wave})

(x-is-the-y-of-z {person 1} {action person} {wave})

(in-context {wave BC})
(new-not-statement {person 1} {reach object} {wave})
(new-not-statement {person 1} {move torso} {wave})
(new-not-statement {person 1} {move arm} {wave})
(new-not-statement {person 1} {move hand} {wave})
(new-statement {waving hand} {performs the action of} {move})

(in-context {wave AC})
(new-statement {person 1} {reach object} {wave})
(new-statement {person 1} {move torso} {wave})
(new-statement {person 1} {move arm} {wave})
(new-statement {person 1} {move hand} {wave})
(new-not-statement {waving hand} {is in direct contact to} {torso})

(new-statement {wave} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF PUNCHING (ACTION 9 FROM IXMAS)
(new-type-role {fist} {person} {body part} :n 2)

(new-relation {has hit}
	      :a-inst-of {thing}
	      :b-inst-of {thing})

(new-relation {reach punch object}
	      :a-inst-of {event}
	      :b-inst-of {object})	      

(in-context {general})
(new-action-type {punch} 
		 :agent-type {person}
		 :object-type {object})
		 
(new-type {punching person} {person})
(new-is-a {punching person} {person})
(new-type  {punching fist} {fist})
(new-is-a  {punching fist} {fist})
(new-type  {punched object} {object})
(new-is-a  {punched object} {object})
		 
(new-context {punch BC} {general})
(new-is-a {punch BC} {before context})
(x-is-the-y-of-z {punch BC} {before context} {punch})

(new-context {punch AC} {general})
(new-is-a {punch AC} {after context})
(x-is-the-y-of-z {punch AC} {after context} {punch})

(x-is-the-y-of-z {person 1} {action person} {punch})

(in-context {punch BC})
(new-not-statement {person 1} {reach punch object} {punch})
(new-not-statement {person 1} {move right arm} {punch})
(new-not-statement {person 1} {move left arm} {punch})
(new-not-statement {punching fist} {is in direct contact to} {punched object})
(new-statement {punching person} {approaches} {punched object})
(new-statement {punching person} {is closed to} {punched object})

(in-context {punch AC})
(new-statement {person 1} {reach punch object} {punch})
(new-statement {person 1} {move right arm} {punch})
(new-statement {person 1} {move left arm} {punch})
(new-statement {punching person} {has hit} {punched object})

(new-statement {punch} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF KICKING (ACTION 10 FROM IXMAS)
(new-relation {pick up object}
	      :a-inst-of {event}
	      :b-inst-of {object})
	      
(new-relation {get object}
	      :a-inst-of {event}
	      :b-inst-of {object})	      

(in-context {general})
(new-action-type {kick} 
		 :agent-type {person}
		 :object-type {object})
		 
(new-type {kicking person} {person})		 
(new-is-a {kicking person} {person})	
(new-type {kicked object} {thing})	
(new-is-a {kicked object} {thing})
(new-type {kicking foot} {foot})
(new-is-a {kicking foot} {foot})		 		 

(new-context {kick BC} {general})
(new-is-a {kick BC} {before context})
(x-is-the-y-of-z {kick BC} {before context} {kick})

(new-context {kick AC} {general})
(new-is-a {kick AC} {after context})
(x-is-the-y-of-z {kick AC} {after context} {kick})

(x-is-the-y-of-z {person 1} {action person} {kick})

(in-context {kick BC})
(new-not-statement {person 1} {reach object} {kick})
(new-not-statement {person 1} {pick up object} {kick})
(new-not-statement {person 1} {get object} {kick})
(new-not-statement {person 1} {move hand} {kick})
(new-not-statement {kicking foot} {is in direct contact to} {kicked object})
(new-statement {kicking person} {approaches} {kicked object})
(new-statement {kicking person} {is closed to} {kicked object})

(in-context {kick AC})
(new-statement {person 1} {reach object} {kick})
(new-statement {person 1} {pick up object} {kick})
(new-statement {person 1} {get object} {kick})
(new-statement {person 1} {move hand} {kick})
(new-statement {kicking person} {has hit} {kicked object})

(new-statement {kick} {has point of interest in} {leg}) 



;; DEFINITIONS DESCRIBING THE ACTION OF POINTING (ACTION 11 FROM IXMAS)
(new-type-role {hand finger} {person} {body part} :n 10)

(new-relation {focuses attention on}
	      :a-inst-of {person}
	      :b-inst-of {thing})

(new-relation {points at}
	      :a-inst-of {person}
	      :b-inst-of {location})
	      
(new-relation {find object}
	      :a-inst-of {person}
	      :b-inst-of {object})
	      
(new-relation {move finger}
	      :a-inst-of {person}
	      :b-inst-of {finger})	      	      

(in-context {general})
(new-action-type {point} 
		 :agent-type {person}
		 :object-type {object})
		
(new-type {pointing person} {person})		 
(new-is-a {pointing person} {person})
(new-type {pointed object} {thing})
(new-is-a {pointed object} {thing})
(new-type {pointing finger} {hand})
(new-is-a {pointing finger} {hand})
(new-indv-role {pointed object location} {pointed object} {location})

(new-context {point BC} {general})
(new-is-a {point BC} {before context})
(x-is-the-y-of-z {point BC} {before context} {point})

(new-context {point AC} {general})
(new-is-a {point AC} {after context})
(x-is-the-y-of-z {point AC} {after context} {point})

(x-is-the-y-of-z {person 1} {action person} {point})

(in-context {point BC})
(new-not-statement {person 1} {reach object} {point})
(new-not-statement {person 1} {move arm} {point})
(new-not-statement {person 1} {find object} {point})
(new-not-statement {person 1} {move finger} {point})
(new-not-statement {pointing person} {focuses attention on} {pointed object})
(new-not-statement {pointing finger} {points at} {pointed object location})

(in-context {point AC})
(new-statement {person 1} {reach object} {point})
(new-statement {person 1} {move arm} {point})
(new-statement {person 1} {find object} {point})
(new-statement {person 1} {move finger} {point})
(new-statement {pointing person} {focuses attention on} {pointed object})
(new-not-statement {pointing finger} {points at} {pointed object location})

(new-statement {point} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF PICKING UP (ACTION 12 FROM IXMAS)
(new-relation {is hold by}
	      :a-inst-of {thing}
	      :b-inst-of {person})
	      
(in-context {general})
(new-action-type {pick up} 
		 :agent-type {person}
		 :object-type {object})
		
(new-type {picking person} {person})		 
(new-is-a {picking person} {person})
(new-indv-role {picking person location} {picking person} {location})
(new-indv-role {picked object location} {picked object} {location})
(new-type {picked object} {thing})
(new-is-a {picked object} {thing})
(new-indv-role {picked object origin} {picked object} {location})
(new-indv-role {picked object end location} {picked object} {location})
(new-type {picking hand} {thing})
(new-is-a {picking hand} {thing})			 		 

(new-context {pick up BC} {general})
(new-is-a {pick up BC} {before context})
(x-is-the-y-of-z {pick up BC} {before context} {pick up})

(new-context {pick up AC} {general})
(new-is-a {pick up AC} {after context})
(x-is-the-y-of-z {pick up AC} {after context} {pick up})

(x-is-the-y-of-z {person 1} {action person} {pick up})

(in-context {pick up BC})
(new-not-statement {person 1} {reach object} {pick up})
(new-not-statement {person 1} {move torso} {pick up})
(new-not-statement {person 1} {move hand} {pick up})	
(new-not-eq {picking person location} {picked object location})
(x-is-the-y-of-z {picked object location} {picked object} {picked object origin})
(new-statement {picking person} {approaches} {picked object location})
(new-not-statement {picked object} {is hold by} {picking hand})  

(in-context {pick up AC})
(new-statement {person 1} {reach object} {pick up})
(new-statement {person 1} {move torso} {pick up})
(new-statement {person 1} {move hand} {pick up})
(new-eq {picking person location} {picked object location})
(new-statement {picking person} {is in direct contact to} {picked object})
(x-is-the-y-of-z {picked object location} {picked object} {picked object end location})
(new-eq {picked object end location} {picking person location})
(x-is-the-y-of-z {picking person location} {picking person} {picked object location})
(new-statement {picked object} {is hold by} {picking hand})

(new-statement {pick up} {has point of interest in} {arm}) 



;; DEFINITIONS DESCRIBING THE ACTION OF THROWING OVER HEAD (ACTION 13 FROM IXMAS)
(new-type-role {both hands} {person} {body part} :n 1)
(new-type-role {trajectory} {thing} {line})

(new-relation {stands over}
	      :a-inst-of {thing}
	      :b-inst-of {thing})
     
(new-relation {put hand over head}
	      :a-inst-of {person}
	      :b-inst-of {event})	      
	      
(in-context {general})
(new-action-type {throw over head} 
		 :agent-type {person}
		 :object-type {object})
		 
(new-type {throwing person} {person})
(new-is-a {throwing person} {person})
(new-indv-role {throwing person location} {throwing person} {location})
(new-indv-role {thrown object location} {thrown object} {location})
(new-type {thrown object} {thing})
(new-indv-role {thrown object origin} {thrown object} {location})
(new-indv-role {thrown object end location} {thrown object} {location})
(new-not-eq {thrown object origin} {thrown object end location})
(new-type {throwing hands} {thing})
(new-is-a {throwing hands} {thing})
(new-indv-role {throwing person head} {throwing person} {head})

(new-context {throw over head BC} {general})
(new-is-a {throw over head BC} {before context})
(x-is-the-y-of-z {throw over head BC} {before context} {throw over head})

(new-context {throw over head AC} {general})
(new-is-a {throw over head AC} {after context})
(x-is-the-y-of-z {throw over head AC} {after context} {throw over head})

(x-is-the-y-of-z {person 1} {action person} {throw over head})

(in-context {throw over head BC})
(new-not-statement {person 1} {reach object} {throw over head})
(new-not-statement {person 1} {move right arm} {throw over head})
(new-not-statement {person 1} {move left arm} {throw over head})
(new-not-statement {person 1} {put hand over head} {throw over head})		
(new-eq {throwing person location} {thrown object location})
(x-is-the-y-of-z {thrown object location} {thrown object} {thrown object origin})
(new-statement {throwing person} {is in direct contact to} {thrown object location})
(new-statement {thrown object} {is hold by} {throwing hands})
(new-statement {throwing hands} {stands over} {throwing person head})
(new-statement {thrown object} {rests upon} {throwing hands})
(new-statement {thrown object} {is in direct contact to} {throwing hands})
 ;; since knowing the trajectory of the thrown object is not relevant
 ;; for what we are doing at the moment, we are skipping the fluent
 ;; description for the object trajectory
    
(in-context {throw over head AC})
(new-statement {person 1} {reach object} {throw over head})
(new-statement {person 1} {move right arm} {throw over head})
(new-statement {person 1} {move left arm} {throw over head})
(new-statement {person 1} {put hand over head} {throw over head}) 
(new-not-eq {throwing person location} {thrown object location})
(x-is-the-y-of-z {thrown object location} {thrown object} {thrown object end location})
(new-not-statement {throwing person} {is in direct contact to} {thrown object location})
(new-not-statement {thrown object} {is hold by} {throwing hands})  

(new-statement {throw over head} {has point of interest in} {arm})  
