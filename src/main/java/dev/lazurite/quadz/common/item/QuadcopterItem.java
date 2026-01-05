package dev.lazurite.quadz.common.item;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.registry.QuadzEntityTypes;
import dev.lazurite.quadz.common.util.Bindable;
import dev.lazurite.quadz.client.render.entity.QuadcopterEntityRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a quadcopter, allows the player to spawn with right-click on the ground.
 * @see QuadcopterEntityRenderer
 */
public class QuadcopterItem extends Item {

    public QuadcopterItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        var itemStack = player.getItemInHand(interactionHand);
        var hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(itemStack); // wave hand
        } else {
            Quadcopter entity = QuadzEntityTypes.QUADCOPTER.create(level);

            if (entity != null) {
                Bindable.get(itemStack).ifPresent(entity::copyFrom);

                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    entity.absMoveTo(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
                } else {
                    var random = new Random();
                    var direction = hitResult.getLocation().subtract(player.position()).add(0, player.getEyeHeight(), 0).normalize();
                    var pos = player.position().add(direction);

                    entity.absMoveTo(pos.x, pos.y, pos.z);
                }

                level.addFreshEntity(entity);
                itemStack.shrink(1);
                itemStack = new ItemStack(Items.AIR);
            }
        }

        return InteractionResultHolder.success(itemStack); // wave hand
    }
}
