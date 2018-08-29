package main

import (
	"net"
	"github.com/ccal1/middleware/helper"
	"github.com/ccal1/middleware/player"
	"bufio"
	"fmt"
	"time"
)

//type player struct {
//	moveReceiver chan helper.Pair
//	boardSender  chan helper.Board
//	boolSender   chan bool
//}

type stats struct {
	player1, player2, tie int
}

func (s stats) inc(player int) {
	if player == 1 {
		s.player1++
	} else if player == 2 {
		s.player2++
	} else {
		s.tie ++
	}
}
func (s stats) incTie() {
	s.tie ++
}

var statistics stats

func main() {
	playerChan := make(chan player.Player)
	go getPlayersTCP(playerChan)
	players := make(map[int]player.Player)
	for i := 1; i < 3; i++ {
		players[i] = <-playerChan
	}

	for i:=0; i<100; i++{
		playGame(players)
	}
	fmt.Println(statistics)
}

func playGame(players map[int]player.Player) {
	board := helper.Board{}
	var winner int
	hasWinner := false
	for i := 0; i<9 && !hasWinner; i++ {
		playerIdx := 1 + (i%2)
		time.Sleep(500 * time.Millisecond)
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
		fmt.Println(board)
	}
	fmt.Println("saiu!!!!")
	for i, p := range players {
		if i != winner {
			fmt.Println()
			p.SendBool(true)      // send that game is over
			p.SendBool(hasWinner) // send if player lost
		}
	}
	if !hasWinner {
		statistics.incTie()
	}
}

func getValidMove(board helper.Board,p player.Player) helper.Pair {
	validMove := false
	var move helper.Pair
	for !validMove{
		move = p.GetMove()
		validMove = board.Valid(move)
		p.SendBool(validMove)
	}
	fmt.Println("move: ",move)
	return move
}

func getPlayersTCP(c chan player.Player) {
	ln, err := net.Listen("tcp", ":8081")
	if err != nil {
		panic(1)
	}


	for {
		conn, err := ln.Accept()
		if err != nil {
			panic(2)
		}
		p := &player.PlayerWithBuffers{
			Reader: bufio.NewReader(conn),
			Writer: bufio.NewWriter(conn),
			Delimiter: ';',
		}
		c <- p
	}
}
//
//func handleConnection(conn net.Conn, p player) {
//
//	select {
//	case msg1 := <-p.boolSender:
//		fmt.Println("sending", msg1)
//		conn.Write(strconv.AppendBool([]byte{}, msg1))
//	}
//
//	go getMesseges(conn, p.moveReceiver)
//
//}
//
//func getMesseges(conn net.Conn, c chan helper.Pair) {
//	for {
//		str := make([]byte, 9)
//		conn.Read(str)
//		pair := helper.PairFromByteArray(str)
//		c <- pair
//	}
//}
