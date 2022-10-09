package com.github.raink1208.snowballfight.commands

import com.github.raink1208.snowballfight.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class SnowballFightCommand: CommandExecutor, TabCompleter {
    private val subCommands = mutableListOf("create", "destroy")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false
        when (args[0]) {
            "create" -> {
                val res = Main.instance.createGame()
                if (res) {
                    sender.sendMessage("ゲームを作成しました")
                } else {
                    sender.sendMessage("既にゲームが存在します")
                }
            }
            "destroy" -> {
                Main.instance.destroyGame()
                sender.sendMessage("ゲームを削除しました")
            }
            else -> return false
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions = mutableListOf<String>()
        if (args.isEmpty()) return completions
        val subCommand = args[0]
        if (args.size == 1) {
            StringUtil.copyPartialMatches(subCommand, subCommands, completions)
            completions.sort()
            return completions
        }
        return mutableListOf()
    }
}