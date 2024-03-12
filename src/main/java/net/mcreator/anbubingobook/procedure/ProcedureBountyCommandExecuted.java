package net.mcreator.anbubingobook.procedure;

import com.google.common.collect.Lists;
import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.Sys;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;

@ElementsAnbubingobookMod.ModElement.Tag
public class ProcedureBountyCommandExecuted extends ElementsAnbubingobookMod.ModElement {
	public ProcedureBountyCommandExecuted(ElementsAnbubingobookMod instance) {
		super(instance, 8);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("player") == null) {
			System.err.println("Failed to get player");
			return;
		}

		if (dependencies.get("type") == null) {
			System.err.println("Failed to get type");
			return;
		}
		if (dependencies.get("poster") == null){
			System.err.println("Failed to get poster");
		}
		if (dependencies.get("reward") == null){
			System.err.println("Failed to get reward");
		}

		String type = (String) dependencies.get("type");
		EntityPlayer player = (EntityPlayer) dependencies.get("player");
		EntityPlayer poster = (EntityPlayer) dependencies.get("poster");
		ItemStack reward = (ItemStack) dependencies.get("reward");


		UUID playerUUID = player.getUniqueID();
		UUID posterUUID = poster.getUniqueID();

		if (type == "Set"){

			List<ItemStack> rewards = Lists.newArrayList( reward);
			player.getEntityData().setTag("reward", (NBTBase) rewards);
		}

	}
}

