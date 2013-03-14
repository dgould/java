package org.dgould.geb.bloop;
import java_cup.runtime.*;
import org.dgould.*;
class TokenValue {
	public NaturalNumber numberValue;
	public Boolean boolValue;
	public String identifierValue;
	public TokenValue()
	{
  		numberValue		= null;
  		boolValue		= null;
  		identifierValue	= null;
	}
	public TokenValue(NaturalNumber value)
	{
		this();
  		this.numberValue = value;
	}
	public TokenValue(Boolean value)
	{
  		this();
  		this.boolValue = value;
	}
	public TokenValue(boolean value)
	{
  		this(new Boolean(value));
	}
	public TokenValue(String value)
	{
  		this();
  		this.identifierValue = value;
	}
}
class SymHashTable
{
	public java.util.Hashtable Symbols;
	SymHashTable()
	{
		Symbols = new java.util.Hashtable(37);
		Symbols.put("\004",			new Integer(sym.EOF));
		Symbols.put("ENTER",		new Integer(sym.SEPARATOR));
		/* booleans */
		Symbols.put("YES",			new Integer(sym.YES));
		Symbols.put("NO",			new Integer(sym.NO));
		/* keywords */
		Symbols.put("CELL",			new Integer(sym.CELL));
		Symbols.put("OUTPUT",		new Integer(sym.OUTPUT));
		Symbols.put("BLOCK",		new Integer(sym.BLOCK));
		Symbols.put("BEGIN",		new Integer(sym.BEGIN));
		Symbols.put("END",			new Integer(sym.END));
		Symbols.put("QUIT",			new Integer(sym.QUIT));
		Symbols.put("LOOP",			new Integer(sym.LOOP));
		Symbols.put("AT",			new Integer(sym.AT));
		Symbols.put("MOST",			new Integer(sym.MOST));
		Symbols.put("TIMES",		new Integer(sym.TIMES));
		Symbols.put("ABORT",		new Integer(sym.ABORT));
		Symbols.put("IF",			new Integer(sym.IF));
		Symbols.put("THEN",			new Integer(sym.THEN));
		Symbols.put("DEFINE",		new Integer(sym.DEFINE));
		Symbols.put("PROCEDURE",	new Integer(sym.PROCEDURE));
		/* operators */
		Symbols.put("NOT",			new Integer(sym.NOT));
		Symbols.put("<==",			new Integer(sym.ASSIGN));
		Symbols.put("AND",			new Integer(sym.AND));
		Symbols.put("OR",			new Integer(sym.OR));
		Symbols.put("=",			new Integer(sym.EQUALS));
		Symbols.put("<",			new Integer(sym.LESSTHAN));
		Symbols.put("+",			new Integer(sym.ADD));
		Symbols.put("-",			new Integer(sym.SUBTRACT));
		Symbols.put("*",			new Integer(sym.MULTIPLY));
		Symbols.put("/",			new Integer(sym.QUOTIENT));
		Symbols.put("%",			new Integer(sym.REMAINDER));
		/* punctuation */
		Symbols.put("\"",			new Integer(sym.QUOTE));
		Symbols.put("[",			new Integer(sym.LBRACKET));
		Symbols.put("]",			new Integer(sym.RBRACKET));
		Symbols.put("(",			new Integer(sym.LPAREN));
		Symbols.put(")",			new Integer(sym.RPAREN));
		Symbols.put("{",			new Integer(sym.LBRACE));
		Symbols.put("}",			new Integer(sym.RBRACE));
		Symbols.put(":",			new Integer(sym.COLON));
		Symbols.put(";",			new Integer(sym.SEMICOLON));
		Symbols.put(",",			new Integer(sym.COMMA));
		Symbols.put(".",			new Integer(sym.PERIOD));
	}
	public int GetConst(String text)
	{
		if (Symbols.get(text) != null)
		{
			return ((Integer)Symbols.get(text)).intValue();
		}
		else
		{
			return sym.error;
		}
	}
}


