type TileQueueNode struct {
	next *TileQueueNode
	tile *Tile
}

type TileQueue struct {
	current *TileQueueNode
	end *TileQueueNode
}

func (q TileQueue) push(tile *Tile) {
	node := &TileQueueNode{
		next: nil, 
		tile: tile,
	}

	if q.current == nil {
		q.current = node
		q.end = node
	} else {
		q.end.next = node
		q.end = node
	}
}

func (q TileQueue) pop(tile *Tile) *Tile {
	out := q.current
	q.current = out.next
	return out
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
