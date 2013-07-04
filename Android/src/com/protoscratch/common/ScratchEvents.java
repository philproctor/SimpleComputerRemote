package com.protoscratch.common;

public abstract class ScratchEvents {
	
	private ScratchDelegate delegate;
	
	public void InitializeConnection(ScratchDelegate delegate)
	{
		this.delegate = delegate;
		NewConnection();
	}
	
	public void Send(ScratchMessage message)
	{
		delegate.Send(message);
	}
	
	public void Close()
	{
		delegate.Close();
	}
	
	public abstract void NewConnection();
	public abstract void MessageReceived(ScratchMessage Message);
	public abstract void ConnectionLost();
}
