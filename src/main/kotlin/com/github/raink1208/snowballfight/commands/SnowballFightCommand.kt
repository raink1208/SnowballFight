package com.github.raink1208.snowballfight.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class SnowballFightCommand: CommandExecutor, TabCompleter {
    private val subCommands = mutableListOf("create", "destroy")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

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