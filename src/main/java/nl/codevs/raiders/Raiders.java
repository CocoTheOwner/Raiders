package nl.codevs.raiders;

import nl.codevs.raiders.decree.DecreeSystem;
import nl.codevs.raiders.decree.objects.DecreeCommandExecutor;
import nl.codevs.raiders.decrees.DecRaid;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Raiders extends JavaPlugin implements DecreeSystem {

    @Override
    public Plugin instance() {
        return this;
    }

    @Override
    public boolean doCommandSound() {
        return true;
    }

    @Override
    public DecreeCommandExecutor getRootInstance() {
        return new DecRaid();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return decreeCommand(sender, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return decreeTabComplete(sender, args);
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Hello world!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Goodbeye world!");
    }
}
