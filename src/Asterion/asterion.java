package Asterion;

import Asterion.Ascontent.blocks;
import arc.util.*;
import mindustry.mod.*;

public class asterion extends Mod{

    @Override
    public void loadContent(){
        Log.info("Loading some Asterion content.");
        blocks.load();
    }

}
