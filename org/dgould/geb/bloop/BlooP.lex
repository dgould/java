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


%%

%type Symbol

%eofval{
	return new Symbol(sym.EOF, null);
%eofval}

%line
%char
%init{
  SymHash = new SymHashTable();
%init}

%{
  public String sourceFilename;
  public String StringConstant;
  public SymHashTable SymHash;
%}

SEPARATOR=ENTER
ALPHA=[A-Za-z_\-]
DIGIT=[0-9]
ALPHA_NUMERIC={ALPHA}|{DIGIT}
IDENT={ALPHA}({ALPHA_NUMERIC})*(\?)?
INTCONST=({DIGIT})+
WHITE_SPACE=([\ \n\r\t\f])+
BOOLEAN = YES|NO
KEYWORDS = CELL|OUTPUT|BLOCK|BEGIN|END|QUIT|LOOP|AT|MOST|TIMES|ABORT|IF|THEN|DEFINE|PROCEDURE
OPERATORS = <==|NOT|AND|OR|=|<|\+|\*|-|/|\%
PUNCTUATION = \"|\[|\]|\(|\)|\{|\}|:|;|,|\.
LINE_COMMENT = #(.*)\n

%%

<YYINITIAL>{SEPARATOR}
{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}

<YYINITIAL>{PUNCTUATION}
{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}

<YYINITIAL>{OPERATORS}
{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}

<YYINITIAL>{KEYWORDS}
{
	return new Symbol(SymHash.GetConst(yytext()), new TokenValue(yytext()));
}

<YYINITIAL> {BOOLEAN}
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

<YYINITIAL> {INTCONST}
{ 
	return new Symbol(sym.CONSTANT, new TokenValue(NaturalNumber.ReadFromString(yytext(), 10))); 
}

<YYINITIAL> {IDENT}
{ 
	return new Symbol(sym.IDENTIFIER, new TokenValue(yytext())); 
}


<YYINITIAL> {LINE_COMMENT} { }

<YYINITIAL> {WHITE_SPACE} { }

<YYINITIAL> {INTCONST}({ALPHA})+({ALPHA_NUMERIC})* { 
  //PrintError.InvalidID(yyline+1, yytext());
  return new Symbol(sym.error, null); 
}

<YYINITIAL> . {
  return new Symbol(sym.error, null);
}

