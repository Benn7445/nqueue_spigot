package me.absurd.nqueue;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.absurd.nqueue.api.NQueueAPI;
import me.absurd.nqueue.api.PlaceHolder;
import me.absurd.nqueue.commands.*;
import me.absurd.nqueue.listeners.PluginMessageReceive;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class NQueue extends JavaPlugin {

    private HashMap<String, Integer> max = new HashMap<>();
    private HashMap<Player, Integer> position = new HashMap<>();
    private HashMap<Player, String> queue = new HashMap<>();
    public HashMap<String, Integer> getMax(){return max;}
    public HashMap<Player, Integer> getPosition(){return position;}
    public HashMap<Player, String> getQueue(){return queue;}

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerLicense();
        new NQueueAPI(this);
        getCommand("kickqueue").setExecutor(new KickQueueCMD(this));
        if(isEnabled()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "nqueue:nqueue");
            getServer().getMessenger().registerIncomingPluginChannel(this, "nqueue:nqueue", new PluginMessageReceive(this));
            getCommand("joinqueue").setExecutor(new JoinQueueCMD(this));
            getCommand("leavequeue").setExecutor(new LeaveQueueCMD(this));
            getCommand("pausequeue").setExecutor(new PauseQueueCMD(this));
            getCommand("unpausequeue").setExecutor(new UnPauseQueueCMD(this));
            getCommand("openqueue").setExecutor(new OpenQueueCMD(this));
            getCommand("closequeue").setExecutor(new CloseQueueCMD(this));
            getCommand("nqueue").setExecutor(new NQueueCMD(this));
            registerQueueMessages();
            if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
                PlaceHolder placeHolder = new PlaceHolder(new NQueueAPI(this));
                placeHolder.register();
            }
        }
    }

    @Override
    public void onDisable() {
    }

    private void registerLicense(){
        //if(!new License(getConfig().getString("LICENSE"), "http://benvedo440.440.axc.nl/verify.php", this).register()) {
            //Bukkit.getPluginManager().disablePlugin(this);
        //}
    }

    public void sendForwardPluginMessage(Player player, final String finalMessage, String channel) {
        NQueue nQueue = this;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        if (channel.contains("nqueue:")) {
            out.writeUTF(channel);
        } else {
            out.writeUTF("nqueue:" + channel);
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        try {
            dataStream.writeUTF(finalMessage);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to send plugin message to bungeecord");
            return;
        }
        byte[] data = byteStream.toByteArray();
        out.writeShort(data.length);
        out.write(data);
        player.sendPluginMessage(nQueue, "nqueue:nqueue", out.toByteArray());
    }

    public void registerQueueMessages(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    List<String> queues = new ArrayList<>();
                    for (String queue : getQueue().values()) {
                        if (!queues.contains(queue)) {
                            queues.add(queue);
                        }
                    }
                    if (getQueue().containsKey(player) && getPosition().containsKey(player) && getPosition().get(player) != 0) {
                        for (String s : getConfig().getStringList("MESSAGES.POSITION-MESSAGE")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("{queue}", queue.get(player).replace("donator-", "")).replace("{position}", position.get(player) + "").replace("{amount}", max.get(queue.get(player)) + "")));
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, getConfig().getInt("POS-MESSAGE-TIME") * 20);
    }
}