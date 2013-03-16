/* Procedure.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;

public class Procedure extends Object
{
//PRIVATE CLASS MEMBERS
	static private SchemeInterpreter interpreter;
	
//PRIVATE INSTANCE MEMBERS
	private boolean primitive;
	private Object body;
	private SchemeEnvironment procEnv;
	
	
//PUBLIC CLASS METHODS
	public static void SetInterpreter(SchemeInterpreter interpreter)
	{
		Procedure.interpreter = interpreter;
	}

//CONSTRUCTORS
	public Procedure(SchemeObject code, SchemeEnvironment environment)
	{
		primitive = false;
		body = (Object)code;
		procEnv = environment;
	}
	
	public Procedure(Primitive p)
	{
		primitive = true;
		body = (Object)p;
		procEnv = interpreter.globalEnv;
	}

//PUBLIC INSTANCE METHODS
	public boolean isPrimitive()
	{
		return primitive;
	}
	
	public SchemeObject Apply(SchemeObject args, SchemeEnvironment env)
	{
		if(primitive)
		{
			return ((Primitive)body).Invoke(args, env);
		}
		
		//bind arguments to formal parameters
		SchemeEnvironment newEnv = new SchemeEnvironment(procEnv);
		SchemeObject argList = args;
		SchemeObject formalList = ((SchemeObject)body).Car();
		SchemeObject arg;
		SchemeObject result;
		SchemeObject nil = SchemeObject.NIL;
		while(argList != nil && formalList != nil)
		{
			arg = interpreter.Eval(argList.Car(), env);
			newEnv.AddBinding(formalList.Car(), arg);
			
			argList = argList.Cdr();
			formalList = formalList.Cdr();
		}
		if(argList != nil)
		{
			result = new SchemeObject(SchemeObject.ERROR, "too many arguments");
			return result;
		}
		if(formalList != nil)
		{
			result = new SchemeObject(SchemeObject.ERROR, "too few arguments");
			return result;
		}
		
		SchemeObject expressions = ((SchemeObject)body).Cdr();
		result = nil;
		while(expressions != nil)
		{
			result = interpreter.Eval(((SchemeObject)expressions).Car(),
			    newEnv);
			expressions = expressions.Cdr();
		}
		return result;
	}
}
