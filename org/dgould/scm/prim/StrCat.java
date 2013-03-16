/* StrCat.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class StrCat extends Primitive
{
	public StrCat() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject s1 = interpreter.Eval(args.Car(), env);
		SchemeObject s2 = interpreter.Eval(args.Cdr().Car(), env);
		if(!(s1.isString() && s2.isString()))
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong argument type");
		}
		
		SchemeObject result = new SchemeObject(SchemeObject.STRING,
			(String)(s1.mContents) + (String)(s2.mContents));
		
		return result;
	}
}
