package github.pitbox46.shieldbash;

import com.omircon.shield_bash.Registries;
import com.omircon.shield_bash.network.Network;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("shieldbash")
public class ShieldBash {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ShieldBash() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void registerEvents() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(com.omircon.shield_bash.ShieldBash::commonSetup);
    }

    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        if (!event.getSource().isProjectile() && !event.getSource().isMagicDamage() && !event.getSource().isExplosion() && event.getSource().getTrueSource() != null && !event.getEntity().getEntityWorld().isRemote()) {
            Entity pAttacker = event.getSource().getTrueSource();
            Entity pUser = event.getEntity();
            if (pUser instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) pUser;
                if (player.isHandActive() && player.getActiveItemStack().getItem() == Items.SHIELD) {
                    int i = EnchantmentHelper.getEnchantmentLevel(Registries.SPIKES.get(), player.getActiveItemStack());
                    if (pAttacker != null) {
                        pAttacker.attackEntityFrom(DamageSource.causeThornsDamage(pUser), (float) i);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void knockbackEvent(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getTrueSource();
            if (player.isHandActive() && player.getActiveItemStack().getItem() == Items.SHIELD) {
                int j = EnchantmentHelper.getEnchantmentLevel(Registries.SPIKES.get(), player.getActiveItemStack());
                float f = (float) (j + 1);
                boolean flag2 = player.fallDistance > 0.0F && !player.isOnGround() && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Effects.BLINDNESS) && !player.isPassenger() && event.getEntity() instanceof LivingEntity;
                flag2 = flag2 && !player.isSprinting();
                if (flag2) {
                    f = (float) ((double) f * 1.5D);
                }

                event.setAmount(f);
                int i = EnchantmentHelper.getEnchantmentLevel(Registries.GUARD.get(), player.getActiveItemStack());
                if (i > 0) {
                    Entity pTargetEntity = event.getEntity();
                    if (pTargetEntity instanceof LivingEntity) {
                        ((LivingEntity) pTargetEntity).applyKnockback((float) i * 0.5F, MathHelper.sin(player.rotationYaw * 0.017453292F), -MathHelper.cos(player.rotationYaw * 0.017453292F));
                    } else {
                        pTargetEntity.addVelocity(-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float) i * 0.5F, 0.1D, MathHelper.cos(player.rotationYaw * 0.017453292F) * (float) i * 0.5F);
                    }
                }
            }
        }

    }

    public static void commonSetup(FMLClientSetupEvent event) {
        Network.registerMessages();
    }
}
