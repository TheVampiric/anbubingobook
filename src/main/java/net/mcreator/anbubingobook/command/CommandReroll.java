
package net.mcreator.anbubingobook.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.Entity;

import net.mcreator.anbubingobook.procedure.ProcedureRerollCommandExecuted;
import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.narutomod.procedure.ProcedureKGDistribution;

import java.util.*;

import static net.minecraft.command.CommandBase.*;

@ElementsAnbubingobookMod.ModElement.Tag
public class CommandReroll extends ElementsAnbubingobookMod.ModElement {
	public CommandReroll(ElementsAnbubingobookMod instance) {
		super(instance, 8);
	}

	public static List<String> output = new ArrayList<>();

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
			} else if (args.length == 2) {
				if (args[1].equals("true") || args[1].isEmpty()) {
					if (output.size() == 1) {
						output.remove(0);
					}
					output.add("false");
					return output;
				} else {
					if (args[1].equals("false")) {
						if (output.size() == 1) {
							output.remove(0);
						}
						output.add("true");
						return output;
					}
				}
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
			return "/reroll <player> <Bool>";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] cmd) throws CommandException {
			int x = sender.getPosition().getX();
			int y = sender.getPosition().getY();
			int z = sender.getPosition().getZ();

			Entity entity = sender.getCommandSenderEntity();
			Map<String, Object> $_dependencies = new HashMap<>();

			EntityPlayer player;
			boolean reroll;
			if (entity != null) {
				World world = entity.world;
				HashMap<String, String> cmdparams = new HashMap<>();
				int[] index = {0};
				Arrays.stream(cmd).forEach(param -> {
					cmdparams.put(Integer.toString(index[0]), param);
					index[0]++;
				});
				{
					if (cmd.length == 0) {
						player = getCommandSenderAsPlayer(sender);
					} else {
						player = getPlayer(server, sender, cmd[0]);
					}
					if (cmd.length == 2) {
						$_dependencies.put("remove", Boolean.parseBoolean(cmd[1]));
					} else {
						reroll = false;
						$_dependencies.put("remove", reroll);
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

