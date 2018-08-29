package helper

import (
	"strings"
	"strconv"
	"bytes"
	"fmt"
)

type Pair struct {
	x,y int
}

func NewPair(x, y int) Pair{
	return Pair{x,y,}
}

func PairFromByteArray(in []byte) Pair {
	fmt.Println("recieved:" + string(in))
	ints := strings.Split(string(in), " ")
	x, _:= strconv.Atoi(ints[0])
	y, _:= strconv.Atoi(ints[1])

	return Pair{
		x: x,
		y: y,
	}
}

func (p Pair) ToByteArray() []byte {
	var buffer bytes.Buffer

	buffer.WriteString(strconv.Itoa(p.x))
	buffer.WriteString(" ")
	buffer.WriteString(strconv.Itoa(p.y))

	return buffer.Bytes()
}
