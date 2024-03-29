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

% part 5a.

getallmeetings([],[]).

getallmeetings([H|T],Z):-
	H=[_|M],
	M=[F|N],
	getallmeetings(T,Z1),
	sortappend(Z1,F,U),
	sortappend(U,N,Z),
	!.

getallmeetings(C,Z):-
	C=[_|M],
	Z=M.

% part 5b.

participants([],[]):- !.

participants(C,Z):-
	getallmeetings(C,MEETINGS),
	participants(MEETINGS,C,Z),
	!.

% base case for three args
participants(_,[],[]).
participants([],_,[]).


% list of meetings here
% recurse on T first, then do MNEXT
participants([M1|MNEXT],[H|T],LIST):-
	H=[N,PMEETS],
	member(M1,PMEETS),
	participants(M1,T,REST),
	S=[N|REST],
	CURRMEET=[M1,S],
	participants(MNEXT,[H|T],NEXTML), % calls this version
	LIST=[CURRMEET|NEXTML].

% failure on first M of list not member case
participants([M1|MNEXT],[H|T],LIST):-
	H=[_,PMEETS],
	\+(member(M1,PMEETS)),
	participants(M1,T,REST),
	CURRMEET=[M1,REST],
	participants(MNEXT,[H|T],NEXTML),
	LIST=[CURRMEET|NEXTML].

% failure case for single M not member of PMEETS
participants(M,[H|T],NAMES):-
	H=[_,PMEETS],
	\+(member(M,PMEETS)),
	participants(M,T,NAMES).

% here we take in a single meeting and the tail
% of the people list to find another name
participants(M,[H|T],NAMES):-
	H=[N,PMEETS],
	member(M,PMEETS),
	participants(M,T,NEXTNAME),
	sort([N|NEXTNAME],NAMES).

% part 5c.

osched(MR,MH,C,Z):-
	crossmyfor(MR,MH,SLOTS),
	participants(C,MEETLIST),
	osched(SLOTS,MEETLIST,Z).

osched(_,[],[]).
osched([],[],[]):- !.

osched(SLOTS,[M1|MR],Z):-
	list(M1),
	select(SLT1,SLOTS,RSLOTS),
	COMB1=[SLT1,M1],
	osched(RSLOTS,MR,ZR),
	sort([COMB1|ZR],Z).

% part 5d.
% NAME cannot appear in two slots with the same hour, room
% is not important

% first, take in the parameters, generate the potential
% lists, and start searching it for dupes
xsched(MR,MH,C,Z):-
	osched(MR,MH,C,Z),
	xsched(Z).

% now we break up the first meeting in IZ into hour and names
% and see if we can match those against any other meeting
% in the remainder of the meeting list (MSR)
% then we call this again on the MSR to check its M1
% if we make it past that, then this IZ is a valid Z
xsched(IZ):-
	IZ=[M1|MSR],
	M1=[SLOT,DESC],
	SLOT=[_ROOM,HOUR],
	DESC=[_TOPIC,NAMES],
	xsched(HOUR,NAMES,MSR),
	xsched(MSR).

xsched([]).

% this rule only needs three since we will be enforcing the rule
% take this HOUR and these NAMES and check against the rest of list
% case where the hour is not the same
xsched(SH,SN,IZ):-
	integer(SH),
	IZ=[M1|MSR],
	M1=[SLOT,_DESC],
	SLOT=[_ROOM,HOUR],
	SH\=HOUR,
	xsched(SH,SN,MSR).

% case where hour is the same and check the names,
% need to recurse down SN too
xsched(SH,SN,IZ):-
	integer(SH),
	list(SN),
	IZ=[M1|MSR],
	M1=[SLOT,DESC],
	SLOT=[_ROOM,HOUR],
	DESC=[_TOPIC,NAMES],
	SH=HOUR,
	select(SN1,SN,SNR),
	\+(member(SN1,NAMES)),
	xsched(SH,SNR,IZ),
	xsched(SH,SN,MSR).

% this base case is for when you have checked all the meetings in MSR, you are done
xsched(_,_,[]).

% this base case is for having exhausted the meeting participants,
% no need to keep recursing.
xsched(_,[],_).
