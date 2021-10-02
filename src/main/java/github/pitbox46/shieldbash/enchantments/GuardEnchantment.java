package github.pitbox46.shieldbash.enchantments;

import com.omircon.shield_bash.Registries;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class GuardEnchantment extends Enchantment {
    public GuardEnchantment() {
        super(Rarity.UNCOMMON, Registries.SHIELDS, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    public int getMaxLevel() {
        return 2;
    }
}
