(new-type {expectations} {thing})
(new-type-role {has expectation} {expectations} {event})

;; **************************************
;; *** EXPECTATION FOR READING A BOOK ***
;; **************************************
(new-indv {picking up a book for reading it} {expectations})

(the-x-of-y-is-z {has expectation} {picking up a book for reading it} {walk towards})
(the-x-of-y-is-z {has expectation} {picking up a book for reading it} {pick up})
(the-x-of-y-is-z {has expectation} {picking up a book for reading it} {turn around})
(the-x-of-y-is-z {has expectation} {picking up a book for reading it} {sit down})
(the-x-of-y-is-z {has expectation} {picking up a book for reading it} {get up})

; (show-all-x-of-y  {has expectation}  {picking up a book for reading it} )

(new-indv {dropping an ojbect} {expectations})
(the-x-of-y-is-z {has expectation} {dropping an ojbect} {sit down})
(the-x-of-y-is-z {has expectation} {dropping an ojbect} {get up})
(the-x-of-y-is-z {has expectation} {dropping an ojbect} {pick up})


;; **************************************
;; *** EXPECTATION FOR PLAYING WITH   ***
;; *** PUCHING BALL                   ***
;; **************************************
(new-indv {playing with punching ball} {expectations})

(the-x-of-y-is-z {has expectation} {playing with punching ball} {walk towards})
(the-x-of-y-is-z {has expectation} {playing with punching ball} {turn around})
(the-x-of-y-is-z {has expectation} {playing with punching ball} {punch})

(new-indv {kicking around punching ball} {expectations})
(the-x-of-y-is-z {has expectation} {kicking around punching ball} {walk towards})
(the-x-of-y-is-z {has expectation} {kicking around punching ball} {turn around})
(the-x-of-y-is-z {has expectation} {kicking around punching ball} {kick})


;; **************************************
;; *** EXPECTATION FOR PUNCHING AND   ***
;; *** KICKING	                      ***
;; **************************************
(new-indv {punching and kicking} {expectations})

(the-x-of-y-is-z {has expectation} {punching and kicking} {punch})
(the-x-of-y-is-z {has expectation} {punching and kicking} {kick})
(the-x-of-y-is-z {has expectation} {punching and kicking} {punch})

;; **************************************
;; *** EXPECTATION FOR PLAYING        ***
;; *** SITTING DOWN                   ***
;; **************************************

(new-indv {sitting down} {expectations})

(the-x-of-y-is-z {has expectation} {sitting down} {walk towards})
(the-x-of-y-is-z {has expectation} {sitting down} {sit down})
(the-x-of-y-is-z {has expectation} {sitting down} {get up})
(the-x-of-y-is-z {has expectation} {sitting down} {walk towards})

;; **************************************
;; *** EXPECTATION FOR WAVING         ***
;; *** PEOPLE	                      ***
;; **************************************
(new-indv {waving and leaving} {expectations})

(the-x-of-y-is-z {has expectation} {waving and leaving} {walk towards})
(the-x-of-y-is-z {has expectation} {waving and leaving} {wave})
(the-x-of-y-is-z {has expectation} {waving and leaving} {walk towards})


(new-indv {just waving} {expectations})

(the-x-of-y-is-z {has expectation} {just waving} {walk towards})
(the-x-of-y-is-z {has expectation} {just waving} {wave})


(new-indv {saying goodbye} {expectations})
(the-x-of-y-is-z {has expectation} {saying goodbye} {wave})
(the-x-of-y-is-z {has expectation} {saying goodbye} {walk towards})



(new-indv {sitting down and standing up} {expectations})

(the-x-of-y-is-z {has expectation} {sitting down and standing up} {sit down})
(the-x-of-y-is-z {has expectation} {sitting down and standing up} {get up})

