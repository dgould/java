/* Primitive.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;

public abstract class Primitive extends Object
{
//PROTECTED CLASS MEMBERS
/* TODO: REFACTOR
BAD pattern here: using class statics here means HORRIBLE things would happen
if we tried to create multiple interpreters. Should:
1. eliminate globalEnv by always getting it from interpreter
2. eliminate interpreter by passing it in to the methods
Then, would be possible, say, to have multiple threads each owning an
interpreter, each with separate but overlapping envs.
*/
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
