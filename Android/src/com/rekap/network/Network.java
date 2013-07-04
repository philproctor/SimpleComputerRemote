package com.rekap.network;

import com.protoscratch.client.ScratchClient;
import com.protoscratch.common.ScratchEvents;
import com.protoscratch.common.ScratchMessage;
import com.protoscratch.locator.LocatorClient;
import com.rekap.remote.Globals;

public class Network extends ScratchEvents {

	private static ScratchEvents client;
	private static LocatorClient locator;
	private static long lastConnect;
	
	public synchronized static ScratchEvents getClient()
	{
		return client;
	}
	
	public synchronized static boolean isConnected()
	{
		return client != null;
	}
	
	public synchronized static void LocatorStart()
	{
		if (locator == null)
		{
			int Key = 0x3FB3BFEF;
			byte[] key = new byte[4];
			key[0] = (byte)((Key >> 24) & 0xFF);
			key[1] = (byte)((Key >> 16) & 0xFF);
			key[2] = (byte)((Key >> 8) & 0xFF);
			key[3] = (byte)(Key & 0xFF);
			locator = new LocatorClient(28533, key);
			locator.start();
		}
	}
	
	public synchronized static void Connect()
	{
		if (locator == null || !Globals.AutoConnect) return;
		Connect(Globals.Server);
	}
	
	public synchronized static void Connect(String Host)
	{
		CloseConnection();
		if (client != null || Host == null) return;
		long thisTime = System.currentTimeMillis();
		if (thisTime - lastConnect > 5000)
		{
			lastConnect = thisTime;
			ScratchClient client = new ScratchClient(Network.class, 28532);
			client.setKey(0x3FB3BFEF);
			client.setVersion((byte)1,(byte)0);
			client.Connect(locator.GetServerAddress(Host));
		}
	}
	
	public synchronized static String[] GetServers()
	{
		if (locator != null)
		{
			return locator.GetServerNames();
		}
		return new String[0];
	}
	
	public synchronized static void CloseConnection()
	{
		if (client != null)
		{
			client.Close();
			client = null;
		}
	}
	
	public synchronized static void Detect()
	{
		if (locator != null)
			locator.Detect();
	}
	
	@Override
	public void NewConnection() {
		client = this;
	}

	@Override
	public void MessageReceived(ScratchMessage Message) {
	}

	@Override
	public void ConnectionLost() {
		client = null;
	}

}
