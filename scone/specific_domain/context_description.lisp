;;;*******************************************************
;;;** --------------------------------------------------**
;;;**  RELEVANT KNOWLEDGE FOR THE SUPERVISED CONTEXT    **
;;;** This file defines and describes some of the       **
;;;** objects and structural elements that are present  **
;;;** in the supervised room.			        **
;;;** REQUIRES:    			                **
;;;** --------------------------------------------------**
;;;*******************************************************

;; SOME RELATIONS
(new-relation {requires} 
	      :a-inst-of {event}
	      :b-inst-of {event})

;; GENERAL STATEMENTS

(new-type {surface} {thing})
(new-is-a {floor} {surface})


(new-indv {test room} {room})
(new-indv {test room doorway} {doorway})
(the-x-of-y-is-z {doorway} {test room} {test room doorway})
(new-indv {test room floor} {floor})
(the-x-of-y-is-z {floor} {test room} {test room floor})

(new-type {chair} {thing})
(new-type-role {chair leg} {chair} {thig}) ;;We do not say anything about how many legs a chair can have
(new-type-role {chair sitting surface} {chair} {surface} :n 1)
(new-indv {test room chair} {chair})

(new-type {bouncing element} {thing})
(new-type {punching ball} {thing})
(new-is-a {punching ball} {bouncing element})
(new-indv-role {punching ball location} {punching ball} {location})
;(the-x-of-y-is-z {punching ball location} {pixel?})
(new-statement {punching ball} {is in} {test room}) 
(new-statement {punching ball} {rests upon} {test room floor})

(new-indv {test book} {book})
(new-statement {test book} {rests upon} {test room floor})
(new-statement {test book} {is in direct contact to} {test room floor})


;; Since the book is on the floor, picking up a book is an action that
;; requires the person to lower down in order to approach the book.

;; (new-statement {pick up} {requires} {lower down})

;; Since the chair is not at the entrance of the scenario, it can be
;; assume that in order to sit down on the chair, you have to walk
;; towards it first

(new-statement {sit down} {requires} {walk towards})

;; In order to throw something, you have to have something to throw,
;; so a pick up action is required before the throwing one

(new-statement {throw over head} {requires} {pick up})
(new-statement {throw bottom up} {requires} {pick up})

;; Also kick and punch requires the person to approach the punching
;; ball wich is not at the entrance of the scenario

(new-statement {punch} {requires} {walk towards})
(new-statement {kick} {requires} {walk towards})
