package dev.lazurite.quadz.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

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
