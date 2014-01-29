package com.desparddesign.orienteering.codechecking.sportident.recordBuilders;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.desparddesign.orienteering.codechecking.sportident.commands.SI6ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI6ReadBlock.SI6PunchRecord;

public class SI6DownloadRecordBuilder 
{
	Logger logger = Logger.getLogger(SI6DownloadRecordBuilder.class.getName());
	
	SI6ReadBlock dataBlock0,dataBlock1,dataBlock6,dataBlock7;
	
	Document doc;
	
	public SI6DownloadRecordBuilder(SI6ReadBlock block0, SI6ReadBlock block1, SI6ReadBlock block6, SI6ReadBlock block7)
	{
		dataBlock0 = block0;
		dataBlock1 = block1;
		dataBlock6 = block6;
		dataBlock7 = block7;
		
		try
		{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element root = doc.createElement("downloadRecord");
            doc.appendChild(root);
            
            Element card = doc.createElement("card");
            card.setAttribute("type", "SI-6");
            Text cardValue = doc.createTextNode(new Integer(dataBlock0.cardNumber).toString());
            card.appendChild(cardValue);
            root.appendChild(card);
            
            Element stationID = doc.createElement("stationID");
            Text stationIDValue = doc.createTextNode(new Integer(dataBlock0.control).toString());
            stationID.appendChild(stationIDValue);
            root.appendChild(stationID);
            
            Element time = doc.createElement("time");
            String currentDateTime = (new LocalDateTime()).toString("YYYY-MM-dd HH:mm:ss");
            Text timeValue = doc.createTextNode(currentDateTime);
            time.appendChild(timeValue);
            root.appendChild(time);
            
            if(!(block0.check.punchTime == null))
            {
            	Element check = doc.createElement("punch");
            	check.setAttribute("type","check");
            	Element controlID = doc.createElement("controlID");
            	controlID.appendChild(doc.createTextNode(new Integer(block0.check.controlNumber).toString()));
            	check.appendChild(controlID);
            	Element checkTime = doc.createElement("time");
            	checkTime.appendChild(doc.createTextNode(block0.check.punchTime.toString("HH:mm:ss")));
            	check.appendChild(checkTime);
                root.appendChild(check);
            }
            
            if(!(block0.clear.punchTime == null))
            {
            	Element clear = doc.createElement("punch");
            	clear.setAttribute("type","clear");
            	Element controlID = doc.createElement("controlID");
            	controlID.appendChild(doc.createTextNode(new Integer(block0.clear.controlNumber).toString()));
            	clear.appendChild(controlID);
            	Element checkTime = doc.createElement("time");
            	checkTime.appendChild(doc.createTextNode(block0.clear.punchTime.toString("HH:mm:ss")));
            	clear.appendChild(checkTime);
                root.appendChild(clear);
            }
            
            if(!(block0.start.punchTime == null))
            {
            	Element start = doc.createElement("punch");
            	start.setAttribute("type","start");
            	Element controlID = doc.createElement("controlID");
            	controlID.appendChild(doc.createTextNode(new Integer(block0.start.controlNumber).toString()));
            	start.appendChild(controlID);
            	Element startTime = doc.createElement("time");
            	startTime.appendChild(doc.createTextNode(block0.start.punchTime.toString("HH:mm:ss")));
            	start.appendChild(startTime);
                root.appendChild(start);
            }
            
            Integer sequence = 0;
            for(SI6PunchRecord punchRecord : block6.punchRecords)
            {
            	Element punch = doc.createElement("punch");
            	if(punchRecord.punchTime == null)
            		punch.setAttribute("type","punchNoTime");
            	else
            		punch.setAttribute("type", "punch");
            	Element controlID = doc.createElement("controlID");
            	controlID.appendChild(doc.createTextNode(new Integer(punchRecord.controlNumber).toString()));
            	Element punchTime = doc.createElement("time");
            	punchTime.appendChild(doc.createTextNode(punchRecord.punchTime.toString("HH:mm:ss")));
            	Element punchSequence = doc.createElement("sequence");
            	punchSequence.appendChild(doc.createTextNode(sequence.toString()));
            	punch.appendChild(punchSequence);
            	if(!(punchRecord.punchTime == null))
            		punch.appendChild(punchTime);
            	punch.appendChild(controlID);
            	root.appendChild(punch);
            	sequence++;         	
            }
            for(SI6PunchRecord punchRecord : block7.punchRecords)
            {
            	Element punch = doc.createElement("punch");
            	if(punchRecord.punchTime == null)
            		punch.setAttribute("type","punchNoTime");
            	else
            		punch.setAttribute("type", "punch");
            	Element controlID = doc.createElement("controlID");
            	controlID.appendChild(doc.createTextNode(new Integer(punchRecord.controlNumber).toString()));
            	Element punchTime = doc.createElement("time");
            	punchTime.appendChild(doc.createTextNode(punchRecord.punchTime.toString("HH:mm:ss")));
            	Element punchSequence = doc.createElement("sequence");
            	punchSequence.appendChild(doc.createTextNode(sequence.toString()));
            	punch.appendChild(punchSequence);
            	if(!(punchRecord.punchTime == null))
            		punch.appendChild(punchTime);
            	punch.appendChild(controlID);
            	root.appendChild(punch);
            	sequence++;         	
            }
            
            if(!(block0.finish.punchTime == null))
            {
            	Element finish = doc.createElement("punch");
            	finish.setAttribute("type","finish");
            	Element controlID = doc.createElement("controlID");
            	controlID.appendChild(doc.createTextNode(new Integer(block0.finish.controlNumber).toString()));
            	finish.appendChild(controlID);
            	Element finishTime = doc.createElement("time");
            	finishTime.appendChild(doc.createTextNode(block0.finish.punchTime.toString("HH:mm:ss")));
            	finish.appendChild(finishTime);
                root.appendChild(finish);
            }
            
            logger.debug(toString());
            
		}
		catch(Exception e)
		{
			//return black document
		}
	}
	
	public String toString()
	{
		try
		{
			//set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        transfac.setAttribute("indent-number", new Integer(4));
	        Transformer trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");        
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	
	        //create string from xml tree
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(doc);
	        trans.transform(source, result);
	        return sw.toString();
		}
		catch (Exception e)
		{
			return "";
		}
	}
}
