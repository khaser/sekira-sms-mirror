package ru.emkn.kotlin.sms.io

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import mu.KotlinLogging
import ru.emkn.kotlin.sms.ObjectFields
import java.io.File
import java.io.IOException

private val logger = KotlinLogging.logger { }

/**
 * Represents [Reader] that can read from csv [file].
 *
 */
class CSVReader(val file: File) : Reader {
    private val csvReader = csvReader()
    private var buffer: List<List<String>> = csvReader.readAll(file)

    private fun toObjectFields(
        headers: Map<String, ObjectFields>,
        table: List<Map<String, String>>
    ): List<Map<ObjectFields, String>> {
        return table.map { line ->
            line.map { entry ->
                val header = headers[entry.key] ?: throw IOException("Header doesn't have key '${entry.key}'")
                header to entry.value
            }.toMap()
        }
    }

    /**
     * Returns list of map, each of which transform header name to field
     *
     * Returns `null` if data [file] is inappropriate
     */
    private fun tableWithHeader(): List<Map<String, String>>? {
        return try {
            val header = buffer.first()
            buffer = buffer.drop(1)
            val table = buffer.map { line ->
                line.mapIndexed { index, field ->
                    Pair(header[index], field)
                }.toMap()
            }
            val data = table.map { record ->
                record.mapKeys {
                    it.key.trim()
                }.mapValues { it.value.trim() }
            }
            data.ifEmpty {
                logger.warn { "${file.name} doesn't have header or members, so it was ignored" }
                null
            }
        } catch (err: NoSuchElementException) {
            logger.warn { "There is no header in ${file.name}" }
            null
        }

    }

    /**
     * Returns name of table
     *
     * Returns `null` if [file] is empty
     */
    private fun name(): String? =
        try {
            val line = buffer.first()
            buffer = buffer.drop(1)
            line[0]
        } catch (err: NoSuchElementException) {
            logger.warn { "${file.name} is empty, so it was ignored" }
            null
        }

    /**
     * Convert start protocol to list of participants
     */
    private fun preprocess(table: List<Map<String, String>>): List<Map<String, String>> {
        var groupName = ""
        return table.mapNotNull { record ->
            val currentGroupName = record.values.filter(String::isNotEmpty)
            if (currentGroupName.size == 1 && currentGroupName.first() != groupName) {
                groupName = currentGroupName.first()
                null
            } else
                record + ("????????????" to groupName)
        }
    }

    override fun team(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "????????????" to ObjectFields.Group,
            "??????????????" to ObjectFields.Surname,
            "??????" to ObjectFields.Name,
            "??.??." to ObjectFields.BirthdayYear,
            "????????." to ObjectFields.Grade,
            "??????????????" to ObjectFields.Team,
        )
        val name = name() ?: return null
        val table = tableWithHeader()?.map { record ->
            record + ("??????????????" to name)
        } ?: return null
        return toObjectFields(header, table)
    }

    override fun groups(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "???????????????? ????????????" to ObjectFields.Name,
            "??????????????????" to ObjectFields.ResultType,
            "??????????????????" to ObjectFields.RouteName,
        )
        val table = tableWithHeader() ?: return null
        return toObjectFields(header, table)
    }

    override fun courses(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "??????" to ObjectFields.Type,
            "???????????????????? ??/??" to ObjectFields.Amount,
            "????????????????" to ObjectFields.Name,
            "??/??" to ObjectFields.CheckPoints,
        )
        val table = tableWithHeader() ?: return null
        val correctedTable = table.map { record ->
            val checkPoints =
                "??/??" to record.filterKeys { it.toIntOrNull() != null }.values.filter { it.isNotEmpty() }
                    .joinToString(",")
            record.filterKeys { it.toIntOrNull() == null } + checkPoints
        }
        if (correctedTable.isEmpty())
            logger.warn { "List of courses is empty" }
        return toObjectFields(header, correctedTable)
    }

    override fun event(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "????????????????" to ObjectFields.Name,
            "????????" to ObjectFields.Date,
        )
        val table = tableWithHeader() ?: return null
        return toObjectFields(header, table)
    }


    override fun timestamps(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "??????????" to ObjectFields.ID,
            "??????????" to ObjectFields.Time,
            "?????????? ??/??" to ObjectFields.Name,
        )
        val name = name()
        val table = tableWithHeader()?.map { record ->
            record + ("?????????? ??/??" to name.toString())
        } ?: return null
        return toObjectFields(header, table)
    }

    override fun toss(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "????????????" to ObjectFields.Group,
            "??????????????" to ObjectFields.Surname,
            "??????" to ObjectFields.Name,
            "??.??." to ObjectFields.BirthdayYear,
            "????????." to ObjectFields.Grade,
            "??????????????" to ObjectFields.Team,
            "??????????" to ObjectFields.ID,
            "?????????? ????????????" to ObjectFields.StartTime,
        )
        val table = tableWithHeader() ?: return null
        return toObjectFields(header, preprocess(table))
    }

    override fun checkPoints(): List<Map<ObjectFields, String>>? {
        val header = mapOf(
            "?????????? ??/??" to ObjectFields.Name,
            "??????????????????" to ObjectFields.Weight
        )
        val table = tableWithHeader() ?: return null
        return toObjectFields(header, table)
    }

}