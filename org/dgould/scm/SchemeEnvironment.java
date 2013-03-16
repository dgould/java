/* SchemeEnvironment.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;

//import java.awt.*;
import java.util.*;

public class SchemeEnvironment extends Object
{
//PRIVATE DATA MEMBERS
	private Hashtable frame;
	private SchemeEnvironment parent;
	
//CONSTRUCTORS
	public SchemeEnvironment(SchemeEnvironment parent)
	{
		frame = new Hashtable();
		this.parent = parent;
	}
	
//PUBLIC INSTANCE METHODS
	public SchemeEnvironment GetParent()
	{
		return parent;
	}
	
	public void AddBinding(SchemeObject variable, SchemeObject value)
	{
		//System.out.println("AddBinding");
		frame.put((Object)(variable.mContents), (Object)value);
	}
	
	public boolean SetBinding(SchemeObject variable, SchemeObject value)
	{
		if(frame.containsKey((Object)(variable.mContents)))
		{
			AddBinding(variable, value);
			return true;
		}
		if(parent == null)
		{
			return false;
		}
		return parent.SetBinding(variable, value);
	}
	
	public SchemeObject GetBinding(SchemeObject variable)
	{
		if(frame.containsKey((Object)(variable.mContents)))
		{
			return (SchemeObject)frame.get((Object)(variable.mContents));
		}
		if(parent == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
			       "unbound variable: " + (String)variable.mContents);
		}
		return parent.GetBinding(variable);
	}
}
