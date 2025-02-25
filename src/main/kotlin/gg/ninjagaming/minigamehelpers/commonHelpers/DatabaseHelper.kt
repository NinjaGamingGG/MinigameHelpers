package gg.ninjagaming.minigamehelpers.commonHelpers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import gg.ninjagaming.minigamehelpers.commonTables.sqlTableString
import org.bukkit.configuration.file.FileConfiguration
import org.ktorm.database.Database
import kotlin.apply
import kotlin.isInitialized
import kotlin.use

/**
 * A helper object to manage database connections, initialization, and table setup.
 * Provides functionality for establishing connections to a MySQL database using HikariCP,
 * managing the lifecycle of the database connection, and initializing required tables.
 */
object DatabaseHelper {
    private lateinit var database: Database
    private lateinit var dataSource: HikariDataSource


    /**
     * Establishes a connection to a MySQL database using configuration details provided in a FileConfiguration object.
     * The method initializes a HikariCP connection pool to manage database connections.
     *
     * @param config The FileConfiguration object containing database connection details such as "host", "port", "database", "user", and "password".
     */
    fun connectMysql(config: FileConfiguration){
        if (::database.isInitialized)
            return

        val hostString = config.getString("mysql.host")?: "localhost"
        val portString = config.getString("mysql.port")?: "3306"
        val databaseString = config.getString("mysql.databaseName")?: "oitc"
        val userString = config.getString("mysql.userName")?: "root"
        val passwordString = config.getString("mysql.password")?: ""

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://${hostString}:${portString}/${databaseString}"
            username = userString
            password = passwordString
            idleTimeout = 10000
            maximumPoolSize = 10
            minimumIdle = 10
        }

        dataSource = HikariDataSource(hikariConfig)

        synchronized(this) {
            if (::database.isInitialized)
                return

            try {
                database = Database.connect(dataSource)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Closes the MySQL database connection by shutting down the HikariCP connection pool.
     * This method ensures that all resources tied to the connection are released properly.
     *
     * It is recommended to call this method during the shutdown phase of the application or plugin to avoid resource leaks.
     */
    fun disconnectMysql(){
        if (::dataSource.isInitialized)
            dataSource.close()
    }

    /**
     * Retrieves the database instance used for managing database connections and queries.
     *
     * @return The Database instance currently initialized.
     */
    fun getDatabase(): Database{
        return database
    }

    /**
     * Initializes database tables using the provided SQL strings. If the `initCommonTables` flag is set to true,
     * additional common table SQL strings will be appended to the initialization process.
     *
     * @param sqlStrings An array of SQL strings used to create or initialize database tables.
     * @param initCommonTables A boolean flag indicating whether to include common table SQL strings in the initialization process.
     *                         Defaults to `true`.
     */
    fun initTables(sqlStrings: Array<String> = arrayOf(""), initCommonTables: Boolean = true){
        if (!::database.isInitialized)
            return

        var tableStrings = sqlStrings

        if (initCommonTables)
            tableStrings = tableStrings.plus(getCommonTableSqlStrings())

        tableStrings.forEach { sqlString ->
            database.useConnection { connection ->
                connection.createStatement().use { statement ->
                    statement.execute(sqlString)
                }
            }

        }
    }

    private fun getCommonTableSqlStrings(): Array<String>{
        return arrayOf("CREATE TABLE IF NOT EXISTS ConfigurationIndex(" +
                "entryId INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "entryName VARCHAR(255)," +
                "entryDescription VARCHAR(255)," +
                "defaultLobby VARCHAR(255)," +
                "isEnabled TINYINT(1),"  +
                "minimumPlayers INT," +
                "maximumPlayers INT)",

            "CREATE TABLE IF NOT EXISTS ArenaConfigurationIndex(" +
                    "entryId INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "arenaName VARCHAR(255)," +
                    "arenaDescription VARCHAR(255)," +
                    "arenaWorld VARCHAR(255)," +
                    "arenaCenter VARCHAR(255)," +
                    "arenaEnabled TINYINT(1)," +
                    "arenaIcon VARCHAR(255))",

            "CREATE TABLE IF NOT EXISTS PlayerSpawnPointIndex (" +
                    "entryId INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "arenaName VARCHAR(255)," +
                    "X DOUBLE," +
                    "Y DOUBLE," +
                    "Z DOUBLE," +
                    "YAW FLOAT," +
                    "PITCH FLOAT," +
                    "isEnabled TINYINT(1))",

            sqlTableString
            )
    }
}