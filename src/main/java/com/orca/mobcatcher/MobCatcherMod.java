package com.orca.mobcatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobCatcherMod implements ModInitializer {
    public static final String MOD_ID = "mob-catcher";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item MOB_CATCHER = new MobCatcherItem(new Item.Settings().maxCount(16));

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "mob_catcher"), MOB_CATCHER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(MOB_CATCHER);
        });

        LOGGER.info("Mob Catcher mod initialized!");
    }
}
