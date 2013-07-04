package com.protoscratch.client;

import com.protoscratch.common.ScratchEvents;
import com.protoscratch.common.ScratchSession;

public class ScratchClient extends ScratchSession {

	ScratchEvents handler;
	
	public ScratchClient(@SuppressWarnings("rawtypes") Class classRef, int port)
	{
		try {
			handler = (ScratchEvents)classRef.newInstance();
		} 
		catch (InstantiationException e) {} 
		catch (IllegalAccessException e) {}
		this.port = port;
	}
	
	public void Connect(String Host)
	{
		this.lock();
		
		ClientConnection client = new ClientConnection(this,handler);
		client.Connect(Host);
	}
}
