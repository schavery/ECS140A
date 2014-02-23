;; part 1
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

;; part 3
(defun my-mapcar (fun l1 l2)
	(cond ((null l1) nil)
		((null l2) nil)
		(t (cons (apply fun (list (car l1) (car l2)))
			(my-mapcar fun (cdr l1) (cdr l2))))))

;; part 4
(defun rewrite (x)
	(cond ((null x) nil)
		((not (listp x)) x)
		((listp (car x)) (cons (rewrite (containif (car x))) (rewrite (containif (cdr x)))))
		(t (containif (cons (rewrite (car x)) (rewrite (containif (cdr x))))))))

(defun containif (x)
	(cond ((member 'if x) (replaceif x)) (t x)))

(defun replaceif (x)
	(cond ((= (list-length x) 4) ; with else
			(list 'cond (list (cadr x) (caddr x)) (list 't (cadddr x))))
		((= (list-length x) 3) ; no else
			(list 'cond (list (cadr x) (caddr x))))))

;; part 5
(defun check (x)
	(list (equal (eval x) (eval (rewrite x)))
		(eval x)
		(eval (rewrite x))))
