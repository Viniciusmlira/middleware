package player

import (
	"errors"
	"github.com/dgmneto/middleware/atividades/atividade-03/game"
)

func MakeMove(board *game.Board) (*game.Move, error){
	for i := 0; i < 3; i++{
		for j := 0; j < 3; j++{
			move := &game.Move{i, j}
			if board.IsMoveValid(move){
				return move, nil
			}
		}
	}
	return nil, errors.New("no moves available")
}