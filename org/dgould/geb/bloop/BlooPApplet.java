package org.dgould.geb.bloop;

import org.dgould.geb.bloop.ast.*;

import java.io.*;
import java.awt.*;
import java.applet.Applet;

public class BlooPApplet extends Applet
{
	public static final int PROCEDURE	= 1;
	public static final int CALL_EXP	= 2;
	public static final int PROGRAM		= 3;

	java.awt.Button		Define		= new java.awt.Button();
	java.awt.Button		Call		= new java.awt.Button();
	java.awt.Button		Run			= new java.awt.Button();
	java.awt.Button		Reset		= new java.awt.Button();
	
	java.awt.Label		inLabel		= new java.awt.Label();
	java.awt.TextArea	inText		= new java.awt.TextArea();
	java.awt.Label		outLabel	= new java.awt.Label();
	java.awt.TextArea	outText		= new java.awt.TextArea();
	
	BlooPInterpreter interpreter;
	boolean debug = false;
	boolean printTree = false;
	
	public BlooPApplet() 
	{
		interpreter = new BlooPInterpreter();
		interpreter.debug = false;
	}
	
	public void Output(String text)
	{
		this.outText.appendText(text);
		repaint();
	}
	
	public void init() {
		try {
			initComponents();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		this.start();
	}

	public void initComponents() throws Exception
	{
	
		// the following code sets the frame's initial state
		
		Define.setLocation(new java.awt.Point(80, 260));
		Define.setLabel("Define Procedure");
		Define.setVisible(true);
		Define.setSize(new java.awt.Dimension(140, 20));
		
		Call.setLocation(new java.awt.Point(235, 260));
		Call.setLabel("Evaluate Call");
		Call.setVisible(true);
		Call.setSize(new java.awt.Dimension(115, 20));
		
		Run.setLocation(new java.awt.Point(365, 260));
		Run.setLabel("Run Full Program");
		Run.setVisible(true);
		Run.setSize(new java.awt.Dimension(140, 20));
		
		Reset.setLocation(new java.awt.Point(520, 260));
		Reset.setLabel("Reset");
		Reset.setVisible(true);
		Reset.setSize(new java.awt.Dimension(70, 20));

		inLabel.setText("Enter function definition or call expression");
		inLabel.setLocation(new java.awt.Point(10, 10));
		inLabel.setVisible(true);
		inLabel.setFont(new java.awt.Font("Dialog", 0, 12));
		inLabel.setSize(new java.awt.Dimension(300, 12));

		inText.setLocation(new java.awt.Point(10, 25));
		inText.setForeground(java.awt.Color.black);
		inText.setBackground(java.awt.Color.white);
		inText.setFont(new java.awt.Font("Courier", 0, 12));
		inText.setVisible(true);
		inText.setSize(new java.awt.Dimension(580, 230));

		outLabel.setText("Output");
		outLabel.setLocation(new java.awt.Point(10, 270));
		outLabel.setVisible(true);
		outLabel.setFont(new java.awt.Font("Dialog", 0, 12));
		outLabel.setSize(new java.awt.Dimension(70, 12));
		
		outText.setLocation(new java.awt.Point(10, 285));
		outText.setForeground(java.awt.Color.black);
		outText.setBackground(java.awt.Color.white);
		outText.setFont(new java.awt.Font("Courier", 0, 12));
		outText.setVisible(true);
		outText.setSize(new java.awt.Dimension(580, 105));

		
		setLocation(new java.awt.Point(0, 0));
		setBackground(new java.awt.Color(192, 192, 192));
		setLayout(null);
		setSize(new java.awt.Dimension(600, 400));
		add(Define);
		add(Call);
		add(Run);
		add(Reset);
		add(inLabel);
		add(inText);
		add(outLabel);
		add(outText);
	}
	
	protected Ast ParseInput(String code, int whichKind)
	{
		java.io.StringReader codeStream;
		Yylex lexerObj;
		Ast programElement;
		java_cup.runtime.Symbol parseTree = null;
		
		codeStream = new java.io.StringReader(code);
		lexerObj = new Yylex(codeStream);
		
		try
		{
			if(whichKind == PROCEDURE)
			{
				ProcedureParser parserObj = new ProcedureParser();
				parserObj.assignLex(lexerObj);
			
				if(debug)
				{
					parseTree = parserObj.debug_parse();
				}
				else
				{
					parseTree = parserObj.parse();
				}
			}
			if(whichKind == CALL_EXP)
			{
				CallParser parserObj = new CallParser();
				parserObj.assignLex(lexerObj);
			
				if(debug)
				{
					parseTree = parserObj.debug_parse();
				}
				else
				{
					parseTree = parserObj.parse();
				}
			}
			if(whichKind == PROGRAM)
			{
				ProgramParser parserObj = new ProgramParser();
				parserObj.assignLex(lexerObj);
			
				if(debug)
				{
					parseTree = parserObj.debug_parse();
				}
				else
				{
					parseTree = parserObj.parse();
				}
			}
			
			if(parseTree == null)
			{
				programElement = null;
			}
			else
			{
				programElement = (Ast)(parseTree.value);
			}
		}
		catch(Exception e1)
		{
			Output("SYNTAX ERROR: " + e1.getMessage() + "\n");
			programElement = null;
		}
		
		return programElement;
	}
	
	public boolean action(Event evt, Object what)
	{
		if(evt.target == this.Define ||
		   evt.target == this.Call ||
		   evt.target == this.Run)
		{
			int whichKind = 0;
			
			if(evt.target == this.Define)
			{
				whichKind = PROCEDURE;
				Output("\nparsing procedure definition...\n");
			}
			if(evt.target == this.Call)
			{
				whichKind = CALL_EXP;
				Output("\nparsing procedure call expression...\n");
			}
			if(evt.target == this.Run)
			{
				whichKind = PROGRAM;
				Output("\nparsing full program...\n");
			}
			
			String code = this.inText.getText();
			Ast programElement = this.ParseInput(code, whichKind);
			
			if(programElement == null)
			{
				Output("parse failed.\n");
				return true;
			}
			
			if(printTree)
			{
				Output("parse tree: (not implemented)\n");
			}
			
			Output("processing... ");
			try
			{
				if(programElement instanceof Program)
				{
					Output("\n");
					while(!(programElement instanceof CallExpression))
					{
						interpreter.DefineProcedure(((Program)programElement).firstProc);
						Output("procedure \"" +
						       ((Program)programElement).firstProc.name +
						       "\" defined.\n");
						programElement = ((Program)programElement).theRest;
					}
				}
				
				if(programElement instanceof ProcedureDefinition)
				{
					interpreter.DefineProcedure((ProcedureDefinition)programElement);
					Output("procedure \"" +
					       ((ProcedureDefinition)programElement).name +
					       "\" defined.\n");
				}
				
				if(programElement instanceof CallExpression)
				{
					Output("result: ");
					Value result = interpreter.EvaluateCall((CallExpression)programElement);
					if(result == null)
					{
						Output("EvaluateCall failed.\n");
					}
					else
					{
						Output(result.toString() + "\n");
					}
				}
			
			}
			catch(Exception e)
			{
				Output("ERROR: " + e.getMessage() + "\n");
			}
			
			return true;
		}
		
		if(evt.target == this.Reset)
		{
			Output("resetting interpreter.\n");
			
			interpreter.Reset();
			
			return true;
		}
		
		return false;
	}
}
