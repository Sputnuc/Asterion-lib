package Asterion;

import Asterion.ui.ModSet;
import arc.Events;
import mindustry.game.EventType;
public class ModEventHandler {
    public static void init() {

        Events.on(EventType.ClientLoadEvent.class, e -> AsterionMusicLoader.attach());
        Events.on(EventType.ClientLoadEvent.class, e -> ModSet.init());

        Events.on(EventType.MusicRegisterEvent.class, e -> AsterionMusicLoader.load());
    }
}

