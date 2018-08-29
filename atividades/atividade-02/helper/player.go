package helper

import (
	"bufio"
	"strconv"
	"strings"
	"fmt"
)

type Player interface {
	SendBool(bool)
	SendBoard(board Board)
	GetMove() Pair
}

type PlayerWithBuffers struct {
	Reader    *bufio.Reader
	Writer    *bufio.Writer
	Delimiter byte
}

func (p *PlayerWithBuffers) SendBool(message bool) {
	str := strconv.AppendBool([]byte{}, message)
	str1 := string(append(str, p.Delimiter))
	fmt.Println("bool:", str1)
	_, err := p.Writer.WriteString(str1)
	p.Writer.Flush()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}

}

func (p *PlayerWithBuffers) SendBoard(board Board) {
	str := board.ToPrint()
	str1 := string(append([]byte(str), p.Delimiter))
	fmt.Println("board:\n", str1)
	_, err := p.Writer.WriteString(str1)
	p.Writer.Flush()
	if err != nil {
		fmt.Println(err)
		panic(1)
	}
}

func (p *PlayerWithBuffers) GetMove() Pair {
	str, _ := p.Reader.ReadString(p.Delimiter)
	fmt.Println("messege received: ", str)
	return PairFromByteArray([]byte(strings.Trim(str, string(p.Delimiter))))
}
