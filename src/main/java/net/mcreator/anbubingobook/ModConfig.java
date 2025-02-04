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


	@Config.Comment("Max Ninjaxp that a player can obtain")
	public static double Max_Ninja_XP = 100000;

	@Config.Comment("Jutsu xp amount changer, setting it to 2 = 2 jutsu xp per hit")
	public static int Jutsu_XP_MULTI = 1;



	@Config.Comment("true or false to if a tamed wolf should evolve the owners sharingan on death")
	public static boolean solo_MS = false;

	@Config.Comment("NinjaXP needed to evolve the sharingan via the wolf")
	public static double Wolf_XP = 1000.0;



	@Config.Comment("Should Player spawn with the ninja advancement?")
	public static boolean NINJA_START = false;




	@Config.Comment("Should Evolving a sharingan into MS give the player the base sharingan advancement as well as the Mangekyo advancement (also effects /evolve)")
	public static boolean BASE_SHARINGAN_ADVANCEMENT = false;

	@Config.Comment("Set to true to allow /reroll to remove advancement + item for wood release")
	public static boolean WOOD_REROLL = false;


	@Config.Comment("amount of ticks that have to pass before player regens chakra (20 ticks = 1 second)")
	public static int REGEN_TICKS = 80;

	@Config.Comment("what % of chakra a player should regen every 4 seconds (even while moving) 0.1 = 10%")
	public static float PASSIVE_REGEN_AMOUNT = 0.00f;

	@Config.Comment("% amount of chakra to regain after respawn")
	public static float RESPAWN_AMOUNT = 0.00f;



	@Config.Comment("Ticks needed for susanoo drain (20 ticks = 1 second)")
	public static int SUSANOO_TICKS = 20;

	@Config.Comment("Drain of susanoo at each stage per second")
	public static double[] SUSANOO_DRAIN = {30,30,30,30,30};



}
