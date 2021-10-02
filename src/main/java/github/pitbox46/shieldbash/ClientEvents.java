package github.pitbox46.shieldbash;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.omircon.shield_bash.network.Network;
import com.omircon.shield_bash.network.ShieldBashPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
        value = {Dist.CLIENT},
        bus = Bus.FORGE,
        modid = "shield_bash"
)
public class ClientEvents {
    private static int cooldown = 0;

    public ClientEvents() {
    }

    public static void setup() {
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::clientTick);
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::keyboardEvent);
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::mouseEvent);
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::renderHand);
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        if (cooldown > 0) {
            --cooldown;
        }

    }

    @SubscribeEvent
    public static void keyboardEvent(KeyInputEvent event) {
        if (event.getKey() == Minecraft.getInstance().gameSettings.keyBindAttack.getKey().getKeyCode() && event.getAction() == 1) {
            execute();
        }

    }

    @SubscribeEvent
    public static void mouseEvent(MouseInputEvent event) {
        if (event.getButton() == Minecraft.getInstance().gameSettings.keyBindAttack.getKey().getKeyCode() && event.getAction() == 1) {
            execute();
        }

    }

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
        ClientPlayerEntity pPlayer = Minecraft.getInstance().player;
        if (pPlayer.swingProgress > 0.0F && pPlayer.isHandActive() && pPlayer.getActiveItemStack().getItem() == Items.SHIELD) {
            event.setCanceled(true);
            Hand pHand = event.getHand();
            MatrixStack pMatrixStack = event.getMatrixStack();
            IRenderTypeBuffer pBuffer = event.getBuffers();
            int pCombinedLight = event.getLight();
            float pSwingProgress = event.getSwingProgress();
            float pEquippedProgress = event.getEquipProgress();
            boolean flag = pHand == Hand.MAIN_HAND;
            HandSide handside = flag ? pPlayer.getPrimaryHand() : pPlayer.getPrimaryHand().opposite();
            ItemStack pStack = event.getItemStack();
            boolean flag3 = handside == HandSide.RIGHT;
            pMatrixStack.push();
            float f5 = -0.4F * MathHelper.sin(MathHelper.sqrt(pSwingProgress) * 3.1415927F);
            float f6 = 0.2F * MathHelper.sin(MathHelper.sqrt(pSwingProgress) * 6.2831855F);
            float f10 = -0.2F * MathHelper.sin(pSwingProgress * 3.1415927F);
            int l = flag3 ? 1 : -1;
            pMatrixStack.translate((float) l * f5, f6, f10);
            applyItemArmTransform(pMatrixStack, handside, pEquippedProgress);
            applyItemArmAttackTransform(pMatrixStack, handside, pSwingProgress);
            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(pPlayer, pStack, flag3 ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND, !flag3, pMatrixStack, pBuffer, pCombinedLight);
            pMatrixStack.pop();
        }

    }

    public static void execute() {
        if (Minecraft.getInstance().currentScreen == null && Minecraft.getInstance().player.isHandActive() && Minecraft.getInstance().player.getActiveItemStack().getItem() == Items.SHIELD && cooldown <= 0) {
            cooldown = 32;
            Network.sendToServer(new ShieldBashPacket());
            Minecraft.getInstance().player.swingProgress = 1.0F;
            Minecraft.getInstance().player.swing(Minecraft.getInstance().player.getActiveHand(), true);
        }

    }

    private static void applyItemArmTransform(MatrixStack pMatrixStack, HandSide pHand, float pEquippedProg) {
        int i = pHand == HandSide.RIGHT ? 1 : -1;
        pMatrixStack.translate((float) i * 0.56F, -0.52F + pEquippedProg * -0.6F, -0.7200000286102295D);
    }

    private static void applyItemArmAttackTransform(MatrixStack pMatrixStack, HandSide pHand, float pSwingProgress) {
        int i = pHand == HandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(pSwingProgress * pSwingProgress * 3.1415927F);
        pMatrixStack.rotate(Vector3f.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
        float f1 = MathHelper.sin(MathHelper.sqrt(pSwingProgress) * 3.1415927F);
        pMatrixStack.rotate(Vector3f.ZP.rotationDegrees((float) i * f1 * -20.0F));
        pMatrixStack.rotate(Vector3f.XP.rotationDegrees(f1 * -80.0F));
        pMatrixStack.rotate(Vector3f.YP.rotationDegrees((float) i * -45.0F));
    }
}
