/*34567890123456789012345678901234567890123456789012345678901234567890123456*/

package org.dgould;

import java.lang.*;

public class NaturalNumber extends Object implements Cloneable
{
	/* must be >= 2 and <= max int. The "base" in which numbers are
	   represented. 
	*/
	//public static final long RADIX = 2147483648L;
	public static final long RADIX = 65536;
	
	/* tradeoff is wasted memory for small numbers vs. an avoidable
	   reallocation for large numbers. Assumed to be >= 2. */
	public static final int INITIAL_CAPACITY = 2;
	
	public NaturalNumber()
	{
		this.digits = new int [INITIAL_CAPACITY];
		this.size = 0;
		
		/* XXX How does a Java constructor fail? Is there a way to make it
		   return null? */
	}

	public NaturalNumber(long d)
	{
		this();
		this.SetToNative(d);
	}
	
	public NaturalNumber(String s, int radix)
	{
		this();
		
		NaturalNumber n = NaturalNumber.ReadFromString(s, radix);
		this.SetTo(n);
	}

	public void SetToNative(long d)
	{
		this.digits[0] = (int)(d % RADIX);
		this.digits[1] = (int)(d / RADIX);
		if(this.digits[1] > 0)
		{
			this.size = 2;
		}
		else
		{
			this.size = 1;
		}
	}

	public static NaturalNumber Copy(NaturalNumber n)
	{
		NaturalNumber m = new NaturalNumber();
		if(m == null)
		{
			return null;
		}
		m.digits = new int [n.digits.length];
		if(m.digits == null)
		{
			return null;
		}
		
		for(int i = 0; i < n.size; i++)
		{
			m.digits[i] = n.digits[i];
		}
		
		m.size = n.size;
		return m;
	}
	
	public Object clone()
	{
		return NaturalNumber.Copy(this);
	}
	
	public boolean equals(Object obj)
	{
		return (obj instanceof NaturalNumber) &&
		       (Compare(this, (NaturalNumber)obj) == 0);
	}
	
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	public boolean SetTo(NaturalNumber n)
	{
		/* if this's array is big enough to hold n's defined digits, keep it,
		   else reallocate. */
		if(this.digits.length < n.size)
		{
			int [] temp = new int [n.digits.length];
			if(temp == null)
			{
				/* if reallocation fails, fail */
				return false;
			}
			this.digits = temp;
		}
		
		for(int i = 0; i < n.size; i++)
		{
			this.digits[i] = n.digits[i];
		}
		
		this.size = n.size;
		return true;
	}

	public boolean AddLeadingZero()
	{
		if(this.size >= this.digits.length)
		{
			if(!this.GrowVector())
			{
				return false;
			}
		}
		this.digits[this.size] = 0;
		this.size++;
		
		return true;
	}

	public boolean AddTrailingZero()
	{
		return this.Shift(1);
	}

	public static int Compare(NaturalNumber n, NaturalNumber m)
	{
		int i, comparison;
		
		if(n.size != m.size)
		{
			/* the longer one is not necessarily the greater, if the 
			   difference is all leading zeroes -- scan the excess: if there
			   is a nonzero digit, then the longer one is the greater, else
			   proceed as if they were of equal length. */
			NaturalNumber longer = null, shorter = null;
			
			if(n.size > m.size)
			{
				comparison = 1;
				longer = n;
				shorter = m;
			}
			else
			{
				comparison = -1;
				longer = m;
				shorter = n;
			}
			
			for(i = longer.size - 1; i >= shorter.size; i--)
			{
				if(longer.digits[i] > 0)
				{
					return comparison;
				}
			}
		}
		else
		{
			i = n.size - 1;
		}
		
		/* starting from the most significant position, see if any digits are
		   unequal: if so, then the one with the greater digit is the greater
		   number; if not, then they are equal. */
		while(i >= 0)
		{
			if(n.digits[i] > m.digits[i])
			{
				return 1;
			}
			if(n.digits[i] < m.digits[i])
			{
				return -1;
			}
			
			i--;
		}
		
		return 0;
	}

