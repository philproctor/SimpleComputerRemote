// +build windows

package osimpl

import (
	"syscall"
)

func SendKeycode(keyCode uint8) {
	var dwFlags uint = KEYEVENTF_EXTENDEDKEY
	syscall.Syscall6(uintptr(keyboardEvent), 0, uintptr(keyCode), 0x45, uintptr(dwFlags), 0, 0, 0)
	dwFlags |= KEYEVENTF_KEYUP
	syscall.Syscall6(uintptr(keyboardEvent), 0, uintptr(keyCode), 0x45, uintptr(dwFlags), 0, 0, 0)
}

func SendKeyboardString(toSend string) {
	for _, s := range []rune(toSend) {
		sendKeyboardRune(s)
	}
}

func sendKeyboardRune(toSend rune) {
	var dwFlags uint = KEYEVENTF_UNICODE
	syscall.Syscall6(uintptr(keyboardEvent), 0, 0, uintptr(toSend), uintptr(dwFlags), 0, 0, 0)
	dwFlags |= KEYEVENTF_KEYUP
	syscall.Syscall6(uintptr(keyboardEvent), 0, 0, uintptr(toSend), uintptr(dwFlags), 0, 0, 0)
}

func MoveMouse(X, Y int) {
	syscall.Syscall6(uintptr(mouseEvent), 0, MOVE, uintptr(X), uintptr(Y), 0, 0, 0)
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
	syscall.Syscall6(uintptr(mouseEvent), 0, LEFTDOWN, 0, 0, 0, 0, 0)
}

func RightDown() {
	syscall.Syscall6(uintptr(mouseEvent), 0, RIGHTDOWN, 0, 0, 0, 0, 0)
}

func LeftUp() {
	syscall.Syscall6(uintptr(mouseEvent), 0, LEFTUP, 0, 0, 0, 0, 0)
}

func RightUp() {
	syscall.Syscall6(uintptr(mouseEvent), 0, RIGHTUP, 0, 0, 0, 0, 0)
}

func MiddleDown() {
	syscall.Syscall6(uintptr(mouseEvent), 0, MIDDLEDOWN, 0, 0, 0, 0, 0)
}

func MiddleUp() {
	syscall.Syscall6(uintptr(mouseEvent), 0, MIDDLEUP, 0, 0, 0, 0, 0)
}

func ScrollDown() {
	val := SCROLLDOWN_INCREMENT
	syscall.Syscall6(uintptr(mouseEvent), 0, WHEEL, 0, 0, uintptr(val), 0, 0)
}

func ScrollUp() {
	val := SCROLLUP_INCREMENT
	syscall.Syscall6(uintptr(mouseEvent), 0, WHEEL, 0, 0, uintptr(val), 0, 0)
}
