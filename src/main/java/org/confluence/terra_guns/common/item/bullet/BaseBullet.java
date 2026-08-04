package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.lib.ConfluenceMagicLib;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.definition.BulletBehavior;
import org.confluence.terra_guns.common.definition.BulletDefinition;
import org.confluence.terra_guns.common.definition.BulletImpactEffect;
import org.confluence.terra_guns.common.init.TGDataComponents;

import java.util.List;

/**
 * Item-side representation of ammunition.
 *
 * <p>Damage on collision, homing, splitting and debuffs belong to the
 * projectile runtime. This class only exposes the immutable ammo definition,
 * its behavior strategy and its item presentation.</p>
 */
public class BaseBullet extends Item {
    private final BulletDefinition definition;
    private final BulletPropertyComponent component;
    protected String colorID = "";

    public BaseBullet(Properties properties, BulletDefinition definition) {
        super(prepareProperties(properties, definition));
        this.definition = definition;
        this.component = definition.component();
    }

    public BaseBullet(Properties properties, BulletDefinition definition, BulletBehavior behavior) {
        this(properties, definition.withBehavior(behavior));
    }

    private static Properties prepareProperties(Properties properties, BulletDefinition definition) {
        properties.component(TGDataComponents.BULLET_PROPERTY_COMPONENT.get(), definition.component());
        Object maxStackSize = properties.components.map.get(DataComponents.MAX_STACK_SIZE);
        if (maxStackSize instanceof Integer stackSize && stackSize == 99 && ConfluenceMagicLib.IS_CONFLUENCE_LOAD) {
            properties.stacksTo(9999);
        }
        return properties;
    }

    public BaseBullet(Properties properties, float damage, float velocity, float velocityMultiplier,
                      float knockback, ModRarity rarity, int penetrate, boolean infinity) {
        this(properties, new BulletDefinition(damage, velocity, velocityMultiplier, knockback, penetrate,
                rarity, infinity));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.damage", component.damage())
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.knockback", component.knockback())
                .withStyle(ChatFormatting.GRAY));
        String abilityTooltipKey = definition.behavior().tooltipKey();
        if (!abilityTooltipKey.isEmpty()) {
            tooltipComponents.add(Component.translatable(abilityTooltipKey).withStyle(ChatFormatting.AQUA));
        }
    }

    public String colorID() {
        return colorID;
    }

    public BulletDefinition getDefinition() {
        return definition;
    }

    public BulletBehavior getBehavior() {
        return definition.behavior();
    }

    public BulletImpactEffect getImpactEffect() {
        return definition.impactEffect();
    }

    public static class EmptyBullet extends BaseBullet {
        public EmptyBullet(Properties properties) {
            super(properties, new BulletDefinition(0, 0, 1, 0, 0, ModRarity.WHITE, false));
        }

        void setColorID(String colorID) {
            this.colorID = colorID;
        }
    }
}
