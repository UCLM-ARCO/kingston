(new-type {expectations} {thing})
(new-type-role {has expectation} {expectations} {event})

;; **************************************
;; *** EXPECTATION FOR READING A BOOK ***
;; **************************************
(new-indv {picking up a book for reading it} {expectations})

(x-is-the-y-of-z {has expectation} {picking up a book for reading it} {walk towards})
(x-is-the-y-of-z {has expectation} {picking up a book for reading it} {pick up})
(x-is-the-y-of-z {has expectation} {picking up a book for reading it} {turn around})
(x-is-the-y-of-z {has expectation} {picking up a book for reading it} {sit down})
(x-is-the-y-of-z {has expectation} {picking up a book for reading it} {get up})

; (show-all-x-of-y  {has expectation}  {picking up a book for reading it} )

(new-indv {dropping an object} {expectations})
(x-is-the-y-of-z {has expectation} {dropping an object} {sit down})
(x-is-the-y-of-z {has expectation} {dropping an object} {get up})
(x-is-the-y-of-z {has expectation} {dropping an object} {pick up})

(new-indv {picking up an object while sit} {expectations})
(x-is-the-y-of-z {has expectation} {picking up an object while sit} {get up})
(x-is-the-y-of-z {has expectation} {picking up an object while sit} {pick up})

;; **************************************
;; *** EXPECTATION FOR PLAYING WITH   ***
;; *** PUCHING BALL                   ***
;; **************************************
(new-indv {playing with punching ball} {expectations})

(x-is-the-y-of-z {has expectation} {playing with punching ball} {walk towards})
;(x-is-the-y-of-z {has expectation} {playing with punching ball} {turn around})
(x-is-the-y-of-z {has expectation} {playing with punching ball} {punch})

;(new-indv {kicking around punching ball} {expectations})
;(x-is-the-y-of-z {has expectation} {kicking around punching ball} {walk towards})
;(x-is-the-y-of-z {has expectation} {kicking around punching ball} {turn around})
;(x-is-the-y-of-z {has expectation} {kicking around punching ball} {kick})


;; **************************************
;; *** EXPECTATION FOR PUNCHING AND   ***
;; *** KICKING	                      ***
;; **************************************
(new-indv {punching and kicking} {expectations})

(x-is-the-y-of-z {has expectation} {punching and kicking} {punch})
(x-is-the-y-of-z {has expectation} {punching and kicking} {kick})
(x-is-the-y-of-z {has expectation} {punching and kicking} {punch})

(new-indv {start punching} {expectations})

;(x-is-the-y-of-z {has expectation} {start punching} {walk towards})
;(x-is-the-y-of-z {has expectation} {start punching} {punch})
;(x-is-the-y-of-z {has expectation} {start punching} {walk towards})
;(x-is-the-y-of-z {has expectation} {start punching} {kick})

;; **************************************
;; *** EXPECTATION FOR PLAYING        ***
;; *** SITTING DOWN                   ***
;; **************************************

;(new-indv {sitting down} {expectations})

;(x-is-the-y-of-z {has expectation} {sitting down} {walk towards})
;(x-is-the-y-of-z {has expectation} {sitting down} {sit down})
;(x-is-the-y-of-z {has expectation} {sitting down} {get up})


;; **************************************
;; *** EXPECTATION FOR WAVING         ***
;; *** PEOPLE	                      ***
;; **************************************
;(new-indv {waving and leaving} {expectations})

;(x-is-the-y-of-z {has expectation} {waving and leaving} {wave})
;(x-is-the-y-of-z {has expectation} {waving and leaving} {walk towards})



(new-indv {sitting down and standing up with object} {expectations})

(x-is-the-y-of-z {has expectation} {sitting down and standing up with object} {pick up})
(x-is-the-y-of-z {has expectation} {sitting down and standing up with object} {sit down})
(x-is-the-y-of-z {has expectation} {sitting down and standing up with object} {get up})

