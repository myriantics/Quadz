package dev.lazurite.quadz.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;

import java.util.Properties;

/**
 * Represents the goggles a player wears in order to see their quadcopter's POV.
 */
public class GogglesItem extends Item implements Equipable {
    public GogglesItem(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
