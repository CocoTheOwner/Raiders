package nl.codevs.raiders;

import com.google.gson.Gson;
import lombok.Getter;
import nl.codevs.raiders.decree.util.KList;

import java.io.*;
import java.util.Date;

@Getter
public class RShop {
    private final String name;
    private final double discount;
    private final int requiredLevel;
    private final KList<RItem> items;
    private final Date expireOn;

    private RShop(String name, double discount, int requiredLevel, KList<RItem> items, Date expireOn){
        this.name = name;
        this.discount = discount;
        this.requiredLevel = requiredLevel;
        this.items = items;
        this.expireOn = expireOn;
    }

    public boolean save() {
        File file = new File(Raiders.instance.getDataFolder() + "/players/" + getName() + ".json");
        file.mkdirs();
        try {
            new FileWriter(file).write(new Gson().toJson(this));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static RShop create(String name, double discount, int requiredLevel, KList<RItem> items, Date expireOn) {
        File file = new File(Raiders.instance.getDataFolder() + "/shops/" + name + ".json");
        if (!file.exists()) {
            return new RShop(name, discount, requiredLevel, items, expireOn);
        }
        try {
            return new Gson().fromJson(new FileReader(file), RShop.class);
        } catch (FileNotFoundException e){
            return null;
        }
    }

    public static KList<RShop> loadAll() {
        Gson g = new Gson();
        KList<RShop> shops = new KList<>();
        File dir = new File(Raiders.instance.getDataFolder() + "/shops/");
        dir.mkdirs();
        for (File file : dir.listFiles()){
            try {
                if (file.isFile() && file.getPath().endsWith(".json")) {
                    shops.add(g.fromJson(new FileReader(file), RShop.class));
                }
            } catch (FileNotFoundException ignored) {

            }
        }
        return shops;
    }
}
