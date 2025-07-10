package Asterion.Asworld.blocks;

import arc.Core;
import arc.util.Strings;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class GeneratorCore extends CoreBlock {
    public float passiveEnergyOut = 60 / 60f;
    public GeneratorCore(String name) {
        super(name);
        hasPower = true;
        outputsPower = true;
        conductivePower= true;
        consumesPower= false;
    }
    @Override
    public void setStats(){
        super.setStats();
        stats.add(Stat.basePowerGeneration, passiveEnergyOut * 60, StatUnit.powerSecond);
    };
    @Override
    public void setBars(){
        super.setBars();
        addBar("poweroutput", (GeneratorCoreBuild entity) ->
                new Bar(
                        () -> Core.bundle.format("bar.poweroutput", Strings.fixed(passiveEnergyOut * 60, 1)),
                        () -> Pal.powerBar,
                        () -> 1f
                )
        );
    }


    public class GeneratorCoreBuild extends CoreBuild {

        @Override
        public void onProximityUpdate(){
            super.onProximityUpdate();
            if(!allowUpdate()){
                enabled = false;
            }
        }
        @Override
        public float getPowerProduction(){
            return enabled ? passiveEnergyOut : 0f;
        }

    }

}
