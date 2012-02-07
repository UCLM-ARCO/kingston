;;;*****************************************************************************
;;;** ------------------------------------------------------------------------**
;;;** EVENTS DESCRIPTION FOR A SERVICE ORIENTED ARCHITECTURE                  **
;;;** Describing the considere events, and possible actions                   **
;;;** ------------------------------------------------------------------------**
;;;*****************************************************************************


(in-context {general})
(in-namespace "events" :include "common")

;; Anything that was involved in the event belongs to this type-role.
(new-type-role {object involved} {event} {thing})

;; Usually events have a location.
;(the-x-of-y-is-z  {location} {event} {place})
;(new-indv-role {location} {event} {place}
; :english '(:no-iname "location" "venue" "place")
; :may-have t)

;; things cause events
(new-relation {causes} :a-inst-of {thing}
		       :b-inst-of {event})

;; A. Subevents

(new-type {expandable event} {event})

;;; All expandable events have sub-events
(new-type-role {subevent} {expandable event} {event})

(new-complete-split-subtypes {expandable event}
 '({sequential event}
   {all event}
   {or event}
   {iterative event}
   {concurrent event}
   {optional event}
   {conditional event}))

;; 1. Sequantial Events

;; sequential event's subevent have two relations defined for them
;; they can be viewed as a chain of events weaved with these relations
(get-the-x-role-of-y {subevent} {sequential event} :iname {ordered event})

;; both these relations belong to a sequential event when used as statements.
;; These sequential events may use the same event twice, to keep the ordering
;; right we need indeces for each relation. That is the c-value
(new-relation {happens before} :a-inst-of {ordered event}
			       :b-inst-of {ordered event}
			       :c-inst-of {integer}
			       :english '(:relation
					  :inverse-relation "happens after")
			       :transitive t)

;; we might have cycles, so keep a head of the sequence so that we know where to start
(new-indv-role {first ordered event} {sequential event} {ordered event})

;; 2. ALL Events
;; 3. OR Events
;; 4. Optional Events
;; 5. Concurrent Events
;; Being a subevent of these is sufficient knowledge to reason about them.


;; 6. Iterative Events

;; there are two types, these are complete and split, for now
(new-complete-split-subtypes {iterative event}
 '({do n times event} {do until event}))

;;do n times events have a number of iterations
(new-indv-role {number of iterations} {do n times event} {integer})

;;do until events have a termination clause function
(new-indv-role {termination clause} {do until event} {function})


;; 7. Conditional Events

;; Conditional events have predicates.
(new-indv-role {predicate} {conditional event} {function})

