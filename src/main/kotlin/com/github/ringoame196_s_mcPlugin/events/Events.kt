package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.notion.NotifierServerStart
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.plugin.Plugin

class Events(private val plugin: Plugin) : Listener {
    @EventHandler
    fun onServerLoad(e: ServerLoadEvent) {
        val notionURL = plugin.config.getString("NotionURL") ?: return
        NotifierServerStart.notifier(notionURL)
    }
}
