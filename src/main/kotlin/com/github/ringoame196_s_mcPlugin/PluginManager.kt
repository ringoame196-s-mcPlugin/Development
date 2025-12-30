package com.github.ringoame196_s_mcPlugin

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
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
        sendOpMessage(message)
    }

    fun createReloadMessage(pluginName: String): TextComponent {
        val command = "/pluginupdate $pluginName"

        // メインメッセージ部分
        val mainMessage = TextComponent("${ChatColor.YELLOW}[$developPluginName] プラグイン名($pluginName) ")
        // クリック可能なリロード部分
        val reloadComponent = TextComponent("${ChatColor.AQUA}[リロード]")
        // ホバーテキストを設定
        reloadComponent.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("クリックしてプラグインをリロードします").create())
        // クリック時にコマンド実行
        reloadComponent.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        // メインメッセージにリロード部分を追加
        mainMessage.addExtra(reloadComponent)
        return mainMessage
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

    fun sendOpMessage(message: TextComponent) {
        // メッセージをOPプレイヤーに送信
        for (player in Bukkit.getOnlinePlayers()) {
            if (!player.isOp) {
                continue
            }
            player.spigot().sendMessage(message)
        }
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
