/* And.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//////////////////////////////////////////////////////////////////////////////
// THESE USE NORMAL-ORDER EVALUATION OF ARGUMENTS INSTEAD OF APPLICATIVE ORDER
// I CAN CHEAT LIKE THIS TO MAKE PRIMITIVES THAT ARE REALLY SPECIAL FORMS
// THEY ALSO TAKE VARIABLE NUMBERS OF ARGUMENTS

// and evaluates args from left to right and returns false at the first false
// or true when it reaches the end of the list
public class And extends Primitive
{
	public And() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject argList = args;
		while(argList != SchemeObject.NIL)
		{
			if(interpreter.Eval(argList.Car(), env) == SchemeObject.NIL)
			{
				return SchemeObject.NIL;
			}
			argList = argList.Cdr();
		}
		return SchemeObject.TRUE;
	}
}

