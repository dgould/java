/* WriteLine.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;
import java.io.*;


public class WriteLine extends Primitive
{
	public WriteLine() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject filehandle = interpreter.Eval(args.Car(), env);
		if(!filehandle.isNative() || !(filehandle.mContents instanceof FileWriter))
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong argument type");
		}
		
		SchemeObject line = interpreter.Eval(args.Cdr().Car(), env);
		if(!line.isString())
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong argument type");
		}
		
		try
		{
			((FileWriter)(filehandle.mContents)).write(((String)(line.mContents)) + "\n");
		}
		catch(IOException e)
		{
			return new SchemeObject(SchemeObject.ERROR, (Object)(e.getMessage()));
		}
		
		return SchemeObject.TRUE;
	}
}
