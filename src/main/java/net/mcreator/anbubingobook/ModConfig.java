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

	@Config.Comment("Ninja xp multiplier, multiplier is not exact")
	public static double Ninja_XP_MULTI = 1.0;

	@Config.Comment("Max Ninjaxp that a player can obtain")
	public static double Max_Ninja_XP = 100000;

	@Config.Comment("true or false to if a tamed wolf should evolve the owners sharingan on death")
	public static boolean solo_MS = false;

	@Config.Comment("NinjaXP needed to evolve the sharingan via the wolf")
	public static double Wolf_XP = 1000.0;

	@Config.Comment("Jutsu xp amount changer, setting it to 2 = 2 jutsu xp per hit")
	public static int Jutsu_XP_MULTI = 1;


	@Config.Comment("percent amount of chakra to respawn with (0.5 = 50%)")
	public static float Respawn_Chakra_amount = 0.8f;

}
