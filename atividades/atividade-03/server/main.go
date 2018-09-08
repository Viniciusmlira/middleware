package main

import (
	"net"
	"fmt"
	"github.com/dgmneto/middleware/atividades/atividade-03/helper"
)

type stats struct {
	player1, player2, tie int
}

func (s *stats) inc(player int) {
	if player == 1 {
		s.player1++
	} else if player == 2 {
		s.player2++
	} else {
		s.tie ++
	}
}
func (s *stats) incTie() {
	s.tie ++
}

var statistics stats

func main() {
	playerChan := make(chan helper.Player)
	go getPlayersUDP(playerChan)
	players := make(map[int]helper.Player)
	for i := 1; i < 3; i++ {
		players[i] = <-playerChan
	}

	defer func() {
		for _, players:= range players{
			players.Close()
		}
	}()

	for i:=0; i<100; i++{
		playGame(players)
	}
	fmt.Println(statistics)
}

func playGame(players map[int]helper.Player) {
	board := helper.Board{}
	var winner int
	hasWinner := false
	for i := 0; i<9 && !hasWinner; i++ {
		playerIdx := 1 + (i%2)
		//time.Sleep(500 * time.Millisecond)
		p := players[playerIdx]
		p.SendBool(false) // send that game is not over
		p.SendBoard(board)

		move := getValidMove(board, p)
		board.SetPlayer(move, playerIdx)
		win := board.CheckWin(playerIdx)
		hasWinner = win
		p.SendBool(win)
		if win {
			winner = playerIdx
			statistics.inc(playerIdx)
			break
		}
		//fmt.Println(board)
	}
	for i, p := range players {
		if i != winner {
			//fmt.Println()
			p.SendBool(true)      // send that game is over
			p.SendBool(hasWinner) // send if player lost
		}
	}
	if !hasWinner {
		statistics.incTie()
	}
}

func getValidMove(board helper.Board,p helper.Player) helper.Pair {
	validMove := false
	var move helper.Pair
	for !validMove{
		move = p.GetMove()
		validMove = board.Valid(move)
		p.SendBool(validMove)
	}
	//fmt.Println("move: ",move)
	return move
}

func getPlayersTCP(c chan helper.Player) {
	ln, err := net.Listen("tcp", ":8081")
	if err != nil {
		panic(1)
	}


	for {
		conn, err := ln.Accept()
		if err != nil {
			panic(2)
		}
		p := helper.NewPlayerWithBuffers(conn, ';')
		c <- p
	}
}

func getPlayersUDP(c chan helper.Player) {
	ServerAddr, err := net.ResolveUDPAddr("udp",":8083")
	if err != nil {
		panic(err)
	}

	/* Now listen at selected port */
	ServerConn, err := net.ListenUDP("udp", ServerAddr)
	if err != nil {
		panic(err)
	}
	defer ServerConn.Close()

	buf := make([]byte, 1024)

	m := make(map[int]*helper.PlayerUdp)

	outChan := make(chan helper.UdpMessage)

	closeChan := make(chan bool, 1)
	go sendUdpMessages(outChan, ServerConn, closeChan)

	for {
		select {
		case _ = <- closeChan:
			ServerConn.Close()
			break
		default:
			n,addr,err := ServerConn.ReadFromUDP(buf)
			//fmt.Println("Received ",string(buf[0:n]), " from ",addr)

			if err != nil {
				panic(err)
			}
			player, ok := m[addr.Port]
			if !ok {
				player = helper.NewPlayerUdp(addr, outChan , make(chan string), ';')
				m[addr.Port] = player
				c <- player
			} else{
				player.InChan <- string(buf[0:n])
			}
		}
	}
}

func sendUdpMessages(c chan helper.UdpMessage, conn *net.UDPConn, close chan bool) {
	for {
		message := <-c
		//fmt.Println(message)
		if message.Addr == nil {
			close<-true
			break
		} else {
			conn.WriteTo([]byte(message.Str),message.Addr)
		}
	}
	for {
		<-c
	}
}
