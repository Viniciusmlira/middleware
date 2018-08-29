package main

import (
	"github.com/dgmneto/middleware/atividades/atividade-02/helper"
	"net"
	"fmt"
	"math/rand"
	"time"
)

var r *rand.Rand
var temp int

func nextPair() helper.Pair{

	temp++
	temp%=9
	return helper.NewPair(temp%3, temp/3)
}

func main()  {
	conn, err := net.Dial("tcp", "localhost:8081")
	if err != nil {
		panic(1)
	}
	fmt.Println("Connected")
	c := helper.NewClientWithBuffersFromConn(conn, ';')
	fmt.Println("Created client")

	s1 := rand.NewSource(time.Now().UnixNano())
	r = rand.New(s1)
	for i := 0; i< 100; i++{
		play(c)
	}
}

func play(c helper.Client) {
	win := false
	gameOver := false
	i := 0
	for !gameOver {
		i++
		fmt.Println("new turn:", i)
		gameOver = c.GetBool()
		fmt.Println("game status:", gameOver)
		if gameOver {
			break
		}
		str := c.GetBoardString()
		fmt.Println("board: ", str)
		validMove := false
		for !validMove {
			//move := helper.NewPair(r.Intn(3), r.Intn(3))
			move := nextPair()
			fmt.Println("move: ",move)
			c.SendMove(move)
			validMove = c.GetBool()
			fmt.Println("renatão:",validMove)
		}
		win = c.GetBool() //check if player won
		gameOver = gameOver || win
	}
	if win {
		fmt.Println("you won")
	} else {
		if c.GetBool() {
			fmt.Println("you lost")
		} else {
			fmt.Println("you tied")
		}
	}

}
