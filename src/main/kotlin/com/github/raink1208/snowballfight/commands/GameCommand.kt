package com.github.raink1208.snowballfight.commands

import com.github.raink1208.snowballfight.Main
import com.github.raink1208.snowballfight.game.GamePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GameCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val game = Main.instance.game
        if (game == null) {
            sender.sendMessage("ゲームが存在しません")
            return true
        }
        if (args.isEmpty()) return false
        if (sender !is Player) return true
        when (args[0]) {
            "join" -> {
                game.joinPlayer(sender)
            }
            "leave" -> {
                val gamePlayer = game.getGamePlayer(sender) ?: return true
                game.leavePlayer(gamePlayer)
            }
            "team" -> {
                val gamePlayer = game.getGamePlayer(sender) ?: return true
                when (args[1]) {
                    "join" -> {
                        if (args.size < 3) return true
                        val team = game.getTeam(args[2])
                        if (team == null) {
                            sender.sendMessage("チーム["+args[2]+"]が見つかりません")
                            return true
                        }
                        team.joinTeam(gamePlayer)
                    }
                    "leave" -> {
                        gamePlayer.team?.leaveTeam(gamePlayer)
                    }
                    else -> {
                        sender.sendMessage("/snowballfightgame team [join|leave]")
                        return true
                    }
                }
            }
            "spectate" -> {
                val gamePlayer = game.getGamePlayer(sender) ?: return true
                gamePlayer.status = GamePlayer.PlayerStatus.SPECTATOR
            }
            else -> return false
        }
        return true
    }
}