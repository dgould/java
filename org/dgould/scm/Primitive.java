/* Primitive.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;

public abstract class Primitive extends Object
{
//PROTECTED CLASS MEMBERS
	protected static SchemeEnvironment globalEnv;
	protected static SchemeInterpreter interpreter;

//PROTECTED INSTANCE METHODS
	protected SchemeObject parameters;

//CONSTRUCTORS
	//none

//PUBLIC CLASS METHODS
	public static void SetGlobalEnv(SchemeEnvironment env)
	{
		globalEnv = env;
	}
	public static void SetInterpreter(SchemeInterpreter i)
	{
		interpreter = i;
	}
	
//PUBLIC INSTANCE METHODS
	public abstract SchemeObject Invoke(SchemeObject args, SchemeEnvironment env);
	
//PROTECTED INSTANCE METHODS
	protected SchemeEnvironment BindArgs(SchemeObject args, SchemeEnvironment env)
	{
		SchemeEnvironment result = new SchemeEnvironment(globalEnv);
		SchemeObject argList = args;
		SchemeObject formalList = parameters;
		SchemeObject nil = SchemeObject.NIL;
		while(argList != SchemeObject.NIL && formalList != SchemeObject.NIL)
		{
			result.AddBinding(formalList.Car(),
			                  interpreter.Eval(argList.Car(), env));
			formalList = formalList.Cdr();
			argList = argList.Cdr();
		}
		if(argList != nil || formalList != nil)
		{
			//handle error?
			return null;
		}
		return result;
	}
};
