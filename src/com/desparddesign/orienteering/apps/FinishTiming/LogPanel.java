package com.desparddesign.orienteering.apps.FinishTiming;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class LogPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private LogController controller;
	private JTextArea text;
	
	LogPanel(LogController cont)
	{
		controller = cont;
		
		Dimension minimumSize = new Dimension(0, 0);
		setMinimumSize(minimumSize);
		setPreferredSize(new Dimension(500,200));
		 
		text = new JTextArea("",10,40);
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		text.setVisible(true);
		add(new JScrollPane(text));
	}
	
	public void append(String message)
	{
		text.append(message);
	}
	
	
}