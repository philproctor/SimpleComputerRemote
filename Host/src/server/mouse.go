package server

import (
	"osimpl"
	"scratch"
)

const (
	mouseMove  = 1
	mouseLeft  = 2
	mouseRight = 3
	leftDown   = 4
	leftUp     = 5
	rightDown  = 6
	rightUp    = 7
	middleDown = 8
	middleUp   = 9
	scrollDown = 10
	scrollUp   = 11
)

func processMouse(message *scratch.ScratchMessage) {
	switch message.SecondaryCommand {
	case mouseMove:
		x, err := message.GetInt32()
		if err != nil {
			return
		}

		y, err := message.GetInt32()
		if err != nil {
			return
		}

		osimpl.MoveMouse(int(x), int(y))

	case mouseLeft:
		osimpl.LeftClick()

	case mouseRight:
		osimpl.RightClick()

	case leftDown:
		osimpl.LeftDown()

	case leftUp:
		osimpl.LeftUp()

	case rightDown:
		osimpl.RightDown()

	case rightUp:
		osimpl.RightUp()

	case middleDown:
		osimpl.MiddleDown()

	case middleUp:
		osimpl.MiddleUp()

	case scrollDown:
		osimpl.ScrollDown()

	case scrollUp:
		osimpl.ScrollUp()
	}
}
