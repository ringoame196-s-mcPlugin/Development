package com.github.ringoame196_s_mcPlugin

import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class ReloadManagementServer(private val plugin: Plugin) {
    private var server: HttpServer? = null
    private val developPluginName = plugin.name

    fun start(port: Int) {
        if (checkServer()) {
            Bukkit.getLogger().severe("[$developPluginName] setup() was not called")
            return
        }
        ReloadManagementServerManager.create(port, plugin).start()
    }
    fun stop() {
        if (!checkServer()) return
        server?.stop(1) // サーバーを止める
        Bukkit.getLogger().info("[$developPluginName] Server stopped")
    }

    private fun checkServer(): Boolean {
        return server != null
    }
}
