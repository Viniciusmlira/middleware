package manager

import(
	"time"
)
 
type Timer struct{
	Elapsed time.Duration
}

func (t *Timer) Count(start time.Time, end time.Time){
	t.Elapsed += end.Sub(start)
}
