package nl.codevs.raiders;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RaiderPlayerRegistrar implements Listener {

    private static final ConcurrentHashMap<UUID, RaiderPlayer> players = new ConcurrentHashMap<>();

    @EventHandler
    public void on(PlayerJoinEvent e) {
        register(e.getPlayer());
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        deregister(e.getPlayer());
    }

    /**
     * Register a player
     * @param player player
     */
    public void register(Player player){
        if (players.containsKey(player.getUniqueId())) {
            Raiders.instance.getLogger().warning("Player " + player.getName() + " registered but was already registered!");
        }
        try {
            players.put(player.getUniqueId(), RaiderPlayer.create(player));
            Raiders.instance.getLogger().info("Registered new player: " + player.getName());
            Raiders.instance.getLogger().info("Currently have " + players.size() + " registered players");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Deregister player with saving
     * @param player player
     */
    public void deregister(Player player){
        deregister(player, true);
    }

    /**
     * Deregister player without saving
     * @param player player
     */
    public void deregisterForce(Player player){
        deregister(player, false);
    }


    /**
     * Deregister a player
     * @param player player
     * @param save whether to save or not
     */
    private void deregister(Player player, boolean save){
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
     */
    public void reload(Player player) {
        RaiderPlayer raiderPlayer = players.get(player.getUniqueId());
        deregister(player);
        register(player);
    }

    /**
     * Reload player without saving
     * @param player player
     */
    public void reloadForce(Player player) {
        RaiderPlayer raiderPlayer = players.get(player.getUniqueId());
        deregisterForce(player);
        register(player);
    }

    /**
     * Reload all players (saving first)
     */
    public void reloadAll() {
        Enumeration<UUID> keys = players.keys();
        while (keys.hasMoreElements()) {
            reload(players.get(keys.nextElement()).getPlayer());
        }
    }
}
