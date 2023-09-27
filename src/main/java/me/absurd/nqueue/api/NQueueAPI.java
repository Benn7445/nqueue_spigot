package me.absurd.nqueue.api;

import me.absurd.nqueue.NQueue;
import org.bukkit.entity.Player;

public class NQueueAPI {
    public static NQueue nQueue;

    private static void setnQueue(NQueue nQueue) {
        NQueueAPI.nQueue = nQueue;
    }

    public NQueueAPI(NQueue nQueue) {
        setnQueue(nQueue);
        instance = this;
    }

    public static NQueueAPI instance;

    public String getPosition(Player player){
        if(nQueue.getQueue().containsKey(player) && nQueue.getPosition().containsKey(player)) {
            return nQueue.getPosition().get(player) + "";
        }
        return "-";
    }

    public String getMax(String queue){
        for(String queueS : nQueue.getMax().keySet()){
            if(queueS.equalsIgnoreCase(queue)){
                return nQueue.getMax().get(queueS) + "";
            }
        }
        return "-";
    }

    public String getQueue(Player player){
        if(nQueue.getQueue().containsKey(player)) {
            return nQueue.getQueue().get(player).replace("donator-", "");
        }
        return null;
    }

    public void joinQueue(Player player, String queue){
        nQueue.sendForwardPluginMessage(player, player.getName() + "/" + queue + "/" + getPermission(player) + "/" + player.hasPermission("nqueue.bypass") + "/" + player.hasPermission("nqueue.donator." + queue), "nqueue:joinqueue");
    }

    public void leaveQueue(Player player){
        nQueue.sendForwardPluginMessage(player, player.getName(), "nqueue:leavequeue");
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
