/* Unpin.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Unpin extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	
//CONSTRUCTORS
	public Unpin() {parameters = null;}

//PUBLIC INSTANCE METHODS
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{

/*	This unpins the argument without evaluating it first, which is right for
	(unpin #[reference ...]), but nothing else
		if(!args.Car().isReference())
		{
			return new SchemeObject(SchemeObject.ERROR, "argument must be a REFERENCE");
		}
		SchemeObject.Unpin((Long)(args.Car().mContents));
*/

/*	This evaluates the argument, requires it to evaluate to a REFERENCE, and unpins
	that. (unpin #[reference ...]) does not work, but (unpin '#[reference ...]) does,
	as well as (begin (define r '#[reference ...]) (unpin r)).
*/
		SchemeObject ref = interpreter.Eval(args.Car(), env);
		
		if(!ref.isReference())
		{
			return new SchemeObject(SchemeObject.ERROR, "argument must be a REFERENCE");
		}
		SchemeObject.Unpin((Long)(ref.mContents));
		
		return SchemeObject.TRUE;
	}
};

