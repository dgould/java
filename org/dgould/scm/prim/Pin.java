/* Pin.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Pin extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	
//CONSTRUCTORS
	public Pin() {parameters = null;}

//PUBLIC INSTANCE METHODS
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject obj = interpreter.Eval(args.Car(), env);
		
		return new SchemeObject(SchemeObject.REFERENCE, SchemeObject.Pin(obj));
	}
};

