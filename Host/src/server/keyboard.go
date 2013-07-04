package server

import (
	"osimpl"
	"scratch"
)

const (
	keyCode   = 1
	keyString = 2
)

func processKeyboard(message *scratch.ScratchMessage) {
	switch message.SecondaryCommand {
	case keyCode:
		bte, err := message.GetInt32()
		if err == nil {
			osimpl.SendKeycode(uint8(bte))
		}

	case keyString:
		str, err := message.GetString()
		if err == nil {
			osimpl.SendKeyboardString(str)
		}
	}
}
