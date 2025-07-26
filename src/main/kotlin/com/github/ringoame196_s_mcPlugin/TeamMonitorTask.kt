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
            teamChangeMessage(player)
        }
    }

    private fun displayTeamName(player: Player) {
        val teamName = TeamManager.getTeamName(player) ?: "${ChatColor.WHITE}参加していません"
        val message = "${ChatColor.GOLD}[開発用] 参加チーム：$teamName"
        // アクションバーに表示
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

    private fun teamChangeMessage(player: Player) {
        if (!TeamManager.hasTeamChanged(player)) return

        val teamName = TeamManager.getTeamName(player)
        val message = if (teamName != null) "${ChatColor.GOLD}[開発用] ${teamName}に参加しました"
        else "${ChatColor.GOLD}[開発用]${ChatColor.AQUA}チームから抜けました"
        player.sendMessage(message)
    }
}
