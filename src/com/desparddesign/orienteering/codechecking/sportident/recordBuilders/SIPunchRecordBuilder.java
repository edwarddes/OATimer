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

import com.desparddesign.orienteering.codechecking.sportident.SICardNumberFormatter;
import com.desparddesign.orienteering.codechecking.sportident.commands.SIPunchRecord;

public class SIPunchRecordBuilder 
{
	Logger logger = Logger.getLogger(SIPunchRecordBuilder.class.getName());
	
	SIPunchRecord record;
	Document doc;
	
	public SIPunchRecordBuilder(SIPunchRecord record, String controlType)
	{
		this.record = record;
		try
		{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			Element root = doc.createElement("downloadRecord");
            doc.appendChild(root);
            
            Element card = doc.createElement("card");
            card.setAttribute("type", SICardNumberFormatter.kindOfCard(record.cardNumber));
            Text cardValue = doc.createTextNode(new Integer(record.cardNumber).toString());
            card.appendChild(cardValue);
            root.appendChild(card);
            
            Element stationID = doc.createElement("stationID");
            Integer stationIDInt = new Integer(record.control);
            Text stationIDValue = doc.createTextNode(stationIDInt.toString());
            stationID.appendChild(stationIDValue);
            root.appendChild(stationID);
            
            Element time = doc.createElement("time");
            String currentDateTime = (new LocalDateTime()).toString("YYYY-MM-dd HH:mm:ss");
            Text timeValue = doc.createTextNode(currentDateTime);
            time.appendChild(timeValue);
            root.appendChild(time);
            
        	Element punch = doc.createElement("punch");
        	punch.setAttribute("type",controlType);
        	Element punchTime = doc.createElement("time");
        	punchTime.appendChild(doc.createTextNode(record.punchTime.toString("HH:mm:ss")));
        	punch.appendChild(punchTime);
        	Element sequence = doc.createElement("sequence");
        	sequence.appendChild(doc.createTextNode("1"));
        	punch.appendChild(sequence);
        	Element controlID = doc.createElement("controlID");
        	Integer controlIDInt = new Integer(record.control);
        	controlID.appendChild(doc.createTextNode(controlIDInt.toString()));
        	punch.appendChild(controlID);
            root.appendChild(punch);
            
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
<punch type="preliminary">
	<sequence>1</sequence>
	<controlID>4</controlID>
	<time>7:21:1</time>
</punch>
</downloadRecord>
*/