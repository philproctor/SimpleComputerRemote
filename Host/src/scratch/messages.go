package scratch

import (
	"bytes"
	"encoding/binary"
	"unicode/utf16"
)

type ScratchMessage struct {
	Channel                          byte
	PrimaryCommand, SecondaryCommand int16
	flags                            uint16
	data                             *bytes.Buffer
	sessionid                        uint32
}

func MakeMessage() (message ScratchMessage) {
	return ScratchMessage{0, 0, 0, 0, bytes.NewBuffer(make([]byte, 0)), 0}
}

func (msg *ScratchMessage) GetBytes() []byte {
	return msg.data.Bytes()
}

func (msg *ScratchMessage) GetSize() uint16 {
	return uint16(len(msg.data.Bytes()))
}

func (msg *ScratchMessage) GetInt16() (result int16, err error) {
	err = binary.Read(msg.data, binary.BigEndian, &result)
	return
}

func (msg *ScratchMessage) GetInt32() (result int32, err error) {
	err = binary.Read(msg.data, binary.BigEndian, &result)
	return
}

func (msg *ScratchMessage) GetUint16() (result uint16, err error) {
	err = binary.Read(msg.data, binary.BigEndian, &result)
	return
}

func (msg *ScratchMessage) GetUint32() (result uint32, err error) {
	err = binary.Read(msg.data, binary.BigEndian, &result)
	return
}

func (msg *ScratchMessage) GetByte() (result byte, err error) {
	err = binary.Read(msg.data, binary.BigEndian, &result)
	return
}

func (msg *ScratchMessage) GetString() (result string, err error) {
	size, err := msg.GetUint16()
	if err != nil {
		return
	}

	key1, err := msg.GetByte()
	key2, err := msg.GetByte()

	u16 := make([]uint16, (size-2)/2)
	if key1 == 0xFF && key2 == 0xFE {
		err = binary.Read(msg.data, binary.LittleEndian, &u16)
	} else if key1 == 0xFE && key2 == 0xFF {
		err = binary.Read(msg.data, binary.BigEndian, &u16)
	}
	if err == nil {
		result = string(utf16.Decode(u16))
	}
	return
}

func (msg *ScratchMessage) PackInt16(toAdd int16) (err error) {
	err = binary.Write(msg.data, binary.BigEndian, &toAdd)
	return
}

func (msg *ScratchMessage) PackInt32(toAdd int32) (err error) {
	err = binary.Write(msg.data, binary.BigEndian, &toAdd)
	return
}

func (msg *ScratchMessage) PackUint16(toAdd uint16) (err error) {
	err = binary.Write(msg.data, binary.BigEndian, &toAdd)
	return
}

func (msg *ScratchMessage) PackUint32(toAdd uint32) (err error) {
	err = binary.Write(msg.data, binary.BigEndian, &toAdd)
	return
}

func (msg *ScratchMessage) PackByte(toAdd byte) (err error) {
	err = binary.Write(msg.data, binary.BigEndian, &toAdd)
	return
}

func (msg *ScratchMessage) PackBytes(toAdd []byte) (err error) {
	err = binary.Write(msg.data, binary.BigEndian, &toAdd)
	return
}

func (msg *ScratchMessage) PackString(toAdd string) (err error) {
	msg.PackUint16(uint16(len(toAdd)*2) + 2)
	msg.PackByte(0xFF)
	msg.PackByte(0xFE)
	u16 := utf16.Encode([]rune(toAdd))
	err = binary.Write(msg.data, binary.LittleEndian, &u16)
	return
}
