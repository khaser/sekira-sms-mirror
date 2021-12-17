package ru.emkn.kotlin.sms

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

enum class FileType {
    JSON, CSV
}

enum class Target {
    TOSS, PERSONAL_RESULT, TEAM_RESULT
}

val headers = mapOf(
    "Название группы" to "name",
    "Группа" to "group",
    "Фамилия" to "surname",
    "Имя" to "name",
    "Название" to "name",
    "Г.р." to "birthdayYear",
    "Разр." to "grade",
    "Дистанция" to "routeName",
    "Команда" to "team",
    "Дата" to "date",
    "Номер" to "participantId",
    "Время старта" to "startTime",
    "Время" to "time"
)

/**
 * A class for defining the format of command line arguments
 */
class ArgumentsFormat(parser: ArgParser) {
    val competitionName by parser.positional("EVENT", """
        name of competition directory
    """.trimIndent())

    val target by parser.mapping( mapOf(
        "--toss" to Target.TOSS,
        "--personal" to Target.PERSONAL_RESULT,
        "--team" to Target.TEAM_RESULT
    ), """
        sets the goal to be completed in the following format:
        --toss
            Forms the starting protocols according to the application lists. A simple draw is used with an interval of 1 minute and the start at 12:00:00.
        --personal
            According to the starting protocols and protocols of passing checkpoints, form protocols of results.
        --team
            According to the protocols of results, form a protocol of results for teams.
    """.replace("\n", "\r"))

    val competitionsRoot by parser.positional("DIR", """
        sets path for directory, which storing all competitions
    """.trimIndent()).default<String>("competitions")
}

const val maxTextLength = 127

