package me.absurd.nqueue.commands;

import me.absurd.nqueue.NQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class KickQueueCMD implements CommandExecutor {

    public NQueue nQueue;

    public KickQueueCMD(NQueue nQueue) {
        this.nQueue = nQueue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kickqueue")) {
            if (args.length > 0) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    if (sender.hasPermission("nqueue.kickqueue")) {
                        nQueue.sendForwardPluginMessage(player, player.getName(), "nqueue:kickqueue");
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.KICKED-QUEUE").replace("{player}", player.getName())));
                        nQueue.getPosition().remove(player);
                        nQueue.getQueue().remove(player);
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.NO-PERMISSION")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.ONLY-PLAYERS")));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES.USAGE").replace("{usage}", "/kickqueue {player}")));
            }
        }
        return true;
    }
}
