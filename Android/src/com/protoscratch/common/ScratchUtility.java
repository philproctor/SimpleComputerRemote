package com.protoscratch.common;

public class ScratchUtility {
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static final byte[] shortToByteArray(short value) {
	    return new byte[] {
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static final int byteArrayToInt(byte[] value, int offset)
	{
		return (int)(((int)value[0 + offset] << 24) & 0xFF000000) |
			   (((int)value[1 + offset] << 16) & 0x00FF0000) |
			   (((int)value[2 + offset] << 8) & 0x0000FF00) |
			   ((int)value[3 + offset] & 0x000000FF);
	}
	
	public static final short byteArrayToShort(byte[] value, int offset)
	{
		return (short)((((short)value[0 + offset] << 8) & 0xFF00) |
			   ((short)value[1 + offset] & 0x00FF));
	}
}
