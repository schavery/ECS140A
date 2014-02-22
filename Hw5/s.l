(defun double-recursive (x)
	(cond ((null x) nil)
		((not (listp x)) nil)
		(t (cons (car x) (cons (car x) (double-recursive (cdr x)))))))

(defun double-iterative (x)
	(cond ((null x) nil)
		((not (listp x)) nil)
		(t (di-helper x ())))) ; we need a helper function

(defun di-helper (in out)
	(do ((ii 0 (1+ ii))) ; internal variable set up
		((= (list-length in) ii) out) ; end condition and return thingy
		(setq out (append out (cons (nth ii in) (cons (nth ii in) nil)))))) ; body of the loop

(defun double-mapcar (x)
	(cond ((not (listp x)) nil)
		(t (mapcar #'list x x))))

;; part 2
(defun rdouble (x)
	(cond ((null x) nil)
		((not (listp x)) nil)
		((listp (car x)) (cons (rdouble (car x)) (rdouble (cdr x))))
		(t (cons (car x) (cons (car x) (rdouble (cdr x)))))))
