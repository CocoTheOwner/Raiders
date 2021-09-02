package nl.codevs.raiders.decrees;

import nl.codevs.raiders.decree.objects.*;

@Decree(name = "adminraid", aliases = "ar", description = "Admin Raid Commands")
public class DecRaidAdmin implements DecreeCommandExecutor {
    DecRaidAdminPlayer p;

}
