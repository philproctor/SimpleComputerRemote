package com.protoscratch.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.protoscratch.common.ScratchDelegate;
import com.protoscratch.common.ScratchEvents;
import com.protoscratch.common.ScratchFlags;
import com.protoscratch.common.ScratchMessage;
import com.protoscratch.common.ScratchProtocol;
import com.protoscratch.common.ScratchSession;

public final class ClientConnection extends ScratchProtocol implements ScratchDelegate{

	boolean initialized;
	private String Host;
	
	ClientConnection(ScratchSession session, ScratchEvents handler)
	{
		this.session = session;
		this.handler = handler;
	}
	
	public void Connect(String Host)
	{
		this.Host = Host;
		this.start();
	}

	@Override
	protected void handleSession() 
	{
		try {
			socket = new Socket(Host, session.port);
			socket.setTcpNoDelay(true);
			out = socket.getOutputStream();
			out.flush();
			in = socket.getInputStream();
		} 
		catch (UnknownHostException e) { return; } 
		catch (IOException e) { return; }
		
		while (!interrupted())
		{
			ScratchMessage message = receiveFrame();
			if (message != null)
			{
				processMessage(message);
			}
			else
			{
				if (initialized)
					handler.ConnectionLost();
				break;
			}
		}
	}
	
	private void processMessage(ScratchMessage message)
	{
		if (message.getFlag(ScratchFlags.Internal))
		{
			switch (message.PrimaryCommand)
			{
			case Internal.Config:
				if (initialized) Abort();
				processConfigMessage(message);
				configConfirm();
				handler.InitializeConnection(this);
				initialized = true;
				break;
				
			case Internal.PingPong:
				if (message.SecondaryCommand == Internal.Ping)
					Pong();
				break;
			}
		}
		else
			handler.MessageReceived(message);
	}
	
	private void processConfigMessage(ScratchMessage message)
	{
		sessionId = message.getSessionID();
	}
	
	private void configConfirm()
	{
		ScratchMessage message = new ScratchMessage((byte)0, Internal.Config, Internal.Confirm);
		message.setFlag(ScratchFlags.Internal, true);
		Send (message);
	}

	public void Close() {
		Abort();
	}
	
}
