package com.protoscratch.common;

public interface ScratchDelegate {
	public boolean Send(ScratchMessage message);
	public void Close();
}
