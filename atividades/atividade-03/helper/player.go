package helper

import (
	"bufio"
	"strconv"
	"strings"
	"net"
)

type Player interface {
	SendBool(bool)
	SendBoard(board Board)
	GetMove() Pair
	Close()
}

type PlayerWithBuffers struct {
	Reader    *bufio.Reader
	Writer    *bufio.Writer
	Delimiter byte
	Conn      net.Conn
}

func NewPlayerWithBuffers(conn net.Conn, delimiter byte) *PlayerWithBuffers {
	return &PlayerWithBuffers{
		Reader: bufio.NewReader(conn),
		Writer: bufio.NewWriter(conn),
		Delimiter: delimiter,
		Conn: conn,
	}
}

func (p *PlayerWithBuffers) Close() {
	p.Conn.Close()
}

func (p *PlayerWithBuffers) SendBool(message bool) {
	str := strconv.AppendBool([]byte{}, message)
	str1 := string(append(str, p.Delimiter))
	//fmt.Println("bool:", str1)
	_, err := p.Writer.WriteString(str1)
	p.Writer.Flush()
	if err != nil {
		panic(err)
	}
}

func (p *PlayerWithBuffers) SendBoard(board Board) {
	str := board.ToPrint()
	str1 := string(append([]byte(str), p.Delimiter))
	//fmt.Println("board:\n", str1)
	_, err := p.Writer.WriteString(str1)
	p.Writer.Flush()
	if err != nil {
		panic(err)
	}
}

func (p *PlayerWithBuffers) GetMove() Pair {
	str, _ := p.Reader.ReadString(p.Delimiter)
	//fmt.Println("messege received: ", str)
	return PairFromByteArray([]byte(strings.Trim(str, string(p.Delimiter))))
}

type UdpMessage struct {
	Addr *net.UDPAddr
	Str  string
}

type PlayerUdp struct {
	Addr      *net.UDPAddr
	OutChan   chan UdpMessage
	InChan    chan string
	Delimiter byte
}

func NewPlayerUdp(addr *net.UDPAddr, outChan chan UdpMessage, inChan chan string, delimiter byte) *PlayerUdp {
	return &PlayerUdp{
		Addr:      addr,
		OutChan:   outChan,
		InChan:    inChan,
		Delimiter: delimiter,
	}
}

func (p *PlayerUdp) send(str string) {
	p.OutChan <- UdpMessage{p.Addr, str}
}

func (p *PlayerUdp) SendBool(message bool) {
	str := strconv.AppendBool([]byte{}, message)
	str1 := string(append(str, p.Delimiter))
	p.send(str1)
}

func (p *PlayerUdp) SendBoard(board Board) {
	str := board.ToPrint()
	str1 := string(append([]byte(str), p.Delimiter))
	p.send(str1)
}

func (p *PlayerUdp) GetMove() Pair {
	str := <-p.InChan
	//fmt.Println("getmove:",str)
	return PairFromByteArray([]byte(strings.Trim(str, string(p.Delimiter))))
}

func (p *PlayerUdp) Close() {
	p.OutChan <- UdpMessage{Addr: nil,}
}
