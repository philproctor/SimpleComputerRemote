package server

import (
	"osimpl"
	"scratch"
)

const (
	volumeDown = 1
	volumeUp   = 2
)

func processMixer(message *scratch.ScratchMessage) {
	switch message.SecondaryCommand {
	case volumeDown:
		osimpl.VolumeDown()

	case volumeUp:
		osimpl.VolumeUp()
	}
}
