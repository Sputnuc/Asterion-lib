package Asterion.Asworld.blocks;

import Asterion.Asworld.meta.AsStat;
import arc.Core;
import arc.util.Strings;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

public class AccelTurret extends ItemTurret{
    public float reloadTo, reloadFrom;
    public float acceleration;

    public AccelTurret(String name){
        super(name);
    }
    public class AccelerateTurretBuild extends ItemTurret.ItemTurretBuild {
        public float nreload = reloadFrom;
        public void updateTile(){
            if(isShooting()){
                if(nreload > reloadTo) {
                    nreload = nreload - acceleration;
                }
                else {
                    nreload = reloadTo;
                }
            }
            else nreload = reloadFrom;
            reload = nreload;
            super.updateTile();
        }
    }
    @Override
    public void setStats(){
        super.setStats();

        stats.add(AsStat.reloadFrom, reloadFrom/60);
        stats.add(AsStat.reloadTo, reloadTo/60);
    }
        public void setBars() {
        super.setBars();
        addBar("Accelerating", (AccelerateTurretBuild entity) ->
                new Bar(
                        () -> "Speed Up:",
                        () -> Pal.lightOrange,
                        () -> entity.nreload/reloadTo
                )
        );
    }

}
