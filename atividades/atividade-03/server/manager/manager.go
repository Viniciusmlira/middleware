package manager

import (
	"fmt"
	"time"
	"github.com/dgmneto/middleware/atividades/atividade-03/client/player"
)

func PlayGameRepeated(manager *Manager, times int){
	logger := &Timer{0}

	for i := 0; i < times; i++{
		PlayGame(manager, logger)

		//fmt.Printf("\n-- NEW GAME --\n")
		//fmt.Printf("%s\n", manager.Board.ToString())
		//fmt.Printf("\nPlayer %d WON!\n", manager.Board.GameOver())

		manager.Board.Restart()
	}

	fmt.Printf("%d\n", int64(logger.Elapsed/time.Millisecond))
}

func PlayGame(manager *Manager, logger *Timer) {
	for i := 0; i < 9 && manager.Board.GameOver() == 0; i++ {

		request := &player.MakeMoveRequest{manager.Board}
		var response player.MakeMoveResponse

		start := time.Now()
		manager.Players[i%2].Call("Player.MakeMoveEndpoint", request, &response)
		end := time.Now()
		logger.Count(start, end)
		
		manager.Board.MakeMove(response.Move, (i%2)+1)
	}

	request := &player.EndGameRequest{manager.Board.GameOver()}
	var response player.EndGameResponse

	for i := 0; i < 2; i++{
		start := time.Now()
		manager.Players[i].Call("Player.EndGameEndpoint", request, &response)
		end := time.Now()
		logger.Count(start, end)
	}	
}
