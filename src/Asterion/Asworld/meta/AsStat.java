package Asterion.Asworld.meta;

import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.Stats;

public class AsStat {
    public static final Stat
            regenerationPerTick = new Stat("regenPerTick", StatCat.function),
            canCounters = new Stat("can counters", StatCat.function);
}