/* LoadFile.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;
import java.io.*;


public class LoadFile extends Primitive
{
	public LoadFile() {parameters = null;}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject filename = interpreter.Eval(args.Car(), env);
		if(!(filename.isString() || filename.isSymbol()))
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong argument type");
		}
		
		java.net.URL url;
		try
		{
			url = new java.net.URL((String)(filename.mContents));
		}
		catch(java.net.MalformedURLException e1)
		{
			return new SchemeObject(SchemeObject.ERROR,
				"malformed URL: \"" + (String)(filename.mContents) + "\"");
		}
		
		SchemeObject obj;
		obj = interpreter.EvalFile(url);
		if(obj.isError())
		{
			return obj;
		}
		return SchemeObject.TRUE;
	}
}

