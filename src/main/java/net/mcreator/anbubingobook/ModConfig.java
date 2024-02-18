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

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;

import java.util.Random;
import net.narutomod.*;

@Config(modid = AnbubingobookMod.MODID)
@ElementsAnbubingobookMod.ModElement.Tag
public class ModConfig extends ElementsAnbubingobookMod.ModElement	 {
	/**
	 * Do not remove this constructor
	 */
	public ModConfig(ElementsAnbubingobookMod instance) {
		super(instance, 3);
	}

	@Config.Comment("Ninja xp multiplier, multiplier is additive")
	public static double Ninja_XP_MULTI = 200.0;


	@Config.Comment("Jutsu xp amount changer, 1 = 2 jutsu xp per hit")
	public static double Jutsu_XP_MULTI = 10;

}
