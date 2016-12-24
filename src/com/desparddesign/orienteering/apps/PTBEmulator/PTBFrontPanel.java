package com.desparddesign.orienteering.apps.PTBEmulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PTBFrontPanel extends JPanel 
{
	//JButton valid;
	JButton session;
	JButton clear;
	JButton memory;
	
	JButton[] manualChannels;
	JButton[] blockChannels;
	IndicatorButton[] channelIndicators;
	IndicatorButton[] blockedIndicators;
	
	
	EmulatedPTBController controller;
	
	PTBFrontPanel(EmulatedPTBController PTB)
	{
		controller = PTB;
		
		session = new JButton("Session");
		clear = new JButton("Clear");
		memory = new JButton("Memory");
		
		manualChannels = new JButton[4];
		manualChannels[0] = new JButton("M1");
		manualChannels[1] = new JButton("M2");
		manualChannels[2] = new JButton("M3");
		manualChannels[3] = new JButton("M4");
		
		channelIndicators = new IndicatorButton[4];
		channelIndicators[0] = new IndicatorButton(Color.BLACK);
		channelIndicators[1] = new IndicatorButton(Color.BLACK);
		channelIndicators[2] = new IndicatorButton(Color.BLACK);
		channelIndicators[3] = new IndicatorButton(Color.BLACK);
		
		blockedIndicators = new IndicatorButton[4];
		blockedIndicators[0] = new IndicatorButton(Color.BLACK);
		blockedIndicators[1] = new IndicatorButton(Color.BLACK);
		blockedIndicators[2] = new IndicatorButton(Color.BLACK);
		blockedIndicators[3] = new IndicatorButton(Color.BLACK);
		
		blockChannels = new JButton[4];
		blockChannels[0] = new JButton("Block 1");
		blockChannels[1] = new JButton("Block 2");
		blockChannels[2] = new JButton("Block 3");
		blockChannels[3] = new JButton("Block 4");

		setLayout(new GridLayout(0,1));
		
		JPanel controlButtonPanel = new JPanel();
		controlButtonPanel.setLayout(new GridLayout(1,4));
		controlButtonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		controlButtonPanel.add(new JPanel());
		controlButtonPanel.add(session);
		controlButtonPanel.add(clear);
		controlButtonPanel.add(memory);
		
		JPanel manualButtonPanel = new JPanel();
		manualButtonPanel.setLayout(new GridLayout(1,4));
		manualButtonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		manualButtonPanel.add(manualChannels[0]);
		manualButtonPanel.add(manualChannels[1]);
		manualButtonPanel.add(manualChannels[2]);
		manualButtonPanel.add(manualChannels[3]);
		
		JPanel blockButtonPanel = new JPanel();
		blockButtonPanel.setLayout(new GridLayout(1,4));
		blockButtonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		blockButtonPanel.add(blockChannels[0]);
		blockButtonPanel.add(blockChannels[1]);
		blockButtonPanel.add(blockChannels[2]);
		blockButtonPanel.add(blockChannels[3]);
		
		JPanel channelIndicatorPanel = new JPanel();
		channelIndicatorPanel.setLayout(new GridLayout(1,4));
		channelIndicatorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		channelIndicatorPanel.add(channelIndicators[0]);
		channelIndicatorPanel.add(channelIndicators[1]);
		channelIndicatorPanel.add(channelIndicators[2]);
		channelIndicatorPanel.add(channelIndicators[3]);
		
		JPanel blockedIndicatorPanel = new JPanel();
		blockedIndicatorPanel.setLayout(new GridLayout(1,4));
		blockedIndicatorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		blockedIndicatorPanel.add(blockedIndicators[0]);
		blockedIndicatorPanel.add(blockedIndicators[1]);
		blockedIndicatorPanel.add(blockedIndicators[2]);
		blockedIndicatorPanel.add(blockedIndicators[3]);
		
		manualChannels[0].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(1, true);}});
		manualChannels[1].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(2, true);}});
		manualChannels[2].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(3, true);}});
		manualChannels[3].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.timingImpulse(4, true);}});
		
		clear.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.clearMemory();}});
		session.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.newSession();}});
		memory.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {controller.sendMemory();}});
		add(controlButtonPanel);
		add(channelIndicatorPanel);
		add(manualButtonPanel);
		add(blockedIndicatorPanel);
		add(blockButtonPanel);
		
		setBorder(BorderFactory.createLoweredBevelBorder());
	}
	
	class IndicatorButton extends JPanel
	{
		Color color;
		Color baseColor;
		Timer timer;
		
		int flashLength = 200;
		
		IndicatorButton(Color c)
		{
			color = c;
			baseColor = c;
			
			timer = new Timer( 0 , new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					color = baseColor;
					repaint();
				}	
			});
			timer.setRepeats(false);
		    timer.setInitialDelay(flashLength);
		}
		
		public void flashColor(Color c)
		{
			color = c;
			repaint();
			
		    timer.restart();
		}
		protected void paintComponent(Graphics g) 
		{ 
		    int h = getHeight();
		    int w = getWidth();
		    int diameter = Math.min(h, w);
		    super.paintComponent(g); 
		    g.setColor(color);
		    g.fillOval(w/2, 0, diameter, diameter); 
		} 
	}
}
