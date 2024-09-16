package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.narutomod.procedure.ProcedureKGDistribution;
import net.narutomod.procedure.ProcedureUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ElementsAnbubingobookMod.ModElement.Tag
public class ProcedureRerollCommandExecuted extends ElementsAnbubingobookMod.ModElement {
	public ProcedureRerollCommandExecuted(ElementsAnbubingobookMod instance) {
		super(instance, 8);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {

		EntityPlayerMP player = (EntityPlayerMP) dependencies.get("player");

		List<String> advancement = extras.CheckAdvancements(player);
		if (advancement != null) {
			extras.RemoveAdvancement(player, advancement.get(0));
			player.sendMessage(new TextComponentString(advancement.get(0)));
			advancement.remove(0);
			if (!advancement.isEmpty()) {
				executeProcedure(dependencies);

			}
		}
	}
}