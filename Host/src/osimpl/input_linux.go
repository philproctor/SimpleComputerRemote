// +build linux

package osimpl

//#cgo LDFLAGS: -lX11 -lXtst -lXext
//#include "input_linux.c"
import "C"

func SendKeycode(keyCode uint8) {
	C.SendKeycode(C.int(keyCode))
}

func SendKeyboardString(toSend string) {
	for _, s := range []rune(toSend) {
		sendKeyboardRune(s)
	}
}

func sendKeyboardRune(toSend rune) {
	C.SendKeysym(C.int(toSend))
}

func MoveMouse(X, Y int) {
	C.SendMouseMove(C.int(X), C.int(Y))
}

func LeftClick() {
	LeftDown()
	LeftUp()
}

func RightClick() {
	RightDown()
	RightUp()
}

func LeftDown() {
	C.SendClick(1, 1)
}

func RightDown() {
	C.SendClick(3, 1)
}

func LeftUp() {
	C.SendClick(1, 0)
}

func RightUp() {
	C.SendClick(3, 0)
}

func MiddleDown() {
	C.SendClick(2, 1)
}

func MiddleUp() {
	C.SendClick(2, 0)
}

func ScrollDown() {
	C.SendClick(5, 1)
	C.SendClick(5, 0)
}

func ScrollUp() {
	C.SendClick(4, 1)
	C.SendClick(4, 0)
}

func VolumeDown() {
	C.SendKeysym(C.int(0x1008ff11))
}

func VolumeUp() {
	C.SendKeysym(C.int(0x1008FF13))
}
