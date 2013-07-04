package com.rekap.remote;

import com.rekap.network.NetInput;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchpadHandler implements OnTouchListener {
	float lastX, lastY, lastScrollY, originX, originY;
	long touchDown;
	boolean multiEvent, dragging, strayedOrigin;
	
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getPointerCount() > 1) multiEvent = true;
		if (!strayedOrigin && !dragging && event.getAction() != MotionEvent.ACTION_DOWN) {
			if (	originX - lastX > 2 || originX - lastX < -2 ||
					originY - lastY > 2 || originY - lastY < -2) {
				strayedOrigin = true;
			}
			if (System.currentTimeMillis() - touchDown > 500) {
				dragging = true;
				NetInput.LeftDown();
			}
		}
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			lastY = event.getY();
			originX = lastX;
			originY = lastY;
			touchDown = System.currentTimeMillis();
			break;
			
		case MotionEvent.ACTION_UP:
			if (System.currentTimeMillis() - touchDown < 150)
			{
				if (multiEvent)
					NetInput.RightClick();
				else
					NetInput.LeftClick();
			}
			if (dragging) {
				NetInput.LeftUp();
			}
			multiEvent = false;
			dragging = false;
			strayedOrigin = false;
			break;
			
		case MotionEvent.ACTION_MOVE:
			float xOffset = (event.getX() - lastX);
			float yOffset = (event.getY() - lastY);
			lastX = event.getX();
			lastY = event.getY();
			if (event.getPointerCount() == 1) {
				NetInput.MoveMouse((int)(xOffset * (5 * Globals.Sensitivity)), (int)(yOffset * (5 * Globals.Sensitivity)));
			} else { //2 or more touches down
				if (lastScrollY ==0) {
					lastScrollY = lastY;
				} else {
					if (lastScrollY - lastY > 20) {
						NetInput.ScrollUp();
						lastScrollY = lastY;
					} else if (lastScrollY - lastY < -20) {
						NetInput.ScrollDown();
						lastScrollY = lastY;
					}
				}
			}
			break;
		}
		return true;
	}
}
