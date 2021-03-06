package com.sammy.fufo.common.blockentity;

import com.sammy.fufo.core.systems.logistics.FlowDir;
import com.sammy.fufo.core.systems.logistics.PipeNode;
import com.sammy.fufo.core.systems.logistics.PressureSource;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ValveBlockEntity extends PipeNodeBlockEntity implements PressureSource {

	private boolean isOpen;
	public ValveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PipeNode getConnection(FlowDir dir) {
		return null;
	}

	@Override
	public int getForce(FlowDir dir) {
		if (isOpen) return 0;
		else return (int)(dir == FlowDir.IN ? this.getPressure() : -this.getPressure());
	}

}
