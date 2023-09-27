package me.absurd.nqueue.commands;

import me.absurd.nqueue.NQueue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinQueueCMD implements CommandExecutor {

    public NQueue nQueue;

    public JoinQueueCMD(NQueue nQueue) {
        this.nQueue = nQueue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("joinqueue")){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    nQueue.sendForwardPluginMessage(player, player.getName() + "/" + args[0] + "/" +
                            getPermission(player) + "/" + sender.hasPermission("nqueue.bypass") + "/" +
                            sender.hasPermission("nqueue.donator." + args[0]), "nqueue:joinqueue");
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            nQueue.getConfig().getString("MESSAGES.USAGE").replace("{usage}", "/joinqueue {queue}")));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.ONLY-PLAYERS")));
            }
        }
        return true;
    }

    public int getPermission(Player player){
        for(String s : nQueue.getConfig().getConfigurationSection("PRIORITY").getKeys(false)){
            if(player.hasPermission(nQueue.getConfig().getString("PRIORITY." + s + ".PERMISSION"))){
                return Integer.parseInt(nQueue.getConfig().getString("PRIORITY." + s + ".PRIORITY"));
            }
        }
        return 1000;
    }
}
