package dev.lazurite.quadz.common.item;

import dev.lazurite.quadz.common.component.BindingComponent;
import dev.lazurite.quadz.common.entity.Quadcopter;
import dev.lazurite.quadz.common.registry.QuadzEntityTypes;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import dev.lazurite.quadz.client.render.entity.QuadcopterEntityRenderer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.Random;

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
            Quadcopter quadcopter = QuadzEntityTypes.QUADCOPTER.create(level);

            if (quadcopter != null) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    quadcopter.absMoveTo(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
                } else {
                    var direction = hitResult.getLocation().subtract(player.position()).add(0, player.getEyeHeight(), 0).normalize();
                    var pos = player.position().add(direction);

                    quadcopter.absMoveTo(pos.x, pos.y, pos.z);
                }

                // split 1 quadcopter off of the stack and set that stack to being within the quadcopter entity
                quadcopter.setQuadcopterStack(itemStack.split(1));
                level.addFreshEntity(quadcopter);
            }
        }

        return InteractionResultHolder.success(itemStack); // wave hand
    }
}
