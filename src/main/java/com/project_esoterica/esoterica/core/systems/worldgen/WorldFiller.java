package com.project_esoterica.esoterica.core.systems.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class WorldFiller
{
    public ArrayList<FillerEntry> entries = new ArrayList<>();
    public final boolean careful;
    public WorldFiller(boolean careful)
    {
        this.careful = careful;
    }
    public void fill(WorldGenLevel level)
    {
        for (FillerEntry entry : entries)
        {
            if (careful && !entry.canPlace(level))
            {
                continue;
            }
            entry.place(level);
        }
    }
    public void replaceAt(int index, FillerEntry entry)
    {
        entries.set(index, entry);
    }

    public static class FillerEntry {
        public final BlockState state;
        public final BlockPos pos;

        public FillerEntry(BlockState state, BlockPos pos) {
            this.state = state;
            this.pos = pos;
        }

        public boolean canPlace(WorldGenLevel level) {
            return canPlace(level, pos);
        }

        public boolean canPlace(WorldGenLevel level, BlockPos pos) {
            if (level.isOutsideBuildHeight(pos)) {
                return false;
            }
            BlockState state = level.getBlockState(pos);
            return level.isEmptyBlock(pos) || state.getMaterial().isReplaceable();
        }

        public void place(WorldGenLevel level) {
            level.setBlock(pos, state, 3);
        }
    }
}