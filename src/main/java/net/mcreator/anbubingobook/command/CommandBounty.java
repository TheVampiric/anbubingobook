
package net.mcreator.anbubingobook.command;

import com.google.common.collect.Lists;
import net.mcreator.anbubingobook.procedure.ProcedureBountyCommandExecuted;
import net.mcreator.anbubingobook.procedure.procedureevolve;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandBase.*;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static net.minecraft.command.CommandBase.*;

@ElementsAnbubingobookMod.ModElement.Tag
public class CommandBounty extends ElementsAnbubingobookMod.ModElement {
	public CommandBounty(ElementsAnbubingobookMod instance) {
		super(instance, 8);
	}

	@Override
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandHandler());
	}
	public static class CommandHandler implements ICommand {

		private final List<String> usages = Lists.newArrayList("Claim", "Set", "Cancel");


		@Override
		public int compareTo(ICommand c) {
			return getName().compareTo(c.getName());
		}

		@Override
		public boolean checkPermission(MinecraftServer server, ICommandSender var1) {
			return true;
		}

		@Override
		public List getAliases() {
			return new ArrayList();
		}

		@Override
		public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
			if (args.length == 1) {
				return getListOfStringsMatchingLastWord(args, usages);
			}

			if (args.length == 2) {
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
			return "bounty";
		}

		@Override
		public String getUsage(ICommandSender var1) {
			return "/bounty [<arguments>]";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] cmd) throws CommandException {
			EntityPlayer player;
			Map<String, Object> dependencies = new HashMap<>();

			if (cmd.length == 0) {
				System.err.println("No Type selected");
				return;
			}
			if (cmd.length == 1) {
				System.err.println("No player selected");
				return;
			} else {


				player = getPlayer(server, sender, cmd[1]);
				String type = cmd[0];
				EntityPlayer poster = getCommandSenderAsPlayer(sender);
				ItemStack reward = poster.getHeldItemMainhand();

				dependencies.put("player", player);
				dependencies.put("type", type);
				dependencies.put("poster", poster);
				dependencies.put("reward", reward);
				ProcedureBountyCommandExecuted.executeProcedure(dependencies);
			}
		}
	}



}
