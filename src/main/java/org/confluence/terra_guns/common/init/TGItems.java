package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.common.item.gun.CustomGun;
import org.confluence.terra_guns.common.item.gun.Shotgun;

import java.util.function.Function;

/// tr2mc数值转换
///
/// 时间 三分之一
///
/// 伤害 二分之一，然后适当偏下0.5f-1f的范围
///
/// 速度 八分之一
///
/// 击退 二十分之一
public class TGItems {
    public static final DeferredRegister<Item> GUNS = DeferredRegister.create(Registries.ITEM, TerraGuns.MODID);
    public static final DeferredRegister<Item> BULLETS = DeferredRegister.create(Registries.ITEM, TerraGuns.MODID);
    public static final DeferredRegister<Item> OTHER = DeferredRegister.create(Registries.ITEM, TerraGuns.MODID);

    public static final RegistryObject<BaseGun> BLOWGUN = registerItem(GUNS, "blowgun", properties -> new CustomGun(properties, 8, 2.8f, 1.4f, 0.17f, 0.04f, 0, ModRarity.WHITE, 0.08f)); // 吹管
    public static final RegistryObject<BaseGun> SNOWBALL_CANNON = registerItem(GUNS, "snowball_cannon", properties -> new CustomGun(properties, 3, 5, 1.4f, 0.05f, 0.04f, 3.0f, ModRarity.BLUE, 0.05f)); // 雪球炮
    public static final RegistryObject<BaseGun> FLINTLOCK_PISTOL = registerItem(GUNS, "flintlock_pistol", properties -> new BaseGun(properties, 5, 7f, 0.7f, 0.05f, 0.10f, 0, ModRarity.BLUE)); // 燧发枪
    public static final RegistryObject<BaseGun> THE_UNDERTAKER = registerItem(GUNS, "the_undertaker", properties -> new BaseGun(properties, 9, 5.5f, 0.75f, 0.1f, 0.04f, 0, ModRarity.BLUE)); // 夺命枪
    public static final RegistryObject<BaseGun> MUSKET = registerItem(GUNS, "musket", properties -> new BaseGun(properties, 14, 16f, 1.1f, 0.25f, 0.12f, 0, ModRarity.BLUE)); // 火枪
    public static final RegistryObject<BaseGun> MINISHARK = registerItem(GUNS, "minishark", properties -> new BaseGun(properties, 3, 3.4f, 1.0f, 0.0f, 0.04f, 5.0f, ModRarity.GREEN)); // 迷你鲨
    public static final RegistryObject<BaseGun> BOOMSTICK = registerItem(GUNS, "boomstick", properties -> new Shotgun(properties, 13, 7f, 0.66f, 0.28f, 0.04f, 8.0f, ModRarity.GREEN, 3, 4)); // 三发猎枪
    public static final RegistryObject<BaseGun> HAND_GUN = registerItem(GUNS, "hand_gun", properties -> new BaseGun(properties, 5, 13f, 1.25f, 0.15f, 0.04f, 0, ModRarity.GREEN)); // 手枪
    public static final RegistryObject<BaseGun> PHOENIX_BLASTER = registerItem(GUNS, "phoenix_blaster", properties -> new BaseGun(properties, 4, 16f, 2.50f, 0.10f, 0.04f, 0, ModRarity.ORANGE)); // 凤凰爆破枪
    public static final RegistryObject<BaseGun> SHOTGUN = registerItem(GUNS, "shotgun", properties -> new Shotgun(properties, 15, 12f, 0.9f, 0.375f, 0.04f, 10.0f, ModRarity.LIGHT_RED, 3, 5)); // 霰弹枪
    public static final RegistryObject<BaseGun> TACTICAL_SHOTGUN = registerItem(GUNS, "tactical_shotgun", properties -> new Shotgun(properties, 11, 18f, 0.75f, 0.35f, 0.04f, 12.0f, ModRarity.YELLOW, 6, 6)); // 战术霰弹枪

