package org.confluence.terra_guns.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.component.BouncesComponent;
import org.confluence.terra_guns.common.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class SimpleItemModelProjectile extends BaseAmmoEntity implements ItemSupplier {
    BouncesComponent bouncesComponent = new BouncesComponent(3);

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(SimpleItemModelProjectile.class, EntityDataSerializers.ITEM_STACK);
    public SimpleItemModelProjectile(EntityType<SimpleItemModelProjectile> type, Level level) {
        super(type, level);
    }

    public SimpleItemModelProjectile(Level level, LivingEntity shooter,ItemStack ammoStack) {
        super(ModEntities.SIMPLE_ITEM_MODEL_PROJECTILE.get(), shooter, 0, 0, 0, level);
        this.setItem(ammoStack);
    }


    public void setItem(ItemStack stack) {
        this.getEntityData().set(DATA_ITEM_STACK, stack);
    }

    @Override @NotNull
    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, this.getItem())
                .resultOrPartial(TerraGuns.LOGGER::error)
                .ifPresent((p_37011_) -> {
                    tag.put("itemStack", p_37011_);
                });
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("itemStack"))
                .resultOrPartial(TerraGuns.LOGGER::error)
                .ifPresent(this::setItem);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        bouncesComponent.onHitBlock(this,pResult);
    }
}
