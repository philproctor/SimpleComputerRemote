package server

import (
	"scratch"
)

const (
	mouse    = 10
	keyboard = 20
)

func Run() {
	server := new(scratch.Server)
	server.SetPort(28532)
	server.SetKey(0x3FB3BFEF)
	server.SetVersion(1, 0)
	server.SetReceiver(new(remoteServer))
	server.StartServer()
}

type remoteServer struct {
	Sender scratch.Sender
}

func (ts *remoteServer) MessageReceived(message scratch.ScratchMessage) {
	switch message.PrimaryCommand {
	case mouse:
		processMouse(&message)

	case keyboard:
		processKeyboard(&message)
	}
}

func (ts *remoteServer) Construct(sender scratch.Sender) scratch.Receiver {
	return &remoteServer{Sender: sender}
}

func (ts *remoteServer) NewConnection() {
}

func (ts *remoteServer) ConnectionLost() {
}
