package com.protoscratch.locator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class LocatorClient extends Thread{
	
	private int Port;
	private byte[] Key;
	private DatagramSocket dSocket;
	private Map<String, String> servers;
	private Timer detectTimer;
	
	TimerTask detectTask = new TimerTask() {
		@Override
		public void run() {
			Detect();
		}
	};
	
	public LocatorClient(int Port, byte[] Key)
	{
		this.Port = Port;
		this.Key = Key;
		servers = new HashMap<String, String>();
	}
	
	public void run()
	{
        if (detectTimer == null)
        {
        	detectTimer = new Timer();
        	detectTimer.schedule(detectTask, 5000, 5000);
        }
        
		try {
			byte[] buff = new byte[1024];
			dSocket = new DatagramSocket();
			DatagramPacket dPacket = new DatagramPacket(buff, buff.length);
			Detect();
			
			while (!interrupted())
			{
				dSocket.receive(dPacket);
				String address = dPacket.getAddress().getHostAddress();
				if (dPacket.getLength() > 4 && buff[0] == Key[0] && buff[1] == Key[1] && buff[2] == Key[2] && buff[3] == Key[3])
				{
					byte[] data = new byte[dPacket.getLength() - 4];
					System.arraycopy(buff, 4, data, 0, data.length);
					processFrame(address, data);
				}
			}
		} catch (IOException e) { }
		dSocket = null;
	}
	
	public synchronized String GetServerAddress(String Name)
	{
		return servers.get(Name);
	}
	
	public synchronized String[] GetServerNames()
	{
		return servers.keySet().toArray(new String[servers.keySet().size()]);
	}
	
	private void processFrame(String address, byte[] data)
	{
		try {
			ByteBuffer bbData = ByteBuffer.wrap(data);
			bbData.order(ByteOrder.BIG_ENDIAN);
			byte[] rawName = new byte[bbData.getShort()];
			bbData.get(rawName, 0, rawName.length);
			String name = new String(rawName, "UTF16");
			if (!servers.containsKey("First"))
				servers.put("First", address);
			servers.put(name, address);
		} catch (UnsupportedEncodingException e) { } //should never happen
	}
	
	public synchronized void Detect()
	{
		if (dSocket == null) return;
		try {
			DatagramPacket sPacket = new DatagramPacket(Key, Key.length);
			sPacket.setPort(Port);
			sPacket.setAddress(InetAddress.getByAddress(new byte[] { (byte)255, (byte)255, (byte)255, (byte)255 }));
			dSocket.send(sPacket);
		} catch (UnknownHostException e) { } catch (IOException e) { }
	}
}
