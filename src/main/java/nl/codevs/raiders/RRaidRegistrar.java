package nl.codevs.raiders;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public class RRaidRegistrar {
    @Getter
    private static final ConcurrentHashMap<String, RRaid> raids = new ConcurrentHashMap<>();
}
