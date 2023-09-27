package me.absurd.nqueue.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.absurd.nqueue.NQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessageReceive implements PluginMessageListener {

    public NQueue nQueue;

    public PluginMessageReceive(NQueue nQueue) {
        this.nQueue = nQueue;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        if (!channel.equals("nqueue:nqueue")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        in.readUTF();
        in.readUTF();
        String subChannel = in.readUTF();
        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        String actualMessage;
        try {
            actualMessage = msgin.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        switch (subChannel) {
            case "nqueue:message": {
                String[] s = actualMessage.split("/");
                Player sender = Bukkit.getPlayer(s[1]);
                if (sender != null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', nQueue.getConfig().getString("MESSAGES." + s[0]).replace("{queue}", s[2].replace("donator-", "")).replace("{player}", s[1])));
                }
                break;
            }
            case "nqueue:queue": {
                String[] s = actualMessage.split("/");
                Player sender = Bukkit.getPlayer(s[0]);
                String queue = s[1];
                if (sender != null && !queue.equalsIgnoreCase("null")) {
                    nQueue.getQueue().put(sender, queue);
                }
                break;
            }
            case "nqueue:position": {
                String[] s = actualMessage.split("/");
                Player sender = Bukkit.getPlayer(s[0]);
                int position = Integer.parseInt(s[1]);
                if (sender != null) {
                    nQueue.getPosition().put(sender, position);
                }
                break;
            }
            case "nqueue:positionall": {
                String[] s = actualMessage.split("/");
                for (String strings : s) {
                    Player players = Bukkit.getPlayer(strings.split(":")[0]);
                    int position = Integer.parseInt(strings.split(":")[1]);
                    if (players != null) {
                        nQueue.getPosition().put(players, position);
                    }
                }
                break;
            }
            case "nqueue:maxamount": {
                String[] s = actualMessage.split("/");
                String sender = s[0];
                int position = Integer.parseInt(s[1]);
                nQueue.getMax().put(sender, position);
                break;
            }
            case "nqueue:askpriority": {
                Player target = Bukkit.getPlayer(actualMessage);
                if (target != null) {
                    nQueue.sendForwardPluginMessage(player, getPermission(target) + "/" + target.getName(), "nqueue:priority");
                }
                break;
            }
            case "nqueue:askbypass": {
                Player target = Bukkit.getPlayer(actualMessage);
                if (target != null && target.hasPermission("nqueue.bypass")) {
                    nQueue.sendForwardPluginMessage(player, target.getName(), "nqueue:bypass");
                }
                break;
            }
        }
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
