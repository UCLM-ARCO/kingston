;; Movement Knowledge Base - Requires gen-events.lisp
;; --------------------------------------------------

(in-context {general})
(new-type {move} {action})
(new-type {origin} {place})
(new-type {destination} {place})
(new-type {object} {thing})
(new-type {animated object} {thing})
(new-is-a {person} {animated object})
(new-type {movable object} {object})
(new-type {moving object} {movable object})
(new-type {move an object} {movable object})

(new-action-type {move} 
		 :agent-type {animated object} 
		 :object-type {movable object}
		 :recipient-type {movable object})
 
(new-context {move BC} {general})
(new-is-a {move BC} {before context})
(x-is-the-y-of-z {move BC} {before context} {move})
 
(new-context {move AC} {general})
(new-is-a {move AC} {after context})
(x-is-the-y-of-z {move AC} {after context} {move}) 
 
(in-context {move BC})
(new-statement {moving object} {located at} {origin})
 
(in-context {move AC})
(new-not-statement {moving object} {located at} {origin})
(new-statement {moving object} {located at} {destination})

(new-is-a {move an object} {move})
(new-action-type {move an object} 
		 :agent-type {move}		
		 :object-type {object}
		 :recipient-type {movable object})
		 
(new-type {traveler} {person})

(new-action-type {travel} 
		 :agent-type {traveler} 
		 :object-type {movable object}
		 :recipient-type {movable object})
		 
(new-type {travel on land} {travel})
(new-type {land} {thing})		 		 
(x-is-the-y-of-z {origin} {travel on land} {land})
(x-is-the-y-of-z {destination} {travel on land} {land})

(new-not-eq {origin} {destination})

(new-type {travel in water} {travel})
(x-is-the-y-of-z {water} {origin} {travel in water})
(x-is-the-y-of-z {water} {destination} {travel in water})

(new-type {travel in air} {travel})
(new-type {air} {thing})
(x-is-the-y-of-z {origin} {travel in air} {air})
(x-is-the-y-of-z {destination} {travel in air} {air})

(new-type {travel in space} {travel})
(new-type {space} {thing})
(x-is-the-y-of-z {space} {origin} {travel in space})
(x-is-the-y-of-z {space} {destination} {travel in space})

(new-type {travel by carrier} {travel})
(new-type {carrier} {thing})
(new-type-role {travel carrier} {travel by carrier} {carrier})
(x-is-a-y-of-z {travel by carrier} {travel carrier} {moving object})

(new-type {travel by vehicle} {travel by carrier})
(new-type {vehicle} {object})
(new-type {travel vehicle} {vehicle})

(new-type {travel by animal} {travel by carrier})
(new-action-type {travel by animal} 
		 :agent-type {person} 
		 :object-type {animal})
(new-type {travel animal} {travel by carrier})	
(new-type {mountable animal} {thing})	 
(new-is-a {travel animal} {mountable animal})

(new-type {travel as passenger} {travel by carrier})	
(new-type {travel as driver} {travel by carrier})
(new-type {travel as} {action})	
(new-split-subtypes {travel as} '({passenger} {driver}))
(x-is-the-y-of-z {passenger} {action agent} {travel as passenger})
(x-is-the-y-of-z {driver} {action agent} {travel as driver})

(new-type {runner} {person})
(new-type {run} {action})
(new-is-a {run} {travel on land})
(new-action-type {run} 
		 :agent-type {runner} 
		 :object-type {animated object}
		 :recipient-type {animated object})
(new-is-a {runner} {animated object})	
		 
(new-type {travel by car} {travel by vehicle})
(new-type {car} {object})
(new-action-type {travel by car} 
		 :agent-type {person} 
		 :object-type {car})
(new-is-a {travel by car} {travel on land})		 
(x-is-the-y-of-z {car} {travel carrier} {travel by car})

(new-type {travel by bus} {travel by vehicle})
(new-type {bus} {object})
(new-action-type {travel by bus} 
		 :agent-type {person} 
		 :object-type {bus})
(new-is-a {travel by bus} {travel on land})		 
(x-is-the-y-of-z {bus} {travel carrier} {travel by bus})

(new-type {travel by taxi} {travel by vehicle})
(new-type {taxi} {car})
(new-type {cabdriver} {person})
(new-action-type {travel by taxi} 
		 :agent-type {cabdriver} 
		 :object-type {car})
(new-is-a {travel by taxi} {travel on land})		 
(x-is-the-y-of-z {taxi} {travel carrier} {travel by taxi})

(new-type {travel by bike} {travel by vehicle})
(new-type {bike} {object})
(new-type {cyclist} {person})
(new-action-type {travel by bike} 
		 :agent-type {cyclist} 
		 :object-type {bike})
(new-is-a {travel by bike} {travel on land})		 
(x-is-the-y-of-z {bike} {travel carrier} {travel by bike})

(new-type {travel by motorbike} {travel by vehicle})
(new-type {motorbike} {object})
(new-type {motorcyclist} {person})
(new-action-type {travel by motorbike} 
		 :agent-type {motorcyclist} 
		 :object-type {motorbike})
(new-is-a {travel by motorbike} {travel on land})		 
(x-is-the-y-of-z {motorbike} {travel carrier} {travel by motorbike})

(new-type {travel by boat} {travel by vehicle})
(new-type {boat} {object})
(new-action-type {travel by boat} 
		 :agent-type {person} 
		 :object-type {boat})
(new-is-a {travel by boat} {travel in water})		 
(x-is-the-y-of-z {boat} {travel carrier} {travel by boat})

(new-type {travel by plane} {travel by vehicle})
(new-type {airplane} {object})
(new-action-type {travel by plane} 
		 :agent-type {person} 
		 :object-type {airplane})
(new-is-a {travel by plane} {travel in air})		 
(x-is-the-y-of-z {airplane} {travel carrier} {travel by plane})

(new-type {plane} {vehicle})
(new-type {flier} {person})
(new-action-type {fly} 
		 :agent-type {flier} 
		 :object-type {plane})
(new-is-a {flier} {animated object})	
(new-is-a {fly} {travel in air})

(new-type {swimmer} {person})
(new-action-type {swim} 
		 :agent-type {swimmer} 
		 :object-type {water})
(new-is-a {swimmer} {animated object})
(new-is-a {swim} {travel in water})
