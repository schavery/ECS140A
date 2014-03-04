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