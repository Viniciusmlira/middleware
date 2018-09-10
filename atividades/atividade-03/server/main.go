package main

import (
	"net/rpc"
	"net/http"
	"time"
	"github.com/dgmneto/middleware/atividades/atividade-03/server/manager"
	"github.com/dgmneto/middleware/atividades/atividade-03/game"
)

func main(){
	board := &game.Board{[3]int{0,0,0},
						 [3]int{0,0,0},
						 [3]int{0,0,0}}

	players := []*rpc.Client{}

	manager := &manager.Manager{board, players}

	rpc.Register(manager)
    rpc.HandleHTTP()

	go http.ListenAndServe(":1234", nil)
	
	time.Sleep(1 * time.Hour)
}