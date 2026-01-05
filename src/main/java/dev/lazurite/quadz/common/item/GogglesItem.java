package dev.lazurite.quadz.common.item;

import dev.lazurite.quadz.QuadzCommon;
import dev.lazurite.quadz.common.registry.item.QuadzDataComponentTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents the goggles a player wears in order to see their quadcopter's POV.
 */
public class GogglesItem extends ArmorItem {

    public GogglesItem() {
        super(
                ArmorMaterials.LEATHER,
                Type.HELMET,
                new Properties().stacksTo(1)
        );
    }
}
