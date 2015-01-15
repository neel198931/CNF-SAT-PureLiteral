CNF-SAT-PureLiteral
===
A propositional logic formula, also called Boolean expression, is built from variables, operators AND (conjunction, also denoted by /\ ), OR (disjunction, \/), NOT (negation, ¬), and parentheses. A formula is said to be satisfiable if it can be made TRUE by assigning appropriate logical values (i.e. TRUE, FALSE) to its variables. The Boolean satisfiability problem (SAT) is, given a formula, to check whether it is satisfiable. This decision problem is of central importance in various areas of computer science, including theoretical computer science, complexity theory, algorithmics, and artificial intelligence.

This program will find only one possible solutions to a given boolean expression using Backtrack algorithm and write it in output file. 
The program considers solution using pure literal rule.

Let C’ be the subset of clauses that are not yet true or false.

An unassigned variable is pure if all its occurrences in C’ are either negated or non-negated. The pure literal rule is:

If the variable is always non-negated assign it the value true. The effect is that all clauses that contain it are now true.

If the variable is always negated assign it the value false. The effect is that all clauses that contain it are now true.

It is possible that after applying the pure variable rule for some variables, that new variables that were not pure in the past, have become pure. Deeper backtracking should continue only when there are no more pure variables.

This program will read from the command line the following two parameters: .

The input file has the following structure: Line 1: n m sizeC // where n is the number of variables, and m the number of clauses and sizeC the number of literals in the clauses Line 2 to m+1: a list of sizeC positive and negative numbers in the range 1 to n. If the number is negative the literal is negated
