The "org.dgould.geb.bloop.*" package is an implementation of the "BlooP"
language described in Douglas Hofstadter's book _Godel, Escher, Bach_.
Being a language designed to illustrate certain mathematical properties,
it's a rather strange one for actual coding, especially by modern 
standards.

As a programming exercise, this implementation is interesting in several
ways:
* It includes a scanner and parser built using JLex and CUP, which are
  Java equivalents of Lex and YACC (or Flex and Bison).
* There's a neat use of exceptions for control flow in handling multi-
  level block-exit statements.
* Computation is done using "org.dgould.NaturalNumber", to support
  arbitrarily large integers. This is something I wrote separately, as 
  an exploration of arithmetic via the "elementary-school algorithms".

To build it, you first need to install JLex and CUP:
http://www.cs.princeton.edu/~appel/modern/java/JLex/
http://www.cs.princeton.edu/~appel/modern/java/CUP/

[ADD INSTRUCTIONS FOR RUNNING JLEX AND CUP]

[ADD INSTRUCTIONS FOR COMPILING]

[ADD INSTRUCTIONS FOR RUNNING]


