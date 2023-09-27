package me.absurd.nqueue.commands;

import me.absurd.nqueue.NQueue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NQueueCMD implements CommandExecutor {

    public NQueue nQueue;

    public NQueueCMD(NQueue nQueue) {
        this.nQueue = nQueue;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nqueue")) {
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------");
            sender.sendMessage(ChatColor.YELLOW + nQueue.getDescription().getFullName() + ":");
            sender.sendMessage(ChatColor.YELLOW + "");
            sender.sendMessage(ChatColor.YELLOW + "Name: " + ChatColor.GRAY + nQueue.getDescription().getName());
            sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.GRAY + nQueue.getDescription().getVersion());
            sender.sendMessage(ChatColor.YELLOW + "");
            sender.sendMessage(ChatColor.YELLOW + "Author: " + ChatColor.GRAY + nQueue.getDescription().getAuthors().get(0));
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------");
            if ((sender.hasPermission("nqueue.*"))) {
                sender.sendMessage(ChatColor.YELLOW + "Customized License: " + ChatColor.GREEN + "Active" +
                        ChatColor.GRAY + " (" + ChatColor.YELLOW + nQueue.getConfig().getString("LICENSE") + ChatColor.GRAY + ")");
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------------");
            }
        }
        return true;
    }
}