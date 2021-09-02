package nl.codevs.raiders;

import nl.codevs.raiders.decree.DecreeSystem;
import nl.codevs.raiders.decrees.DecRaid;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Raiders extends JavaPlugin {

    public final DecreeSystem decreeSystem = new DecreeSystem(new DecRaid(), this);
    public static Raiders instance = null;

    public Raiders() {
        instance = this;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return decreeSystem.onTabComplete(sender, args);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return decreeSystem.onCommand(sender, args);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(decreeSystem, this);
        Bukkit.getPluginManager().registerEvents(new RRaiderRegistrar(), this);
        Bukkit.getServer().getOnlinePlayers().forEach(RRaiderRegistrar::register);
        Bukkit.getLogger().info("Loaded " + RShopRegistrar.loadAll(true).size() + " shops");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Goodbye world!");
    }
}
