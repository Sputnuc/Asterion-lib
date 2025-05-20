package Asterion.Ascontent;

import Asterion.Asworld.blocks.AccelTurret;
import Asterion.Asworld.blocks.environment.BiggerVent;
import mindustry.content.Items;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ShieldWall;
import mindustry.world.meta.Attribute;

import static mindustry.Vars.tilesize;
import static mindustry.content.Blocks.stone;
import static mindustry.type.ItemStack.with;

public class blocks {
    public static Block
            ShieldWall, obeme, testBiggerVent;

    public static void load() {
        ShieldWall = new ShieldWall("Shielded-Wall") {{
            requirements(Category.defense, with(Items.phaseFabric, 70, Items.surgeAlloy, 50, Items.beryllium, 40));
            consumePower(3f / 60f);
            outputsPower = false;
            hasPower = true;
            consumesPower = true;
            conductivePower = true;
            health = (70 * 50 * 4) / 2;
            armor = 15f;
            size = 2;
            shieldHealth = 2000;
        }};
        {
            {
                testBiggerVent = new BiggerVent("test-bigger-vent", 1) {{
                    blendGroup = parent = stone;
                    attributes.set(Attribute.steam, 1f);
                }};
                obeme = new AccelTurret("sdsdwefef") {{
                    requirements(Category.turret, with(Items.copper, 1));
                    reload = 180;
                    range = 270;
                    maxBoost = 12f;
                    acceleration = 0.001f;
                    deceleration = 0.1f;

                    // Параметры перегрева
                    heatMax = 750f;
                    heatPerShot = 0.5f;
                    coolingRate = 1.5f;
                    overheatPenalty = 0.5f;

                    size = 3;
                    health = 500;
                    ammo(
                            Items.graphite, new BasicBulletType(9f, 20) {{
                                width = 7f;
                                height = 9f;
                                lifetime = 30f;
                                speed = 9;
                                ammoMultiplier = 2;
                            }}
                    );
                }};
            }
        }
    }
}