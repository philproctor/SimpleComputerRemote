package main

import (
	"locator"
	"server"
	"webui"
)

func main() {
	go server.Run()
	go locator.Run()
	webui.Start()
}
