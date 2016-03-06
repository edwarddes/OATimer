package com.desparddesign.orienteering.apps.SIDownloader;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;

import com.desparddesign.orienteering.codechecking.sportident.Preferences;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI5Inserted;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI5ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI5ReadBlockRequest;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI6Inserted;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI6ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI6ReadBlockRequest;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI89Inserted;
import com.desparddesign.orienteering.codechecking.sportident.commands.SI89ReadBlock;
import com.desparddesign.orienteering.codechecking.sportident.commands.SICommand;
import com.desparddesign.orienteering.codechecking.sportident.recordBuilders.SI5DownloadRecordBuilder;
import com.desparddesign.orienteering.codechecking.sportident.recordBuilders.SI6DownloadRecordBuilder;
import com.desparddesign.orienteering.codechecking.sportident.transport.RxTxSerialManager;

public class Downloader
{
	public Downloader()
	{
		super();
	}
    
    private enum SI6BlockState {none,zero,one,six,seven};
    //private enum SI89BlockState {};
    
    public static void main ( String[] args )
    {
    	SI6BlockState SI6State = SI6BlockState.none;
    	SI6ReadBlock SI6Block0 = null,SI6Block1 = null,SI6Block6 = null,SI6Block7 = null;
    	
    	RxTxSerialManager serialPort;
    	
        try
        {
        	@SuppressWarnings("unused")
			Downloader main = new Downloader();
            serialPort = new RxTxSerialManager();
            serialPort.connect("/dev/tty.SLAB_USBtoUART", 0, true);
            while(true)
            {
            	SICommand command = serialPort.reader.recievePacketQueue().take();
            	
            	if(Preferences.getInstance().printRecievePackets())
            		System.out.println(command);
            	
            	if(command instanceof SI5Inserted)
            	{
            		serialPort.writer.packetizer.dePacketize((new SI5ReadBlockRequest()).rawPacket());
            	}
            	if(command instanceof SI5ReadBlock)
            	{
            		//System.out.print(new SI5DownloadRecordBuilder((SI5ReadBlock)command).toString());
            		
            		//http://localhost:55555/cgi-bin/WebObjects/OA.woa/ra/download
            		HttpClient httpclient = new DefaultHttpClient();
            		URI uri = URIUtils.createURI("http", "localhost", 55555, "/cgi-bin/WebObjects/OA.woa/ra/race/4/download", null, null);
            		HttpPut put = new HttpPut(uri);
            		
            		StringEntity entity = new StringEntity(new SI5DownloadRecordBuilder((SI5ReadBlock)command).toString(), "UTF-8");
            		entity.setContentType("application/text");
            		put.setEntity(entity);
            		
            		HttpResponse response = null;
            		response = httpclient.execute(put);
            		
            		@SuppressWarnings("unused")
					int statusCode = response.getStatusLine().getStatusCode();
            	}
            	
            	if(command instanceof SI6Inserted)
            	{
            		SI6State = SI6BlockState.zero;
            		serialPort.writer.packetizer.dePacketize((new SI6ReadBlockRequest(0x08)).rawPacket());
            	}
            	/*
            	if(command instanceof SI6ReadBlock)
            	{
            		if(SI6State == SI6BlockState.zero)
            		{
            			SI6Block0 = (SI6ReadBlock)command;
            			serialPort.writer.packetizer.dePacketize((new SI6ReadBlockRequest(0x01)).rawPacket());
            			SI6State = SI6BlockState.one;
            		}
            		else if(SI6State == SI6BlockState.one)
            		{
            			SI6Block1 = (SI6ReadBlock)command;
            			serialPort.writer.packetizer.dePacketize((new SI6ReadBlockRequest(0x06)).rawPacket());
            			SI6State = SI6BlockState.six;
            		}
            		else if(SI6State == SI6BlockState.six)
            		{
            			SI6Block6 = (SI6ReadBlock)command;
            			serialPort.writer.packetizer.dePacketize((new SI6ReadBlockRequest(0x07)).rawPacket());
            			SI6State = SI6BlockState.seven;
            		}
            		else if(SI6State == SI6BlockState.seven)
            		{
            			SI6Block7 = (SI6ReadBlock)command;
            			SI6State = SI6BlockState.none;
            			@SuppressWarnings("unused")
						StringEntity entity = new StringEntity(new SI6DownloadRecordBuilder(SI6Block0,SI6Block1,SI6Block6,SI6Block7).toString(), "UTF-8");
            		}
            	}
            	*/
            	if(command instanceof SI89Inserted)
            	{
            		
            	}
            	if(command instanceof SI89ReadBlock)
            	{
            		
            	}
            		
            }
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}