package Asterion.ui;

import arc.Core;
import mindustry.Vars;

public class ModSet{
    public static void init() {
        Vars.ui.settings.addCategory("Asterion", root -> {
            root.checkPref("@settings.onlyModMus", false);
        });
    }

    public static boolean getModMusOnly() {
        return Core.settings.getBool("@settings.onlyModMus", false);
    }
}