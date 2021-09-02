package nl.codevs.raiders.decrees;

import nl.codevs.raiders.decree.objects.Decree;
import nl.codevs.raiders.decree.objects.DecreeCommandExecutor;
import nl.codevs.raiders.decree.objects.DecreeOrigin;
import nl.codevs.raiders.decree.objects.Param;

@Decree(name = "raiders", aliases = {"raider", "raids", "raid"}, description = "Main commands")
public class DecRaid implements DecreeCommandExecutor {

    // This line is a category pointer
    private DecRaidAdmin decAdminRaid;

    @Decree(
            description = "Send hello!",
            origin = DecreeOrigin.PLAYER,
            aliases = "owo",
            sync = true,
            name = "hi"
    )
    public void hello(
            @Param(
                    defaultValue = "Hello!",
                    aliases = "oki",
                    description = "The message to send"
            )
                    String message
    ){
        sender().sendMessage(message);
    }
}
