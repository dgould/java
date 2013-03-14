package org.dgould.geb.bloop;

import org.dgould.geb.bloop.ast.*;

public class BlooPTerminal extends Object
{
	public static boolean debug = false;
	public static boolean printTree = false;
	public static boolean testLex = false;
	public static boolean testParse = false;
	
	public static void main(String[] args) throws java.lang.Exception
	{
		debug = false;
		printTree = false;
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equalsIgnoreCase("-debug"))
			{
				debug = true;
			}
			if(args[i].equalsIgnoreCase("-tree"))
			{
				printTree = true;
			}
			if(args[i].equalsIgnoreCase("-testlex"))
			{
				testLex = true;
			}
			if(args[i].equalsIgnoreCase("-testparse"))
			{
				testParse = true;
			}
		}
		
		Yylex lexerObj = new Yylex(System.in);
		
		if(testLex)
		{
			//test scanner
			java_cup.runtime.Symbol symbol;
			TokenValue token;
			for(symbol = lexerObj.yylex(); symbol.sym != sym.EOF; symbol = lexerObj.yylex())
			{
				
				token  = (TokenValue)(symbol.value);
				System.out.println("Symbol: " + symbol.sym);
				System.out.println("Value:  " + ((symbol.sym == sym.CONSTANT) ?
				                                 token.numberValue.toString() :
				                                 ((symbol.sym == sym.YES) ?
				                                  "YES" :
				                                  ((symbol.sym == sym.NO) ?
				                                   "NO" :
				                                   token.identifierValue))));
			}
		}
		else
		{
			ProgramParser parserObj = new ProgramParser();
			parserObj.assignLex(lexerObj);
			
			Ast programElement;
			java_cup.runtime.Symbol parseTree;
			
			BlooPInterpreter interpreter = new BlooPInterpreter();
			interpreter.debug = debug;
			
			boolean quit = false;
			while(!quit)
			{
				System.out.println("parsing...");
				
				try
				{
					if(debug)
					{
						parseTree = parserObj.debug_parse();
					}
					else
					{
						parseTree = parserObj.parse();
					}
					
					if(parseTree == null)
					{
						programElement = null;
					}
					else
					{
						programElement = (Ast)(parseTree.value);
					}
				}
				catch(Exception e1)
				{
					System.out.println("SYNTAX ERROR: " + e1.getMessage());
					programElement = null;
				}
				
				if(programElement == null)
				{
					return;
				}
				
				if(testParse || printTree)
				{
					System.out.println("parse tree:");
					programElement.Print(System.out);
				}
				else if(!testParse)
				{
					try
					{
						if(programElement instanceof Program)
						{
							while(!(programElement instanceof CallExpression))
							{
								System.out.println("Analyzing definition of procedure \"" +
								                   ((Program)programElement).firstProc.name +
								                   "\"");
								interpreter.DefineProcedure(((Program)programElement).firstProc);
								programElement = ((Program)programElement).theRest;
							}
							quit = true;
						}
						
						if(programElement instanceof ProcedureDefinition)
						{
							interpreter.DefineProcedure((ProcedureDefinition)programElement);
						}
						
						if(programElement instanceof CallExpression)
						{
							System.out.println("Analyzing call expression...");
							Value result = interpreter.EvaluateCall((CallExpression)programElement);
							if(result == null)
							{
								System.out.println("EvaluateCall returned null");
							}
							else
							{
								System.out.println("result: " + result.toString());
							}
						}
					
					}
					catch(Exception e)
					{
						System.out.println("ERROR: " + e.getMessage());
						quit = true;
					}
				}
			}
		}
	}
}
