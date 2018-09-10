package main

import (
	"os"
	"net/rpc"
	"net/http"
	"fmt"
	"time"
	"github.com/dgmneto/middleware/atividades/atividade-03/client/player"
	"github.com/dgmneto/middleware/atividades/atividade-03/server/manager"
)

func main(){
	port :=	os.Args[1]
	player := &player.Player{port}

	rpc.Register(player)
    rpc.HandleHTTP()

	go http.ListenAndServe(":"+port, nil)
	
	server, err := rpc.DialHTTP("tcp", ":1234")
    if err != nil {
        fmt.Printf("dialing: %s", err.Error())
	}

	request := &manager.RegisterPlayerRequest{port}
	var response manager.RegisterPlayerResponse
	
	err = server.Call("Manager.RegisterPlayerEndpoint", request, &response)
	if err != nil {
        fmt.Printf("dialing: %s", err.Error())
	}

	time.Sleep(1 * time.Hour)
}