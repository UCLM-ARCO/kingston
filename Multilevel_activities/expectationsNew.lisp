(new-type {expectations} {thing})
(new-type-role {has expectation} {expectations} {event})

;; **************************************
;; *** EXPECTATION FOR READING A BOOK ***
;; **************************************
(new-indv {picking up a book for reading it} {expectations})

(x-is-the-y-of-z {walk} {has expectation} {picking up a book for reading it})
(x-is-the-y-of-z {pick up} {has expectation} {picking up a book for reading it})
(x-is-the-y-of-z {turn around} {has expectation} {picking up a book for reading it})
(x-is-the-y-of-z {sit down on} {has expectation} {picking up a book for reading it})
(x-is-the-y-of-z {get up} {has expectation} {picking up a book for reading it})

;(show-all-x-of-y {has expectation} {picking up a book for reading it})

(new-indv {dropping an object} {expectations})
(x-is-the-y-of-z {sit down on} {has expectation} {dropping an object})
(x-is-the-y-of-z {get up} {has expectation} {dropping an object})
(x-is-the-y-of-z {pick up} {has expectation} {dropping an object})

(new-indv {picking up an object while sit} {expectations})
(x-is-the-y-of-z {get up} {has expectation} {picking up an object while sit})
(x-is-the-y-of-z {pick up} {has expectation} {picking up an object while sit})

;; **************************************
;; *** EXPECTATION FOR PLAYING WITH   ***
;; *** PUCHING BALL                   ***
;; **************************************
(new-indv {playing with punching ball} {expectations})

(x-is-the-y-of-z {walk} {has expectation} {playing with punching ball})
(x-is-the-y-of-z {turn around} {has expectation} {playing with punching ball})
(x-is-the-y-of-z {punch} {has expectation} {playing with punching ball})

(new-indv {kicking around punching ball} {expectations})

(x-is-the-y-of-z {walk} {has expectation} {kicking around punching ball})
(x-is-the-y-of-z {turn around} {has expectation} {kicking around punching ball})
(x-is-the-y-of-z {kick} {has expectation} {kicking around punching ball})


;; **************************************
;; *** EXPECTATION FOR PUNCHING AND   ***
;; *** KICKING	                      ***
;; **************************************
(new-indv {punching and kicking} {expectations})

(x-is-the-y-of-z {punch} {has expectation} {punching and kicking})
(x-is-the-y-of-z {kick} {has expectation} {punching and kicking})
(x-is-the-y-of-z {punch} {has expectation} {punching and kicking})

(new-indv {start punching} {expectations})

(x-is-the-y-of-z {walk} {has expectation} {start punching})
(x-is-the-y-of-z {punch} {has expectation} {start punching})
(x-is-the-y-of-z {walk} {has expectation} {start punching})
(x-is-the-y-of-z {kick} {has expectation} {start punching})

;; **************************************
;; *** EXPECTATION FOR PLAYING        ***
;; *** SITTING DOWN                   ***
;; **************************************

(new-indv {sitting down} {expectations})

(x-is-the-y-of-z {walk} {has expectation} {sitting down})
(x-is-the-y-of-z {sit down on} {has expectation} {sitting down})
(x-is-the-y-of-z {get up} {has expectation} {sitting down})


;; **************************************
;; *** EXPECTATION FOR WAVING         ***
;; *** PEOPLE	                      ***
;; **************************************
(new-indv {waving and leaving} {expectations})

(x-is-the-y-of-z {wave} {has expectation} {waving and leaving})
(x-is-the-y-of-z {walk} {has expectation} {waving and leaving})



(new-indv {sitting down and standing up with object} {expectations})

(x-is-the-y-of-z {pick up} {has expectation} {sitting down and standing up with object})
(x-is-the-y-of-z {sit down on} {has expectation} {sitting down and standing up with object})
(x-is-the-y-of-z {get up} {has expectation} {sitting down and standing up with object})

