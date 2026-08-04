package org.confluence.terra_guns.common.definition.behavior;

public final class PartyBulletBehavior extends AbstractBulletBehavior {
    public static final PartyBulletBehavior INSTANCE = new PartyBulletBehavior();

    private PartyBulletBehavior() {
        super("tooltip.terra_guns.ability.party_confetti");
    }
}
