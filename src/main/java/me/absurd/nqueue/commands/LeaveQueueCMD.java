package me.absurd.nqueue.commands;

import me.absurd.nqueue.NQueue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveQueueCMD implements CommandExecutor {

    public NQueue nQueue;

    public LeaveQueueCMD(NQueue nQueue) {
        this.nQueue = nQueue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("leavequeue")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                nQueue.sendForwardPluginMessage(player, player.getName(), "nqueue:leavequeue");
                nQueue.getPosition().remove(player);
                nQueue.getQueue().remove(player);
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.ONLY-PLAYERS")));
            }
        }
        return true;
    }
}
