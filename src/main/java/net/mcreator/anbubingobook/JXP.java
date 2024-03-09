/**
 * This mod element is always locked. Enter your code in the methods below.
 * If you don't need some of these methods, you can remove them as they
 * are overrides of the base class ElementsAnbubingobookMod.ModElement.
 *
 * You can register new events in this class too.
 *
 * As this class is loaded into mod element list, it NEEDS to extend
 * ModElement class. If you remove this extend statement or remove the
 * constructor, the compilation will fail.
 *
 * If you want to make a plain independent class, create it in
 * "Workspace" -> "Source" menu.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
*/
package net.mcreator.anbubingobook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.narutomod.item.ItemJutsu;

@ElementsAnbubingobookMod.ModElement.Tag
public class JXP extends ElementsAnbubingobookMod.ModElement {
	/**
	 * Do not remove this constructor
	 */
	public JXP(ElementsAnbubingobookMod instance) {
		super(instance, 7);
	}

	public static void logBattleXP(EntityPlayer player) {
		ItemStack stack = player.getHeldItemMainhand();
		if (!(stack.getItem() instanceof ItemJutsu.Base)) {
			stack = player.getHeldItemOffhand();
		}
		if (stack.getItem() instanceof ItemJutsu.Base) {
			ItemJutsu.Base baseitem = (ItemJutsu.Base) stack.getItem();
			if (baseitem.getCurrentJutsuXp(stack) < baseitem.getCurrentJutsuRequiredXp(stack)) {
				baseitem.addCurrentJutsuXp(stack, ModConfig.Jutsu_XP_MULTI);


			}
		}
	}
}


