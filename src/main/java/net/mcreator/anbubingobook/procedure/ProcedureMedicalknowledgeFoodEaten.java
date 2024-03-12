package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;
import net.narutomod.item.ItemIryoJutsu;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@ElementsAnbubingobookMod.ModElement.Tag
public class ProcedureMedicalknowledgeFoodEaten extends ElementsAnbubingobookMod.ModElement {
	public ProcedureMedicalknowledgeFoodEaten(ElementsAnbubingobookMod instance) {
		super(instance, 9);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {


		// learnt from the ProcedureOnCraftingItem.java file in main mod
		if (dependencies.get("player") instanceof EntityPlayerMP && !(dependencies.get("player") == null)) {

			EntityPlayerMP player = (EntityPlayerMP) dependencies.get("player");

			if (player instanceof EntityPlayerMP) {
				Advancement _adv = ( (player).mcServer).getAdvancementManager()
						.getAdvancement(new ResourceLocation("narutomod:achievementmedicalgenin"));
				AdvancementProgress _ap = ( player).getAdvancements().getProgress(_adv);
				if (!_ap.isDone()) {
					Iterator _iterator = _ap.getRemaningCriteria().iterator();
					while (_iterator.hasNext()) {
						String _criterion = (String) _iterator.next();
						( player).getAdvancements().grantCriterion(_adv, _criterion);


						ItemStack release = new ItemStack(ItemIryoJutsu.block, (1));
						release.setCount(1);
						release.setTagInfo("player_idMost", new NBTTagLong(player.getUniqueID().getMostSignificantBits()));
						release.setTagInfo("player_idLeast", new NBTTagLong(player.getUniqueID().getLeastSignificantBits()));
						ItemHandlerHelper.giveItemToPlayer(player, release);
					}
				}
			}
		}
	}
}
