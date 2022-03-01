package com.sammy.fundamental_forces.core.systems.sound;

import com.sammy.fundamental_forces.core.systems.blockentity.SimpleBlockEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class SimpleBlockEntitySoundInstance<T extends SimpleBlockEntity> extends AbstractTickableSoundInstance {

    public T blockEntity;

    public SimpleBlockEntitySoundInstance(T blockEntity, SoundEvent soundEvent, float volume, float pitch) {
        super(soundEvent, SoundSource.BLOCKS);
        this.blockEntity = blockEntity;
        this.volume = volume;
        this.pitch = pitch;
        this.delay = 0;
    }

    @Override
    public void tick() {
        if (blockEntity.isRemoved()) {
            stop();
        }
    }
}
