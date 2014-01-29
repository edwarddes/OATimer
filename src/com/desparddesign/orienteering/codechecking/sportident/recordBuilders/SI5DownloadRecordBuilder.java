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

import com.desparddesign.orienteering.codechecking.sportident.commands.SI5ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI5ReadBlock.SI5PunchRecord;

public class SI5DownloadRecordBuilder 
{
	Logger logger = Logger.getLogger(SI5DownloadRecordBuilder.class.getName());
	
	SI5ReadBlock dataBlock;
	
	Document doc;
	
	public SI5DownloadRecordBuilder(SI5ReadBlock block)
	{
		dataBlock = block;
		try
		{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element root = doc.createElement("downloadRecord");
            doc.appendChild(root);
            
            Element card = doc.createElement("card");
            card.setAttribute("type", "SI-5");
            Text cardValue = doc.createTextNode(new Integer(dataBlock.cardNumber).toString());
            card.appendChild(cardValue);
            root.appendChild(card);
            
            Element stationID = doc.createElement("stationID");
            Text stationIDValue = doc.createTextNode(new Integer(dataBlock.control).toString());
            stationID.appendChild(stationIDValue);
            root.appendChild(stationID);
            
            Element time = doc.createElement("time");
            String currentDateTime = (new LocalDateTime()).toString("YYYY-MM-dd HH:mm:ss");
            Text timeValue = doc.createTextNode(currentDateTime);
            time.appendChild(timeValue);
            root.appendChild(time);
            
            if(!(block.check == null))
            {
            	Element check = doc.createElement("punch");
            	check.setAttribute("type","check");
            	Element checkTime = doc.createElement("time");
            	checkTime.appendChild(doc.createTextNode(block.check.toString("HH:mm:ss")));
            	check.appendChild(checkTime);
                root.appendChild(check);
            }
            
            if(!(block.start == null))
            {
            	Element start = doc.createElement("punch");
            	start.setAttribute("type","start");
            	Element startTime = doc.createElement("time");
            	startTime.appendChild(doc.createTextNode(block.start.toString("HH:mm:ss")));
            	start.appendChild(startTime);
                root.appendChild(start);
            }
            
            Integer sequence = 0;
            //for(SI5PunchRecord punchRecord : block.punchRecords)
            for(int i=0;i<block.recordCount;i++)
            {
            	SI5PunchRecord punchRecord = block.punchRecords.get(i);
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
            
            if(!(block.finish == null))
            {
            	Element finish = doc.createElement("punch");
            	finish.setAttribute("type","finish");
            	Element finishTime = doc.createElement("time");
            	finishTime.appendChild(doc.createTextNode(block.finish.toString("HH:mm:ss")));
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

/*
<downloadRecord>
<card type="SI5">432258</card>
<stationID>1</stationID>
<time>12:1:2</time>
<punch type="start">
	<controlID></controlID>
	<time>7:21:1</time>
</punch>
<punch type="finish">
	<controlID></controlID>
	<time>7:21:1</time>
</punch>
<punch type="check">
	<controlID></controlID>
	<time>7:21:1</time>
</punch>
<punch type="clear">
	<controlID></controlID>
	<time>7:21:1</time>
</punch>
<punch type="control">
	<controlID></controlID>
	<sequence>1</sequence>
	<time>7:21:1</time>
</punch>
<punch type="controlNoTime">
	<controlID></controlID>
	<sequence>2</sequence>
</punch>
</downloadRecord>
*/