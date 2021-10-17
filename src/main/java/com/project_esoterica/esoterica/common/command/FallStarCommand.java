package com.project_esoterica.esoterica.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.project_esoterica.esoterica.core.systems.worldevent.WorldEventManager;
import com.project_esoterica.esoterica.common.worldevent.starfall.StarfallInstance;
import com.project_esoterica.esoterica.common.worldevent.starfall.StarfallResult;
import com.project_esoterica.esoterica.core.data.SpaceModLang;
import com.project_esoterica.esoterica.core.registry.worldevent.StarfallResults;
import com.project_esoterica.esoterica.core.systems.command.StarfallResultArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class FallStarCommand {
    public FallStarCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("fallstar")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("result", new StarfallResultArgumentType())
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    StarfallResult result = StarfallResults.STARFALL_RESULTS.get(context.getArgument("result", String.class));
                                    ServerLevel level = source.getLevel();
                                    BlockPos pos = BlockPosArgument.getSpawnablePos(context, "position");
                                    WorldEventManager.addWorldEvent(level, new StarfallInstance(result, level, pos), false);
                                    source.sendSuccess(SpaceModLang.getCommandKey("fallstar_natural_position"), true);
                                    return 1;
                                }))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    StarfallResult result = StarfallResults.STARFALL_RESULTS.get(context.getArgument("result", String.class));
                                    ServerLevel level = source.getLevel();
                                    Player target = EntityArgument.getPlayer(context, "target");
                                    WorldEventManager.addWorldEvent(level, new StarfallInstance(result, level, target), false);
                                    source.sendSuccess(SpaceModLang.getCommandKey("fallstar_natural_target"), true);
                                    return 1;
                                })))

                .then(Commands.argument("result", new StarfallResultArgumentType())
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .then(Commands.argument("countdown", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            CommandSourceStack source = context.getSource();
                                            int countdown = IntegerArgumentType.getInteger(context, "countdown");
                                            StarfallResult result = StarfallResults.STARFALL_RESULTS.get(context.getArgument("result", String.class));
                                            ServerLevel level = source.getLevel();
                                            BlockPos pos = BlockPosArgument.getSpawnablePos(context, "position");
                                            WorldEventManager.addWorldEvent(level, new StarfallInstance(result, pos, countdown), false);
                                            source.sendSuccess(SpaceModLang.getCommandKey("fallstar_artificial_position"), true);
                                            return 1;
                                        })))
                        .then(Commands.argument("target", EntityArgument.player())

                                .then(Commands.argument("countdown", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            CommandSourceStack source = context.getSource();
                                            int countdown = IntegerArgumentType.getInteger(context, "countdown");
                                            StarfallResult result = StarfallResults.STARFALL_RESULTS.get(context.getArgument("result", String.class));
                                            ServerLevel level = source.getLevel();
                                            Player target = EntityArgument.getPlayer(context, "target");
                                            WorldEventManager.addWorldEvent(level, new StarfallInstance(result, target, countdown), false);
                                            source.sendSuccess(SpaceModLang.getCommandKey("fallstar_artificial_target"), true);
                                            return 1;
                                        }))));
    }
}