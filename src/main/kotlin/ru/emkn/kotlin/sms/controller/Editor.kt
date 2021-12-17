package ru.emkn.kotlin.sms.controller

import mu.KotlinLogging
import ru.emkn.kotlin.sms.controller.Creator.convert
import ru.emkn.kotlin.sms.model.*
import java.time.LocalDate
import java.time.LocalTime

private val logger = KotlinLogging.logger { }

object Editor {
    fun editParticipant(participant: Participant, values: Map<String, String>) {
        try {
            val name = convert<String>(values["name"])
            val surname = convert<String>(values["surname"])
            val birthdayYear = convert<Int>(values["birthdayYear"])
            val grade = convert<String?>(values["grade"])
            val groupName = convert<String>(values["groupName"])
            val teamName = convert<String>(values["teamName"])
            if (!Group.checkByName(groupName))
                throw IllegalArgumentException("Cannot find group $groupName")
            if (!Team.checkByName(teamName))
                throw IllegalArgumentException("Cannot find team $teamName")
            if (CompetitionController.state >= State.TOSSED) {
                val startTime = convert<LocalTime>(values["startTime"])
                participant.startTime = startTime
            }
            participant.change(name, surname, birthdayYear, groupName, teamName, grade)
            logger.info { "Participant was successfully edited" }
        } catch (err: IllegalArgumentException) {
            logger.info { "Cannot edit participant ${participant.name}" }
            throw err
        }
    }

    fun editGroup(group: Group, values: Map<String, String>) {
        try {
            val name = convert<String>(values["name"])
            val routeName = convert<String>(values["routeName"])
            if (!Route.checkByName(routeName))
                throw IllegalArgumentException("Cannot find route $routeName")
            group.change(name, routeName)
            logger.info { "Group was successfully edited" }
        } catch (err: IllegalArgumentException) {
            logger.info { "Cannot edit group ${group.name}" }
            throw err
        }
    }


    fun editEvent(event: Event, values: Map<String, String>) {
        try {
            val eventName = convert<String>(values["name"])
            val data = convert<LocalDate>(values["date"])
            event.change(eventName, data)
            logger.info { "Event was successfully edited" }
        } catch (err: IllegalArgumentException) {
            logger.info { "Cannot edit event ${event.name}" }
            throw err
        }
    }

    fun editTeam(team: Team, values: Map<String, String>) {
        try {
            val teamName = convert<String>(values["name"])
            team.change(teamName)
            logger.info { "Team was successfully edited" }
        } catch (err: IllegalArgumentException) {
            logger.info { "Cannot edit team ${team.name}" }
            throw err
        }
    }

    fun editRoute(route: Route, values: Map<String, String>) {
        try {
            val routeName = convert<String>(values["name"])
            val checkPoints = convert<List<Checkpoint>>(values["checkPoints"])
            route.change(routeName, checkPoints)
            logger.info { "Route was successfully edited" }
        } catch (err: IllegalArgumentException) {
            logger.info { "Cannot edit route ${route.name}" }
            throw err
        }
    }
}