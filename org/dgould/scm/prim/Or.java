/* Or.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//////////////////////////////////////////////////////////////////////////////
// THESE USE NORMAL-ORDER EVALUATION OF ARGUMENTS INSTEAD OF APPLICATIVE ORDER
// I CAN CHEAT LIKE THIS TO MAKE PRIMITIVES THAT ARE REALLY SPECIAL FORMS
// THEY ALSO TAKE VARIABLE NUMBERS OF ARGUMENTS

// or evaluates args from left to right and returns true at the first true
// or false when it reaches the end of the list
public class Or extends Primitive
{
	public Or() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject argList = args;
		while(argList != SchemeObject.NIL)
		{
			if(interpreter.Eval(argList.Car(), env) != SchemeObject.NIL)
			{
				return SchemeObject.TRUE;
			}
			argList = argList.Cdr();
		}
		return SchemeObject.NIL;
	}
}

