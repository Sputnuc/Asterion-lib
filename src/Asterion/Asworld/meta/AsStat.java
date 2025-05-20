package Asterion.Asworld.meta;

import arc.Core;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class AsStat {
    // Добавляем кастомные статистики
    public static final Stat
            maxBoost = new Stat("maxBoost", StatCat.function), // Максимальное ускорение
            acceleration = new Stat("acceleration", StatCat.function), // Скорость разгона
            heatDissipation = new Stat("heatDissipation", StatCat.function); // Скорость охлаждения

    // Инициализация (если требуется)
    public static void load(){
    }
}