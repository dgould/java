/* Open.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;
import java.io.*;


public class Open extends Primitive
{
	public Open() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject filename = interpreter.Eval(args.Car(), env);
		if(!filename.isString())
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong argument type");
		}
		boolean append = false;
		if(args.Cdr() != SchemeObject.NIL && args.Cdr().Car().isBoolean())
		{
			append = ((Boolean)(args.Cdr().Car().mContents)).booleanValue();
		}
		try
		{
			return new SchemeObject(SchemeObject.NATIVE,
				(Object)(new FileWriter((String)filename.mContents, append)));
		}
		catch(IOException e)
		{
			return new SchemeObject(SchemeObject.ERROR, (Object)(e.getMessage()));
		}
	}
}

