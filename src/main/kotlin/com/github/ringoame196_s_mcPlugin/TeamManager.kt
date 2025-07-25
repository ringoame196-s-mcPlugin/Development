package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

object TeamManager {
    fun getTeamName(player: Player): String {
        val team = player.scoreboard.getEntryTeam(player.name)
        val teamName = team?.name ?: "参加していません"
        return teamName
    }
}
