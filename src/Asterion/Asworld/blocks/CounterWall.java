package Asterion.Asworld.blocks;

import Asterion.Asworld.meta.AsStat;
import arc.Core;
import arc.util.Strings;
import arc.util.Time;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.StatUnit;

public class CounterWall extends Wall {
    public CounterWall(String name) {
        super(name);
        update = true;
    }

    public int canCountered = 3;
    public float coolDown = 3 * 60;

    @Override
    public void setStats(){
        super.setStats();
        stats.add(AsStat.canCounters, canCountered);
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("cooldown", (CounterWall.CounterWallBuild entity) ->
                new Bar(
                        () -> Core.bundle.format("bar.cooldown", Strings.autoFixed((entity.counter)* 100, 0)),
                        () -> Pal.powerBar,
                        () -> entity.counter / coolDown
                )
        );
        addBar("counters", (CounterWall.CounterWallBuild entity) ->
                new Bar(
                        () -> Core.bundle.format("bar.counters", Strings.autoFixed(entity.counters, canCountered)),
                        () -> Pal.powerBar,
                        () -> (float) entity.counters / canCountered
                )
        );
    }

    public class CounterWallBuild extends WallBuild{
        public int counters = canCountered;
        private float counter = 0;

        @Override
        public void updateTile(){
            if (counters < canCountered){
                if (counter >= coolDown){
                    counters++;
                    counter = 0;
                }else{
                    counter += delta();
                }
            }
           super.updateTile();
        }

        @Override
        public void damage(float damage){
            if(counters <= 0){
                super.damage(damage);
            }else {
                counters--;
            }
        }
    }

}