	public boolean IsZero()
	{
		int i;
		
		if(this.size == 0)
		{
			/* might change this to treat [] as a special "not-a-number"
			   value, instead of same as [0*] */
			return true;
		}
		
		for(i = 0; i < this.size; i++)
		{
			if(this.digits[i] > 0)
			{
				return false;
			}
		}
		
		return true;
	}

	public boolean Shift(int amt)
	{
		int i;
		
		if(amt < 0) 
		{
			/* right-shift: discarding digits */
			
			/* get positive magnitude */
			amt *= -1;
			
			if(amt >= this.size) 
			{
				/* discarding all digits -- set to zero */
				this.SetToNative(0);
			}
			else 
			{
				/* copy digits down, and update size */
				for(i = 0; i < this.size - amt; i++)
				{
					this.digits[i] = this.digits[i + amt];
				}
				this.size -= amt;
			}
		}
		else if(amt > 0) 
		{
			/* left-shift: adding trailing zeroes --
			   can require reallocation, which can fail */
			i = this.size + amt;
			while(this.size < i)
			{
				if(!this.AddLeadingZero())
				{
					return false;
				}
			}
			for(i--; i >= amt; i--)
			{
				this.digits[i] = this.digits[i - amt];
			}
			while(i >= 0)
			{
				this.digits[i] = 0;
				i--;
			}
		}
		
		return true;
	}

	public boolean MultiplyByOneDigit(int d)
	{
		int i;
		long acc;
		
		/* starting from least significant, multiply each digit by d,
		   set that position to low digit of result and carry high digit. */
		for(i = 0, acc = 0; i < this.size; i++)
		{
			acc += (this.digits[i] * d);
			this.digits[i] = (int)(acc % RADIX);
			acc /= RADIX;
		}
		/* total length can increase by as much as one digit, if there is
		   a carry out from the last digit -- just save it. */
		if(acc > 0)
		{
			if(!(this.AddLeadingZero()))
			{
				return false;
			}
			this.digits[i] = (int)acc;
		}
		return true;
	}

	public int DivideByOneDigit(int d)
	{
		int i;
		long acc;
		
		if(d == 0)
		{
			return 0;
		}
		
		/* Start from most significant; what's left from previous digit is
		left-shifted by one position and new digit is added; then divide by
		d -- output quotient and save remainder. */
		for(acc = 0, i = this.size - 1; i >= 0; i--)
		{
			acc *= RADIX;
			acc += this.digits[i];
			this.digits[i] = (int)(acc / d);
			acc %= d;
		}
		
		return (int)acc;
	}

	public boolean AddOneDigit(int d)
	{
		int i;
		long acc;
		
		/* ripple-carry: start by adding to least significant, and stop
		   whent here is no carry */
		for(acc = d, i = 0; acc > 0; i++)
		{
			if((i >= this.size) && !(this.AddLeadingZero()))
			{
				return false;
			}
			acc += this.digits[i];
			this.digits[i] = (int)(acc % RADIX);
			acc /= RADIX;
		}
		
		return true;
	}

	public boolean SubtractOneDigit(int d)
	{
		int i;
		long acc;
		
		/* ripple-borrow: start by subtracting from least significant, and
		   stop when there is no borrow */
		for(acc = d, i = 0; i < this.size && acc > 0; i++)
		{
			if(acc > this.digits[i])
			{
				this.digits[i] = (int)(RADIX - acc) + this.digits[i];
				acc = 1;
			}
			else
			{
				this.digits[i] -= acc;
				acc = 0;
			}
		}
		
		return ((i < this.size) || (acc == 0));
	}
	
	public static NaturalNumber Add(NaturalNumber n, NaturalNumber m)
	{
		int i;
		long acc;
		NaturalNumber result = new NaturalNumber();
		if(result == null)
		{
			return null;
		}
		
		/* while there is a carry-in, or another digit in either n or m,
		   add these, output the low digit, and carry the high digit. */
		for(i = 0, acc = 0; i < n.size || i < m.size || acc > 0; i++)
		{
			if(!(result.AddLeadingZero()))
			{
				return null;
			}
			if(i < n.size)
			{
				acc += n.digits[i];
			}
			if(i < m.size)
			{
				acc += m.digits[i];
			}
			result.digits[i] = (int)(acc % RADIX);
			acc /= RADIX;
		}
		return result;
	}

