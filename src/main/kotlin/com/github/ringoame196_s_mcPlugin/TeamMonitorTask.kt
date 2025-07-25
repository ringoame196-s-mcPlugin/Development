package com.github.ringoame196_s_mcPlugin

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class TeamMonitorTask : BukkitRunnable() {
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            displayTeamName(player)
        }
    }

    private fun displayTeamName(player: Player) {
        val teamName = TeamManager.getTeamName(player)
        val message = "${ChatColor.GOLD}[開発用] 参加チーム：$teamName"
        // アクションバーに表示
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }
}
