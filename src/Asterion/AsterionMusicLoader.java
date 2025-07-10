package Asterion;

import Asterion.ui.ModSet;
import arc.Events;
import arc.audio.Music;
import arc.struct.ArrayMap;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.audio.SoundControl;
import mindustry.core.GameState;
import mindustry.game.EventType.StateChangeEvent;
import mindustry.game.EventType.WorldLoadEvent;
import mindustry.gen.Musics;

import java.lang.reflect.Field;

public class AsterionMusicLoader {
    public static Seq<Music> asAmbientMusic = new Seq<>();
    public static Seq<Music> asDarkMusic = new Seq<>();
    public static Seq<Music> asBossMusic = new Seq<>();

    public static String[] asAmbientList = {"fearInTheAir", "importantTerritory"};
    public static String[] asDarkList = {"yourLastTick"};
    public static String[] asBossList = {"ascendancy"};

    public static Seq<Music> origAmbientMusic;
    public static Seq<Music> origDarkMusic;
    public static Seq<Music> origBossMusic;



    public static Field currentMus;

    // Don't change from outside I trust you by putting it in public
    public static boolean enabled = true;

    public static void load() {
        asAmbientMusic = loadMultiple(asAmbientList, "ambient");
        asDarkMusic = loadMultiple(asDarkList, "dark");
        asBossMusic = loadMultiple(asBossList, "boss");



        origAmbientMusic = Vars.control.sound.ambientMusic.copy();
        origDarkMusic = Vars.control.sound.darkMusic.copy();
        origBossMusic = Vars.control.sound.bossMusic.copy();
    }

    public static Seq<Music> loadMultiple(String[] filenames, String folder) {
        Seq<Music> result = new Seq<>();

        for (String filename : filenames) {
            Music music = Vars.tree.loadMusic(folder + "/" + filename);
            if (music != null) {
                result.add(music);
            } else {
                Log.warn("Failed to load music: " + filename);
            }
        }

        return result;
    }

    public static void attach() {
        Events.on(WorldLoadEvent.class, e -> {
            if (Vars.state.rules.planet.parent != null && Vars.state.rules.planet.parent.name.equals("asterion-asterion")) {
                enableCustomMusic();
            } else if (enabled) {
                disableCustomMusic();
            }
        });

        Events.on(StateChangeEvent.class, e -> {
            if (e.from != GameState.State.menu && e.to == GameState.State.menu) {
                disableCustomMusic();
            }
        });

        reflectCurMus();
    }

    public static void enableCustomMusic() {
        if (!ModSet.getModMusOnly()) {
            Vars.control.sound.ambientMusic = Seq.with(asAmbientMusic).addAll(origAmbientMusic);
            Vars.control.sound.darkMusic = Seq.with(asDarkMusic).addAll(origDarkMusic);
            Vars.control.sound.bossMusic = Seq.with(asBossMusic).addAll(origBossMusic);
        } else {
            Vars.control.sound.ambientMusic = Seq.with(asAmbientMusic);
            Vars.control.sound.darkMusic = Seq.with(asDarkMusic);
            Vars.control.sound.bossMusic = Seq.with(asBossMusic);
        }

        enabled = true;
    }

    public static void disableCustomMusic() {
        Vars.control.sound.ambientMusic = Seq.with(origAmbientMusic);
        Vars.control.sound.darkMusic = Seq.with(origDarkMusic);
        Vars.control.sound.bossMusic = Seq.with(origBossMusic);
        enabled = false;
    }

    public static void reflectCurMus() {
        try {
            currentMus = SoundControl.class.getDeclaredField("current");
            currentMus.setAccessible(true);
        } catch (Exception e) {
            Log.err("Failed to set visibility of music things");
            Log.err(e);
        }
    }
}