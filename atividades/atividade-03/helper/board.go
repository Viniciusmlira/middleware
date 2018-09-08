package helper

import (
	"bytes"
	"strconv"
	"strings"
)

type Board [3][3]int

func (b *Board) ToByteArray() []byte {
	var buffer bytes.Buffer
	for elm := range b {
		buffer.WriteString(strconv.Itoa(elm))
		buffer.WriteString(" ")
	}
	return buffer.Bytes()
}

func (b *Board) ToPrint() string {
	s := make([]byte, 12)
	for i := 0; i < 3; i++ {
		for j:= 0; j<3; j++{
			idx := 4*i + j
			if b[i][j] == 0 {
				s[idx] = '_'
			} else {
				if b[i][j] & 1 == 1 {
					s[idx] = 'x'
				} else {
					s[idx] = 'o'
				}
			}
		}
		s[4*(i+1) - 1] = '\n'
	}
	return string(s)
}

func FromByteArray(array []byte) Board {
	ints := strings.Split(string(array), " ")
	b := Board{}
	var err error
	for i := 0; i<3; i++{
		for j := 0; j<3; j++ {
			idx := 3*i +j
			b[i][j], err = strconv.Atoi(ints[idx])
			if err != nil {
				panic(3)
			}
		}
	}

	return b
}

func (b *Board) SetPlayer(p Pair, player int) {
	b[p.x][p.y] = player
}

func (b *Board) Valid(p Pair) bool{
	if p.x < 0 || p.x>2 {
		return false
	}
	if p.y < 0 || p.y > 2 {
		return false
	}
	if b[p.x][p.y] == 0 {
		return true
	}
	return false
}

func (b Board)CheckWin(player int) bool {
	for i := 0; i < 3; i++ {
		if b.checkRowOrColumnOrDiagonal(i, 0, 0, 1, player) {
			return true
		}
		if b.checkRowOrColumnOrDiagonal(0, i, 1, 0, player) {
			return true
		}
	}
	if b.checkRowOrColumnOrDiagonal(0, 0, 1, 1, player) {
		return true
	}
	if b.checkRowOrColumnOrDiagonal(0, 2, 1, -1, player) {
		return true
	}
	return false
}

func (b Board)checkRowOrColumnOrDiagonal(x, y, row, column, player int) bool {
	ret := true
	for i := 0; i < 3; i++ {
		if b[x][y] != player {
			ret = false
		}
		x += row
		y += column
	}
	return ret
}

