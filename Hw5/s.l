(defun double-recursive (x)
	(cond ((null x) nil)
		((not (listp x)) nil)
		(t (cons (car x) (cons (car x) (double-recursive (cdr x)))))))

(defun double-iterative (x)
	(cond ((null x) nil)
	(t (do ((ii 0 (1+ ii))) ; internal variable set up
		((= (list-length x) ii) x) ; end condition and return thingy
		(append x (nth ii x)) ;(cons (nth ii x) (cons (nth ii x) x))
		)))) ; body

(defun double-mapcar (x))
