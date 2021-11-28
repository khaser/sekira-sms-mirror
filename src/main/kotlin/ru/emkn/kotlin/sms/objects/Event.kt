package ru.emkn.kotlin.sms.objects

import java.time.LocalDate
import ru.emkn.kotlin.sms.io.Readable
import ru.emkn.kotlin.sms.io.SingleLineWritable
import java.time.format.DateTimeFormatter

/**
 * Class for storing metadata about the competition.
 */
data class Event(val name: String, val date: LocalDate) : Readable, SingleLineWritable {
    override fun toLine(): List<String?> {
        val pattern = "dd.MM.yyyy"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return listOf(name, date.format(formatter))
    }
}
