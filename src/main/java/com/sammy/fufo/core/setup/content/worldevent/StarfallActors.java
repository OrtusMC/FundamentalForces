package com.sammy.fufo.core.setup.content.worldevent;

import com.sammy.fufo.common.worldevents.starfall.StarfallActor;
import com.sammy.fufo.common.worldevents.starfall.actors.AsteroidStarfallActor;
import com.sammy.fufo.common.worldevents.starfall.actors.BlankStarfallActor;
import com.sammy.fufo.common.worldevents.starfall.actors.InitialStarDebrisStarfallActor;
import com.sammy.fufo.common.worldevents.starfall.actors.SpaceDebrisStarfallActor;

import java.util.HashMap;

public class StarfallActors {
    public static HashMap<String, StarfallActor> ACTORS = new HashMap<>();

    public static final StarfallActor BLANK = registerActor(new BlankStarfallActor());
    public static final StarfallActor ASTEROID = registerActor(new AsteroidStarfallActor());
    public static final StarfallActor SPACE_DEBRIS = registerActor(new SpaceDebrisStarfallActor());
    public static final StarfallActor INITIAL_SPACE_DEBRIS = registerActor(new InitialStarDebrisStarfallActor());

    private static StarfallActor registerActor(StarfallActor actor) {
        ACTORS.put(actor.id, actor);
        return actor;
    }
}
