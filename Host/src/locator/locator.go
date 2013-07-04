package locator

import (
	"bytes"
	"encoding/binary"
	"fmt"
	"net"
	"os"
	"unicode/utf16"
)

const (
	key  = 0x3FB3BFEF
	port = ":28533"
)

func Run() {
	for {
		udpAddr, err := net.ResolveUDPAddr("udp", port)
		if err != nil {
			fmt.Println(err.Error())
			return
		}
		conn, err := net.ListenUDP("udp", udpAddr)

		if err != nil {
			fmt.Println(err.Error())
			return
		}

		handler(conn)
	}
}

func handler(conn *net.UDPConn) {
	idResponse := createResponse()
	for {
		b := make([]byte, 4)
		n, host, err := conn.ReadFromUDP(b)
		if err != nil {
			return
		} else if n < 4 {
			continue
		}
		if validateData(b) {
			conn.WriteToUDP(idResponse, host)
		}
	}
}

func validateData(buf []byte) bool {
	var result int32
	err := binary.Read(bytes.NewBuffer(buf), binary.BigEndian, &result)
	if err != nil || result != key {
		return false
	}
	return true
}

func createResponse() []byte {
	buf := bytes.NewBuffer(make([]byte, 0))
	theKey := int32(key)
	name, _ := os.Hostname()
	theSize := uint16(len(name)*2) + 2
	u16 := utf16.Encode([]rune(name))
	var pre1, pre2 byte = 0xFF, 0xFE
	binary.Write(buf, binary.BigEndian, &theKey)
	binary.Write(buf, binary.BigEndian, &theSize)
	binary.Write(buf, binary.BigEndian, &pre1)
	binary.Write(buf, binary.BigEndian, &pre2)
	binary.Write(buf, binary.LittleEndian, &u16)
	return buf.Bytes()
}
