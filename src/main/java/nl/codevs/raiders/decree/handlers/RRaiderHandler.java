package nl.codevs.raiders.decree.handlers;

import nl.codevs.raiders.decree.exceptions.DecreeParsingException;
import nl.codevs.raiders.decree.exceptions.DecreeWhichException;
import nl.codevs.raiders.decree.objects.DecreeParameterHandler;
import nl.codevs.raiders.decree.util.KList;

public class RRaiderHandler implements DecreeParameterHandler<RRaider> {
    @Override
    public KList<RRaider> getPossibilities() {
        return (KList<RRaider>) RRaiderRegistrar.getPlayers().values();
    }

    @Override
    public String toString(RRaider rRaider) {
        return rRaider.getPlayer().getName();
    }

    @Override
    public RRaider parse(String in, boolean force) throws DecreeParsingException, DecreeWhichException {
        KList<RRaider> possibilities = getPossibilities();
        if (in.equals("any") && possibilities.stream().noneMatch(p -> p.getPlayer().getName().equals("any"))) {
            return possibilities.getRandom();
        }
        for (RRaider possibility : possibilities) {
            if (possibility.getPlayer().getName().equals(in)){
                return possibility;
            }
        }
        for (RRaider possibility : possibilities) {
            if (possibility.getPlayer().getName().contains(in) || in.contains(possibility.getPlayer().getName())){
                return possibility;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(RRaider.class);
    }
}
