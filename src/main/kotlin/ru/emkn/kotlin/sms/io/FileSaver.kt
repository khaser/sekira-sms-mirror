package ru.emkn.kotlin.sms.io

import ru.emkn.kotlin.sms.FileType
import ru.emkn.kotlin.sms.model.Competition
import ru.emkn.kotlin.sms.model.Group
import ru.emkn.kotlin.sms.model.formatterForPersonalResults
import java.io.File

class FileSaver(file: File) : Saver {
    val writer: Writer = Writer(file, FileType.CSV)
    override fun saveResults() {
        writer.add(
            listOf(
                "Место", "Номер", "Имя", "Фамилия", "Г.р.", "Разр.", "Время старта", "Время финиша", "Отставание"
            )
        )

        Competition.groups.forEach { group ->
            writer.add(group.name)
            val sortedGroup = Competition.result.sortMembersIn(group)
            sortedGroup.forEach { participant ->
                writer.add(participant) { formatterForPersonalResults(it) }
            }
        }
        writer.write()
    }


    override fun saveToss() {
        writer.add(listOf("Номер", "Имя", "Фамилия", "Г.р.", "Команда", "Разр.", "Время старта"))
        writer.addAll(Group.byName.values.toList())
        writer.write()
    }

    override fun saveTeamResults() {
        writer.add(listOf("Номер", "Название", "Очки"))
        val sortedTeams = Competition.teamResult.sortTeams(Competition.teams)
        sortedTeams.forEachIndexed { index, team ->
            writer.add<SingleLineWritable>(team) { listOf(index + 1) + it.toLine() }
        }
        writer.write()
    }
}