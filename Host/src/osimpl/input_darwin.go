// +build darwin

package osimpl

//#cgo LDFLAGS: -framework ApplicationServices
//#include "input_darwin.c"
import "C"

func SendKeycode(keyCode uint8) {
	C.SendKeycode(C.char(keyCode))
}

func SendKeyboardString(toSend string) {
	for _, s := range []rune(toSend) {
		C.SendString(C.short(s))
	}
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
	C.SendLeftDown()
}

func RightDown() {
	C.SendRightDown()
}

func LeftUp() {
	C.SendLeftUp()
}

func RightUp() {
	C.SendRightUp()
}

func MiddleDown() {
	//Nothing on mac
}

func MiddleUp() {
	//Nothing on mac
}

func ScrollDown() {
	C.ScrollDown()
}

func ScrollUp() {
	C.ScrollUp()
}

func VolumeDown() {

}

func VolumeUp() {

}
