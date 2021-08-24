package nl.codevs.raiders.decrees;

import nl.codevs.raiders.decree.objects.Decree;
import nl.codevs.raiders.decree.objects.DecreeNodeExecutor;
import nl.codevs.raiders.decree.objects.DecreeOrigin;
import nl.codevs.raiders.decree.objects.Param;
import org.bukkit.entity.Player;

@Decree(name = "sub", description = "Sub commands")
public class SubCommandClass implements DecreeNodeExecutor {

    @Decree(
            description = "Kill a player",
            origin = DecreeOrigin.PLAYER,
            aliases = "ded"
    )
    public void hello(
            @Param(
                    defaultValue = "self",
                    description = "The player to kill"
            )
                    Player player
    ){
        player.setHealth(0.0d);
    }
}
