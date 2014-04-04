package com.github.robotics_in_concert.rocon_rosjava_core.rocon_interactions;

/*****************************************************************************
** Enums
*****************************************************************************/

public enum InteractionMode {
    STANDALONE, // unmanaged interaction
    PAIRED,     // paired with master, normally a robot
    CONCERT;    // running inside a concert

    public String toString() { return name().toLowerCase(); }
}
