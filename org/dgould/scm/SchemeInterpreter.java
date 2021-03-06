/* SchemeInterpreter.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;

import org.dgould.*;
import org.dgould.scm.prim.*;

import java.net.*;
import java.io.*;

public class SchemeInterpreter extends Object
{
//CLASS CONSTANTS
	public final static String READER_EOF		= "PARSING REACHED END OF INPUT SOURCE";
	
	public final static String SPECIAL_IF		= "if";
	public final static String SPECIAL_COND		= "cond";
	public final static String SPECIAL_LAMBDA	= "lambda";
	public final static String SPECIAL_DEFINE	= "define";
	public final static String SPECIAL_LET		= "let";
	public final static String SPECIAL_SET		= "set!";
	public final static String SPECIAL_SETCAR	= "set-car!";
	public final static String SPECIAL_SETCDR	= "set-cdr!";
	public final static String SPECIAL_QUOTE	= "quote";
	public final static String KEYWORD_ELSE		= "else";
	
//PUBLIC DATA MEMBERS
	public SchemeEnvironment globalEnv;
	public boolean debug;
	public int depth;
	public int tab;
	
//CONSTRUCTORS
	public SchemeInterpreter()
	{
//		Reset();
		Procedure.SetInterpreter(this);
		Primitive.SetInterpreter(this);
		this.globalEnv = new SchemeEnvironment(null);
		this.debug = false;
		this.depth = 0;
		this.tab = 1;
		Primitive.SetGlobalEnv(globalEnv);
		
		// install primitives
		// for each Primitive subclass Y with Scheme symbol 'X:
		// InstallPrimitive("X", (Primitive)(new Y()));
		
	//	InstallPrimitive("assemble",	(Primitive)(new Assemble()));
		InstallPrimitive("+",			(Primitive)(new Plus()));
		InstallPrimitive("-",			(Primitive)(new Minus()));
		InstallPrimitive("*",			(Primitive)(new Times()));
		InstallPrimitive("/",			(Primitive)(new DividedBy()));
		InstallPrimitive("=",			(Primitive)(new NumEquals()));
		InstallPrimitive(">",			(Primitive)(new GreaterThan()));
		InstallPrimitive(">=",			(Primitive)(new GreaterOrEqual()));
		InstallPrimitive("<",			(Primitive)(new LessThan()));
		InstallPrimitive("list",		(Primitive)(new org.dgould.scm.prim.List()));
		InstallPrimitive("cons",		(Primitive)(new Cons()));
		InstallPrimitive("car",			(Primitive)(new Car()));
		InstallPrimitive("cdr",			(Primitive)(new Cdr()));
		InstallPrimitive("null?",		(Primitive)(new Null()));
		InstallPrimitive("pair?",		(Primitive)(new IsPair()));
		InstallPrimitive("eq?",			(Primitive)(new Eq()));
		InstallPrimitive("equal?",		(Primitive)(new Equal()));
		InstallPrimitive("time",		(Primitive)(new org.dgould.scm.prim.Time()));
		InstallPrimitive("and",			(Primitive)(new And()));
		InstallPrimitive("or",			(Primitive)(new Or()));
		InstallPrimitive("begin",		(Primitive)(new Begin()));
		InstallPrimitive("not",			(Primitive)(new Not()));
		InstallPrimitive("print",		(Primitive)(new Print()));
		InstallPrimitive("random",		(Primitive)(new org.dgould.scm.prim.Random()));
		InstallPrimitive("open",		(Primitive)(new Open()));
		InstallPrimitive("close",		(Primitive)(new Close()));
		InstallPrimitive("write-line",	(Primitive)(new WriteLine()));
		InstallPrimitive("concatenate",	(Primitive)(new StrCat()));
		InstallPrimitive("load",		(Primitive)(new LoadFile()));
		InstallPrimitive("pin",			(Primitive)(new Pin()));
		InstallPrimitive("unpin",		(Primitive)(new Unpin()));
		InstallPrimitive("word",		(Primitive)(new Word()));
		InstallPrimitive("list-ref",	(Primitive)(new ListRef()));
		InstallPrimitive("cadr",		(Primitive)(new Cadr()));
		InstallPrimitive("caddr",		(Primitive)(new Caddr()));
		InstallPrimitive("-1+",			(Primitive)(new MinusOnePlus()));
		InstallPrimitive("1+",			(Primitive)(new OnePlus()));
		InstallPrimitive("map",			(Primitive)(new org.dgould.scm.prim.Map()));
		InstallPrimitive("member",		(Primitive)(new org.dgould.scm.prim.Member()));
		InstallPrimitive("assoc",		(Primitive)(new Assoc()));
		
//		this.EvalString("(define (append l1 l2) (if (null? l1) l2 (if (null? (cdr l1)) (cons (car l1) l2) (cons (car l1) (append (cdr l1) l2)))))");
//		this.EvalString("(define (reverse l) (if (or (null? l) (null? (cdr l))) l (append (reverse (cdr l)) (list (car l)))))");
//		this.EvalString("(define (assoc x l) (cond ((null? l) '()) ((equal? x (car (car l))) (car l)) (#t (assoc x (cdr l)))))");
//		this.EvalString("(define (member x l) (cond ((null? l) '()) ((equal? x (car l)) l) (#t (member x (cdr l)))))");
//		this.EvalString("(define (map f l) (if (null? l) '() (cons (f (car l)) (map f (cdr l)))))");
//		this.EvalString("(define (list-ref l n) (if (= n 0) (car l) (list-ref (cdr l) (- n 1))))");
//		this.EvalString("(define (cadr l) (car (cdr l)))");
//		this.EvalString("(define (caddr l) (car (cdr (cdr l))))");
//		this.EvalString("(define (-1+ x) (- x 1))");
//		this.EvalString("(define (1+ x) (+ x 1))");
	}

//PRIVATE CLASS METHODS	
	private static SchemeObject ReadList(Scanner input)
	{
		int nextChar = input.peek();
		if((nextChar < 0) || (nextChar == ')'))
		{
			try
			{
				input.read();
			}
			catch(IOException e)
			{
			}
			return SchemeObject.NIL;
		}
		
		SchemeObject car = Read(input);
		SchemeObject cdr = ReadList(input);
		return Pair.Cons(car, cdr);
	}

//PUBLIC CLASS METHODS
	
	public SchemeObject ReadString(String code)
	{
		StringReader sr    = new StringReader(code);
		Scanner      input = new Scanner(sr, true);
		SchemeObject obj   = Read(input);
		return obj;
	}
	
	public SchemeObject EvalString(String code)
	{
		if(this.debug)
		{
			System.out.println("EvalString: \"" + code + "\"");
		}
		SchemeObject obj   = ReadString(code);
		return Eval(obj, globalEnv);
	}
	
	public SchemeObject EvalFile(URL sourceURL)
	{
		InputStream source;
		try
		{
			source = sourceURL.openStream();
		}
		catch(IOException e)
		{
			return new SchemeObject(SchemeObject.ERROR, "failed to open '" + sourceURL + "'");
		}
		InputStreamReader isr    = new InputStreamReader(source);
		Scanner           input  = new Scanner(isr, true);
		
		SchemeObject obj = SchemeObject.NIL;
		
		while(true)
		{
			obj = Read(input);
			if(obj.isError() && ((String)(obj.mContents)).equals(READER_EOF))
			{
				break;
			}
			obj = Eval(obj);
			if(obj.isError())
			{
				return SchemeObject.NIL;
			}
		}
		
		return SchemeObject.TRUE;
	}
	
	public static SchemeObject Read(Scanner input)
	{
		SchemeObject obj;
		char c;
		String word;
		boolean wasUsingExceptions = input.UsingExceptions();
		input.UseExceptions();
	//	int justRead, i, textSize;
	//	char nextChar;
		
		//An IOException can happen anywhere during the Read(), meaning we reached EOF,
		//so we'll catch it and return an ERROR SchemeObject
		try
		{
			//skip leading whitespace
			input.EatWhitespace();
			
			c = (char)input.read();
			if(c != '(') //base case - atom
			{
				//check for macros
				if(c == '#')
				{
					c = (char)input.read();
					
					//Booleans
					if(c == 't') {return SchemeObject.TRUE;}
					if(c == 'f') {return SchemeObject.NIL;}
					
					//Special objects are "#[TYPE CONTENT]"
					if(c == '[')
					{
						//get type: eatwhitespace, then read until whitespace
						input.EatWhitespace();
						String type = input.ReadUntilWhitespace();
						type = type.toUpperCase();
						input.EatWhitespace();
						String content = input.ReadUntil(']');
						c = (char)input.read();
						
						if(type.equals("REFERENCE"))
						{
							long procNum = Long.parseLong(content);
							return new SchemeObject(SchemeObject.REFERENCE, 
								new Long(procNum));
						}
						
						else if(type.equals("ERROR"))
						{
							return new SchemeObject(SchemeObject.ERROR, content);
						}
						
						else if(type.equals("PRIMITIVE"))
						{
							return new SchemeObject(SchemeObject.ERROR,
								"unimplemented literal type '" + type + "'");
						}
						
						else
						{
							return new SchemeObject(SchemeObject.ERROR,
								"unknown literal type '" + type + "'");
						}
					}
					
					if(c == '(') //vectors (not implemented)
					{
						return new SchemeObject(SchemeObject.ERROR, "vectors not implemented");
					}
					
					return new SchemeObject(SchemeObject.ERROR, "unknown '#' macro form]");
				}
				
				//check for strings
				if(c == '"')
				{
					word = input.ReadUntil('"');
					while(word.endsWith("\\") && (input.peek() > 0))
					{
						word = word + input.read() + input.ReadUntil('"');
					}
					if(input.peek() > 0)
					{
						input.read();
					}

					return new SchemeObject(SchemeObject.STRING, (Object)word);
				}
				
				//check for syntactic sugar of quote special form
				if(c == '\'')
				{
					SchemeObject theCar = new SchemeObject(SchemeObject.SYMBOL,
						(Object)SPECIAL_QUOTE);
					SchemeObject theCadr = Read(input);
					return Pair.Cons(theCar, Pair.Cons(theCadr, SchemeObject.NIL));
				}
				
				//get word
				word = c + input.ReadUntilWhitespaceOr(')');
				boolean number = true;
				Double value = null;
				
				try
				{
					value = new Double(word);
				}
				catch(NumberFormatException e)
				{
					number = false;
				}
				if(number)
				{
					return new SchemeObject(SchemeObject.NUMBER, (Object)value);
				}
				
				//else symbol
				//return word
				return new SchemeObject(SchemeObject.SYMBOL, (Object)word);
			}
			
			//else, list - call recursive helper
			SchemeObject theList = ReadList(input);
			if(SpecialForm_define(theList)) //need to check for special define syntax
			{
				if(theList.Cdr().Car().isPair()) //if second element is a list
				{
					SchemeObject variable = theList.Cdr().Car().Car();
					SchemeObject formals = theList.Cdr().Car().Cdr();
					SchemeObject body = theList.Cdr().Cdr();
					SchemeObject lambda = new SchemeObject(SchemeObject.SYMBOL,
						(Object)SPECIAL_LAMBDA);
					theList.SetCdr(
					Pair.Cons(variable,
					          Pair.Cons(Pair.Cons(lambda,
						                      Pair.Cons(formals, body)),
						                      SchemeObject.NIL)));
				}
			}
			return theList;
		}
		catch(IOException e)
		{
			return new SchemeObject(SchemeObject.ERROR, READER_EOF);
		}
		finally
		{
			if(!wasUsingExceptions)
			{
				input.DontUseExceptions();
			}
		}
	}
	
	public static String Print(SchemeObject exp)
	{
		String rep = "";
		SchemeObject nil = SchemeObject.NIL;
		if(exp == null)
		{
			return rep;
		}
		if(exp.isNumber())
		{
			rep = ((Double)(exp.mContents)).toString();
		}
		if(exp.isBoolean())
		{
			if(((Boolean)(exp.mContents)).booleanValue())
			{
				rep = "#t";
			}
			else
			{
				rep = "#f";
			}
		}
		if(exp.isString())
		{
			rep = "\"" + (String)exp.mContents + "\"";
		}
		if(exp.isSymbol())
		{
			rep = (String)exp.mContents;
		}
		if(exp.isPair())
		{
			rep = "(";
			if(exp.ProperList())
			{
				for(SchemeObject list = exp; list != nil; list = list.Cdr())
				{
					if(list != exp) //not first element
					{
						rep += " ";
					}
					rep += Print(list.Car());
				}
			}
			else
			{
				if(exp.Car() == SchemeObject.NIL)
				{
					rep += "()";
				}
				else
				{
					rep += Print(exp.Car());
				}
				if(exp.Cdr() != SchemeObject.NIL)
				{
					rep += " ";
					if(!(exp.Cdr()).isPair())
					{
						rep += ". ";
					}
					rep += Print(exp.Cdr());
				}
			}
			rep += ")";
		}
		if(exp.isError())
		{
			rep = "ERROR: '" + (String)(exp.mContents) + "'";
		}
		if(exp.isNative())
		{
			rep = "#[native Java object]";
		}
		if(exp.isProcedure())
		{
			if(((Procedure)(exp.mContents)).isPrimitive())
			{
				rep = "#[primitive prodecure]";
			}
			rep = "#[lambda procedure]";
		}
		if(exp.isReference())
		{
			rep = "#[reference " + ((Long)(exp.mContents)).toString() + "]";
		}
		return rep;
	}

	public static boolean SpecialForm_if(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_IF);
	}
	public static boolean SpecialForm_cond(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_COND);
	}
	public static boolean SpecialForm_lambda(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_LAMBDA);
	}
	public static boolean SpecialForm_define(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_DEFINE);
	}
	public static boolean SpecialForm_let(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_LET);
	}
	public static boolean SpecialForm_set(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_SET);
	}
	public static boolean SpecialForm_setCar(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_SETCAR);
	}
	public static boolean SpecialForm_setCdr(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_SETCDR);
	}
	public static boolean SpecialForm_quote(SchemeObject obj)
	{
		return obj.isPair() && obj.Car().isSymbol() &&
		       ((String)((obj.Car()).mContents)).equalsIgnoreCase(SPECIAL_QUOTE);
	}
	
	public static boolean Keyword_else(SchemeObject obj)
	{
		return obj.isSymbol() && ((String)(obj.mContents)).equalsIgnoreCase(KEYWORD_ELSE);
	}

//PUBLIC INSTANCE METHODS
	//create a SchemeObject for a procedure for the primitive and add to globalEnv
	public void InstallPrimitive(String name, Primitive prim)
	{
		Procedure proc = new Procedure(prim);
		SchemeObject obj = new SchemeObject(SchemeObject.PROCEDURE, (Object)proc);
		SchemeObject sym = new SchemeObject(SchemeObject.SYMBOL, (Object)name);
		globalEnv.AddBinding(sym, obj);
	}
	
	//evaluate exp in env and return value
	public SchemeObject Eval(SchemeObject exp)
	{
		return this.Eval(exp, this.globalEnv);
	}
	public SchemeObject Eval(SchemeObject exp, SchemeEnvironment env)
	{
		String tabStr = "";
		if(this.debug)
		{
			for(int i = 0; i < this.depth; i++)
			{
				tabStr += " ";
			}
			System.out.println(tabStr + "Eval ==> \"" + Print(exp) + "\"");
			this.depth += this.tab;
		}
		SchemeObject result = null;
		
		if(exp.isSelfEvaluating())
		{
			result = exp;
		}
		else if(exp.isSymbol())
		{
			result = env.GetBinding(exp);
		}
		else if(exp.isReference())
		{
			result = SchemeObject.DeReference((Long)(exp.mContents));
		}
		else if(exp.isPair()) //application or special form
		{
			if(SpecialForm_if(exp))
			{
				result = Eval_if(exp, env);
			}
			else if(SpecialForm_cond(exp))
			{
				result = Eval_cond(exp, env);
			}
			else if(SpecialForm_lambda(exp))
			{
				result = Eval_lambda(exp, env);
			}
			else if(SpecialForm_define(exp))
			{
				result = Eval_define(exp, env);
			}
			else if(SpecialForm_let(exp))
			{
				result = Eval_let(exp, env);
			}
			else if(SpecialForm_set(exp))
			{
				result = Eval_set(exp, env);
			}
			else if(SpecialForm_setCar(exp))
			{
				result = Eval_setCar(exp, env);
			}
			else if(SpecialForm_setCdr(exp))
			{
				result = Eval_setCdr(exp, env);
			}
			else if(SpecialForm_quote(exp))
			{
				result = Eval_quote(exp, env);
			}
			else
			{
				result = Eval_Application(exp, env);
			}
		}
		
		if(this.debug)
		{
			this.depth -= this.tab;
			System.out.println(tabStr + "^--- <== \"" + Print(result) + "\"");
		}
		return result;
	}
	
	public SchemeObject Eval_if(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject args = exp.Cdr();
		SchemeObject testClause = args.Car();
		args = args.Cdr();
		SchemeObject thenClause = args.Car();
		args = args.Cdr();
		SchemeObject elseClause;
		if(args == SchemeObject.NIL)
		{
			elseClause = SchemeObject.NIL;
		}
		else
		{
			elseClause = args.Car();
		}
		args = null;
		
		if(Eval(testClause, env).True())
		{
			return Eval(thenClause, env);
		}
		else
		{
			return Eval(elseClause, env);
		}
	}
	public SchemeObject Eval_cond(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject args = exp.Cdr();
		while(args != SchemeObject.NIL)
		{
			if(Keyword_else(args.Car().Car()) || Eval(args.Car().Car(), env).True())
			{
				SchemeObject expList = args.Car().Cdr();
				SchemeObject temp = SchemeObject.NIL;
				while(expList != SchemeObject.NIL)
				{
					temp = Eval(expList.Car(), env);
					expList = expList.Cdr();
				}
				return temp;
			}
			args = args.Cdr();
		}
		return SchemeObject.NIL;
	}
	public SchemeObject Eval_lambda(SchemeObject exp, SchemeEnvironment env)
	{
		Procedure p = new Procedure(exp.Cdr(), env);
		SchemeObject result = new SchemeObject(SchemeObject.PROCEDURE, (Object)p);
		return result;
	}
	public SchemeObject Eval_define(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject value = Eval(exp.Cdr().Cdr().Car(), env);
		env.AddBinding(exp.Cdr().Car(), value);
		return exp.Cdr().Car();
	}
	public SchemeObject Eval_let(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject bindings = exp.Cdr().Car();
		SchemeObject body = exp.Cdr().Cdr();
		SchemeObject binding;
		SchemeObject value = SchemeObject.NIL;
		SchemeEnvironment newEnv = new SchemeEnvironment(env);
		
		while(bindings != SchemeObject.NIL)
		{
			binding = bindings.Car();
			bindings = bindings.Cdr();
			newEnv.AddBinding(binding.Car(), Eval(binding.Cdr().Car(), env));
		}
		
		while(body != SchemeObject.NIL)
		{
			value = Eval(body.Car(), newEnv);
			body = body.Cdr();
		}
		return value;
	}
	public SchemeObject Eval_set(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject variable = exp.Cdr().Car();
		SchemeObject value = Eval(exp.Cdr().Cdr().Car(), env);
		
		env.SetBinding(variable, value);
		return SchemeObject.TRUE;
	}
	public SchemeObject Eval_setCar(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject pair = Eval(exp.Cdr().Car(), env);
		SchemeObject value = Eval(exp.Cdr().Cdr().Car(), env);
		
		pair.SetCar(value);
		return SchemeObject.TRUE;
	}
	public SchemeObject Eval_setCdr(SchemeObject exp, SchemeEnvironment env)
	{
		SchemeObject pair = Eval(exp.Cdr().Car(), env);
		SchemeObject value = Eval(exp.Cdr().Cdr().Car(), env);
		
		pair.SetCdr(value);
		return SchemeObject.TRUE;
	}
	public SchemeObject Eval_quote(SchemeObject exp, SchemeEnvironment env)
	{
		return exp.Cdr().Car();
	}
	public SchemeObject Eval_Application(SchemeObject exp,
	                                     SchemeEnvironment env)
	{
		SchemeObject proc = Eval(exp.Car(), env);
		if(!proc.isProcedure())
		{
			return new SchemeObject(SchemeObject.ERROR,
			       "Operator is not a procedure: " + Print(proc));
		}
		return ((Procedure)(proc.mContents)).Apply(exp.Cdr(), env);
	}
	
	public void Reset()
	{
		globalEnv = new SchemeEnvironment(null);
		
	}
}
