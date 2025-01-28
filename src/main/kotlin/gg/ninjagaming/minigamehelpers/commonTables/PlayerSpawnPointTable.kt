package gg.ninjagaming.minigamehelpers.commonTables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.float
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object PlayerSpawnPointTable: Table<PlayerSpawnPointEntry>("PlayerSpawnPointIndex") {
    val id = int("entryId").primaryKey().bindTo { it.id }
    val arenaName = varchar("arenaName").bindTo { it.arenaName }
    val x = double("X").bindTo { it.x }
    val y = double("Y").bindTo { it.y }
    val z = double("Z").bindTo { it.z }
    val yaw = float("YAW").bindTo { it.yaw }
    val pitch = float("PITCH").bindTo { it.pitch }
    val isEnabled = int("isEnabled").bindTo { it.isEnabled }
}

interface PlayerSpawnPointEntry: Entity<PlayerSpawnPointEntry>{
    companion object : Entity.Factory<PlayerSpawnPointEntry>()
    val id: Int
    var arenaName: String
    var x: Double
    var y: Double
    var z: Double
    var yaw: Float
    var pitch: Float
    var isEnabled: Int
}