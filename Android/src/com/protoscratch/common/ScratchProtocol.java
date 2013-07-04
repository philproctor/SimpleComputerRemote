package com.protoscratch.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class ScratchProtocol extends Thread {
	protected ScratchSession session;
	protected ScratchEvents handler;
	protected int sessionId;
	
	protected Socket socket;
	protected OutputStream out;
	protected InputStream in;
	
	protected static class Internal
	{
		//Primary Commands...
		public static final short Config = 1;
		public static final short PingPong = 10;
		
		//Config Secondaries...
		public static final short Confirm = 1;
		
		//PingPong Secondaries...
		public static final short Ping = 1;
		public static final short Pong = 2;
	}
	
	public void run()
	{
		handleSession();
	}
	
	protected abstract void handleSession();
	
	protected ScratchMessage receiveFrame()
	{
		byte[] Header = readChunk(4);
		byte[] key = session.getKey();
		if (Header == null || Header[0] != key[0] || Header[1] != key[1]
				|| Header[2] != key[2] || Header[3] != key[3]) return null;
		Header = readChunk(15);
		int sID = ScratchUtility.byteArrayToInt(Header, 2);
		short Flags = ScratchUtility.byteArrayToShort(Header, 6);
		if (((Flags & ScratchFlags.Internal) != ScratchFlags.Internal && sID != sessionId) ||
				Header[0] != session.getvMajor() || Header[1] != session.getvMinor())
		{
			return null;
		}
		byte Channel = Header[8];
		short PrimaryCommand = ScratchUtility.byteArrayToShort(Header, 9);
		short SecondaryCommand = ScratchUtility.byteArrayToShort(Header, 11);
		int Length = (int)(ScratchUtility.byteArrayToInt(Header, 11) & 0x0000FFFF);
		byte[] Data = readChunk(Length);
		
		if ((Flags & ScratchFlags.Encrypted) == ScratchFlags.Encrypted ||
			(Flags & ScratchFlags.Compressed) == ScratchFlags.Compressed)
			return null; //Unsupported at this time...
		
		return new ScratchMessage(Channel, PrimaryCommand, SecondaryCommand, Data, Flags, sID);
	}
	
	public synchronized boolean Send(ScratchMessage message)
	{
		byte[] Data = new byte[message.dataSize() + 19];
		System.arraycopy(session.getKey(), 0, Data, 0, 4);
		Data[4] = session.getvMajor();
		Data[5] = session.getvMinor();
		System.arraycopy(ScratchUtility.intToByteArray(sessionId), 0, Data, 6, 4);
		System.arraycopy(ScratchUtility.shortToByteArray(message.getFlags()), 0, Data, 10, 2);
		Data[12] = message.Channel;
		System.arraycopy(ScratchUtility.shortToByteArray(message.PrimaryCommand), 0, Data, 13, 2);
		System.arraycopy(ScratchUtility.shortToByteArray(message.SecondaryCommand), 0, Data, 15, 2);
		System.arraycopy(ScratchUtility.shortToByteArray((short)message.dataSize()), 0, Data, 17, 2);
		System.arraycopy(message.getData(), 0, Data, 19, message.dataSize());
		try {
			out.write(Data);
			out.flush();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	protected void Abort()
	{
		this.interrupt();
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {}
	}
	
	protected void Ping()
	{
		Send(new ScratchMessage((byte)0,Internal.PingPong, Internal.Ping));
	}
	
	protected void Pong()
	{
		Send(new ScratchMessage((byte)0,Internal.PingPong, Internal.Pong));
	}
	
	private byte[] readChunk(int Size)
	{
		byte[] data = new byte[Size];
		int offset = 0, sizeLeft = Size;
		
		try
		{
			while (sizeLeft > 0)
			{
				int thisRead = in.read(data, offset, sizeLeft);
				sizeLeft -= thisRead;
				offset += thisRead;
			}
		}
		catch (IOException e)
		{
			return null;
		}
		catch (Exception e)
		{
			return null;
		}
		return data;
	}
}
