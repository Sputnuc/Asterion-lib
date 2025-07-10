package Asterion.Ascontent;

import Asterion.Asworld.blocks.*;
import Asterion.Asworld.blocks.environment.BiggerVent;
import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ShieldWall;
import mindustry.world.draw.*;
import mindustry.world.meta.Attribute;

import static mindustry.Vars.tilesize;
import static mindustry.content.Blocks.spectre;
import static mindustry.content.Blocks.stone;
import static mindustry.content.Items.*;
import static mindustry.type.ItemStack.with;

public class blocks {
    public static Block
            ShieldWall, obeme, regenwalle,testBiggerVent, strangecore, turbine, counterwall;

    public static void load() {
        ShieldWall = new ShieldWall("Shielded-Wall") {{
            requirements(Category.defense, with(phaseFabric, 70, Items.surgeAlloy, 50, Items.beryllium, 40));
            consumePower(3f / 60f);
            outputsPower = false;
            hasPower = true;
            consumesPower = true;
            conductivePower = true;
            size = 2;
            health = (phaseFabric.hardness * surgeAlloy.hardness * beryllium.hardness) / size;
            armor = 15f;
            shieldHealth = health/2f;
        }};
        regenwalle = new RegenWall("regeneration-Wall"){{
            requirements(Category.defense, with(surgeAlloy, 50, Items.phaseFabric, 80));
            consumePower(12/60f);
            consumesPower = true;
            conductivePower = true;
            size = 3;
            health = 1100;
            regenPerTick = 1;
            effectInterval = 1;
            regenColor = Color.valueOf("9f3cb0");
            armor = 3;
        }};
        {
            {
                testBiggerVent = new BiggerVent("test-bigger-vent", 1) {{
                    blendGroup = parent = stone;
                    attributes.set(Attribute.steam, 1f);
                }};
                obeme = new AccelTurret("sdsdwefef") {{
                    requirements(Category.turret, with(Items.copper, 1, Items.lead, 1));
                    reload = 1000;
                    range = 270;
                    maxAccel = 105f;
                    speedUpPerShoot = 1.25f;
                    cooldownSpeed = 0.5f;
                    size = 3;
                    shootCone = 5;
                    shake = 2.5f;
                    health = 500;
                    inaccuracy = 4;
                    shootSound = Sounds.shootBig;
                    ammo(
                            Items.graphite, new BasicBulletType(9f, 55) {{
                                damage = 55;
                                width = 13f;
                                height = 17f;
                                lifetime = 30f;
                                speed = 9;
                                ammoMultiplier = 1;
                            }}
                    );
                }};
                strangecore = new GeneratorCore("aaa"){{
                    size = 3;
                    requirements(Category.effect, with(Items.copper, 1));
                }};
                turbine = new WindTurbine("turbine"){{
                    requirements(Category.power, with(Items.lead, 1));
                    size = 2;
                    range = 10;
                    powerProduction = 32/60f;
                    maxObstructions = (this.range * this.range) - (this.size * this.size)/2;
                    drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator", 5, true));
                }};
                counterwall = new CounterWall("counterWall"){{
                    requirements(Category.defense, with(lead, 85, silicon, 75, surgeAlloy, 80));
                    size = 2;
                    canCountered = 15;
                    coolDown = 300;
                    health = 1000;
                }};
            }
        }
    }
}