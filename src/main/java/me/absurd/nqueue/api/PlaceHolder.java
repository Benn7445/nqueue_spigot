package me.absurd.nqueue.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceHolder extends PlaceholderExpansion {

    private NQueueAPI nQueueAPI;

    public PlaceHolder(NQueueAPI nQueueAPI) {
        this.nQueueAPI = nQueueAPI;
    }

    public String getIdentifier() {
        return "nqueue";
    }

    public String getAuthor() {
        return "DevQuartz";
    }

    public String getVersion() {
        return "1.0.8";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if(player != null) {
            if (identifier.equalsIgnoreCase("position")) {
                return String.valueOf(nQueueAPI.getPosition(player));
            } else if (identifier.equalsIgnoreCase("amount")) {
                return String.valueOf(nQueueAPI.getMax(nQueueAPI.getQueue(player)));
            } else if (identifier.equalsIgnoreCase("pqueue")) {
                return String.valueOf(nQueueAPI.getQueue(player) == null ? "n/a" : nQueueAPI.getQueue(player));
            }
        }
        return String.valueOf(nQueueAPI.getMax(identifier));
    }
}