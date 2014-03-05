% part 1 queries.

c_numbers(N):-
	course(N,_,_).

c_pl(N):-
	course(N,programming_languages,_).

c_notpl(N):-
	course(N,X,_),
	X\=programming_languages.

c_inst60(L):-
	course(60,_,L).

c_inst60_sorted(L):-
	course(60,_,U),
	sort(U,L).

c_inst20(L):-
	course(20,_,L).

c_inst20_sorted(L):-
	course(20,_,U),
	sort(U,L).

c_inst_sorted(N,L):-
	course(N,_,U),
	sort(U,L).

c_single_inst(N):-
	course(N,_,[_]).

c_multi_inst(N):-
	course(N,_,[_|T]),
	T\=[].

c_exclusive(I,N):-
	course(N,_,[I]).

c_12_inst_1or(N):-
	course(N,_,[_]);
	course(N,_,[_,_]).

c_12_inst_2wo(N):-
	course(N,_,[_]).
c_12_inst_2wo(N):-
	course(N,_,[_,_]).

% part 2, delete

delete_question(X):-
	X="157".

% part 2 continued

sortappend(L1,L2,Z):-
	append(L1,L2,U),
	sort(U,Z).

% part 3.

distribute(_,[],[]).
distribute(W,[H|T],Z):-
	distribute(W,T,R),
	Q=[W,H],
	Z=[Q|R].

% part 4.
% given myfor.

myfor(L,U,Result):-
	L=<U,
	L1 is L+1,
	myfor(L1,U,Res1),
	Result=[L|Res1].
myfor(L,U,[]):-
	L>U.

% the cross product part.

crossmyfor(R,H,Z):-
	integer(R),
	integer(H),
	myfor(1,R,D1),
	myfor(1,H,D2),
	crossmyfor(D1,D2,Z).

crossmyfor(_,[],[]):- !.

crossmyfor([],_,[]):- !.

crossmyfor([],[],[]).

crossmyfor([H|T],D2,Z):-
	D2=[_|_],
	crossmyfor(T,D2,Z2),
	distribute(H,D2,Z1),
	append(Z1,Z2,Z).

crossmyfor(T,D2,Z):-
	integer(T),
	list(D2),
	distribute(T,D2,Z).

