package com.github.raink1208.snowballfight.game


class SnowballFightGame {
    var gameStatus = GameStatus.BEFORE_GAME; private set
    private val gameEventListener = GameListener(this)

    enum class GameStatus {
        BEFORE_GAME,
        IN_GAME,
        AFTER_GAME
    }
}