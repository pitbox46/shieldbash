package github.pitbox46.shieldbash.datagen;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.glm.AutoSmeltLootModifier;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate.Builder;
import net.minecraft.advancements.criterion.MinMaxBounds.IntBound;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;

public class LootProvider extends GlobalLootModifierProvider {
    public LootProvider(DataGenerator gen) {
        super(gen, "shield_bash");
    }

    protected void start() {
        this.add("smelting_touch_loot", Registries.MODIFIER.get(), new AutoSmeltLootModifier(new ILootCondition[]{MatchTool.builder(Builder.create().enchantment(new EnchantmentPredicate(Registries.SMELTING_TOUCH.get(), IntBound.atLeast(1)))).build()}));
    }
}
