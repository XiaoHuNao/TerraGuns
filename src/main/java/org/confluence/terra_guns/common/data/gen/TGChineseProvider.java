package org.confluence.terra_guns.common.data.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_guns.common.init.TGItems;

import static org.confluence.terra_guns.TerraGuns.MODID;

public class TGChineseProvider extends LanguageProvider {
    public TGChineseProvider(PackOutput output) {
        super(output, MODID, "zh_cn");
    }
    @Override
    protected void addTranslations() {
        add(TGItems.FLINTLOCK_PISTOL.get(), "燧发枪");
        add(TGItems.MUSKET.get(), "火枪");
        add(TGItems.THE_UNDERTAKER.get(), "夺命枪");
        add(TGItems.REVOLVER.get(), "左轮手枪");
        add(TGItems.MINISHARK.get(), "迷你鲨");
        add(TGItems.FLARE_GUN.get(), "信号枪");
        add(TGItems.BLOWGUN.get(), "吹箭枪");
        add(TGItems.BLOWPIPE.get(), "吹管");
        add(TGItems.HANGGUN.get(), "手枪");
        add(TGItems.ONYX_BLASTER.get(), "玛瑙爆破枪");
        add(TGItems.PHOENIX_BLASTER.get(), "凤凰爆破枪");
        add(TGItems.SANDGUN.get(), "沙枪");
        add(TGItems.SLIME_GUN.get(), "史莱姆枪");
        add(TGItems.SNIPER_RIFLE.get(), "狙击步枪");
        add(TGItems.SNOWBALL_CANNON.get(), "雪球炮");
        add(TGItems.STAR_CANNON.get(), "星星炮");
        add(TGItems.TACTICAL_SHOTGUN.get(), "战术霰弹枪");
        add(TGItems.UZI.get(), "乌兹冲锋枪");
        add(TGItems.BOOMSTICK.get(), "三发猎枪");
        add(TGItems.SHOTGUN.get(), "霰弹枪");

        add(TGItems.MUSKET_BULLET.get(), "火枪子弹");
        add(TGItems.METEOR_BULLET.get(), "流星子弹");
        add(TGItems.SILVER_BULLET.get(), "银子弹");
        add(TGItems.CRYSTAL_BULLET.get(), "水晶子弹");
        add(TGItems.CURSED_BULLET.get(), "诅咒子弹");
        add(TGItems.CHLOROPHYTE_BULLET.get(), "叶绿弹");
        add(TGItems.HIGH_VELOCITY_BULLET.get(), "高速子弹");
        add(TGItems.ICHOR_BULLET.get(), "灵液弹");
        add(TGItems.VENOM_BULLET.get(), "毒液子弹");
        add(TGItems.PARTY_BULLET.get(), "派对子弹");
        add(TGItems.NANO_BULLET.get(), "纳米子弹");
        add(TGItems.EXPLODING_BULLET.get(), "爆炸子弹");
        add(TGItems.GOLDEN_BULLET.get(), "黄金子弹");
        add(TGItems.ENDLESS_MUSKET_POUCH.get(), "无尽火枪袋");
        add(TGItems.LUMINITE_BULLET.get(), "夜明子弹");
        add(TGItems.TUNGSTEN_BULLET.get(), "钨子弹");

    }
}
