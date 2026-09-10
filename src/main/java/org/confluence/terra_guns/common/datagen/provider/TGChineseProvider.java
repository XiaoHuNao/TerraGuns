package org.confluence.terra_guns.common.datagen.provider;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_guns.TerraGuns;

import static org.confluence.terra_guns.common.init.TGEntities.BASE_BULLET_ENTITY;
import static org.confluence.terra_guns.common.init.TGEntities.GRAVITY_BULLET_ENTITY;
import static org.confluence.terra_guns.common.init.TGItems.*;

public class TGChineseProvider extends LanguageProvider {
    public TGChineseProvider(PackOutput output) {
        super(output, TerraGuns.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("effect.terra_guns.ichor", "灵液");
        add("effect.terra_guns.cursed_inferno", "诅咒地狱火");
        add("effect.terra_guns.venom", "毒液");
        add("tooltip.terra_guns.damage", "远程伤害：%s");
        add("tooltip.terra_guns.critical", "暴击率：%s%%");
        add("tooltip.terra_guns.knockback", "击退力：%s");
        add("tooltip.terra_guns.emergency_melee.cooldown", "紧急近战冷却：%s秒");
        add("tooltip.terra_guns.emergency_melee.cooldown.ready", "紧急近战冷却：就绪");
        add("tooltip.terra_guns.ability.silver_particles", "特殊效果：飞行时产生白色粒子，碰撞后产生十字闪光");
        add("tooltip.terra_guns.ability.party_confetti", "特殊效果：命中敌怪或方块时释放彩纸");
        add("tooltip.terra_guns.ability.crystal_split", "特殊效果：命中后向后分裂为 2 枚碎片，伤害为原弹 50%，可穿过方块");
        add("tooltip.terra_guns.ability.chlorophyte_homing", "特殊效果：会追踪附近的目标，并留下明亮的绿色轨迹");
        add("tooltip.terra_guns.ability.meteor_ricochet", "特殊效果：可反弹 1 次或穿透 1 个敌怪，但不能同时进行");
        add("tooltip.terra_guns.ability.nano_ricochet", "特殊效果：击中方块后弹向最近敌怪，造成 66% 伤害，最多弹射 1 次");
        add("tooltip.terra_guns.ability.high_velocity_damage_decay", "特殊效果：每次命中后伤害降低 15%");
        add("tooltip.terra_guns.ability.explosive", "特殊效果：命中后爆炸");
        add("tooltip.terra_guns.ability.ichor_debuff", "特殊效果：施加灵液，降低目标护甲");
        add("tooltip.terra_guns.ability.cursed_debuff", "特殊效果：施加凋零");
        add("tooltip.terra_guns.ability.venom_debuff", "特殊效果：施加中毒");
        add("tooltip.terra_guns.ability.luminite_damage_decay", "特殊效果：每次命中后伤害降低 4%，最低降至 0");
        add("enchantment.terra_guns.emergency_melee", "紧急近战");
        add("enchantment.terra_guns.temporary_reserve", "临时战备");
        add("enchantment.terra_guns.compressed_tactics", "压缩战法");

        addItem(HAND_GUN, "手枪");
        addItem(SHOTGUN, "霰弹枪");
        addItem(FLINTLOCK_PISTOL, "燧发枪");
        addItem(BOOMSTICK, "三发猎枪");
        addItem(THE_UNDERTAKER, "夺命枪");
        addItem(MUSKET, "火枪");
        addItem(MINISHARK, "迷你鲨");
        addItem(BLOWGUN, "吹管");
        addItem(TACTICAL_SHOTGUN, "战术霰弹枪");
        addItem(SNOWBALL_CANNON, "雪球炮");
        addItem(PHOENIX_BLASTER, "凤凰爆破枪");

        addItem(MUSKET_BULLET, "火枪子弹");
        addItem(METEOR_SHOT, "流星弹");
        addItem(SILVER_BULLET, "银弹");
        addItem(CRYSTAL_BULLET, "水晶子弹");
        addItem(CURSED_BULLET, "诅咒弹");
        addItem(CHLOROPHYTE_BULLET, "叶绿弹");
        addItem(HIGH_VELOCITY_BULLET, "高速子弹");
        addItem(ICHOR_BULLET, "灵液弹");
        addItem(VENOM_BULLET, "毒液弹");
        addItem(PARTY_BULLET, "派对子弹");
        addItem(NANO_BULLET, "纳米弹");
        addItem(EXPLODING_BULLET, "爆破弹");
        addItem(GOLDEN_BULLET, "金弹");
        addItem(ENDLESS_MUSKET_POUCH, "无限火枪袋");
        addItem(LUMINITE_BULLET, "夜明弹");
        addItem(TUNGSTEN_BULLET, "钨子弹");

        addEntityType(BASE_BULLET_ENTITY, "子弹");
        addEntityType(GRAVITY_BULLET_ENTITY, "重力子弹");

        add("key.terra_guns.shoot", "射击");
        add("key.terra_guns.aim", "瞄准");
        add("key.terra_guns.inspect", "检视");

        add("creative_tab.terra_guns.gun_tab", "泰拉枪械");
        add("death.attack.bullet_damage", "%1$s 被 %2$s 枪击");
    }
}
