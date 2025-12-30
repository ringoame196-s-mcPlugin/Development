package com.github.ringoame196_s_mcPlugin.commands

import com.github.ringoame196_s_mcPlugin.PluginManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class PluginUpdateCommand() : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false

        val pluginName = args[0]
        PluginManager.reloadPlugin(sender, pluginName)

        return true
    }

    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        return when (args.size) {
            1 -> mutableListOf("[プラグイン名]")
            else -> mutableListOf()
        }
    }
}
