package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.narutomod.item.*;
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
		Item[] items = {ItemBakuton.block, ItemByakugan.helmet, ItemFutton.block, ItemHyoton.block, ItemJiton.block, ItemJinton.block, ItemRanton.block, ItemShakuton.block, ItemShikotsumyaku.block, ItemYooton.block, ItemMangekyoSharinganEternal.helmet, ItemMangekyoSharingan.helmet, ItemMangekyoSharinganObito.helmet, ItemRinnegan.helmet, ItemTenseigan.helmet, ItemSharingan.helmet, ItemByakugan.helmet };

		if (advancement != null && !advancement.isEmpty()) {
			for (String i: advancement) {
				extras.RemoveAdvancement(player, i);
			}
		}

		for (Item i : items){
			extras.Removeitem(player, i);

			if(ProcedureUtils.hasItemInInventory(player, i)){
				Map<String, Object> $_dependencies = new HashMap<>();
				$_dependencies.put("player", player);
				executeProcedure($_dependencies);
			}
		}


	}
}