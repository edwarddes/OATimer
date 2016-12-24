package com.desparddesign.orienteering.apps.TimingDBInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class DBPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private DBController controller;
	
	private JComboBox events;
	private JCheckBox primaryCheck;
	private JCheckBox secondaryCheck;
	
	DBPanel(DBController cont)
	{
		controller = cont;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		Map<Integer,String> eventsMap = controller.getEvents();
		
		Object[] eventNames = eventsMap.values().toArray();
		events = new JComboBox(eventNames);
		int selectedEvent = controller.getEventId();
		String selectedEventName = eventsMap.get(selectedEvent);
		
		events.setSelectedIndex(java.util.Arrays.asList(eventNames).indexOf(selectedEventName));
		
		events.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JComboBox combo = (JComboBox)e.getSource();
                        String name = (String)combo.getSelectedItem();
                        
                        for(Map.Entry<Integer,String> entry : controller.getEvents().entrySet())
                        {
                        	if(entry.getValue().equals(name))
                        		controller.setEventId(entry.getKey());
                        }
                    }
                }            
        );

		add(events);
		
		primaryCheck = new JCheckBox("Create Primary Records");
		primaryCheck.setSelected(true);
		primaryCheck.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent) 
					{	
						AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				        boolean selected = abstractButton.getModel().isSelected();
				        controller.setShouldCreatePrimaryRecords(selected);
					}
					
				}
		);
		secondaryCheck = new JCheckBox("Create Secondary Records");
		secondaryCheck.setSelected(false);
		secondaryCheck.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent actionEvent) 
					{	
						AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				        boolean selected = abstractButton.getModel().isSelected();
				        controller.setShouldCreateSecondaryRecords(selected);
					}
					
				}
		);
		add(primaryCheck);
		add(secondaryCheck);
	}

}
