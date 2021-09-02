package nl.codevs.raiders;

import com.google.gson.Gson;
import lombok.Data;
import org.bukkit.entity.Player;

import java.io.*;

@Data
public class RRaider {
    private final Player player;
    private double xp = 0;
    private int credits = 0;
    private int bounty = 0; // The more bounty, the higher chance of a raid, and the stronger a raid is

    private RRaider(Player player){
        this.player = player;
    }

    public void save() throws IOException {
        File file = new File(Raiders.instance.getDataFolder() + "/players/" + player.getUniqueId() + ".json");
        file.mkdirs();
        new FileWriter(file).write(new Gson().toJson(this));
    }

    public static RRaider create(Player player) throws IOException {
        File file = new File(Raiders.instance.getDataFolder() + "/players/" + player.getUniqueId() + ".json");
        if (!file.exists()) {
            return new RRaider(player);
        }
        return new Gson().fromJson(new FileReader(file), RRaider.class);
    }
}
