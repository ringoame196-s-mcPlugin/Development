package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

object PluginManager {
    lateinit var plugin: Plugin
    lateinit var reloadCommand: String
    lateinit var developPluginName: String
    private var reloadablePlugin = mutableSetOf<String>()

    fun setup(plugin: Plugin, reloadCommand: String) {
        this.plugin = plugin
        this.reloadCommand = reloadCommand
        this.developPluginName = plugin.name
    }

    fun addReloadPlugin(pluginName: String) {
        reloadablePlugin.add(pluginName)

        val message = "${ChatColor.YELLOW}[$developPluginName]${pluginName}を自動リロードしました"
        autoReload(pluginName)
        sendOpMessage(message)
    }

    fun reloadPlugin(sender: CommandSender, pluginName: String) {
        if (reloadablePlugin.contains(pluginName)) {
            reloadablePlugin.remove(pluginName)
            val command = reloadCommand.replace("@pluginName", pluginName)
            Bukkit.dispatchCommand(sender, command)
        } else {
            val message = "${ChatColor.RED}既にリロードされています"
            sender.sendMessage(message)
        }
    }

    fun autoReload(pluginName: String) {
        val sender = Bukkit.getConsoleSender()
        Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                reloadPlugin(sender, pluginName)
            }
        )
    }

    fun acquisitionPlugin(pluginName: String): Plugin? { // プラグインがあるか確認
        return Bukkit.getPluginManager().getPlugin(pluginName)
    }

    fun sendOpMessage(message: String) {
        // メッセージをOPプレイヤーに送信
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.isOp) {
                continue
            }
            player.sendMessage(message)
        }
    }
}
