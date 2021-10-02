package github.pitbox46.shieldbash.datagen;

import com.omircon.shield_bash.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.data.LanguageProvider;

public class LangProvider extends LanguageProvider {
    public LangProvider(DataGenerator gen) {
        super(gen, "shield_bash", "en_us");
    }

    protected void addTranslations() {
        this.add(Registries.SMELTING_TOUCH.get(), "Smelting Touch");
        this.add(Registries.GUARD.get(), "Guard");
        this.add(Registries.SPIKES.get(), "Spikes");
    }
}
