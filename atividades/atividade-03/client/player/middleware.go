package player

import (
	"fmt"
	"github.com/dgmneto/middleware/atividades/atividade-03/game"
)

type Player struct{
	Port string
}

type MakeMoveRequest struct{
	Board *game.Board
}

type MakeMoveResponse struct{
	Move *game.Move
}

func (p *Player) MakeMoveEndpoint(request *MakeMoveRequest, response *MakeMoveResponse) error {
	move, err := MakeMove(request.Board)
	if err != nil{
		return err
	}

	response.Move = move
	return nil
}

type EndGameRequest struct{
	Winner int
}

type EndGameResponse struct{

}

func (p *Player) EndGameEndpoint(request *EndGameRequest, response *EndGameResponse) error {
	fmt.Printf("Game Over. Winner: %d\n", request.Winner)
	return nil
}

