package dev.lazurite.quadz.common.entity;

import com.mojang.math.Axis;
import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.client.render.QuadcopterView;
import dev.lazurite.quadz.common.registry.QuadzItems;
import dev.lazurite.quadz.common.util.Bindable;
import dev.lazurite.quadz.common.util.Search;
import dev.lazurite.quadz.common.item.GogglesItem;
import dev.lazurite.quadz.common.item.RemoteItem;
import dev.lazurite.quadz.common.util.BetaflightHelper;
import dev.lazurite.quadz.common.util.Matrix4fHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Optional;

public class Quadcopter extends LivingEntity implements Bindable {

    public static final DamageSource PROP_DAMAGE = new DamageSource("quadcopter").setProjectile().damageHelmet();
    public static final EntityDataAccessor<String> TEMPLATE = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> PREV_TEMPLATE = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Boolean> ARMED = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> BIND_ID = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> CAMERA_ANGLE = SynchedEntityData.defineId(Quadcopter.class, EntityDataSerializers.INT);

    public Quadcopter(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
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
            this.level.getEntities(this, this.getBoundingBox(), entity -> entity instanceof LivingEntity).forEach(entity -> {
                entity.hurt(PROP_DAMAGE, 2.0f);
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

            var pitch = player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "pitch"));
            var yaw = -1 * player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "yaw"));
            var roll = player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "roll"));
            var throttle = player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "throttle")) + 1.0f;

            var rate = player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "rate"));
            var superRate = player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "super_rate"));
            var expo = player.quadz$getJoystickValue(new ResourceLocation(QuadzCommon.MOD_ID, "expo"));

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
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            final var stack = player.getInventory().getSelected();

            if (stack.getItem() instanceof RemoteItem) {
                Bindable.get(stack).ifPresent(bindable -> Bindable.bind(this, bindable));
                player.displayClientMessage(Component.translatable("quadz.message.bound"), true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void kill() {
        var itemStack = new ItemStack(QuadzItems.QUADCOPTER_ITEM);
        Bindable.get(itemStack).ifPresent(bindable -> bindable.copyFrom(this));
        this.spawnAtLocation(itemStack);
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!level.isClientSide() && source.getEntity() instanceof ServerPlayer) {
            this.kill();
            return true;
        }

        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        getEntityData().set(TEMPLATE, tag.getString("template"));
        getEntityData().set(BIND_ID, tag.getInt("bind_id"));
        getEntityData().set(CAMERA_ANGLE, tag.getInt("camera_angle"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("bind_id", getEntityData().get(BIND_ID));
        tag.putInt("camera_angle", getEntityData().get(CAMERA_ANGLE));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TEMPLATE, "");
        getEntityData().define(PREV_TEMPLATE, "");
        getEntityData().define(ARMED, false);
        getEntityData().define(BIND_ID, 0);
        getEntityData().define(CAMERA_ANGLE, 0);
    }

//    @Override
//    public boolean shouldRenderPlayer() {
//        return true;
//    }

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

    @Override
    public void setBindId(int bindId) {
        this.getEntityData().set(BIND_ID, bindId);
    }

    @Override
    public int getBindId() {
        return this.getEntityData().get(BIND_ID);
    }

    public void setArmed(boolean armed) {
        this.getEntityData().set(ARMED, armed);
    }

    public boolean isArmed() {
        return this.getEntityData().get(ARMED);
    }

}
