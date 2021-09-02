package nl.codevs.raiders.decrees;

import nl.codevs.raiders.RaiderPlayerRegistrar;
import nl.codevs.raiders.decree.objects.Decree;
import nl.codevs.raiders.decree.objects.DecreeCommandExecutor;
import nl.codevs.raiders.decree.objects.Param;
import org.bukkit.entity.Player;


@Decree(name = "player", description = "Player registrar access")
public class DecRaidAdminPlayer implements DecreeCommandExecutor {
    @Decree(
            description = "Deregister a player from the Raider system"
    )
    public void deregister(
            @Param(
                    description = "The player to deregister"
            )
            Player player,
            @Param(
                    description = "Whether to save the players data or not.",
                    defaultValue = "false"
            )
            boolean save
    ) {
        RaiderPlayerRegistrar.deregister(player, save);
        success("Saved player data for " + player.getName() + "!");
    }

    @Decree(
            description = "Register a player to the Raider system"
    )
    public void register(
            @Param(
                    description = "The player to register"
            )
                    Player player
    ) {
        RaiderPlayerRegistrar.register(player);
        success("Registered " + player.getName() + "!");
    }

    @Decree(
            description = "Reload a player in the Raider system"
    )
    public void reload(
            @Param(
                    description = "The player to reload"
            )
                    Player player,
            @Param(
                    description = "Whether to save the players data or not.",
                    defaultValue = "false"
            )
                    boolean save
    ) {
        RaiderPlayerRegistrar.reload(player, save);
        success("Reloaded player " + player.getName() + "!");
    }

    @Decree(description = "Reload all players in the Raider system")
    public void reloadall(
            @Param(
                    description = "Whether to save the players data or not.",
                    defaultValue = "false"
            )
                    boolean save
    ) {
        RaiderPlayerRegistrar.reloadAll(save);
        success("Reloaded all players!");
    }
}
