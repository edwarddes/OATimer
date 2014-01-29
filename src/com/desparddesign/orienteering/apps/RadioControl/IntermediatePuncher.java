package com.desparddesign.orienteering.apps.RadioControl;

import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.desparddesign.orienteering.codechecking.sportident.Preferences;
import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;
import com.desparddesign.orienteering.codechecking.sportident.commands.SIPunchRecord;
import com.desparddesign.orienteering.codechecking.sportident.recordBuilders.SIPunchRecordBuilder;
import com.desparddesign.orienteering.codechecking.sportident.transport.RxTxSerialManager;
import com.desparddesign.orienteering.codechecking.sportident.transport.TelnetSerialManager;
import com.desparddesign.orienteering.codechecking.sportident.transport.Transport;

public class IntermediatePuncher
{	
	static Logger logger = Logger.getLogger(IntermediatePuncher.class.getName());
	
	public IntermediatePuncher()
	{
		super();
	}
    
    public static void main ( String[] args )
    {
       	Transport serialPort;
       	HTTPPoster poster;
       	
		PropertyConfigurator.configure("log4j.properties");
    	
        try
        {
        	@SuppressWarnings("unused")
			IntermediatePuncher main = new IntermediatePuncher();
        	
        	if(Preferences.getInstance().transport().equals("GW212"))
        		serialPort = new TelnetSerialManager();
        	else
        		serialPort = new RxTxSerialManager();
        	
        	logger.info("Serial port: " + Preferences.getInstance().serialPort());
        	
            if(Preferences.getInstance().transport().equals("GW212"))
            	serialPort.connect(Preferences.getInstance().serialPort(), 4660, false);
            else
            	serialPort.connect(Preferences.getInstance().serialPort(),0, false);
            
            poster = new HTTPPoster();
            Thread posterThread = new Thread(poster);
            posterThread.start();
            
            String controlType = Preferences.getInstance().controlType();
            
            while(true)
            {
            	SICommand command = serialPort.reader.recievePacketQueue().take();
            	
            	if(Preferences.getInstance().printRecievePackets())
            		System.out.println(command);
            	
            	if(command instanceof SIPunchRecord)
            	{
            		StringEntity entity = new StringEntity(new SIPunchRecordBuilder((SIPunchRecord)command, controlType).toString(), "UTF-8");
            		poster.postQueue.add(entity);
            	}
            }
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static class HTTPPoster implements Runnable
    {
    	public LinkedBlockingQueue<StringEntity> postQueue = new LinkedBlockingQueue<StringEntity>();
    	
		public void run() 
		{
			try
			{
				while(true)
				{
					StringEntity postContent = postQueue.take();
					
					HttpClient httpclient = new DefaultHttpClient();
		    		URI uri = URIUtils.createURI("http", Preferences.getInstance().host(), Preferences.getInstance().port(), Preferences.getInstance().postURL() + Preferences.getInstance().race() + "/download", null, null);
		    		
					HttpPut put = new HttpPut(uri);
					postContent.setContentType("application/text");
		    		put.setEntity(postContent);
		    		
		    		HttpResponse response = null;
		    		response = httpclient.execute(put);
		    		
		    		@SuppressWarnings("unused")
					int statusCode = response.getStatusLine().getStatusCode();
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}    	
    }
}