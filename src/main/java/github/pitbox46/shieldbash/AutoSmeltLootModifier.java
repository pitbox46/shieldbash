package github.pitbox46.shieldbash;

import com.google.gson.JsonObject;
import com.omircon.shield_bash.Registries;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AutoSmeltLootModifier extends LootModifier {
    public AutoSmeltLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        List<ItemStack> removeList = new ArrayList();
        List<ItemStack> addList = new ArrayList();
        int lvl = EnchantmentHelper.getEnchantmentLevel(Registries.SMELTING_TOUCH.get(), context.get(LootParameters.TOOL).copy());
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        if (entity != null && lvl >= 1 && generatedLoot != null) {
            List<FurnaceRecipe> list = entity.getEntityWorld().getRecipeManager().getRecipesForType(IRecipeType.SMELTING);
            Iterator var8 = generatedLoot.iterator();

            while (true) {
                ItemStack stack;
                while (var8.hasNext()) {
                    stack = (ItemStack) var8.next();
                    ItemStack item = stack;
                    Iterator var11 = list.iterator();

                    while (var11.hasNext()) {
                        FurnaceRecipe recipe = (FurnaceRecipe) var11.next();
                        Ingredient ingredient = recipe.getIngredients().get(0);
                        if (ingredient.test(stack)) {
                            item = recipe.getRecipeOutput();
                            break;
                        }
                    }

                    var11 = stack.getItem().getTags().iterator();

                    while (var11.hasNext()) {
                        ResourceLocation tag = (ResourceLocation) var11.next();
                        if (tag.getPath().endsWith("ores") || tag.getPath().endsWith("ore")) {
                            removeList.add(stack);
                            addList.add(item);
                            break;
                        }
                    }
                }

                var8 = removeList.iterator();

                while (var8.hasNext()) {
                    stack = (ItemStack) var8.next();
                    generatedLoot.remove(stack);
                }

                var8 = addList.iterator();

                while (var8.hasNext()) {
                    stack = (ItemStack) var8.next();
                    generatedLoot.add(stack);
                }
                break;
            }
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<com.omircon.shield_bash.glm.AutoSmeltLootModifier> {
        public Serializer() {
        }

        public com.omircon.shield_bash.glm.AutoSmeltLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new com.omircon.shield_bash.glm.AutoSmeltLootModifier(ailootcondition);
        }

        public JsonObject write(com.omircon.shield_bash.glm.AutoSmeltLootModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}