package com.github.ringoame196_s_mcPlugin

import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.io.OutputStream
import java.net.InetSocketAddress

object ReloadManagementServerManager {
    fun create(port: Int, plugin: Plugin): HttpServer {
        val server = HttpServer.create(InetSocketAddress(port), 0)
        server.createContext("/plugin") { exchange ->
            val response = handlePluginRequest(
                plugin,
                exchange.requestURI.query
            )

            // webサイトの内容を書き換える
            exchange.sendResponseHeaders(200, response.length.toLong())
            exchange.responseBody.use { os: OutputStream ->
                os.write(response.toByteArray())
            }
        }
        return server
    }

    fun handlePluginRequest(plugin: Plugin, query: String?): String {
        val pluginName = getPluginName(query) ?: return "invalidQuery"

        val reloadPlugin = PluginManager.acquisitionPlugin(pluginName)
            ?: return "pluginNotFound"

        runReload(plugin, reloadPlugin.name)
        return "Reload $pluginName"
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

    private fun runReload(plugin: Plugin, targetPlugin: String) {
        Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                PluginManager.addReloadPlugin(targetPlugin)
            }
        )
    }
}
