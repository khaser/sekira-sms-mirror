package ru.emkn.kotlin.sms.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import ru.emkn.kotlin.sms.MAX_TEXT_FIELD_SIZE
import ru.emkn.kotlin.sms.io.SingleLineWritable

object RouteTable : IntIdTable("routes") {
    val name: Column<String> = varchar("name", MAX_TEXT_FIELD_SIZE)
}

/**
 * A class for storing a route along which one group of participants runs.
 */
//class Route(val name: String, val checkPoints: List<CheckPoint>) : IntEntity(id), SingleLineWritable {
class Route(id: EntityID<Int>) : IntEntity(id), SingleLineWritable {
    companion object : IntEntityClass<Route>(RouteTable)

    var name by RouteTable.name
    var checkPoints by Checkpoint via RouteCheckpointsTable

    override fun toLine(): List<String?> = listOf(name) + checkPoints.map { it.id.toString() }
}

object RouteCheckpointsTable : IntIdTable("route_checkpoints") {
    val route: Column<EntityID<Int>> = reference("routes", RouteTable)
    val checkpoint: Column<EntityID<Int>> = reference("checkpoints", CheckpointTable)
}

object CheckpointTable : IntIdTable("checkpoints") {
    val weight: Column<String> = varchar("name", MAX_TEXT_FIELD_SIZE)
}

class Checkpoint(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Checkpoint>(CheckpointTable)

    var weigth by CheckpointTable.weight
    var routes by Route via RouteCheckpointsTable
}
