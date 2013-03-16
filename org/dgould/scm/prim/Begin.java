/* Begin.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//////////////////////////////////////////////////////////////////////////////
// THESE USE NORMAL-ORDER EVALUATION OF ARGUMENTS INSTEAD OF APPLICATIVE ORDER
// I CAN CHEAT LIKE THIS TO MAKE PRIMITIVES THAT ARE REALLY SPECIAL FORMS
// THEY ALSO TAKE VARIABLE NUMBERS OF ARGUMENTS

//begin takes any number of arguments, evaluates them all,
//and returns the value of the last one
public class Begin extends Primitive
{
	public Begin() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject argList = args;
		SchemeObject temp = SchemeObject.NIL;
		while(argList != SchemeObject.NIL)
		{
			temp = interpreter.Eval(argList.Car(), env);
			if(temp.isError())
			{
				return temp;
			}
			argList = argList.Cdr();
		}
		return temp;
	}
}

