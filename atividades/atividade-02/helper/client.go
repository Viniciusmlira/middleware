package helper

import (
	"bufio"
	"net"
	"strings"
	"strconv"
)

type Client interface {
	GetBool() bool
	GetBoardString() string
	SendMove(Pair)
}

type ClientWithBuffers struct {
	Reader    *bufio.Reader
	Writer    *bufio.Writer
	delimiter byte
}

func (c *ClientWithBuffers) GetBool() bool{
	var b bool
	str, err:= c.Reader.ReadString(c.delimiter)
	if err!= nil {
		panic(1)
	}
	//fmt.Println("got bol:",str)
	b, err = strconv.ParseBool(strings.Trim(str, string(c.delimiter)))
	if err != nil {
		panic(1)
	}
	return b
}

func (c *ClientWithBuffers) GetBoardString() string{
	str, _ := c.Reader.ReadString(c.delimiter)
	return strings.Trim(str, string(c.delimiter))
}

func (c *ClientWithBuffers) SendMove(move Pair) {
	//fmt.Println("xallaa:", move)
	str := move.ToByteArray()
	c.Writer.Write(append(str, c.delimiter))
	c.Writer.Flush()
}

func NewClientWithBuffersFromConn(conn net.Conn, delim byte) Client{
	return &ClientWithBuffers{
		Reader:    bufio.NewReader(conn),
		Writer:    bufio.NewWriter(conn),
		delimiter: delim,
	}
}

