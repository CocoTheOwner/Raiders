package nl.codevs.raiders.decrees;

import nl.codevs.raiders.Raiders;
import nl.codevs.raiders.decree.objects.Decree;
import nl.codevs.raiders.decree.objects.DecreeCommandExecutor;
import nl.codevs.raiders.decree.objects.Param;

@Decree(name = "adminshop", description = "Admin Shop Commands")
public class DecAdminShop implements DecreeCommandExecutor {
    @Decree(description = "Reload all shops")
    public void reload() {
        Raiders.instance.shopRegistrar.saveAll();
        RShopRegistrar.loadAll(true);
    }

    @Decree(description = "Remove a shop")
    public void remove(
            @Param(description = "The shop to remove")
            RShop shop
    ) {
        RShopRegistrar.getShops().remove(shop.getName());
        success("Removed shop: " + shop.getName());
    }

    @Decree(description = "Remove an item from a shop", aliases = "ri")
    public void removeitem(
            @Param(description = "The shop to remove the item from")
                    RShop shop,
            @Param(description = "The items number to remove")
                    int item
    ) {
        RItem i = RShopRegistrar.getShops().get(shop.getName()).getItems().remove(item);
        success("Removed item: " + i.getItemStack().getType().getKey());
    }
}
