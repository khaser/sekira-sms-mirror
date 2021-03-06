package ru.emkn.kotlin.sms.view.tables

import org.jetbrains.exposed.sql.transactions.transaction
import ru.emkn.kotlin.sms.ObjectFields
import ru.emkn.kotlin.sms.controller.CompetitionController
import ru.emkn.kotlin.sms.controller.Deleter
import ru.emkn.kotlin.sms.controller.Editor
import ru.emkn.kotlin.sms.model.Timestamp
import ru.emkn.kotlin.sms.view.GUI
import ru.emkn.kotlin.sms.view.PathChooser

class TimestampTable : Table<Timestamp>() {

    private val timestamps
        get() = transaction { Timestamp.all() }

    override val header = TableHeader(listOf(
        TableColumn<Timestamp>(
            "Time",
            ObjectFields.Time,
            visible = true, readOnly = false,
            comparator = TableComparing.compareByLocalTime(ObjectFields.Time),
            getterGenerator = { { it.time.toString() } }
        ),
        TableColumn(
            "Checkpoint",
            ObjectFields.Name,
            visible = true, readOnly = false,
            comparator = TableComparing.compareByString(ObjectFields.Name),
            getterGenerator = { { it.checkpoint.toString() } }
        ),
        TableColumn(
            "Participant",
            ObjectFields.ID,
            visible = true, readOnly = false,
            comparator = TableComparing.compareByString(ObjectFields.ID),
            getterGenerator = { { it.participant.toString() } }
        )
    ), deleteButton = true)

    inner class TimestampTableRow(private val timestamp: Timestamp) : TableRow() {

        override val id = timestamp.id.value
        override val cells = header.makeTableCells(timestamp, ::saveChanges)

        override fun saveChanges() {
            Editor.editTimestamp(timestamp, changes)
        }

        override fun deleteAction() {
            Deleter.deleteTimestamp(id)
            state = State.Outdated
        }
    }

    override val rows: List<TableRow>
        get() = timestamps.map { TimestampTableRow(it) }

    override val creatingState = GUI.State.CreateTimestamp
    override val loadAction = {
        val timestamps = PathChooser("Choose timestamps", "", "Timestamps").choose()
        CompetitionController.loadTimestamps(timestamps?.toPath())
        state = State.Outdated
    }
}
