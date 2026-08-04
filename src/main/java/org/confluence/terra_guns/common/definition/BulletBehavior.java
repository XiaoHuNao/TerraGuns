package org.confluence.terra_guns.common.definition;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.confluence.terra_guns.common.definition.behavior.ChlorophyteHomingBehavior;
import org.confluence.terra_guns.common.definition.behavior.CrystalSplitBehavior;
import org.confluence.terra_guns.common.definition.behavior.CursedDebuffBehavior;
import org.confluence.terra_guns.common.definition.behavior.ExplosiveBulletBehavior;
import org.confluence.terra_guns.common.definition.behavior.HighVelocityDamageDecayBehavior;
import org.confluence.terra_guns.common.definition.behavior.IchorDebuffBehavior;
import org.confluence.terra_guns.common.definition.behavior.LuminiteDamageDecayBehavior;
import org.confluence.terra_guns.common.definition.behavior.MeteorRicochetBehavior;
import org.confluence.terra_guns.common.definition.behavior.NanoRicochetBehavior;
import org.confluence.terra_guns.common.definition.behavior.NormalBulletBehavior;
import org.confluence.terra_guns.common.definition.behavior.PartyBulletBehavior;
import org.confluence.terra_guns.common.definition.behavior.SilverBulletBehavior;
import org.confluence.terra_guns.common.definition.behavior.VenomDebuffBehavior;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;

public interface BulletBehavior {
    BulletBehavior NORMAL = NormalBulletBehavior.INSTANCE;
    BulletBehavior SILVER_PARTICLES = SilverBulletBehavior.INSTANCE;
    BulletBehavior PARTY_CONFETTI = PartyBulletBehavior.INSTANCE;
    BulletBehavior CRYSTAL_SPLIT = CrystalSplitBehavior.INSTANCE;
    BulletBehavior CHLOROPHYTE_HOMING = ChlorophyteHomingBehavior.INSTANCE;
    BulletBehavior METEOR_RICOCHET = MeteorRicochetBehavior.INSTANCE;
    BulletBehavior NANO_RICOCHET = NanoRicochetBehavior.INSTANCE;
    BulletBehavior HIGH_VELOCITY_DAMAGE_DECAY = HighVelocityDamageDecayBehavior.INSTANCE;
    BulletBehavior EXPLOSIVE = ExplosiveBulletBehavior.INSTANCE;
    BulletBehavior ICHOR_DEBUFF = IchorDebuffBehavior.INSTANCE;
    BulletBehavior CURSED_DEBUFF = CursedDebuffBehavior.INSTANCE;
    BulletBehavior VENOM_DEBUFF = VenomDebuffBehavior.INSTANCE;
    BulletBehavior LUMINITE_DAMAGE_DECAY = LuminiteDamageDecayBehavior.INSTANCE;

    default void tick(BaseBulletEntity entity) {
    }

    default boolean onHitBlock(BaseBulletEntity entity, BlockHitResult result) {
        return false;
    }

    default void onHitEntity(BaseBulletEntity entity, EntityHitResult result) {
    }

    default String tooltipKey() {
        return "";
    }
}
