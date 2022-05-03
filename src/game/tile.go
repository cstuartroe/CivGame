package game

type Geography struct {
	altitude, temperature, humidity uint8
}

type Tile struct {
	geo Geography
}

func createTiles() [][]*Tile {
	out := [][]*Tile{}

	for y := uint16(0); y < map_width; y++ {
		row := []*Tile{}

		for x := uint16(0); x < map_height; x++ {
			row = append(row, &Tile{
				geo: Geography {
					altitude: uint8((x + y)*10 % 256),
					temperature: uint8(y),
					humidity: uint8(x),
				},
			})
		}

		out = append(out, row)
	}

	return out
}
