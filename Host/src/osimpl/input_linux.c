#include <X11/extensions/XTest.h>
#include <X11/extensions/dpms.h>

static void WakeUp(Display *display) {
	CARD16 power_level = 0;
	BOOL state = 0;
	Status status = 0;

	status = DPMSInfo(display, &power_level, &state);
	if (status && state && power_level)
		DPMSForceLevel(display, 0);

	XResetScreenSaver(display);
}

static inline void SendKeycode(int keyCode) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
		WakeUp(display);

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
                WakeUp(display);

		XTestFakeKeyEvent(display, XKeysymToKeycode(display, keySym), True, CurrentTime );
		XTestFakeKeyEvent(display, XKeysymToKeycode(display, keySym), False, CurrentTime );
		XFlush(display);
		XCloseDisplay(display);
	}
}

static inline void SendClick(int button, Bool down) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
                WakeUp(display);

		XTestFakeButtonEvent(display, button, down, CurrentTime);
		XFlush(display);
		XCloseDisplay(display);
	}
}

static inline void SendMouseMove(int X, int Y) {
	Display *display = XOpenDisplay(NULL);
	if (display != NULL) {
                WakeUp(display);

		XTestFakeRelativeMotionEvent(display, X, Y, CurrentTime);
		XFlush(display);
		XCloseDisplay(display);
	}
}
