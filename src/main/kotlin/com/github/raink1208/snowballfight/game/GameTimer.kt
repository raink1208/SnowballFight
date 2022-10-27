package com.github.raink1208.snowballfight.game

class GameTimer(private val game: SnowballFightGame): Runnable {
    override fun run() {
        game.teamCheck()
    }
}