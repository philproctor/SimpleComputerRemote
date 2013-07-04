package scratch

type header struct {
	Key                              int32
	VMajor, VMinor                   byte
	SessionId                        uint32
	Flags                            uint16
	Channel                          byte
	PrimaryCommand, SecondaryCommand int16
	Length                           uint16
}

type Sender interface {
	Send(ScratchMessage)
}

type Receiver interface {
	Construct(Sender) Receiver
	NewConnection()
	MessageReceived(ScratchMessage)
	ConnectionLost()
}

const (
	internalConfig           = 1
	internalPingPong         = 10
	internalSecondaryConfirm = 1
	internalSecondaryPing    = 1
	internalSecondaryPong    = 2
	flagInternal             = 0x0001
	flagEncrypted            = 0x0002
	flagCompressed           = 0x0004
	flagPartial              = 0x0008
)
