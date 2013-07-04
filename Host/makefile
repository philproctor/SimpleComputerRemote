GO=go
BUILD=$(GO) build -a
RUN=$(GO) run -gcflags "-N -l"
HOSTOS=$(shell go env GOHOSTOS)
HOSTARCH=$(shell go env GOARCH)
SRCDIR=src/
SRCMAIN=Rekap\ Remote\ Server.go
SRCPATH=$(SRCDIR)$(SRCMAIN)
BINDIR=bin/
BINFILE=server-$(HOSTOS)-$(HOSTARCH)
ifeq ($(HOSTOS),windows)
BINEXT=.exe
EXTOPTS=-ldflags "-s -Hwindowsgui"
else
BINEXT=.out
EXTOPTS=-ldflags "-s"
endif
BINPATH=$(BINDIR)$(BINFILE)$(BINEXT)

export GOPATH=$(CURDIR)/

all:
	$(BUILD) $(EXTOPTS) -o $(BINPATH) $(SRCPATH)

package: all
	cp $(BINPATH) $(BINDIR)/package.out

run:
	$(RUN) $(SRCPATH)
