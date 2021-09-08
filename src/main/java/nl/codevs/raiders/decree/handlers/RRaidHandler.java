package nl.codevs.raiders.decree.handlers;

import nl.codevs.raiders.decree.exceptions.DecreeParsingException;
import nl.codevs.raiders.decree.exceptions.DecreeWhichException;
import nl.codevs.raiders.decree.objects.DecreeParameterHandler;
import nl.codevs.raiders.decree.util.KList;

public class RRaidHandler implements DecreeParameterHandler<RRaid> {
    @Override
    public KList<RRaid> getPossibilities() {
        return (KList<RRaid>) RRaidRegistrar.getRaids().values();
    }

    @Override
    public String toString(RRaid rRaid) {
        return rRaid.getName();
    }

    @Override
    public RRaid parse(String in, boolean force) throws DecreeParsingException, DecreeWhichException {
        KList<RRaid> possibilities = getPossibilities();
        if (in.equals("any") && possibilities.stream().noneMatch(p -> p.getName().equals("any"))) {
            return possibilities.getRandom();
        }
        for (RRaid possibility : possibilities) {
            if (possibility.getName().equals(in)){
                return possibility;
            }
        }
        for (RRaid possibility : possibilities) {
            if (in.contains(possibility.getName()) || possibility.getName().contains(in)){
                return possibility;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(RRaid.class);
    }
}
