package org.confluence.terra_guns.common.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGEntities;
import org.confluence.terra_guns.common.init.TGItems;

import java.util.function.Consumer;

public class TGEnglishProvider extends LanguageProvider {
    public TGEnglishProvider(PackOutput output) {
        super(output, TerraGuns.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        Consumer<DeferredHolder<Item, ? extends Item>> itemAction = item -> add(item.get(), LibUtils.toTitleCase(item.getId().getPath()));
        Consumer<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entityAction = item -> add(item.get(), LibUtils.toTitleCase(item.getId().getPath()));

        add("tooltip.terra_guns.damage", "Ranged Damage: %s");
        add("tooltip.terra_guns.critical", "Critical Strike Chance: %s%%");
        add("tooltip.terra_guns.knockback", "Knockback: %s");
        add("tooltip.terra_guns.ability.silver_particles", "Special Effect: Emits white particles in flight and a cross-shaped flash on impact");
        add("tooltip.terra_guns.ability.party_confetti", "Special Effect: Releases confetti when hitting an enemy or block");
        add("tooltip.terra_guns.ability.crystal_split", "Special Effect: Splits backward into 2 shards at 50% damage; shards pass through blocks");
        add("tooltip.terra_guns.ability.chlorophyte_homing", "Special Effect: Homes in on nearby targets and leaves a bright green trail");
        add("tooltip.terra_guns.ability.meteor_ricochet", "Special Effect: Bounces once or pierces one enemy, but cannot do both");
        add("tooltip.terra_guns.ability.nano_ricochet", "Special Effect: Redirects toward the nearest enemy after hitting a block at 66% damage, once");
        add("tooltip.terra_guns.ability.high_velocity_damage_decay", "Special Effect: Damage drops by 15% after each hit");
        add("tooltip.terra_guns.ability.explosive", "Special Effect: Explodes on impact");
        add("tooltip.terra_guns.ability.ichor_debuff", "Special Effect: Applies Ichor and reduces armor");
        add("tooltip.terra_guns.ability.cursed_debuff", "Special Effect: Applies Wither");
        add("tooltip.terra_guns.ability.venom_debuff", "Special Effect: Applies Poison");
        add("tooltip.terra_guns.ability.luminite_damage_decay", "Special Effect: Damage drops by 4% after each hit, down to 0");
        TGItems.GUNS.getEntries().forEach(itemAction);
        TGItems.BULLETS.getEntries().forEach(itemAction);
        TGEntities.ENTITY_TYPES.getEntries().forEach(entityAction);

        add("effect.terra_guns.ichor", "Ichor");
        add("effect.terra_guns.cursed_inferno", "Cursed Inferno");
        add("effect.terra_guns.venom", "Venom");

        add("key.terra_guns.shoot", "Shoot");
        add("key.terra_guns.aim", "Aim");
        add("key.terra_guns.inspect", "Inspect");

        add("creative_tab.terra_guns.gun_tab", "Terra Guns");
    }
}