	public boolean Add(NaturalNumber n)
	{
		int i;
		long acc;
		
		/* same as static version, but write new digits into this, and stop
		   when n is done and acc is zero, even if this has more digits. */
		for(i = 0, acc = 0; i < n.size || acc > 0; i++)
		{
			if(i >= this.size && !(this.AddLeadingZero()))
			{
				return false;
			}
			acc += this.digits[i];
			if(i < n.size)
			{
				acc += n.digits[i];
			}
			this.digits[i] = (int)(acc % RADIX);
			acc /= RADIX;
		}
		
		return true;
	}
	
	public static NaturalNumber Subtract(NaturalNumber n, NaturalNumber m)
	{
		int i;
		long acc;
		NaturalNumber result = new NaturalNumber();
		if(result == null)
		{
			return null;
		}
		/* here, acc remembers the "borrow": if a digit of m is greater than
		   the corresponding digit of n, "borrow" one from the next higher 
		   digit of n, adding RADIX to that digit of n */
		for(i = 0, acc = 0; i < n.size; i++)
		{
			/* amount to subtract is acc (1 if last digit borrowed, else 0),
			   plus this digit of m, if any */
			if(i < m.size)
			{
				acc += m.digits[i];
			}
			
			if(!(result.AddLeadingZero()))
			{
				return null;
			}
			
			if(acc <= n.digits[i]) /* no borrow */
			{
				result.digits[i] = n.digits[i] - (int)acc;
				acc = 0;
			}
			else /* borrow */
			{
				result.digits[i] = (int)(RADIX - acc) + n.digits[i];
				acc = 1;
			}
		}
		
		/* if the last digit of n needed to borrow, or if m has any higher-order
		   nonzero digits, then (m > n), so (result < 0), i.e., subtraction is
		   not valid, so fail */
		while((acc == 0) && i < m.size)
		{
			if(m.digits[i] > 0)
			{
				acc = 1;
			}
			i++;
		}
		if(acc > 0)
		{
			return null;
		}
		
		return result;
	}
	
	public boolean Subtract(NaturalNumber n)
	{
		int i;
		long acc;
		
		/* same as static version, but write digits into this */
		for(i = 0, acc = 0; i < this.size; i++)
		{
			if(i < this.size)
			{
				acc += n.digits[i];
			}
			
			if(acc <= this.digits[i])
			{
				this.digits[i] -= acc;
				acc = 0;
			}
			else
			{
				this.digits[i] = (int)(RADIX - acc) + this.digits[i];
				acc = 1;
			}
		}
		
		while((acc == 0) && i < n.size)
		{
			if(n.digits[i] > 0)
			{
				acc = 1;
			}
			i++;
		}
		
		/* if acc > 0, then n > (original value of) this, 
		   and this is now invalid */
		return (acc == 0);
	}
	
	/* To explain the elementary-school algorithm for long multiplication,
	   let m = [m3 m2 m1 m0]. This means that m is
	   ((m3 * (RADIX^3)) +
	    (m2 * (RADIX^2)) +
	    (m1 * (RADIX^1)) +
	    (m0 * (RADIX^0)))
	   so (n * m) is
	   ((n * m3 * (RADIX^3)) +
	    (n * m2 * (RADIX^2)) +
	    (n * m1 * (RADIX^1)) +
	    (n * m0 * (RADIX^0))) */
	public static NaturalNumber Multiply(NaturalNumber n, NaturalNumber m)
	{
		int i;
		NaturalNumber acc    = new NaturalNumber();
		NaturalNumber result = new NaturalNumber(0);
		if(result == null || acc == null)
		{
			return null;
		}
		/* for each digit of m, multiply n by that digit,
		   shift by its position, and add to total */
		for(i = 0; i < m.size; i++)
		{
			if(!acc.SetTo(n))
			{
				return null;
			}
			if(!acc.MultiplyByOneDigit(m.digits[i]))
			{
				return null;
			}
			if(!acc.Shift(i))
			{
				return null;
			}
			if(!result.Add(acc))
			{
				return null;
			}
		}
		
		return result;
	}
	
	public boolean Multiply(NaturalNumber n)
	{
		NaturalNumber product;
		boolean success;
		
		success = false;
		product = NaturalNumber.Multiply(this, n);
		
		if(product != null)
		{
			if(this.SetTo(product))
			{
				success = true;
			}
		}
		
		return success;
	}
	
