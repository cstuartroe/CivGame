package main

import (
	"log"
	"github.com/hajimehoshi/ebiten/v2"
	"github.com/cstuartroe/CivGame/src/game"
)


func main() {
	g := game.NewGame()

	ebiten.SetFullscreen(true)
	ebiten.SetMaxTPS(g.MaxTPS)

	if err := ebiten.RunGame(g); err != nil {
		log.Fatal(err)
	}
}