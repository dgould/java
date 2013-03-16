/* SchemeObject.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;


public class SchemeObject extends Object
{
//CLASS CONSTANTS
	public static final int NUMBER		= 0;
	public static final int BOOLEAN		= 1;
	public static final int STRING		= 2;
	public static final int SYMBOL		= 3;
	public static final int PAIR		= 4;
	public static final int PROCEDURE	= 5;
	public static final int ERROR		= 6;
	public static final int NATIVE		= 7;
	public static final int REFERENCE	= 8;
	
	public static final SchemeObject NIL = 
		new SchemeObject(BOOLEAN, new Boolean(false));
	public static final SchemeObject TRUE = 
		new SchemeObject(BOOLEAN, new Boolean(true));
		
//PRIVATE CLASS VARIABLES
	private static java.util.Hashtable pinnedObjects = new java.util.Hashtable();
	private static final boolean randomizeRefs = false;
	private static java.util.Random refMaker = new java.util.Random();
	private static long nextRef = 1;
	
//PUBLIC CLASS METHODS
	public static Long Pin(SchemeObject obj)
	{
		Long ref;
		if(randomizeRefs)
		{
			do
			{
				ref = new Long(Math.abs(refMaker.nextLong()));
			}
			while(pinnedObjects.containsKey((Object)ref));
		}
		else
		{
			ref = new Long(nextRef);
			nextRef++;
		}
		pinnedObjects.put(ref, obj);
		return ref;
	}
	
	public static void Unpin(Long ref)
	{
		pinnedObjects.remove(ref);
	}
	
	public static SchemeObject DeReference(Long ref)
	{
		if(pinnedObjects.containsKey((Object)ref))
		{
			return (SchemeObject)(pinnedObjects.get(ref));
		}
		return new SchemeObject(SchemeObject.ERROR, "Invalid reference");
	}
	
	public static boolean AtomicValueEqual(SchemeObject aVal, SchemeObject bVal)
	{
		if(aVal.isNumber() && bVal.isNumber() &&
		   (((Double)(aVal.mContents)).doubleValue() ==
		    ((Double)(bVal.mContents)).doubleValue()))
		{
			return true;
		}
		
		if(aVal.isString() && bVal.isString() &&
		   ((String)(aVal.mContents)).equals(bVal.mContents))
		{
			return true;
		}
		
		if(aVal.isSymbol() && bVal.isSymbol() &&
		   ((String)(aVal.mContents)).equalsIgnoreCase((String)(bVal.mContents)))
		{
			return true;
		}
		
		if(aVal.isBoolean() && bVal.isBoolean() &&
		   ((Boolean)(aVal.mContents) ==
		    (Boolean)(bVal.mContents)))
		{
			return true;
		}
		
		if(aVal.isReference() && bVal.isReference() &&
		   (((Long)(aVal.mContents)).longValue() ==
		    ((Long)(bVal.mContents)).longValue()))
		{
			return true;
		}
		
		
		
		return aVal == bVal;
	}
	
	//XX this WILL crash on circular lists 
	public static boolean StructuresEqual(SchemeObject a, SchemeObject b)
	{
		if(a.isPair() && b.isPair())
		{
			return StructuresEqual(a.Car(), b.Car()) &&
			       StructuresEqual(a.Cdr(), b.Cdr());
		}
		return SchemeObject.AtomicValueEqual(a, b);
	}
	
//PRIVATE DATA MEMBERS
	private int mType;

//PUBLIC DATA MEMBERS
	public Object mContents;
	
//CONSTRUCTORS
	public SchemeObject(int type, Object contents)
	{
		mContents = contents;
		mType = type;
	}

//PUBLIC INSTANCE METHODS
	public SchemeObject Car()
	{
		return ((Pair)mContents).car;
	}
	public SchemeObject Cdr()
	{
		return ((Pair)mContents).cdr;
	}
	public void SetCar(SchemeObject car)
	{
		((Pair)mContents).car = car;
	}
	public void SetCdr(SchemeObject cdr)
	{
		((Pair)mContents).cdr = cdr;
	}
	
	public boolean isNumber()
	{
		return mType == NUMBER;
	}
	public boolean isBoolean()
	{
		return mType == BOOLEAN;
	}
	public boolean isString()
	{
		return mType == STRING;
	}
	public boolean isSymbol()
	{
		return mType == SYMBOL;
	}
	public boolean isPair()
	{
		return mType == PAIR;
	}
	public boolean isProcedure()
	{
		return mType == PROCEDURE;
	}
	public boolean isError()
	{
		return mType == ERROR;
	}
	public boolean isNative()
	{
		return mType == NATIVE;
	}
	public boolean isReference()
	{
		return mType == REFERENCE;
	}
	
	public boolean isSelfEvaluating()
	{
		return isNumber()	||
		       isBoolean()	||
		       isString()	||
		       isError()	||
		       isNative();
	}
	
	public boolean True()
	{
		return this != NIL;
	}
	
	public boolean ProperList()
	{
		return isPair() && (Cdr() == NIL || Cdr().ProperList());
	}
}
