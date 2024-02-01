
package net.mcreator.anbubingobook.command;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.mcreator.anbubingobook.procedure.procedureevolve;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.narutomod.ElementsNarutomodMod;

import java.util.*;

@ElementsNarutomodMod.ModElement.Tag
public class evolve extends ElementsAnbubingobookMod.ModElement {
    public evolve(ElementsAnbubingobookMod instance) {
        super(instance, 578);
    }

    @Override
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHandler());
    }

    public static class CommandHandler extends CommandBase {
        @Override
        public int getRequiredPermissionLevel() {
            return 3;
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
        public String getUsage(ICommandSender var1) {
            return "/evolve <target>";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] cmd) {
            int x = sender.getPosition().getX();
            int y = sender.getPosition().getY();
            int z = sender.getPosition().getZ();
            Entity entity = sender.getCommandSenderEntity();
            if (entity != null) {
                World world = entity.world;
                HashMap<String, String> cmdparams = new HashMap<>();
                int[] index = {0};
                Arrays.stream(cmd).forEach(param -> {
                    cmdparams.put(Integer.toString(index[0]), param);
                    index[0]++;
                });
                {
                    Map<String, Object> $_dependencies = new HashMap<>();
                    $_dependencies.put("entity", entity);
                    $_dependencies.put("cmdparams", cmdparams);
                    procedureevolve.executeProcedure($_dependencies);
                }
            }
        }
    }
}