package org.dgould.scm;

import org.dgould.*;
import java.lang.*;
import java.io.*;

public class SchemeTerminal extends Object
{	
	public static void main(String[] args) throws java.lang.Exception
	{
		boolean debug = false;
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equalsIgnoreCase("-debug"))
			{
				debug = true;
			}
		}
		
		SchemeInterpreter scheme = new SchemeInterpreter();
		scheme.debug = debug;
		
		InputStreamReader isr = new InputStreamReader(System.in);
		Scanner input = new Scanner(isr);
		
		SchemeObject exp;
		boolean quit = false;
		while(!quit)
		{
			System.out.print("\n> ");
			System.out.flush();
			
			exp = scheme.Read(input);
			if(exp.isError() && 
			   (((String)exp.mContents).equals("EXIT") ||
			    ((String)exp.mContents).equals(SchemeInterpreter.READER_EOF)))
			{
				quit = true;
			}
			
			if(quit)
			{
				continue;
			}
			
			SchemeObject val = scheme.Eval(exp);
			if(val.isError() && ((String)val.mContents).equals("EXIT"))
			{
				quit = true;
			}
			
			String valText = scheme.Print(val);
			System.out.print(valText);
		}
		System.out.print("\n");
	}
};

