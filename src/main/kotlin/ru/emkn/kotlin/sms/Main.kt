package ru.emkn.kotlin.sms

import com.xenomachina.argparser.mainBody
import mu.KotlinLogging
import ru.emkn.kotlin.sms.View.GUI

//https://github.com/xenomachina/kotlin-argparser
//https://github.com/doyaaaaaken/kotlin-csv
//https://github.com/Kotlin/kotlinx-datetime

private val logger = KotlinLogging.logger {}


fun main(args: Array<String>): Unit = mainBody {
    val gui = GUI()
    gui.run()

//    logger.info { "Program started" }
//    val parsedArgs = ArgParser(args).parseInto(::ArgumentsFormat)
//    val competitionPath = Path(parsedArgs.competitionsRoot).resolve(parsedArgs.competitionName)
//    try {
//        when (parsedArgs.target) {
//            Target.TOSS -> tossTarget(competitionPath)
//            Target.PERSONAL_RESULT -> personalResultsTarget(competitionPath)
//            Target.TEAM_RESULT -> teamResultsTarget(competitionPath)
//        }
//
//        logger.info { "Program successfully finished" }
//    } catch (error: Exception) {
//        logger.info { "Wow, that's a big surprise, program was fault" }
//        logger.error { error.message }
//    }
}
