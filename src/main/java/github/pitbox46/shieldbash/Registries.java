package github.pitbox46.shieldbash;

import com.omircon.shield_bash.enchantments.GuardEnchantment;
import com.omircon.shield_bash.enchantments.SmeltingTouchEnchantment;
import com.omircon.shield_bash.enchantments.SpikesEnchantment;
import com.omircon.shield_bash.glm.AutoSmeltLootModifier.Serializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.Items;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registries {
    public static final EnchantmentType SHIELDS = EnchantmentType.create("shields", (item) -> {
        return item == Items.SHIELD;
    });
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM;
    public static final DeferredRegister<Enchantment> ENCHANTS;
    public static final RegistryObject<GuardEnchantment> GUARD;
    public static final RegistryObject<SmeltingTouchEnchantment> SMELTING_TOUCH;
    public static final RegistryObject<SpikesEnchantment> SPIKES;
    public static final RegistryObject<Serializer> MODIFIER;

    static {
        GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, "shield_bash");
        ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "shield_bash");
        GUARD = ENCHANTS.register("guard", () -> {
            return new GuardEnchantment();
        });
        SMELTING_TOUCH = ENCHANTS.register("smelting_touch", () -> {
            return new SmeltingTouchEnchantment();
        });
        SPIKES = ENCHANTS.register("spikes", () -> {
            return new SpikesEnchantment();
        });
        MODIFIER = GLM.register("smelting_touch_loot", Serializer::new);
    }

    public Registries() {
    }
}
