package ru.emkn.kotlin.sms.io

import ru.emkn.kotlin.sms.model.*

interface Loader {
    fun loadEvent(): Event
    fun loadGroups(): Set<Group>
    fun loadTeams(): Set<Team>
    fun loadRoutes(): Set<Route>
    fun loadTimestamps(): Set<TimeStamp>
    fun loadToss()
}