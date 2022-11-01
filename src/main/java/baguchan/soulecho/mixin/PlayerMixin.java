package baguchan.soulecho.mixin;

import baguchan.soulecho.api.IEcho;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IEcho {
	private static final EntityDataAccessor<Boolean> DATA_SOUL_ECHO = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}


	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	protected void defineSynchedData(CallbackInfo callbackInfo) {
		this.entityData.define(DATA_SOUL_ECHO, false);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag p_36265_, CallbackInfo callbackInfo) {
		p_36265_.putBoolean("SoulEcho", hasSoulEcho());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag p_21096_, CallbackInfo callbackInfo) {
		this.setSoulEcho(p_21096_.getBoolean("SoulEcho"));
	}

	@Override
	public void setSoulEcho(boolean soulEcho) {
		this.entityData.set(DATA_SOUL_ECHO, soulEcho);
	}

	@Override
	public boolean hasSoulEcho() {
		return this.entityData.get(DATA_SOUL_ECHO);
	}
}