	public static NaturalNumber FastExpt(NaturalNumber n,
	                                     NaturalNumber expt,
	                                     NaturalNumber mod)
	{
		int bit;
		NaturalNumber result  = new NaturalNumber(1);
		NaturalNumber remExpt = NaturalNumber.Copy(expt);
		NaturalNumber acc     = NaturalNumber.Copy(n);
		NaturalNumber temp;
		
		if(result == null || acc == null || remExpt == null)
		{
			return null;
		}

		while(!remExpt.IsZero())
		{
			bit = remExpt.DivideByOneDigit(2);
			
			if(bit == 1) /* Do you think we can merge with this memory? */
			{
				if(!result.Multiply(acc))
				{
					return null;
				}
				if(mod != null)
				{
					if(!result.Remainder(mod))
					{
						return null;
					}
				}
			}
			
			temp = NaturalNumber.Multiply(acc, acc);
			if(temp != null)
			{
				acc = temp;
			}
			else
			{
				return null;
			}
		
			if(mod != null)
			{
				if(!acc.Remainder(mod))
				{
					return null;
				}
			}
		}
		
		return result;
	}
	
	/* elementary-school algorithm is: starting with most-significant, shift
	   each digit of n into accumulator, output (acc / m) to quotient, and
	   set acc to (acc % m). */
	public static boolean Divide(NaturalNumber n, NaturalNumber m,
		                         NaturalNumber quotient,
		                         NaturalNumber remainder)
	{
		int i, comp;
		boolean firstNonzero;
		NaturalNumber acc, multiple;
		int thisDigit, multiplier;
		
		if(m.IsZero())
		{
			return false;
		}
		
		quotient.size = 0;
		
		acc = new NaturalNumber();
		multiple = new NaturalNumber();
		
		if(acc == null || multiple == null)
		{
			return false;
		}
		
		for(firstNonzero = false, i = n.size - 1; i >= 0; i--)
		{
			/* shift next digit of n into accumulator */
			if(!acc.AddTrailingZero())
			{
				return false;
			}
			acc.digits[0] = n.digits[i];
			
			/* (acc / m) is next digit of quotient. The tricky part is
			   computing this -- these are (in general) multidigit natural
			   numbers, so can't use primitive division, and can't call
			   this function because it isn't written yet. The solution is
			   to count how many times m can be subtracted from acc, but
			   use successive doubling to make it O(log(RADIX)) instead
			   of O(RADIX). */
			thisDigit = 0;
			while(NaturalNumber.Compare(acc, m) >= 0)
			{
				/* start with m */
				if(!multiple.SetTo(m))
				{
					return false;
				}
				
				/* double it until it is >= acc */
				multiplier = 1;
				while((comp = NaturalNumber.Compare(acc, multiple)) > 0)
				{
					multiplier *= 2;
					multiple.MultiplyByOneDigit(2);
				}
				
				/* if >, undo last doubling */
				if(comp < 0)
				{
					/* ASSERT remainder will be 0, since we just multiplied
					   by 2 at least once */
					multiple.DivideByOneDigit(2);
					multiplier /= 2;
				}
				
				thisDigit += multiplier;
				acc.Subtract(multiple);
			}
			
			/* now, thisDigit == ([old value of acc] / m)
			   and  acc       == ([old value of acc] % m). */
			
			/* shift thisDigit into quotient, if not a leading zero */
			if(thisDigit > 0 || firstNonzero)
			{
				firstNonzero = true;
				if(!quotient.AddTrailingZero())
				{
					return false;
				}
				quotient.digits[0] = thisDigit;
			}
		}
		
		/* whatever's left is remainder (hence the name) */
		remainder.SetTo(acc);

		/* done */
		return true;
	}
	
	public static NaturalNumber Quotient(NaturalNumber n, NaturalNumber m)
	{
		NaturalNumber quotient  = new NaturalNumber();
		NaturalNumber remainder = new NaturalNumber();
		if(quotient == null || remainder == null)
		{
			return null;
		}
		
		if(NaturalNumber.Divide(n, m, quotient, remainder))
		{
			return quotient;
		}
		return null;
	}
	public boolean Quotient(NaturalNumber n)
	{
		NaturalNumber quotient;
		
		quotient = NaturalNumber.Quotient(this, n);
		
		if(quotient == null)
		{
			return false;
		}
		else
		{
			/* can't fail because quotient <= n */
			this.SetTo(quotient);
			return true;
		}
	}
	
