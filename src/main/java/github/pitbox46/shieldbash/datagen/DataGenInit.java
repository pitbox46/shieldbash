package github.pitbox46.shieldbash.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(
        bus = Bus.MOD
)
public class DataGenInit {
    public DataGenInit() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new LootProvider(generator));
            System.out.println("yes");
        }

        if (event.includeClient()) {
            generator.addProvider(new LangProvider(generator));
        }

    }
}
