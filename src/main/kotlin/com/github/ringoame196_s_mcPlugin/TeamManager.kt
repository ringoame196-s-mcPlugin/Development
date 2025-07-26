package com.github.ringoame196_s_mcPlugin

import org.bukkit.ChatColor
import org.bukkit.entity.Player

object TeamManager {
    fun getTeamName(player: Player): String {
        val team = player.scoreboard.getEntryTeam(player.name)
        val teamName = team?.name ?: "参加していません"
        val teamDisplayName = team?.displayName ?: teamName
        val teamColor = team?.color ?: ChatColor.WHITE
        return "${teamColor}$teamDisplayName"
    }

    fun hasTeamChanged(player: Player): Boolean {
        val uuid = player.uniqueId
        val currentTeam = getTeamName(player)
        val previousTeam = previousTeams[uuid]

        previousTeams[uuid] = currentTeam

        return currentTeam != previousTeam
    }
}
