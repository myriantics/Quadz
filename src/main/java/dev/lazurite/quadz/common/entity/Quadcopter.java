package dev.lazurite.quadz.common.entity;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.component.BindingComponent;
import dev.lazurite.quadz.common.item.RemoteItem;
import dev.lazurite.quadz.common.registry.QuadzDamageTypes;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import dev.lazurite.quadz.common.registry.item.QuadzItems;
import dev.lazurite.quadz.common.util.Search;
import dev.lazurite.quadz.common.item.GogglesItem;
import dev.lazurite.quadz.common.util.BetaflightHelper;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class Quadcopter extends LivingEntity implements TraceableEntity {

    public static final EntityDataAccessor<String> TEMPLATE = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> PREV_TEMPLATE = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> ARMED = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> CAMERA_ANGLE = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.INT);

    private ItemStack quadcopterStack = new ItemStack(QuadzItems.QUADCOPTER);
    private @Nullable UUID ownerUUID = null;
    private Entity cachedOwner = null;

    public Quadcopter(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    public void setQuadcopterStack(ItemStack quadcopterStack) {
        this.quadcopterStack = quadcopterStack;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Server-side only prioritization
            /*
            Optional.ofNullable(getRigidBody()).ifPresent(player -> {
                if (!((ServerPlayer) player).getCamera().equals(this)) {
                    getRigidBody().prioritize(null);
                }
            });

            // Break grass, flowers, etc if the quadcopter is above a certain weight.
            if (this.getRigidBody().getMass() > 0.1f) {
                var block = this.level.getBlockState(this.blockPosition()).getBlock();

                if (block instanceof BushBlock || block instanceof VineBlock) {
                    this.level.destroyBlock(this.blockPosition(), false, this);
                }
            }
             */
            // Hurt entities on collision
            this.level().getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {

                entity.hurt(
                        this.level().damageSources().source(
                                QuadzDamageTypes.QUADCOPTER,
                                this.getOwner(),
                                this
                        ),
                        2.0f
                );
            });
        }

        Search.forPlayer(this).ifPresentOrElse(player -> {
            this.setArmed(true);
            player.quadz$syncJoystick();

            /*
            if (player instanceof ServerPlayer serverPlayer && serverPlayer.getCamera() == this && !player.equals(this.getRigidBody().getPriorityPlayer())) {
                this.getRigidBody().prioritize(player);
            }
             */

            var pitch = player.quadz$getJoystickValue(QuadzCommon.locate("pitch"));
            var yaw = -1 * player.quadz$getJoystickValue(QuadzCommon.locate("yaw"));
            var roll = player.quadz$getJoystickValue(QuadzCommon.locate("roll"));
            var throttle = player.quadz$getJoystickValue(QuadzCommon.locate("throttle")) + 1.0f;

            var rate = player.quadz$getJoystickValue(QuadzCommon.locate("rate"));
            var superRate = player.quadz$getJoystickValue(QuadzCommon.locate("super_rate"));
            var expo = player.quadz$getJoystickValue(QuadzCommon.locate("expo"));

            this.rotate(
                    (float) BetaflightHelper.calculateRates(pitch, rate, expo, superRate, 0.05f),
                    (float) BetaflightHelper.calculateRates(yaw, rate, expo, superRate, 0.05f),
                    (float) BetaflightHelper.calculateRates(roll, rate, expo, superRate, 0.05f)
            );

            /*
            // Decrease angular velocity
            if (throttle > 0.1f) {
                var correction = getRigidBody().getAngularVelocity(new Vector3f()).multLocal(0.5f * throttle);

                if (Float.isFinite(correction.lengthSquared())) {
                    getRigidBody().setAngularVelocity(correction);
                }
            }

             */

            /*
            // Get the thrust unit vector
            // TODO make this into it's own class
            Matrix4f mat = new Matrix4f();
            Matrix4fHelper.fromQuaternion(mat, Axis.XP.rotationDegrees(90f));
            Vector3f vector3f = Matrix4fHelper.matrixToVector(mat);

            // Calculate basic thrust
            Vector3f thrust = new Vector3f().set(unit).multLocal((float) (getThrust() * (Math.pow(throttle, getThrustCurve()))));

            // Calculate thrust from yaw spin
            Vector3f yawThrust = new Vector3f().set(unit).multLocal(Math.abs(yaw * getThrust() * 0.002f));

            // Add up the net thrust and apply the force
            if (Float.isFinite(thrust.length())) {
                getRigidBody().applyCentralForce(thrust.add(yawThrust).multLocal(-1));
            } else {
                QuadzCommon.LOGGER.warn("Infinite thrust force!");
            }*/
        }, () -> {
            this.setArmed(false);

            /*
            if (!this.level.isClientSide) {
                this.getRigidBody().prioritize(null);
            }
            */
        });
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        ItemStack usedStack = player.getItemInHand(interactionHand);

        if (usedStack.getItem() instanceof RemoteItem && (this.getOwner() == null || player == this.getOwner())) {
            if (!player.level().isClientSide()) {
                usedStack.set(QuadzDataComponentTypes.BINDING, new BindingComponent(this.getUUID()));
                player.displayClientMessage(Component.translatable("quadz.message.entity_bound", this.getName()), true);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.interact(player, interactionHand);
        }
    }

    /*
    public float getThrust() {
        return TemplateLoader.getTemplateById(this.getTemplate())
                .map(template -> template.metadata().get("thrust").getAsFloat())
                .orElse(0.0f);
    }

    public float getThrustCurve() {
        return TemplateLoader.getTemplateById(this.getTemplate())
                .map(template -> template.metadata().get("thrustCurve").getAsFloat())
                .orElse(0.0f);
    }
     */

    public void rotate(float x, float y, float z) {
        Quaternionf rot = new Quaternionf(0, 0, 0, 1);
        rot.rotateX(x);
        rot.rotateY(y);
        rot.rotateZ(z);
        /*
        QuaternionHelper.rotateX(rot, x);
        QuaternionHelper.rotateY(rot, y);
        QuaternionHelper.rotateZ(rot, z);

        var trans = getRigidBody().getTransform(new Transform());
        trans.getRotation().set(trans.getRotation().mult(Convert.toBullet(rot)));
        getRigidBody().setPhysicsTransform(trans);*/
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean bl) {
        super.dropCustomDeathLoot(serverLevel, damageSource, bl);
        this.spawnAtLocation(this.quadcopterStack);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CHAIN_HIT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.CHAIN_BREAK;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!level().isClientSide() && source.getEntity() instanceof ServerPlayer) {
            this.kill();
            return true;
        }

        return super.hurt(source, amount);
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return this.quadcopterStack.copyWithCount(1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        getEntityData().set(TEMPLATE, tag.getString("template"));
        getEntityData().set(CAMERA_ANGLE, tag.getInt("camera_angle"));
        if (tag.contains("owner")) {
            this.ownerUUID = tag.getUUID("owner");
        }
        if (tag.contains("quadcopter_stack")) {
            Optional<ItemStack> quadStack = ItemStack.parse(this.registryAccess(), tag.getCompound("quadcopter_stack"));
            quadStack.ifPresent(this::setQuadcopterStack);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("camera_angle", getEntityData().get(CAMERA_ANGLE));
        if (this.ownerUUID != null) {
            tag.putUUID("owner", this.ownerUUID);
        }
        tag.put(
                "quadcopter_stack",
                this.quadcopterStack.save(this.registryAccess(), new CompoundTag())
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TEMPLATE, "");
        builder.define(PREV_TEMPLATE, "");
        builder.define(ARMED, false);
        builder.define(CAMERA_ANGLE, 0);
    }

//    @Override
    public boolean shouldPlayerBeViewing(Player player) {
        return player != null && player.getInventory().armor.get(3).getItem() instanceof GogglesItem;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return new ItemStack(Items.AIR);
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    public void setArmed(boolean armed) {
        this.getEntityData().set(ARMED, armed);
    }

    public boolean isArmed() {
        return this.getEntityData().get(ARMED);
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            this.cachedOwner = serverLevel.getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }
}
