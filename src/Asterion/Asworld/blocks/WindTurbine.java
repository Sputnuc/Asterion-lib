package Asterion.Asworld.blocks;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.content.Blocks;
import mindustry.entities.units.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.power.*;

import static mindustry.Vars.*;


public class WindTurbine extends PowerGenerator {
    public int range = 6;
    public DrawBlock draw = new DrawDefault();
    public Color baseColor = Pal.accent;
    public Color obstructionColor = Color.valueOf("d98200");
    public int maxObstructions = 10;
    public boolean displayEfficiency = true;

    public WindTurbine(String name) {
        super(name);
        solid = false;
        update = true;
        hasPower = true;
        hasItems = false;
        emitLight = false;
        envEnabled ^= Env.space;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);

        int offsetX = x;
        int offsetY = y;

        x *= tilesize;
        y *= tilesize;
        x += offset;
        y += offset;

        Drawf.dashSquare(baseColor, x, y, range * tilesize);
        int blocksCount = 0;
        int frange = (int) Math.floor(range / 2);

        //countBlocks Cycle
        for (int xi = -frange + 1; xi <= frange; xi++) {
            for(int yi = -frange + 1; yi <= frange; yi++){
                Tile other = world.tile(offsetX+xi, offsetY+yi);
                if (other.solid() && !((xi>-1 && yi>-1) && (xi<size && yi<size)) ){
                    Drawf.selected(other.x, other.y, Blocks.router, obstructionColor);
                    blocksCount++;
                }
            }
        }

        if (displayEfficiency){
            drawPlaceText(Core.bundle.formatFloat("bar.efficiency", 1 * 100, 1), x, y, valid);
        }
    }
    public float getObstructionEff(int obsCount){
        return (float) obsCount < maxObstructions ? Math.max(0,1+(float)Math.sin((float) (Math.PI/(maxObstructions * 2)) * obsCount+Math.PI)) : (float) 0;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list){
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    @Override
    public TextureRegion[] icons(){
        return drawer.finalIcons(this);
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(generationType);
        stats.add(generationType, powerProduction * 60.0f, StatUnit.powerSecond);
    }

    public class WindTurbineBuild extends GeneratorBuild{
        public float totalProgress;
        public int lastChange = -2;
        public int obstructionCount = 0;

        //screw it im making my own indexer
        public int eachTile(int range){
            int rcount = 0;
            int frange = (int) Math.floor(range/2);
            for(int xm = -frange+1;xm<=frange;xm++){
                for(int ym = -frange+1;ym<=frange;ym++){
                    Tile other = tile.nearby(xm,ym);
                    if(other.solid()) {
                        rcount++;
                    }
                }
            }
            //subtract with the block itself
            return rcount - size * size;
        }

        @Override
        public void updateTile(){
            if(lastChange != world.tileChanges){
                lastChange = world.tileChanges;
                obstructionCount = eachTile(range);
            }
            productionEfficiency = getObstructionEff(obstructionCount);
            totalProgress += productionEfficiency * Time.delta;
        }

        @Override
        public void drawSelect(){
            super.drawSelect();

            Drawf.dashSquare(baseColor, x, y, range * tilesize);
            int frange = (int) Math.floor(range/2);
            for (int xi = -frange + 1; xi <= frange; xi++) {
                for(int yi = -frange + 1; yi <= frange; yi++){
                    Tile other = tile.nearby(xi,yi);
                    if (other.solid() && other.build!=this){
                        Drawf.selected(other.x, other.y, Blocks.router, obstructionColor);
                    }
                }
            }
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public float totalProgress(){
            return totalProgress;
        }

    }
}

