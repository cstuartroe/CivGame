package game

import (
	"fmt"
	"image/color"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/ebitenutil"
)

type ScreenPosition struct {
	x, y float32
}


const angle float32 = .3
var hexagonSequence []uint16 = []uint16{0, 1, 2, 2, 3, 4, 4, 5, 0, 0, 2, 4}

func drawHexagon(center ScreenPosition, size float32, color color.Color, screen *ebiten.Image) {
	img := ebiten.NewImage(100, 100)
	img.Fill(color)

	leftEdge := center.x - size/2
	rightEdge := center.x + size/2

	topCornerY := center.y - size*(1 - angle)/2
	bottomCornerY := center.y + size*(1 - angle)/2
	peakY := topCornerY - size*angle
	valleyY := bottomCornerY + size*angle

	vertices := []ebiten.Vertex{
		{
			leftEdge, topCornerY,
			0, 0,
			1, 1, 1, 1,
		},
		{
			center.x, peakY,
			50, 0,
			1, 1, 1, 1,
		},
		{
			rightEdge, topCornerY,
			100, 0,
			1, 1, 1, 1,
		},
		{
			rightEdge, bottomCornerY,
			100, 100,
			1, 1, 1, 1,
		},
		{
			center.x, valleyY,
			50, 100,
			1, 1, 1, 1,
		},
		{
			leftEdge, bottomCornerY,
			0, 100,
			1, 1, 1, 1,
		},
	}

	screen.DrawTriangles(vertices, hexagonSequence, img, &ebiten.DrawTrianglesOptions{})
}

func (g Geography) Color() color.NRGBA {
	red := 255 - int(g.temperature)/3 - int(g.humidity)*2/3
	grn := 255 - int(g.temperature)/3 - int(g.humidity)/3
	blu := 255 - int(g.temperature) - int(g.humidity)*2/3

	if blu < 0 { blu = 0 }

	return color.NRGBA{uint8(red), uint8(grn), uint8(blu), 255}
}

const megamapOffsetLeft = 400
const megamapOffsetTop = 100
const megamapOffsetRight = 100
const megamapOffsetBottom = 300
const tileSize uint16 = 75
const tileBorder = 2
var tileHighlightColor color.NRGBA = color.NRGBA{255, 0, 0, 255}

func (g *Game) drawTile(c Coordinate, screen *ebiten.Image) {
	x := c.x + g.viewportOffset.x
	y := c.y + g.viewportOffset.y

	if int(y) >= len(g.tiles) || int(x) >= len(g.tiles[y]) {
		return
	}

	var tile *Tile = g.tiles[y][x]

	center := ScreenPosition{
		x: float32(megamapOffsetLeft) + float32(c.x*tileSize),
		y: float32(megamapOffsetTop) + float32(c.y*tileSize),
	}

	if c.y % 2 == 1 {
		center.x += float32(tileSize)/2
	}

	if g.focus.x == x && g.focus.y == y {
		drawHexagon(center, float32(tileSize + tileBorder), tileHighlightColor, screen)
	}

	drawHexagon(center, float32(tileSize - tileBorder), tile.geo.Color(), screen)
}

func (g *Game) megamapDimensions() (width, height uint16) {
	w := (g.size.width - megamapOffsetLeft - megamapOffsetRight)/tileSize
	h := (g.size.height - megamapOffsetTop - megamapOffsetBottom)/tileSize

	return w, h
}

func (g *Game) Draw(screen *ebiten.Image) {
	screen.Fill(color.NRGBA{80, 80, 90, 255})

	megamap_cols, megamap_rows := g.megamapDimensions()

	for x := uint16(0); x < megamap_cols; x++ {
		for y := uint16(0); y < megamap_rows; y++ {
			g.drawTile(Coordinate{x, y}, screen)
		}
	}

	t := g.focusedTile().geo
	c := t.Color()

	ebitenutil.DebugPrint(screen, fmt.Sprintf(
		"x%d y%d | a%d t%d h%d | r%d g%d b%d",
		g.focus.x, 
		g.focus.y, 
		t.altitude, 
		t.temperature, 
		t.humidity,
		c.R, c.G, c.B,
	))
}
