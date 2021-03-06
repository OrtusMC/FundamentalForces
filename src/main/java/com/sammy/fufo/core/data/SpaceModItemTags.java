package com.sammy.fufo.core.data;

import com.sammy.fufo.FufoMod;
import com.sammy.fufo.core.registratation.ItemRegistrate;
import com.sammy.fufo.core.setup.content.item.ItemRegistry;
import com.sammy.fufo.core.setup.content.item.ItemTagRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;


public class SpaceModItemTags extends ItemTagsProvider {
    public SpaceModItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, FufoMod.FUFO, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Space Mod Item Tags";
    }

    @Override
    protected void addTags() {
        tag(ItemTagRegistry.METEOR_FLAME_CATALYST).add(ItemRegistrate.ORTUSITE_CHUNK.get());
    }
}