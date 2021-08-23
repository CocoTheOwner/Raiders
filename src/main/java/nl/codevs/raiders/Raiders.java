package nl.codevs.raiders;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import nl.codevs.raiders.decree.DecreeSystem;
import nl.codevs.raiders.decree.virtual.VirtualDecreeCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Raiders extends JavaPlugin implements DecreeSystem {
    @Override
    public VirtualDecreeCommand getRoot() {
        return null;
    }

    @Override
    public File getJarFile() {
        return getFile();
    }

    @Override
    public BukkitAudiences getAudiences() {
        return BukkitAudiences.create(this);
    }
}
