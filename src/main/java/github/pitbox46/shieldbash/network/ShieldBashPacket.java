package github.pitbox46.shieldbash.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ShieldBashPacket {
    public ShieldBashPacket() {
    }

    public static EntityRayTraceResult getEntityHitResult(ServerPlayerEntity player) {
        Vector3d vector3d = player.getEyePosition(0.0F);
        double d0 = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
        Vector3d vector3d1 = player.getSpectatingEntity().getLook(1.0F);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
        float f = 1.0F;
        AxisAlignedBB axisalignedbb = player.getSpectatingEntity().getBoundingBox().expand(vector3d1.scale(d0)).grow(1.0D, 1.0D, 1.0D);
        return getEntityHitResult(player.getSpectatingEntity(), vector3d, vector3d2, axisalignedbb, (p_215312_0_) -> !p_215312_0_.isSpectator() && p_215312_0_.canBeCollidedWith(), d0);
    }

    public static EntityRayTraceResult getEntityHitResult(Entity p_221273_0_, Vector3d p_221273_1_, Vector3d p_221273_2_, AxisAlignedBB p_221273_3_, Predicate<Entity> p_221273_4_, double p_221273_5_) {
        World world = p_221273_0_.world;
        double d0 = p_221273_5_;
        Entity entity = null;
        Vector3d vector3d = null;
        Iterator var12 = world.getEntitiesInAABBexcluding(p_221273_0_, p_221273_3_, p_221273_4_).iterator();

        while (true) {
            while (var12.hasNext()) {
                Entity entity1 = (Entity) var12.next();
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(entity1.getCollisionBorderSize());
                Optional<Vector3d> optional = axisalignedbb.rayTrace(p_221273_1_, p_221273_2_);
                if (axisalignedbb.contains(p_221273_1_)) {
                    if (d0 >= 0.0D) {
                        entity = entity1;
                        vector3d = optional.orElse(p_221273_1_);
                        d0 = 0.0D;
                    }
                } else if (optional.isPresent()) {
                    Vector3d vector3d1 = optional.get();
                    double d1 = p_221273_1_.squareDistanceTo(vector3d1);
                    if (d1 < d0 || d0 == 0.0D) {
                        if (entity1.getLowestRidingEntity() == p_221273_0_.getLowestRidingEntity() && !entity1.canRiderInteract()) {
                            if (d0 == 0.0D) {
                                entity = entity1;
                                vector3d = vector3d1;
                            }
                        } else {
                            entity = entity1;
                            vector3d = vector3d1;
                            d0 = d1;
                        }
                    }
                }
            }

            return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player.getActiveItemStack().getItem() == Items.SHIELD && player.isHandActive()) {
                EntityRayTraceResult entityRayTraceResult = getEntityHitResult(player);
                if (entityRayTraceResult != null) {
                    Entity entity = entityRayTraceResult.getEntity();
                    if (entity != null) {
                        System.out.println("check");
                        player.attackTargetEntityWithCurrentItem(entity);
                        player.getEntityWorld().playSound(null, player.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0F, 0.8F + player.getEntityWorld().rand.nextFloat() * 0.4F);
                    }
                }
            }

        });
        return true;
    }
}
