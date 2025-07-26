package com.github.ringoame196_s_mcPlugin.team

import org.bukkit.entity.Player
import java.util.UUID

object TeamManager {
    private val previousTeams = mutableMapOf<UUID, String?>()

    fun getTeamName(player: Player): String? {
        val team = player.scoreboard.getEntryTeam(player.name) ?: return null
        val teamName = "${team.displayName}(${team.name})"
        val teamColor = team.color
        return "${teamColor}$teamName"
    }

    fun hasTeamChanged(player: Player): Boolean {
        val uuid = player.uniqueId
        val currentTeam = getTeamName(player)
        val previousTeam = previousTeams[uuid]

        previousTeams[uuid] = currentTeam

        return currentTeam != previousTeam
    }
}
