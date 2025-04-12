package org.confluence.terra_guns.common.init;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.confluence.terra_guns.TerraGuns;

public class TGTags {
    public static final TagKey<Item> AMMO = ItemTags.create(TerraGuns.asResource("ammo"));
}