    public static final RegistryObject<BaseBullet> MUSKET_BULLET = registerItem(BULLETS, "musket_bullet", properties -> new BaseBullet(properties.stacksTo(99), 1.5f, 0.5f, 2, 0.1f, ModRarity.WHITE, 0, false));
    public static final RegistryObject<BaseBullet> SILVER_BULLET = registerItem(BULLETS, "silver_bullet", properties -> new BaseBullet(properties.stacksTo(99), 2.5f, 0.56f, 2, 0.15f, ModRarity.WHITE, 0, false));
    public static final RegistryObject<BaseBullet> TUNGSTEN_BULLET = registerItem(BULLETS, "tungsten_bullet", properties -> new BaseBullet(properties.stacksTo(99), 2.5f, 0.56f, 2, 0.2f, ModRarity.WHITE, 0, false));
    public static final RegistryObject<BaseBullet> METEOR_SHOT = registerItem(BULLETS, "meteor_shot", properties -> new BaseBullet(properties.stacksTo(99), 2f, 0.37f, 2, 0.05f, ModRarity.BLUE, 1, false));
    public static final RegistryObject<BaseBullet> PARTY_BULLET = registerItem(BULLETS, "party_bullet", properties -> new BaseBullet(properties.stacksTo(99), 3f, 0.63f, 3, 0.25f, ModRarity.ORANGE, 0, false));

    public static final RegistryObject<BaseBullet> CRYSTAL_BULLET = registerItem(BULLETS, "crystal_bullet", properties -> new BaseBullet(properties.stacksTo(99), 2.5f, 0.62f, 2, 0.05f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> ICHOR_BULLET = registerItem(BULLETS, "ichor_bullet", properties -> new BaseBullet(properties.stacksTo(99), 4.5f, 0.65f, 3, 0.2f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> CURSED_BULLET = registerItem(BULLETS, "cursed_bullet", properties -> new BaseBullet(properties.stacksTo(99), 4f, 0.62f, 3, 0.2f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> CHLOROPHYTE_BULLET = registerItem(BULLETS, "chlorophyte_bullet", properties -> new BaseBullet(properties.stacksTo(99), 2.5f, 0.62f, 3, 0.22f, ModRarity.LIME, 0, false));
    public static final RegistryObject<BaseBullet> HIGH_VELOCITY_BULLET = registerItem(BULLETS, "high_velocity_bullet", properties -> new BaseBullet(properties.stacksTo(99), 3.5f, 0.5f, 8, 0.2f, ModRarity.ORANGE, 2, false));
    public static final RegistryObject<BaseBullet> EXPLODING_BULLET = registerItem(BULLETS, "exploding_bullet", properties -> new BaseBullet(properties.stacksTo(99), 3f, 0.58f, 3, 0.33f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> GOLDEN_BULLET = registerItem(BULLETS, "golden_bullet", properties -> new BaseBullet(properties.stacksTo(99), 3f, 0.57f, 3, 0.18f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> VENOM_BULLET = registerItem(BULLETS, "venom_bullet", properties -> new BaseBullet(properties.stacksTo(99), 5.5f, 0.66f, 3, 0.21f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> NANO_BULLET = registerItem(BULLETS, "nano_bullet", properties -> new BaseBullet(properties.stacksTo(99), 5.5f, 0.57f, 3, 0.18f, ModRarity.ORANGE, 0, false));
    public static final RegistryObject<BaseBullet> ENDLESS_MUSKET_POUCH = registerItem(BULLETS, "endless_musket_pouch", properties -> new BaseBullet(properties.stacksTo(1), 1.5f, 0.5f, 2, 0.1f, ModRarity.GREEN, 0, true));
    public static final RegistryObject<BaseBullet> LUMINITE_BULLET = registerItem(BULLETS, "luminite_bullet", properties -> new BaseBullet(properties.stacksTo(99), 6f, 0.25f, 6, 0.15f, ModRarity.CYAN, -1, false));

    public static final RegistryObject<BaseBullet.EmptyBullet> EMPTY_BULLET = registerItem(OTHER, "empty_bullet", BaseBullet.EmptyBullet::new);

    private static <T extends Item> RegistryObject<T> registerItem(DeferredRegister<Item> register, String name, Function<Item.Properties, T> function) {
        return register.register(name, () -> function.apply(new Item.Properties()));
    }
}
