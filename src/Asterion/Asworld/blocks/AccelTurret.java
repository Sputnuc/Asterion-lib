package Asterion.Asworld.blocks;

import arc.Core;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.bullet.BulletType;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.turrets.*;

public class AccelTurret extends ItemTurret {
    public float speedUpPerShoot = 2;
    public float maxAccel = 0.5f;
    public float cooldownSpeed = 1;

    public AccelTurret(String name){
        super(name);
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("speedingUp", (AccelTurretBuild entity) ->
                new Bar(
                        () -> Core.bundle.format("bar.speedingUp", Strings.autoFixed((entity.speedUp/maxAccel)* 100, 0)),
                        () -> Pal.powerBar,
                        () -> entity.speedUp / maxAccel
                )
        );
    }
    @Override
    public void init(){
        super.init();
    }

    public  class AccelTurretBuild extends ItemTurretBuild {
     public float speedUp = 0;
        @Override
        public void updateTile() {
            //coolDown progress
            if (!isShooting() || !hasAmmo() || !isActive()){

                if(speedUp>0) {
                    speedUp -= delta() * cooldownSpeed;
                }else {
                 speedUp = 0;
                };
            }
            super.updateTile();
        }
        @Override
        public void updateShooting(){
            //override shooting method
            if (reloadCounter >= reload) {

                BulletType type = peekAmmo();

                shoot(type);
                reloadCounter = 0;
            }
            else
            {
                reloadCounter += (1 + speedUp) * edelta() * peekAmmo().reloadMultiplier * baseReloadSpeed() * coolantMultiplier;
            }
        }
        @Override
        public void shoot(BulletType type){
            //speedUp per shoot
            super.shoot(type);
            if (speedUp < maxAccel){
                speedUp += speedUpPerShoot * edelta();
            }else {
                speedUp = maxAccel;
            }
        }
        @Override
        public void write(Writes write){
            super.write(write);
            write.f(speedUp);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            speedUp = read.f();;
        }
    }
}