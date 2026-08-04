package org.confluence.terra_guns.api.client.animation;

/** Controls what happens when a hand is already playing another animation. */
public enum HandAnimationPlayMode {
    /** Replace the current animation only when the new animation has equal or higher priority. */
    INTERRUPT,
    /** Always replace the current animation and clear its queue. */
    FORCE,
    /** Start only when the hand is idle. */
    IF_IDLE,
    /** Start now when idle, otherwise append to the hand's queue. */
    QUEUE
}
