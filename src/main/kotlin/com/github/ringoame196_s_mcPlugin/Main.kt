package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.DevCommand
import com.github.ringoame196_s_mcPlugin.events.Events
import com.github.ringoame196_s_mcPlugin.team.TeamMonitorTask
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    private val plugin = this
    private var reloadManagementServer = ReloadManagementServer(plugin)
    override fun onEnable() {
        super.onEnable()

        plugin.saveDefaultConfig() // configファイル生成

        server.pluginManager.registerEvents(Events(), plugin)

        // コマンド
        val devCommand = getCommand("dev")
        devCommand!!.setExecutor(DevCommand())

        // チーム表示関係
        TeamMonitorTask().runTaskTimer(this, 0L, 20L) // 20 tick（= 約1秒）ごとに実行

        // reloadManagementServer関係
        val config = plugin.config
        val port = config.getInt("Port")
        reloadManagementServer.start(port)

        // プラグインリロード関係
        val reloadCommand = config.getString("ReloadCommand") ?: "pluginmanager reload @pluginName"
        PluginManager.setup(reloadCommand)
    }

    override fun onDisable() {
        super.onDisable()
        reloadManagementServer.stop()
    }
}
