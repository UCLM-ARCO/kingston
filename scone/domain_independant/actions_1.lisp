;;;*****************************************************************************
;;;** ------------------------------------------------------------------------**
;;;**               DESCRIPTION AND DEFINITION OF SOME ACTIONS                **
;;;** This file define and describe some of the actions that might take place **
;;;** in the context of a building. These actions are suppossed to support    **
;;;** the planning task.                                                      **
;;;** REQUIRES: moves.lisp (csahin/events/kb)
;;;** ------------------------------------------------------------------------**
;;;*****************************************************************************


(in-context {general})
(in-namespace "events" :include "common")
;;; This file necessarily contains some forward references.
(setq *defer-unknown-connections* t)
;; For our purposes of planning, it is convinient to know which is
;; the action that counteract a given action. This is modeled by
;; means of the "opposite-action-of" relation.
(new-relation {is-opposite-action-of}
	      :a-inst-of {event}
	      :b-inst-of {event}
	      :symmetric t) 


(new-type {believe} {compound event})
(new-type-role {expectation} {believe}  {compound event})