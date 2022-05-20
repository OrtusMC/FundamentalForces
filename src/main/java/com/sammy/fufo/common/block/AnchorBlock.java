package com.sammy.fufo.common.block;

import com.sammy.fufo.common.blockentity.AnchorBlockEntity;
import com.sammy.ortus.handlers.GhostBlockHandler;
import com.sammy.ortus.systems.block.OrtusEntityBlock;
import com.sammy.ortus.systems.placementassistance.IPlacementAssistant;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;


public class AnchorBlock<T extends AnchorBlockEntity> extends OrtusEntityBlock<T> implements IPlacementAssistant {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final VoxelShape SHAPE = Block.box(5, 5, 5, 11, 11, 11);

    public AnchorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return switch (direction) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(AXIS, pContext.getPlayer().isShiftKeyDown() ? pContext.getClickedFace().getAxis() : pContext.getNearestLookingDirection().getAxis());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player.isShiftKeyDown() || !player.mayBuild()) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
//        IPlacementHelper helper = PlacementHelpers.get(ANCHOR_PLACEMENT_HELPER);
//        if (helper.matchesItem(held))
//            return helper.getOffset(player, level, state, pos, ray).placeInWorld(level, (BlockItem) held.getItem(), player, hand, ray);
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void displayGhost(ClientLevel clientLevel, BlockHitResult blockHitResult, BlockState blockState) {
        GhostBlockHandler.addGhost(blockState, blockState).at(blockHitResult.getBlockPos().relative(blockHitResult.getDirection().getOpposite()));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Predicate<ItemStack> shouldRenderSimple() {
        return s -> true;
    }
}