package game

type Board [3][3]int

func (b *Board) ToString() string {
	s := make([]byte, 12)
	for i := 0; i < 3; i++ {
		for j:= 0; j < 3; j++{
			idx := 4*i + j
			switch b[i][j]{
			case 1:
				s[idx] = 'X'
			case 2:
				s[idx] = 'O'
			default:
				s[idx] = '_'
			}
		}
		s[4*(i+1) - 1] = '\n'
	}
	return string(s)
}

func (b *Board) MakeMove(move *Move, player int) {
	b[move.X][move.Y] = player
}

func (b *Board) IsMoveValid(move *Move) bool {
	switch{
	case move.X < 0 || move.X > 2:
		return false
	case move.Y < 0 || move.Y > 2:
		return false
	case b[move.X][move.Y] != 0:
		return false
	default:
		return true
	}
}

func (b *Board) GameOver() int {
	switch{
	case b.noMovesLeft():
		return -1;
	case b.checkWin(1):
		return 1;
	case b.checkWin(2):
		return 2;
	default:
		return 0;
	}
}

func (b *Board) Restart(){
	for i := 0; i < 3; i++{
		for j := 0; j < 3; j++{
			b[i][j] = 0
		}
	}
}

func (b *Board) noMovesLeft() bool {
	for i := 0; i < 3; i++{
		for j := 0; j < 3; j++{
			if b.IsMoveValid(&Move{i,j}){
				return false
			}
		}
	}
	return true
}

func (b *Board) checkWin(player int) bool {
	for i := 0; i < 3; i++ {
		switch{
		case b.checkRowOrColumnOrDiagonal(i, 0, 0, 1, player):
			return true
		case b.checkRowOrColumnOrDiagonal(0, i, 1, 0, player):
			return true
		}
	}
	switch{
	case  b.checkRowOrColumnOrDiagonal(0, 0, 1, 1, player):
		return true
	case b.checkRowOrColumnOrDiagonal(0, 2, 1, -1, player):
		return true
	default:
		return false
	}
}

func (b *Board) checkRowOrColumnOrDiagonal(x, y, row, column, player int) bool {
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