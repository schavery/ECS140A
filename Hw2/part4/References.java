
public class References {

}





/*



 * This part is a little scattered, but I'm putting it
 * together, gonna try to add more tonight and tomorrow
 * Working with the idea we are using vectors holding
 * objects for the symbol table.
 

part4
After parsing the program,
need to create an output for where and how many times, each
variable was used. Need to output the variables in the same
order they were declared


May need  to keep a list of where the var was used and how
many times, it was used.

Need to count the number of assignments to a variable.
Need to display the lines the variable was displayed on.


import java.util.Stack;

display_output();

Our symbol table will be made of a vector filled with
secondary vectors. Each secondary vector holds the
variables of its block, and for each variable, it will
need a few things about the variable:
name
where it was assigned, how many times on each line
where it was used, how many times per use

Need to create a counter that will reset everytime a new
line is reached in the e-code.
At the end of each line with a count > 0, the value can
be pushed onto the stack, for the respective stacks


For variable counting
if we get an ID token, need to take in the ID value for the
symbol table. 

I don't know how we get line numbers. Read the amount of \n,
and use a counter?

ex:
1   a = 2			//lineCount++  end of line a.lines.addElement(new Integer(linecount));
a.counter.addElement(new Integer(counter)); 
2   b = 5			//lineCount++
b.lines.addElement(new Integer(linecount));
3   a = 1, a = 9000	//lineCount++


each block has a vector to hold an object containing the 
following information on each variable

String id; //Variable name
String value; //

Stack lines = new Stack(); //lines where var was assigned
Stack counter = new Stack(); //times var was given value
Stack used = new Stack();  //lines where var was used
Stack usecount = new Stack(); //count the times used per line


*/