package com.orca.mobcatcher;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class MobCatcherItem extends Item {
    private static final Random RANDOM = new Random();

    public MobCatcherItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (player.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        // Check if catcher is already full
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null && nbtComponent.copyNbt().contains("CapturedMob")) {
            player.sendMessage(Text.literal("This Mob Catcher already contains a mob!").formatted(Formatting.RED), true);
            return ActionResult.FAIL;
        }

        // Don't capture players
        if (entity instanceof PlayerEntity) {
            return ActionResult.FAIL;
        }

        // 50% chance for hostile mobs
        if (entity instanceof HostileEntity) {
            if (RANDOM.nextFloat() > 0.5f) {
                player.sendMessage(Text.literal("The mob escaped!").formatted(Formatting.RED), true);
                // Consume the item even on failure (single-use)
                stack.decrement(1);
                return ActionResult.SUCCESS;
            }
        }

        // Capture the mob
        NbtCompound mobData = new NbtCompound();
        if (entity.saveSelfNbt(mobData)) {
            NbtCompound captureData = new NbtCompound();
            captureData.put("CapturedMob", mobData);
            captureData.putString("MobName", entity.getName().getString());

            // Create a new filled catcher
            ItemStack filledCatcher = new ItemStack(this);
            filledCatcher.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(captureData));

            // Remove the empty catcher
            stack.decrement(1);

            // Give the filled catcher to the player
            if (!player.getInventory().insertStack(filledCatcher)) {
                player.dropItem(filledCatcher, false);
            }

            // Remove the captured entity from the world
            entity.discard();

            player.sendMessage(Text.literal("Captured " + captureData.getString("MobName") + "!").formatted(Formatting.GREEN), true);
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (world.isClient() || player == null) {
            return ActionResult.SUCCESS;
        }

        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent == null) {
            return ActionResult.PASS;
        }

        NbtCompound customData = nbtComponent.copyNbt();
        if (!customData.contains("CapturedMob")) {
            return ActionResult.PASS;
        }

        // Release the mob
        NbtCompound mobData = customData.getCompound("CapturedMob");
        BlockPos pos = context.getBlockPos().offset(context.getSide());

        Entity entity = EntityType.loadEntityWithPassengers(mobData, world, e -> {
            e.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, e.getYaw(), e.getPitch());
            return e;
        });

        if (entity != null) {
            world.spawnEntity(entity);
            player.sendMessage(Text.literal("Released " + customData.getString("MobName") + "!").formatted(Formatting.GREEN), true);

            // Remove the used catcher (single-use)
            stack.decrement(1);
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null) {
            NbtCompound customData = nbtComponent.copyNbt();
            if (customData.contains("MobName")) {
                String mobName = customData.getString("MobName");
                tooltip.add(Text.literal("Contains: " + mobName).formatted(Formatting.GOLD));
            }
        } else {
            tooltip.add(Text.literal("Empty").formatted(Formatting.GRAY));
        }
    }
}
