package Asterion.Asworld.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;

import static mindustry.Vars.tilesize;

public class AccelTurret extends ItemTurret {
    // Настройки ускорения
    public float maxBoost = 1.5f; // Максимальный множитель скорости (150%)
    public float acceleration = 0.02f; // Скорость разгона
    public float deceleration = 0.03f; // Скорость замедления

    // Настройки перегрева
    public float heatMax = 100f;
    public float heatPerShot = 0.5f;
    public float coolingRate = 1f;
    public float overheatPenalty = 0.5f; // Штраф при перегреве

    // Эффекты
    public Effect boostEffect = Fx.none;
    public Effect overheatEffect = Fx.melting;
    public Color heatColor = Pal.redLight;

    public AccelTurret(String name){
        super(name);
        stats = new Stats();
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.reload, Strings.autoFixed((maxBoost - 1f) * 100, 1) + "%", StatUnit.none);
        stats.add(Stat.ammo, StatValues.ammo(ammoTypes));
    }

    @Override
    public void setBars() {
        super.setBars();

        // Шкала ускорения
        addBar("boost", (AccelTurretBuild entity) ->
                new Bar(
                        () -> {
                            float boostValue = entity.currentBoost - 1f;
                            return boostValue > 0.01f ?
                                    Core.bundle.format("bar.boost", Strings.autoFixed(boostValue * 100, 1)) :
                                    Core.bundle.get("bar.standby");
                        },
                        () -> entity.getBarColor(),
                        () -> Math.max(0f, entity.currentBoost - 1f) / (maxBoost - 1f) // Фикс прогресса
                )
        );

        // Шкала нагрева (с использованием Tmp.c1)
        addBar("heat", (AccelTurretBuild entity) ->
                new Bar(
                        () -> Core.bundle.format("bar.heat", Strings.autoFixed(entity.heat / heatMax * 100, 1)),
                        () -> {
                            float t = entity.heat / heatMax;
                            return Tmp.c1.set(
                                    Pal.lightOrange.r + (heatColor.r - Pal.lightOrange.r) * t,
                                    Pal.lightOrange.g + (heatColor.g - Pal.lightOrange.g) * t,
                                    Pal.lightOrange.b + (heatColor.b - Pal.lightOrange.b) * t
                            );
                        },
                        () -> entity.heat / heatMax
                )
        );
    }

    public class AccelTurretBuild extends ItemTurretBuild {
        public float currentBoost = 1f; // Текущий множитель скорости
        public float heat = 0f;
        public boolean overheated = false;
        public float boostProgress = 0f;

        @Override
        public void updateTile(){
            // Управление ускорением
            if(isShooting() && !overheated){
                boostProgress = Mathf.lerpDelta(boostProgress, 1f, acceleration);
                currentBoost = 1f + (maxBoost - 1f) * boostProgress;

                // Нагрев при стрельбе
                heat += heatPerShot * delta() * (currentBoost / maxBoost);
            } else {
                boostProgress = Mathf.lerpDelta(boostProgress, 0f, deceleration);
                currentBoost = Mathf.lerpDelta(currentBoost, 1f, deceleration);

                // Охлаждение
                heat = Math.max(heat - coolingRate * delta(), 0f);
            }

            // Проверка перегрева
            if(heat >= heatMax && !overheated){
                overheated = true;
                overheatEffect.at(x, y);
            }

            // Сброс перегрева
            if(overheated && heat <= 0){
                overheated = false;
                boostProgress = 0f;
            }

            super.updateTile();
        }

        @Override
        protected void updateShooting(){
            float reloadMultiplier = overheated ? currentBoost * overheatPenalty : currentBoost;

            if(reloadCounter >= reload / reloadMultiplier){
                shoot(peekAmmo());
                reloadCounter = 0f;
            } else {
                reloadCounter += delta() * reloadMultiplier;
            }
        }

        public Color getBarColor() {
            if(overheated) return Pal.remove;
            if(currentBoost <= 1f + 0.01f) return Pal.gray;
            return Tmp.c1.set(Pal.lightOrange).lerp(Tmp.c2.set(Pal.accent),
                    (currentBoost - 1f) / (maxBoost - 1f));
        }

        @Override
        public void draw(){
            super.draw();

            // Визуализация нагрева
            if(heat > 0){
                Draw.color(heatColor, Color.white, Mathf.absin(Time.time, 5f, 0.5f));
                Draw.alpha(heat / heatMax * 0.6f);
                Fill.circle(x, y, size * tilesize / 2f);
                Draw.reset();
            }
        }
    }
}