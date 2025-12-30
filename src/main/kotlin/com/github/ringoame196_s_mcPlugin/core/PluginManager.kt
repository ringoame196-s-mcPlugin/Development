package com.github.ringoame196_s_mcPlugin.core

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin

object PluginManager {
    private lateinit var reloadCommand: String

    fun setup(reloadCommand: String) {
        this.reloadCommand = reloadCommand
    }

    fun autoReload(plugin: Plugin, targetPluginName: String) {
        val target = getPlugin(targetPluginName)
        if (target == null) {
            sendOpMessage("${ChatColor.RED}[${plugin.name}]$targetPluginName は見つかりませんでした")
            return
        }
        autoReload(plugin, target)
    }

    fun autoReload(plugin: Plugin, targetPlugin: Plugin) {
        check(::reloadCommand.isInitialized) {
            "PluginManager.setup() was not called"
        }

        Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                val sender = Bukkit.getConsoleSender()

                val command = reloadCommand.replace("@pluginName", targetPlugin.name)
                Bukkit.dispatchCommand(sender, command)

                val message = "${ChatColor.YELLOW}[${plugin.name}]${targetPlugin.name}を自動リロードしました"
                sendOpMessage(message)
            }
        )
    }

    private fun getPlugin(pluginName: String): Plugin? { // プラグインがあるか確認
        return Bukkit.getPluginManager().getPlugin(pluginName)
    }

    private fun sendOpMessage(message: String) {
        // メッセージをOPプレイヤーに送信
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.isOp) {
                continue
            }
            player.sendMessage(message)
        }
    }
}
