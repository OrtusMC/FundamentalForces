package com.sammy.fufo.common.item;

import com.sammy.fufo.FufoMod;
import com.sammy.fufo.common.blockentity.PipeNodeBlockEntity;
import com.sammy.fufo.helpers.Debuggable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DebugTool extends Item {

	public DebugTool(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		BlockEntity tileentity = context.getLevel().getBlockEntity(context.getClickedPos());
		Player ep = context.getPlayer();
		if (tileentity instanceof Debuggable debuggable) {
			FufoMod.LOGGER.debug(debuggable.getDebugMessage(ep.isShiftKeyDown()));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}