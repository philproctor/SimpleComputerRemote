package scratch

type sessioninfo struct {
	port           int
	key            int32
	vMajor, vMinor byte
	receiver       Receiver
}

func (sess *sessioninfo) SetKey(Key int32) {
	sess.key = Key
}

func (sess *sessioninfo) SetVersion(Major, Minor byte) {
	sess.vMajor = Major
	sess.vMinor = Minor
}

func (sess *sessioninfo) SetPort(Port int) {
	sess.port = Port
}

func (sess *sessioninfo) SetReceiver(receiver Receiver) {
	sess.receiver = receiver
}
