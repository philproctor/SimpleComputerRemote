package scratch

import (
	"fmt"
	"math/rand"
	"net"
	"time"
)

type Server struct {
	sessioninfo
}

func (svr Server) StartServer() {
	rand.Seed(time.Now().UnixNano())
	go func() {
		tcpAddr, err := net.ResolveTCPAddr("tcp4", fmt.Sprintf(":%d", svr.port))
		if err != nil {
			fmt.Println(err.Error())
			return
		}
		listener, err := net.ListenTCP("tcp", tcpAddr)

		if err != nil {
			fmt.Println(err.Error())
			return
		}

		svr.handler(listener)
	}()

	return
}

func (svr *Server) handler(listener *net.TCPListener) {
	defer listener.Close()
	for {
		conn, err := listener.Accept()
		if err != nil {
			time.Sleep(1 * time.Second)
			svr.StartServer()
			return
		}

		go svr.handleClient(conn)
	}
}

func (svr Server) handleClient(conn net.Conn) {
	defer conn.Close()
	opened := false
	sess := session{svr.sessioninfo, conn, rand.Uint32()}
	sess.sendConfiguration()
	svr.receiver = svr.receiver.Construct(sess)

	for {
		message, err := sess.receiveFrame()

		if err != nil {
			if opened {
				svr.receiver.ConnectionLost()
			}
			return
		}

		if message.flags&flagInternal != 0 {
			switch message.PrimaryCommand {
			case internalConfig:
				if message.SecondaryCommand == internalSecondaryConfirm {
					opened = true
					svr.receiver.NewConnection()
				}

			case internalPingPong:
				if message.SecondaryCommand == internalSecondaryPing {
					sess.pong()
				}
			}
		} else {
			if message.sessionid != sess.sessid {
				if opened {
					svr.receiver.ConnectionLost()
				}
				return
			} else if opened {
				svr.receiver.MessageReceived(message)
			}
		}
	}
}
