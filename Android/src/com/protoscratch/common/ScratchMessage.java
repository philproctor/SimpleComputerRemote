package com.protoscratch.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ScratchMessage {
	public byte Channel;
	public short PrimaryCommand, SecondaryCommand;
	private short Flags;
	private int Offset, SessionID;
	private ByteBuffer data;
	
	public ScratchMessage(short PrimaryCommand)
	{
		this((byte)0, PrimaryCommand, (short)0);
	}
	
	public ScratchMessage(short PrimaryCommand, short SecondaryCommand)
	{
		this((byte)0, PrimaryCommand, SecondaryCommand);
	}
	
	public ScratchMessage(byte Channel, short PrimaryCommand, short SecondaryCommand)
	{
		this.Channel = Channel;
		this.PrimaryCommand = PrimaryCommand;
		this.SecondaryCommand = SecondaryCommand;
		data = ByteBuffer.allocate(65535);
		data.order(ByteOrder.BIG_ENDIAN);
	}
	
	public ScratchMessage(byte Channel, short PrimaryCommand, short SecondaryCommand, byte[] Data, short Flags, int SessionID)
	{
		this.Channel = Channel;
		this.PrimaryCommand = PrimaryCommand;
		this.SecondaryCommand = SecondaryCommand;
		this.Flags = Flags;
		this.SessionID = SessionID;
		data = ByteBuffer.wrap(Data);
		data.order(ByteOrder.BIG_ENDIAN);
		data.position(Data.length);
	}
	
	public short getFlags()
	{
		return Flags;
	}
	
	public int getSessionID()
	{
		return SessionID;
	}
	
	public void setFlag(short Flag, boolean Value)
	{
		if (Value)
			Flags |= Flag;
		else
			Flags &= ~Flag;
	}
	
	public boolean getFlag(short Flag)
	{
		return (Flags & Flag) == Flag;
	}
	
	public int dataSize()
	{
		return data.position();
	}
	
	public void resetReader()
	{
		Offset = 0;
	}
	
	public byte[] getData()
	{
		byte[] result = new byte[data.position()];
		data.rewind();
		data.get(result, 0, result.length);
		return result;
	}
	
	public byte getByte()
	{
		return data.get(Offset++);
	}
	
	public byte[] getBytes(int Size)
	{
		int original = data.position();
		byte[] result = new byte[Size];
		data.position(Offset);
		data.get(result, 0, Size);
		data.position(original);
		Offset += Size;
		return result;
	}
	
	public short getShort()
	{
		short result = data.getShort(Offset);
		Offset += 2;
		return result;
	}
	
	public int getInt()
	{
		int result = data.getInt(Offset);
		Offset += 4;
		return result;
	}
	
	public String getString()
	{
		short size = getShort();
		try {
			return new String(getBytes(size), "UTF-16");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public void pack(byte newData)
	{
		data.put(newData);
	}
	
	public void pack(byte[] newData)
	{
		data.put(newData);
	}
	
	public void pack(short newData)
	{
		data.putShort(newData);
	}
	
	public void pack(int newData)
	{
		data.putInt(newData);
	}
	
	public void pack(String newData)
	{
		try {
			byte[] result = newData.getBytes("UTF-16");
			pack((short)result.length);
			pack(result);
		} catch (UnsupportedEncodingException e) { }
	}
}
