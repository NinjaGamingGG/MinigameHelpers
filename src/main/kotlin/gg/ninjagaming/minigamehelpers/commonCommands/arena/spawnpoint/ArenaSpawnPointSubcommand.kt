package gg.ninjagaming.minigamehelpers.commonCommands.arena.spawnpoint

import org.bukkit.command.CommandSender

object ArenaSpawnPointSubcommand {
    fun spawnPointSubCommand(sender: CommandSender, args: Array<out String>){
        when(args[1]){
            "add" -> ArenaSpawnPointAddSubcommand.addSpawnPoint(sender)
            "list" -> ArenaSpawnPointListSubcommand.listSpawnPoints(sender)
            "remove" -> {
                if(args.size < 3){
                    sender.sendMessage("Invalid usage. /arena spawnpoint remove <id>")
                    return
                }

                ArenaSpawnPointRemoveSubCommand.removeSpawnPoint(args[2].toInt(), sender)
            }

            else -> sender.sendMessage("Invalid usage. /arena spawnpoint <add, list, remove>")
        }

    }
}