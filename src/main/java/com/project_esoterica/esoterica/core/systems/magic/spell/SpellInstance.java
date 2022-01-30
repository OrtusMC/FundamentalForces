package com.project_esoterica.esoterica.core.systems.magic.spell;

import com.project_esoterica.esoterica.core.setup.magic.SpellTypeRegistry;
import net.minecraft.nbt.CompoundTag;

public class SpellInstance {

    public static final SpellInstance EMPTY = new SpellInstance(SpellTypeRegistry.EMPTY);
    public final SpellType type;

    public SpellInstance(SpellType type) {
        this.type = type;
    }

    public void cast() {
        type.cast(this);
    }

    public boolean isEmpty()
    {
        return this.type.equals(SpellTypeRegistry.EMPTY);
    }
    public CompoundTag serializeNBT(CompoundTag tag) {
        return type.serializeNBT(this, tag);
    }

    public static SpellInstance deserializeNBT(CompoundTag tag) {
        return SpellTypeRegistry.SPELL_TYPES.get(tag.getString("id")).deserializeNBT(tag);
    }
}