package game

type Geography struct {
	altitude, temperature, humidity uint8
}

type Tile struct {
	geo Geography
}
