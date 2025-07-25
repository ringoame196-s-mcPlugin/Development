package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.Events
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    private val plugin = this
    override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(Events(), plugin)
        val command = getCommand("dev")
        command!!.setExecutor(Command())

        TeamMonitorTask().runTaskTimer(this, 0L, 20L) // 20 tick（= 約1秒）ごとに実行
    }
}
