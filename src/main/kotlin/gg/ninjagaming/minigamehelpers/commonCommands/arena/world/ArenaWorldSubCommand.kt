package gg.ninjagaming.minigamehelpers.commonCommands.arena.world

import org.bukkit.command.CommandSender

object ArenaWorldSubCommand {
    fun worldSubCommand(sender: CommandSender, args: Array<out String>){
        when(args[1]){
            "enter" -> {
                if(args.size < 3){
                    sender.sendMessage("Invalid usage. /arena world enter <arena>")
                    return
                }

                ArenaWorldEnterSubCommand.enterWorld(args[2], sender)
            }
            "center" -> {
                if(args.size < 4){
                    sender.sendMessage("Invalid usage. /arena world center <arena> <x> <z>")
                    return
                }

                ArenaWorldSetCenterSubcommand.setCenter(args[2], args[3], args[4], sender)
            }
            "set" -> {
                if (args.size < 4) {
                    sender.sendMessage("Invalid usage. /arena world set <arena> <world>")
                    return
                }

                ArenaWorldSetSubcommand.setWorld(args[2], args[3], sender)
            }
            else -> sender.sendMessage("Invalid usage. /arena world <enter, center , set>")
        }
    }
}