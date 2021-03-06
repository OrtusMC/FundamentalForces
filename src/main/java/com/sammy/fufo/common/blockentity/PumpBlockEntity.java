package com.sammy.fufo.common.blockentity;

import com.sammy.fufo.core.systems.logistics.FlowDir;
import com.sammy.fufo.core.systems.logistics.PipeNode;
import com.sammy.fufo.core.systems.logistics.PressureSource;
import com.sammy.ortus.helpers.BlockHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

// Pumps are pipe nodes with the following additional rules:
// 1. They have exactly two connections that are identifiable as the front and the back
// 2. They depressurize the "back" connection and pressurize the "front" connection
// 3. They force fluid to move from the back to the front, ignoring pressure rules
// (4. They must have a pipe node block directly in front of and behind them)

public class PumpBlockEntity extends PipeNodeBlockEntity implements PressureSource {

	private PipeNode back;
	private PipeNode front;
	private double force;
	
	public PumpBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		force = 20; // for testing
	}
	
	public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

	@Override
	public void onPlace(LivingEntity placer, ItemStack stack) {
		super.onPlace(placer, stack);
	}
	
	@Override
	public boolean addConnection(BlockPos bp) {
		if (level.getBlockEntity(bp) instanceof PipeNode other) {
			if (back == null) back = other;
			else if (front == null) front = other;
			else return false; // if back and front are both spoken for reject the connection
			nearbyAnchorPositions.add(bp);
			if (getNetwork() == null) setNetwork(other.getNetwork(), false);
			else getNetwork().mergeWith(other.getNetwork());
			return true;
		}
		return false;
	}
	
	@Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putLong("back", back.getPos().asLong()); 
        pTag.putLong("front", front.getPos().asLong());
    }
	
	@Override
	public double getPressure() {
		return force;
	}
	public double getPressure(FlowDir dir) {
		return (dir == FlowDir.OUT ? force : -force);
	}

	@Override
	public int getCapacity() {
		return 100;
	}


	@Override
	public PipeNode getConnection(FlowDir dir) {
		// TODO Auto-generated method stub
		return (dir == FlowDir.IN ? back : front);
	}

	@Override
	public int getForce(FlowDir dir) {
		// TODO Auto-generated method stub
		return (int)force;
	}
}
