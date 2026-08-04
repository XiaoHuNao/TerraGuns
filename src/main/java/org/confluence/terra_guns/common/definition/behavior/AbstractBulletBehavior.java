package org.confluence.terra_guns.common.definition.behavior;

import org.confluence.terra_guns.common.definition.BulletBehavior;

public abstract class AbstractBulletBehavior implements BulletBehavior {
    private final String tooltipKey;

    protected AbstractBulletBehavior(String tooltipKey) {
        this.tooltipKey = tooltipKey;
    }

    @Override
    public String tooltipKey() {
        return tooltipKey;
    }
}
