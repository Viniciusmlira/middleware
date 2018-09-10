package manager

import (
	"fmt"
	"github.com/dgmneto/middleware/atividades/atividade-03/client/player"
)

func PlayGameRepeated(manager *Manager, times int){
	for i := 0; i < times; i++{
		PlayGame(manager)

		fmt.Printf("\n-- NEW GAME --\n")
		fmt.Printf("%s\n", manager.Board.ToString())
		fmt.Printf("\nPlayer %d WON!\n", manager.Board.GameOver())

		manager.Board.Restart()
	}
}

func PlayGame(manager *Manager) {
	for i := 0; i < 9 && manager.Board.GameOver() == 0; i++ {

		request := &player.MakeMoveRequest{manager.Board}
		var response player.MakeMoveResponse

		err := manager.Players[i%2].Call("Player.MakeMoveEndpoint", request, &response)
		if err != nil {
			fmt.Printf("dialing: %s", err.Error())
			break
		}

		manager.Board.MakeMove(response.Move, (i%2)+1)
	}
}
