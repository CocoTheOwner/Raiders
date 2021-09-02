package nl.codevs.raiders;


import nl.codevs.raiders.decree.util.KList;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class RShopRegistrar {
    public static final ConcurrentHashMap<String, RShop> shops = new ConcurrentHashMap<>();

    /**
     * Loads all shops from file
     * @param overwrite Whether to overwrite existing shops with the same name
     */
    public static KList<RShop> loadAll(boolean overwrite) {
        KList<RShop> shopz = new KList<>();
        RShop.loadAll().forEach(shop -> {
            if (overwrite || !shops.containsKey(shop.getName())){
                shops.put(shop.getName(), shop);
                shopz.add(shop);
            }
        });
        return shopz;
    }

    /**
     * Load a shop
     * @param name The shop name
     * @return The loaded {@link RShop}
     */
    public static RShop load(String name){
        return load(name, 0, 0, new KList<>(), null);
    }

    /**
     * Load a shop by name & alternative values
     * @param name The name of the shop
     * @param discount The discount float (1.00 = 100% discount)
     * @param requiredLevel The raider level requirement to use this shop
     * @param items The items to sell in the store
     * @param expireOn The date on which to expire (null = never)
     * @return The {@link RShop}
     */
    public static RShop load(String name, double discount, int requiredLevel, KList<RItem> items, Date expireOn){
        RShop shop = RShop.create(name, discount, requiredLevel, items, expireOn);
        if (shop == null) {
            return null;
        }
        shops.put(shop.getName(), shop);
        return shop;
    }

    /**
     * Save a shop
     * @param name The name of the shop to save
     * @return Whether the shop could be saved or not
     */
    public static boolean save(String name) {
        try {
            return shops.get(name).save();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Store all shops
     */
    public static void saveAll() {
        shops.values().forEach(s -> save(s.getName()));
    }
}
