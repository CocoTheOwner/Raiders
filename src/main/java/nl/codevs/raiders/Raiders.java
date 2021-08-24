package nl.codevs.raiders;

import nl.codevs.raiders.decree.DecreeExecutor;
import nl.codevs.raiders.decree.DecreeSystem;
import nl.codevs.raiders.decree.util.AtomicCache;
import nl.codevs.raiders.decree.virtual.VirtualDecreeCommand;
import nl.codevs.raiders.decrees.DecRaiders;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Raiders extends JavaPlugin implements DecreeSystem, DecreeExecutor {

    @Override
    public Plugin instance() {
        return this;
    }

    @Override
    public void debug(String message) {
        System.out.println(message);
    }

    private final transient AtomicCache<VirtualDecreeCommand> commandCache = new AtomicCache<>();

    @Override
    public VirtualDecreeCommand getRoot() {
        return commandCache.aquire(() -> {
            try {
                return VirtualDecreeCommand.createRoot(new DecRaiders(), this);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        });
    }
}
