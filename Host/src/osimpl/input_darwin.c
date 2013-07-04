#include <ApplicationServices/ApplicationServices.h>
#include <unistd.h>
#include <Carbon/Carbon.h>

static inline void SendString(short theCode)
{
	UniChar unicodeString[1];
	unicodeString[0] = theCode;
	CGEventRef e = CGEventCreateKeyboardEvent(NULL, 0, true);
	CGEventKeyboardSetUnicodeString(e, 1, unicodeString);
	CGEventPost(kCGHIDEventTap, e);
	CFRelease(e);
}

static inline void SendKeycode(char keyCode)
{
	CGKeyCode kc = 0;
	switch (keyCode)
	{
		case 8:
			kc = kVK_Delete;
			break;

		case 13:
			kc = kVK_Return;
			break;
	}
	if (kc > 0)
	{
		CGEventRef e = CGEventCreateKeyboardEvent (NULL, kc, true);
    	CGEventPost(kCGSessionEventTap, e);
    	CFRelease(e);
    }
}

static inline CGPoint GetMouseLoc()
{
	CGEventRef ourEvent = CGEventCreate(NULL);
	CGPoint point = CGEventGetLocation(ourEvent);
    CFRelease(ourEvent);
    return point;
}

static inline void SendMouseMove(int X, int Y) {
	CGPoint point = GetMouseLoc();
	CGRect ssize = CGDisplayBounds(CGMainDisplayID());
	int newX = point.x + X, newY = point.y + Y;
	newX = newX < 0 ? 0 : newX;
	newY = newY < 0 ? 0 : newY;
	newX = newX > ssize.size.width - 1 ? ssize.size.width - 1 : newX;
	newY = newY > ssize.size.height - 2 ? ssize.size.height - 1 : newY;

	CGEventRef move = CGEventCreateMouseEvent(
        NULL, kCGEventMouseMoved,
        CGPointMake(newX, newY),
        kCGMouseButtonLeft // ignored
    );

    CGEventPost(kCGHIDEventTap, move);

    CFRelease(move);
}

static inline void SendLeftDown() {
	CGPoint point = GetMouseLoc();
	CGEventRef click = CGEventCreateMouseEvent(
        NULL, kCGEventLeftMouseDown,
        CGPointMake(point.x, point.y),
        kCGMouseButtonLeft
    );

    CGEventPost(kCGHIDEventTap, click);
    CFRelease(click);
}

static inline void SendLeftUp() {
	CGPoint point = GetMouseLoc();
	CGEventRef click = CGEventCreateMouseEvent(
        NULL, kCGEventLeftMouseUp,
        CGPointMake(point.x, point.y),
        kCGMouseButtonLeft
    );

    CGEventPost(kCGHIDEventTap, click);
    CFRelease(click);
}

static inline void SendRightDown() {
	CGPoint point = GetMouseLoc();
	CGEventRef click = CGEventCreateMouseEvent(
        NULL, kCGEventRightMouseDown,
        CGPointMake(point.x, point.y),
        kCGMouseButtonRight
    );

    CGEventPost(kCGHIDEventTap, click);
    CFRelease(click);
}

static inline void SendRightUp() {
	CGPoint point = GetMouseLoc();
	CGEventRef click = CGEventCreateMouseEvent(
        NULL, kCGEventRightMouseUp,
        CGPointMake(point.x, point.y),
        kCGMouseButtonRight
    );

    CGEventPost(kCGHIDEventTap, click);
    CFRelease(click);
}

static inline void ScrollDown() {
	CGEventRef scroll = CGEventCreateScrollWheelEvent(
        NULL, kCGScrollEventUnitLine,
        1, -4);
    
    CGEventPost(kCGHIDEventTap, scroll);
    CFRelease(scroll);
}

static inline void ScrollUp() {
    CGEventRef scroll = CGEventCreateScrollWheelEvent(
        NULL, kCGScrollEventUnitLine,
        1, 4);
    
    CGEventPost(kCGHIDEventTap, scroll);
    CFRelease(scroll);
}
