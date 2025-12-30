package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.DevCommand
import com.github.ringoame196_s_mcPlugin.commands.PluginUpdateCommand
import com.github.ringoame196_s_mcPlugin.events.Events
import com.github.ringoame196_s_mcPlugin.team.TeamMonitorTask
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    private val plugin = this
    lateinit var reloadManagementServer: ReloadManagementServer
    override fun onEnable() {
        super.onEnable()

        plugin.saveDefaultConfig() // configファイル生成

        server.pluginManager.registerEvents(Events(), plugin)

        // コマンド
        val devCommand = getCommand("dev")
        devCommand!!.setExecutor(DevCommand())
        val command = getCommand("pluginupdate")
        command!!.setExecutor(PluginUpdateCommand())

        // チーム表示関係
        TeamMonitorTask().runTaskTimer(this, 0L, 20L) // 20 tick（= 約1秒）ごとに実行

        // reloadManagementServer関係
        val config = plugin.config
        val port = config.getInt("Port")
        reloadManagementServer = ReloadManagementServer(plugin)
        reloadManagementServer.start(port)
        // プラグインリロード関係
        val reloadCommand = config.getString("ReloadCommand") ?: "pluginmanager reload @pluginName"
        PluginManager.setup(plugin, reloadCommand)
    }

    override fun onDisable() {
        super.onDisable()
        if (::reloadManagementServer.isInitialized) {
            reloadManagementServer.stop()
        }
    }
}