class Yylex {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final char YYEOF = '\uFFFF';

  public String sourceFilename;
  public String StringConstant;
  public SymHashTable SymHash;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_lexical_state = YYINITIAL;

  SymHash = new SymHashTable();
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private char yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_start () {
		if ((byte) '\n' == yy_buffer[yy_buffer_start]) {
			++yyline;
		}
		++yychar;
		++yy_buffer_start;
	}
	private void yy_pushback () {
		--yy_buffer_end;
	}
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ((byte) '\n' == yy_buffer[i]) {
				++yyline;
			}
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int yy_acpt[] = {
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR
	};
	private int yy_cmap[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 1, 2, 0, 1, 1, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		1, 0, 3, 4, 0, 5, 0, 0,
		3, 3, 5, 5, 3, 6, 3, 5,
		7, 7, 7, 7, 7, 7, 7, 7,
		7, 7, 3, 3, 8, 9, 0, 10,
		0, 11, 12, 13, 14, 15, 16, 17,
		18, 19, 20, 21, 22, 23, 24, 25,
		26, 27, 28, 29, 30, 31, 20, 20,
		20, 32, 20, 3, 0, 3, 0, 20,
		0, 20, 20, 20, 20, 20, 20, 20,
		20, 20, 20, 20, 20, 20, 20, 20,
		20, 20, 20, 20, 20, 20, 20, 20,
		20, 20, 20, 3, 0, 3, 0, 0
		
	};
	private int yy_rmap[] = {
		0, 1, 2, 1, 1, 3, 4, 1,
		5, 6, 7, 6, 8, 8, 6, 9,
		6, 10, 11, 12, 13, 14, 1, 15,
		16, 17, 18, 19, 20, 21, 22, 23,
		24, 25, 26, 27, 28, 29, 30, 31,
		32, 33, 34, 35, 36, 37, 38, 39,
		40, 41, 42, 43, 44, 45, 46, 47,
		48, 49, 50, 51, 52, 53, 54, 55,
		56, 57, 58, 59, 6, 60 
	};
	private int yy_nxt[][] = {
		{ 1, 2, 2, 3, 13, 4, 14, 5,
			18, 4, 1, 6, 15, 53, 67, 38,
			68, 68, 68, 19, 68, 68, 54, 39,
			20, 21, 69, 40, 68, 68, 55, 68,
			41 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1 
		},
		{ -1, 2, 2, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1 
		},
		{ -1, -1, -1, -1, -1, -1, 8, 5,
			-1, -1, -1, 8, 8, 8, 8, 8,
			8, 8, 8, 8, 8, 8, 8, 8,
			8, 8, 8, 8, 8, 8, 8, 8,
			8 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 42, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			23, 68, 68, 68, 68, 68, 9, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 8, 8,
			-1, -1, -1, 8, 8, 8, 8, 8,
			8, 8, 8, 8, 8, 8, 8, 8,
			8, 8, 8, 8, 8, 8, 8, 8,
			8 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 14, 68,
			68 
		},
		{ 12, 12, 7, 12, 12, 12, 12, 12,
			12, 12, 12, 12, 12, 12, 12, 12,
			12, 12, 12, 12, 12, 12, 12, 12,
			12, 12, 12, 12, 12, 12, 12, 12,
			12 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 56,
			68, 68, 68, 68, 68, 68, 57, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, 4, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, 17, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			9, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 10, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 14, 68, 68, 59,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 14, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 9, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 48, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 37, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 37, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 16, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 37, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 9, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 9, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			9, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 9, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 11, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 37,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 9, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 9,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 9, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			24, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 25, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 26,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 27,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 28, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 29, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 30, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 31,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 31, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 32, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 33,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 34, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 35,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			36, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 36, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 43,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 44, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 45, 61, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 46, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 47, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			62, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 49, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 63, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 50,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 51, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 64, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 65,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 66, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 52,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 58,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68 
		},
		{ -1, -1, -1, -1, -1, -1, 68, 68,
			-1, -1, 22, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 68, 68, 68, 68,
			68, 68, 68, 68, 60, 68, 68, 68,
			68 
		}
	};
	public Symbol yylex ()
		throws java.io.IOException {
		char yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			if (YYEOF != yy_lookahead) {
				yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YYEOF == yy_lookahead && true == yy_initial) {

	return new Symbol(sym.EOF, null);
				}
				else if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_to_mark();
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_pushback();
					}
					if (0 != (YY_START & yy_anchor)) {
						yy_move_start();
					}
					switch (yy_last_accept_state) {
					case 1:
						{
  return new Symbol(sym.error, null);
}
					case -2:
						break;
					case 2:
						{ }
					case -3:
						break;
					case 3:
						{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}
					case -4:
						break;
					case 4:
						{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}
					case -5:
						break;
					case 5:
						{ 
	return new Symbol(sym.CONSTANT, new TokenValue(NaturalNumber.ReadFromString(yytext(), 10))); 
}
					case -6:
						break;
					case 6:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -7:
						break;
					case 7:
						{ }
					case -8:
						break;
					case 8:
						{ 
  //PrintError.InvalidID(yyline+1, yytext());
  return new Symbol(sym.error, null); 
}
					case -9:
						break;
					case 9:
						{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}
					case -10:
						break;
					case 10:
						{
	if(yytext().equals("YES"))
	{
		return new Symbol(sym.YES, new TokenValue(true));
	}
	else
	{
		return new Symbol(sym.NO, new TokenValue(false));
	}
}
					case -11:
						break;
					case 11:
						{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}
					case -12:
						break;
					case 13:
						{
  return new Symbol(sym.error, null);
}
					case -13:
						break;
					case 14:
						{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}
					case -14:
						break;
					case 15:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -15:
						break;
					case 16:
						{
	if(yytext().equals("YES"))
	{
		return new Symbol(sym.YES, new TokenValue(true));
	}
	else
	{
		return new Symbol(sym.NO, new TokenValue(false));
	}
}
					case -16:
						break;
					case 18:
						{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}
					case -17:
						break;
					case 19:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -18:
						break;
					case 20:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -19:
						break;
					case 21:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -20:
						break;
					case 22:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -21:
						break;
					case 23:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -22:
						break;
					case 24:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -23:
						break;
					case 25:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -24:
						break;
					case 26:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -25:
						break;
					case 27:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -26:
						break;
					case 28:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -27:
						break;
					case 29:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -28:
						break;
					case 30:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -29:
						break;
					case 31:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -30:
						break;
					case 32:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -31:
						break;
					case 33:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -32:
						break;
					case 34:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -33:
						break;
					case 35:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -34:
						break;
					case 36:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -35:
						break;
					case 37:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -36:
						break;
					case 38:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -37:
						break;
					case 39:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -38:
						break;
					case 40:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -39:
						break;
					case 41:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -40:
						break;
					case 42:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -41:
						break;
					case 43:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -42:
						break;
					case 44:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -43:
						break;
					case 45:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -44:
						break;
					case 46:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -45:
						break;
					case 47:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -46:
						break;
					case 48:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -47:
						break;
					case 49:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -48:
						break;
					case 50:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -49:
						break;
					case 51:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -50:
						break;
					case 52:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -51:
						break;
					case 53:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -52:
						break;
					case 54:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -53:
						break;
					case 55:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -54:
						break;
					case 56:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -55:
						break;
					case 57:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -56:
						break;
					case 58:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -57:
						break;
					case 59:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -58:
						break;
					case 60:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -59:
						break;
					case 61:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -60:
						break;
					case 62:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -61:
						break;
					case 63:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -62:
						break;
					case 64:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -63:
						break;
					case 65:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -64:
						break;
					case 66:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -65:
						break;
					case 67:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -66:
						break;
					case 68:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -67:
						break;
					case 69:
						{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}
					case -68:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
					}
				}
			}
		}
	}
}
