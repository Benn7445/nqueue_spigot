package me.absurd.nqueue.commands;

import me.absurd.nqueue.NQueue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenQueueCMD implements CommandExecutor {

    public NQueue nQueue;

    public OpenQueueCMD(NQueue nQueue) {
        this.nQueue = nQueue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("openqueue")){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("nqueue.openqueue")) {
                    if (args.length > 0) {
                        nQueue.sendForwardPluginMessage(player, player.getName() + "/" + args[0], "nqueue:open");
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.USAGE").replace("{usage}", "/openqueue {queue}")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.NO-PERMISSION")));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.ONLY-PLAYERS")));
            }
        }
        return true;
    }
}
