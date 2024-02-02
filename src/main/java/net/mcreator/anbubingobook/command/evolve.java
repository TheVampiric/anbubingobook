package net.mcreator.anbubingobook.command;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.command.CommandException;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.mcreator.anbubingobook.procedure.procedureevolve;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

import net.narutomod.procedure.ProcedureAddNinjaXpCommandExecuted;
import net.narutomod.ElementsNarutomodMod;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

@ElementsAnbubingobookMod.ModElement.Tag
public class evolve extends ElementsAnbubingobookMod.ModElement {
    public evolve(ElementsAnbubingobookMod instance) {
        super(instance, 57);
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHandler());
    }

    public static class CommandHandler extends CommandBase {
        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            if (args.length == 1) {
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            }
            return new ArrayList();
        }

        @Override
        public boolean isUsernameIndex(String[] string, int index) {
            return index == 0;
        }

        @Override
        public String getName() {
            return "evolve";
        }

        @Override
        public String getUsage(ICommandSender target) {
            return "/evolve <target> <integer>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Entity entity = getEntity(server, sender, args[0]);
            Map<String, Object> dependencies = new HashMap<>();
            dependencies.put("entity", entity);
            procedureevolve.executeProcedure(dependencies);

        }
    }
}

