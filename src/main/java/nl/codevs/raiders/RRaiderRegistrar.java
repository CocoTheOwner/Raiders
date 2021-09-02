package nl.codevs.raiders;

import lombok.Getter;
import nl.codevs.raiders.decree.util.KList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RRaiderRegistrar implements Listener {

    @Getter
    private static final ConcurrentHashMap<UUID, RRaider> players = new ConcurrentHashMap<>();

    @EventHandler
    public void on(PlayerJoinEvent e) {
        register(e.getPlayer());
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        deregister(e.getPlayer(), true);
    }

    /**
     * Register a player
     * @param player player
     */
    public static void register(Player player){
        if (players.containsKey(player.getUniqueId())) {
            Raiders.instance.getLogger().warning("Player " + player.getName() + " registered but was already registered!");
        }
        try {
            players.put(player.getUniqueId(), RRaider.create(player));
            Raiders.instance.getLogger().info("Registered new player: " + player.getName());
            Raiders.instance.getLogger().info("Currently have " + players.size() + " registered players");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Deregister a player
     * @param player player
     * @param save whether to save or not
     */
    public static void deregister(Player player, boolean save){
        if (!players.containsKey(player.getUniqueId())) {
            Raiders.instance.getLogger().warning("Player " + player.getName() + " left but was not registered!");
        }
        try {
            if (save) {
                players.remove(player.getUniqueId()).save();
            }
            Raiders.instance.getLogger().info("Unregistered player: " + player.getName());
            Raiders.instance.getLogger().info("Currently have " + players.size() + " registered players");
        } catch (Throwable ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Reload player with saving
     * @param player player
     * @param save whether to save before reloading or not
     */
    public static void reload(Player player, boolean save) {
        deregister(player, save);
        register(player);
    }

    /**
     * Reload all players (saving first)
     * @param save Whether to save players that are reloaded or not
     */
    public static void reloadAll(boolean save) {
        Enumeration<UUID> keys = players.keys();
        while (keys.hasMoreElements()) {
            reload(players.get(keys.nextElement()).getPlayer(), save);
        }
    }
}
