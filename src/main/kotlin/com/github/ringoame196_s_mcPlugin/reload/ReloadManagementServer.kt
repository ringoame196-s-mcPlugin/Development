package com.github.ringoame196_s_mcPlugin.reload

import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class ReloadManagementServer(private val plugin: Plugin) {
    private var server: HttpServer? = null
    private val developPluginName = plugin.name

    fun start(port: Int) {
        if (server != null) {
            Bukkit.getLogger().warning("[$developPluginName] Server already started")
            return
        }

        server = ReloadManagementServerManager.create(port, plugin)
        server!!.start()

        Bukkit.getLogger().info("[$developPluginName] Server started on port $port")
    }

    fun stop() {
        val s = server ?: return
        s.stop(1)
        server = null

        Bukkit.getLogger().info("[$developPluginName] Server stopped")
    }
}
