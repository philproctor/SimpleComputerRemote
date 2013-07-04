package com.rekap.remote;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.rekap.network.NetInput;

public class KeypadHandler implements OnKeyListener{
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) return false;
		
		switch (event.getAction())
		{
		case KeyEvent.ACTION_UP:
			SendKeyCode(keyCode, (char)event.getUnicodeChar());
			return true;
			
		case KeyEvent.ACTION_MULTIPLE:
			if (keyCode == KeyEvent.KEYCODE_UNKNOWN)
			{
				NetInput.SendKeySequence(event.getCharacters());
			}
			else
			{
				for (int i = 0; i < event.getRepeatCount(); i++)
				{
					SendKeyCode(keyCode, (char)event.getUnicodeChar());
				}
			}
			return true;
		}
		return false;
	}
	
	private void SendKeyCode(int keyCode, char UnicodeChar)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DEL:
			NetInput.SendKeycode(8);
			break;
			
		case KeyEvent.KEYCODE_ENTER:
			NetInput.SendKeycode(13);
			break;
		
		default:
			NetInput.SendKeySequence(new String() + UnicodeChar);
			break;
		}
	}
}
