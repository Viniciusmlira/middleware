package manager

import (
	"net/rpc"
	"github.com/dgmneto/middleware/atividades/atividade-03/game"
)

type Manager struct{
	Board *game.Board
	Players []*rpc.Client
}

type RegisterPlayerRequest struct{
	Port string
}

type RegisterPlayerResponse struct {

}

func (m *Manager) RegisterPlayerEndpoint(request *RegisterPlayerRequest, response *RegisterPlayerResponse) error {
	client, err := rpc.DialHTTP("tcp", ":"+request.Port)
    if err != nil {
        return err
	}
	
	m.Players = append(m.Players, client)

	if len(m.Players) == 2{
		PlayGameRepeated(m, 100)
	}

	return nil
}

