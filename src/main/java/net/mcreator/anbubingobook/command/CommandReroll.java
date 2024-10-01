
package net.mcreator.anbubingobook.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.Entity;

import net.mcreator.anbubingobook.procedure.ProcedureRerollCommandExecuted;
import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.narutomod.procedure.ProcedureKGDistribution;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

import static net.minecraft.command.CommandBase.*;

@ElementsAnbubingobookMod.ModElement.Tag
public class CommandReroll extends ElementsAnbubingobookMod.ModElement {
	public CommandReroll(ElementsAnbubingobookMod instance) {
		super(instance, 8);
	}

	@Override
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandHandler());
	}
	public static class CommandHandler extends CommandBase {
		@Override
		public int compareTo(ICommand c) {
			return getName().compareTo(c.getName());
		}

		@Override
		public int getRequiredPermissionLevel() {
			return 2;
		}

		@Override
		public List getAliases() {
			return new ArrayList();
		}

		@Override
		public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
			if (args.length == 1) {
				return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
			}
			return new ArrayList();
		}

		@Override
		public boolean isUsernameIndex(String[] string, int index) {
			return true;
		}

		@Override
		public String getName() {
			return "reroll";
		}

		@Override
		public String getUsage(ICommandSender var1) {
			return "/reroll [<arguments>]";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] cmd) throws CommandException {
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

					EntityPlayer player;

					if (cmd.length == 0) {
						player = getCommandSenderAsPlayer(sender);
					}
					else {
						player = getPlayer(server, sender, cmd[0]);
					}

					$_dependencies.put("player", player);

					ProcedureRerollCommandExecuted.executeProcedure($_dependencies);

					Map<String, Object> deps = new HashMap<>();
					deps.put("entity", player);
					deps.put("x", player.getPosition().getX());
					deps.put("y", player.getPosition().getY());
					deps.put("z", player.getPosition().getZ());
					deps.put("world", player.world);
					ProcedureKGDistribution.executeProcedure(deps);
				}
			}
		}
	}
}
