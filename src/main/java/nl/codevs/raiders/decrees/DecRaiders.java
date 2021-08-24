package nl.codevs.raiders.decrees;

import nl.codevs.raiders.decree.DecreeExecutor;
import nl.codevs.raiders.decree.annotations.Decree;

@Decree(name = "raiders", aliases = {"raider", "raid"}, description = "Raiders commands")
public class DecRaiders implements DecreeExecutor {

    @Decree
    public void hello(){
        sender().sendMessage("Hello!");
    }
}
