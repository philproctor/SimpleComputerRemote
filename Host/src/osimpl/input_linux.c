#include <X11/extensions/XTest.h>

static inline void SendKeycode(int keyCode) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
		switch (keyCode)
		{
		case 8:
			XTestFakeKeyEvent(display, XKeysymToKeycode(display, 0xff08), True, CurrentTime );
			XTestFakeKeyEvent(display, XKeysymToKeycode(display, 0xff08), False, CurrentTime );
			break;

		case 13:
			XTestFakeKeyEvent(display, XKeysymToKeycode(display, 0xff0d), True, CurrentTime );
			XTestFakeKeyEvent(display, XKeysymToKeycode(display, 0xff0d), False, CurrentTime );
			break;
		}
		XFlush(display);
		XCloseDisplay(display);
	}
}

static inline void SendKeysym(int keySym) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
		XTestFakeKeyEvent(display, XKeysymToKeycode(display, keySym), True, CurrentTime );
		XTestFakeKeyEvent(display, XKeysymToKeycode(display, keySym), False, CurrentTime );
		XFlush(display);
		XCloseDisplay(display);
	}
}

static inline void SendClick(int button, Bool down) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
		XTestFakeButtonEvent(display, button, down, CurrentTime);
		XFlush(display);
		XCloseDisplay(display);
	}
}

static inline void SendMouseMove(int X, int Y) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
		XTestFakeRelativeMotionEvent(display, X, Y, CurrentTime);
		XFlush(display);
		XCloseDisplay(display);
	}
}
