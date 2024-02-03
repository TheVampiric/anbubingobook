package net.mcreator.anbubingobook.command;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.mcreator.anbubingobook.procedure.procedureevolve;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Entity entity = getCommandSenderAsPlayer(sender);





            Map<String, Object> dependencies = new HashMap<>();
            dependencies.put("entity", entity);
            procedureevolve.executeProcedure(dependencies);

        }
    }
}

