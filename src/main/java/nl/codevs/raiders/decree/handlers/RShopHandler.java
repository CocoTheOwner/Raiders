package nl.codevs.raiders.decree.handlers;

import nl.codevs.raiders.RShop;
import nl.codevs.raiders.RShopRegistrar;
import nl.codevs.raiders.decree.exceptions.DecreeParsingException;
import nl.codevs.raiders.decree.exceptions.DecreeWhichException;
import nl.codevs.raiders.decree.objects.DecreeParameterHandler;
import nl.codevs.raiders.decree.util.KList;

public class RShopHandler implements DecreeParameterHandler<RShop> {
    @Override
    public KList<RShop> getPossibilities() {
        return (KList<RShop>) RShopRegistrar.getShops().values();
    }

    @Override
    public String toString(RShop rShop) {
        return rShop.getName();
    }

    @Override
    public RShop parse(String in, boolean force) throws DecreeParsingException, DecreeWhichException {
        KList<RShop> possibilities = getPossibilities();
        if (in.equals("any") && possibilities.stream().noneMatch(p -> p.getName().equals("any"))) {
            return possibilities.getRandom();
        }
        for (RShop possibility : possibilities) {
            if (possibility.getName().equals(in)){
                return possibility;
            }
        }
        for (RShop possibility : possibilities) {
            if (in.contains(possibility.getName()) || possibility.getName().contains(in)){
                return possibility;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(RShop.class);
    }
}
