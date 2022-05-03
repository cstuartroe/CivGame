package game

import (
	"time"
	"github.com/hajimehoshi/ebiten/v2"
)

const map_width uint16 = 255
const map_height uint16 = 255

type ScreenSize struct {
	width, height uint16
}

type Coordinate struct {
	x, y uint16
}

type Game struct{
	size ScreenSize
	tiles [][]*Tile
	ticks int
	viewportOffset Coordinate
	focus Coordinate
	MaxTPS int
	lastFocusMove int64
}

func NewGame() *Game {
	return &Game{
		tiles: createTiles(),
		ticks: 0,
		// viewportOffset: Coordinate{100, 100},
		// focus: Coordinate{10, 10},
		MaxTPS: 15,
	}
}

func (g *Game) tileWidth() uint16 {
	return uint16(len(g.tiles[0]))
}

func (g *Game) tileHeight() uint16 {
	return uint16(len(g.tiles))
}

func (g *Game) focusedTile() *Tile {
	return g.tiles[g.focus.y][g.focus.x]
}

func (g *Game) moveFocus() {
	t := time.Now().UnixMilli()

	if g.lastFocusMove + 500 > t {
		return
	}

	if ebiten.IsKeyPressed(ebiten.KeyArrowUp) {
		if g.focus.y > 0 {
			g.focus.y -= 1

			if g.focus.y < g.viewportOffset.y {
				g.viewportOffset.y -= 2
			}

			g.lastFocusMove = t
		}
	} else if ebiten.IsKeyPressed(ebiten.KeyArrowDown) {
		if g.focus.y < g.tileHeight() - 1 {
			g.focus.y += 1

			_, h := g.megamapDimensions()
			if g.focus.y >= g.viewportOffset.y + h {
				g.viewportOffset.y += 2
			}

			g.lastFocusMove = t
		}
	}

	if ebiten.IsKeyPressed(ebiten.KeyArrowLeft) {
		if g.focus.x > 0 {
			g.focus.x -= 1

			if g.focus.x < g.viewportOffset.x {
				g.viewportOffset.x -= 2
			}

			g.lastFocusMove = t
		}
	} else if ebiten.IsKeyPressed(ebiten.KeyArrowRight) {
		if g.focus.x < g.tileWidth() - 1 {
			g.focus.x += 1

			w, _ := g.megamapDimensions()
			if g.focus.x >= g.viewportOffset.x + w {
				g.viewportOffset.x += 2
			}

			g.lastFocusMove = t
		}
	}
}

func (g *Game) Update() error {
	g.ticks += 1

	g.moveFocus()

	return nil
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	g.size.width = uint16(outsideWidth)
	g.size.height = uint16(outsideHeight)
	return outsideWidth, outsideHeight
}
