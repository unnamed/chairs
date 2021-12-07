package team.unnamed.seating.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.seating.SeatingHandler;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.SeatingHandler.CRAWL_PERMISSION;

public class CrawlCommand implements CommandExecutor {

    private final MessageHandler messageHandler;
    private final SeatingHandler seatingHandler;

    public CrawlCommand(MessageHandler messageHandler,
                        SeatingHandler seatingHandler) {
        this.messageHandler = messageHandler;
        this.seatingHandler = seatingHandler;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (messageHandler.sendOnlyPlayersMessage(sender)) {
            return true;
        }

        Player player = (Player) sender;

        if (messageHandler.hasPermission(player, CRAWL_PERMISSION)) {
            seatingHandler.crawl(player);
        }

        return true;
    }

}
