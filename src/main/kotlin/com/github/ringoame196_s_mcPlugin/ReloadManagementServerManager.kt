package com.github.ringoame196_s_mcPlugin

import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.net.InetSocketAddress

object ReloadManagementServerManager {
    fun create(port: Int, plugin: Plugin): HttpServer {
        val server = HttpServer.create(InetSocketAddress(port), 0)

        server.createContext("/plugin") { exchange ->
            if (exchange.requestMethod != "GET") {
                exchange.sendResponseHeaders(405, -1)
                exchange.close()
                return@createContext
            }

            val response = handlePluginRequest(
                plugin,
                exchange.requestURI.query
            )

            // webサイトの内容を書き換える
            try {
                exchange.sendResponseHeaders(200, response.length.toLong())
                exchange.responseBody.use { os ->
                    os.write(response.toByteArray())
                }
            } finally {
                exchange.close()
            }
        }
        return server
    }

    fun handlePluginRequest(plugin: Plugin, query: String?): String {
        val pluginName = getPluginName(query) ?: return "invalidQuery"
        addReloadPlugin(plugin, pluginName)
        Bukkit.getLogger().info("[$pluginName] Queued Reload $pluginName")

        return "Queued Reload $pluginName"
    }

    private fun getPluginName(query: String?): String? {
        if (query == null) return null

        return query.split("&").firstNotNullOfOrNull {
            val (key, value) = it.split("=", limit = 2)
            when (key) {
                "plugin", "name" -> value
                else -> null
            }
        }
    }

    private fun addReloadPlugin(plugin: Plugin, pluginName: String) {
        Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                val reloadPlugin = PluginManager.acquisitionPlugin(pluginName) ?: return@Runnable
                PluginManager.addReloadPlugin(reloadPlugin.name)
            }
        )
    }
}
