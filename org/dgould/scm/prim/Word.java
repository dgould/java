/* Word.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//////////////////////////////////////////////////////////////////////////////
// THESE USE NORMAL-ORDER EVALUATION OF ARGUMENTS INSTEAD OF APPLICATIVE ORDER
// I CAN CHEAT LIKE THIS TO MAKE PRIMITIVES THAT ARE REALLY SPECIAL FORMS
// THEY ALSO TAKE VARIABLE NUMBERS OF ARGUMENTS

//word takes any number of symbol, string, or number arguments and concatenates
//them all into a string which is returned as a symbol if they are all symbols,
//else as a string THIS IS NOT RIGHT - SHOULD HANDLE NUMBERS AND SYMBOLs BETTER
public class Word extends Primitive
{
//CONSTRUCTORS
	public Word()
	{
		parameters = null;
	}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject argList = args;
		SchemeObject temp = SchemeObject.NIL;
		String result = "";
		boolean symbol = true;
		while(argList != SchemeObject.NIL)
		{
			temp = interpreter.Eval(argList.Car(), env);
			if(temp.isError())
			{
				return temp;
			}
			if(!(temp.isString() || temp.isSymbol() || temp.isNumber()))
			{
				return new SchemeObject(SchemeObject.ERROR,
					"Wrong argument type: symbol or string expected in primitive Word");
			}
			if(!temp.isSymbol()) //XXX need to detect valid symbols properly
			{
				symbol = false;
			}
			if(temp.isNumber())
			{
				result += ((Double)(temp.mContents)).toString();
			}
			else
			{
				result += (String)(temp.mContents);
			}
			argList = argList.Cdr();
		}
		if(symbol)
		{
			return new SchemeObject(SchemeObject.SYMBOL, result);
		}
		return new SchemeObject(SchemeObject.STRING, result);
	}
}
