package github.pitbox46.shieldbash.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class SmeltingTouchEnchantment extends Enchantment {
    public SmeltingTouchEnchantment() {
        super(Rarity.RARE, EnchantmentType.DIGGER, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    public boolean canApplyTogether(Enchantment pEnch) {
        return super.canApplyTogether(pEnch) && pEnch != Enchantments.FORTUNE && pEnch != Enchantments.SILK_TOUCH;
    }
}
