package com.protoscratch.common;

public class ScratchSession {
	private byte[] key;
	public int port;
	public boolean useSessionIDs;
	private byte vMajor, vMinor;
	private boolean writeLock;
	
	public byte[] getKey()
	{
		return key;	
	}
	
	public void setKey(int Key)
	{
		if (writeLock) return;
		key = new byte[4];
		key[0] = (byte)((Key >> 24) & 0xFF);
		key[1] = (byte)((Key >> 16) & 0xFF);
		key[2] = (byte)((Key >> 8) & 0xFF);
		key[3] = (byte)(Key & 0xFF);
	}
	
	public byte getvMajor()
	{
		return vMajor;
	}
	
	public byte getvMinor()
	{
		return vMinor;
	}
	
	public void setVersion(byte vMajor, byte vMinor)
	{
		if (writeLock) return;
		this.vMajor = vMajor;
		this.vMinor = vMinor;
	}
	
	public void lock()
	{
		writeLock = true;
	}
}
