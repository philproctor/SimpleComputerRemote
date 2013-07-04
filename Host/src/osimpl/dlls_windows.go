// +build windows

package osimpl

import (
	"syscall"
)

const (
	MOVE       = 0x00000001
	LEFTDOWN   = 0x00000002
	LEFTUP     = 0x00000004
	RIGHTDOWN  = 0x00000008
	RIGHTUP    = 0x00000010
	MIDDLEDOWN = 0x00000020
	MIDDLEUP   = 0x00000040
	XDOWN      = 0x00000080
	XUP        = 0x00000100
	ABSOLUTE   = 0x00008000
	WHEEL      = 0x00000800

	KEYEVENTF_EXTENDEDKEY = 0x0001
	KEYEVENTF_KEYUP       = 0x0002
	KEYEVENTF_UNICODE     = 0x0004
	KEYEVENTF_SCANCODE    = 0x0008

	SCROLLUP_INCREMENT   = 120
	SCROLLDOWN_INCREMENT = -120
)

var (
	user32, _        = syscall.LoadLibrary("user32.dll")
	mouseEvent, _    = syscall.GetProcAddress(user32, "mouse_event")
	keyboardEvent, _ = syscall.GetProcAddress(user32, "keybd_event")
)

// type, wVk, wScan, dwFlags, dwExtraInfo
