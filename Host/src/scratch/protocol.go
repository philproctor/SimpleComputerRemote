package scratch

import (
	"bytes"
	"encoding/binary"
	"net"
)

type session struct {
	sessioninfo
	conn   net.Conn
	sessid uint32
}

func (sess *session) receiveFrame() (message ScratchMessage, err error) {
	var head header
	headBytes, err := readUntil(sess.conn, 19)
	if err != nil {
		return
	}

	binary.Read(bytes.NewBuffer(headBytes[:]), binary.BigEndian, &head)
	message.Channel = head.Channel
	message.PrimaryCommand = head.PrimaryCommand
	message.SecondaryCommand = head.SecondaryCommand
	message.flags = head.Flags
	message.sessionid = head.SessionId
	data, err := readUntil(sess.conn, head.Length)
	message.data = bytes.NewBuffer(data)

	return
}

func readUntil(conn net.Conn, size uint16) (buf []byte, err error) {
	buf = make([]byte, size)
	var offset, n int
	for size > 0 {
		n, err = conn.Read(buf[offset:size])
		if err != nil {
			return
		}
		size = size - uint16(n)
		offset += n
	}
	return
}

func (sess session) Send(message ScratchMessage) {
	head := header{sess.key, sess.vMajor, sess.vMinor, sess.sessid, message.flags, message.Channel, message.PrimaryCommand, message.SecondaryCommand, message.GetSize()}
	buf := bytes.NewBuffer(make([]byte, 0))
	binary.Write(buf, binary.BigEndian, &head)
	toWrite := message.GetBytes()
	binary.Write(buf, binary.BigEndian, &toWrite)
	sess.conn.Write(buf.Bytes())
}

func (sess *session) pong() {
	message := MakeMessage()
	message.PrimaryCommand = internalPingPong
	message.SecondaryCommand = internalSecondaryPong
	message.flags = flagInternal
	sess.Send(message)
}

func (sess *session) sendConfiguration() {
	message := MakeMessage()
	message.PrimaryCommand = internalConfig
	message.flags = flagInternal
	sess.Send(message)
}