	public static NaturalNumber Remainder(NaturalNumber n, NaturalNumber m)
	{
		NaturalNumber quotient  = new NaturalNumber();
		NaturalNumber remainder = new NaturalNumber();
		if(quotient == null || remainder == null)
		{
			return null;
		}
		
		if(NaturalNumber.Divide(n, m, quotient, remainder))
		{
			return remainder;
		}
		return null;
	}
	public boolean Remainder(NaturalNumber n)
	{
		NaturalNumber remainder;
		
		remainder = NaturalNumber.Remainder(this, n);
		
		if(remainder == null)
		{
			return false;
		}
		else
		{
			/* can't fail because remainder <= n */
			this.SetTo(remainder);
			return true;
		}
	}
	
	public static NaturalNumber ReadFromString(String s, int radix)
	{
		NaturalNumber n;
		int i;
		char a[];
		
		n = new NaturalNumber(0);
		if(n == null)
		{
			return null;
		}
		
		a = s.toCharArray();
		
		for(i = 0;
		    i < a.length && (NaturalNumber.IsDigit(a[i], radix) ||
		                     a[i] == ',');
		    i++)
		{
			if(a[i] != ',')
			{
				if(!n.MultiplyByOneDigit(radix))
				{
					return null;
				}
				if(!n.AddOneDigit(NaturalNumber.CharToDigit(a[i])))
				{
					return null;
				}
			}
		}
		
		return n;
	}
	
	public String FormatToString(int radix)
	{
		NaturalNumber n;
		String s;
		char a[];
		int i, charsPerDigit;
		
		n = NaturalNumber.Copy(this);
		if(n == null)
		{
			return null;
		}
		
		/* log(base x)(y) = log(y) / log(x) */
		charsPerDigit = (int)Math.ceil(Math.log(RADIX) / Math.log(radix));
		
		/* allocate max length for string */
		a = new char [(charsPerDigit * n.size) + 1];
		if(a == null)
		{
			return null;
		}
		
		/* build the array little-endian, since that is the order in
		   which the digits are naturally computed. Use do...while so that
		   if (n == 0), one zero will be output, instead of none. */
		i = 0;
		do
		{
			int d;
			
			d = n.DivideByOneDigit(radix);
			a[i++] = NaturalNumber.DigitToChar(d);
		}
		while(!n.IsZero());
		
		/* build the string by reversing the array to make it big-endian */
		s = "";
		for(i--; i >= 0; i--)
		{
			s += a[i];
		}
		
		return s;
	}
	
	public String FormatToString()
	{
		return this.FormatToString(10);
	}
	
	public String toString()
	{
		return this.FormatToString();
	}
	
	private int size;
	private int [] digits;
	
	private boolean GrowVector()
	{
		int [] newDigits = new int [this.digits.length * 2];
		
		if(newDigits == null)
		{
			return false;
		}
		
		for(int i = 0; i < this.digits.length; i++)
		{
			newDigits[i] = this.digits[i];
		}
		
		this.digits = newDigits;
		
		return true;
	}
	
	private static boolean IsDigit(char c, int radix)
	{
		return ((c >= '0' && c <= '9') ||
		        (c >= 'a' && c <= 'f') ||
		        (c >= 'A' && c <= 'F')) &&
		       (CharToDigit(c) < radix);
	}

	private static int CharToDigit(char c)
	{
		if(c >= '0' && c <= '9')
		{
			return c - '0';
		}
		if(c >= 'a' && c <= 'f')
		{
			return c - 'a' + 10;
		}
		if(c >= 'A' && c <= 'F')
		{
			return c - 'A' + 10;
		}
		return 0;
	}
	
	private static char DigitToChar(int d)
	{
		if(d < 10)
		{
			return (char)('0' + d);
		}
		if(d < 16)
		{
			return (char)('a' +d - 10);
		}
		return '\0';
	}
}

