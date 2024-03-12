
package net.mcreator.anbubingobook.item;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.item.Item;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.mcreator.anbubingobook.procedure.ProcedureMedicalknowledgeFoodEaten;
import net.mcreator.anbubingobook.ElementsAnbubingobookMod;

import java.util.Map;
import java.util.HashMap;

@ElementsAnbubingobookMod.ModElement.Tag
public class ItemMedicalknowledge extends ElementsAnbubingobookMod.ModElement {
	@GameRegistry.ObjectHolder("anbubingobook:medicalknowledge")
	public static final Item block = null;
	public ItemMedicalknowledge(ElementsAnbubingobookMod instance) {
		super(instance, 9);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemFoodCustom());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation("anbubingobook:medicalknowledge", "inventory"));
	}
	public static class ItemFoodCustom extends ItemFood {
		public ItemFoodCustom() {
			super(0, 0f, false);
			setUnlocalizedName("medicalknowledge");
			setRegistryName("medicalknowledge");
			setAlwaysEdible();
			setCreativeTab(CreativeTabs.FOOD);
			setMaxStackSize(1);
		}

		@Override
		public int getMaxItemUseDuration(ItemStack stack) {
			return 5;
		}

		@Override
		public EnumAction getItemUseAction(ItemStack par1ItemStack) {
			return EnumAction.DRINK;
		}

		@Override
		protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entity) {
			super.onFoodEaten(itemStack, world, entity);
			int x = (int) entity.posX;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			{
				Map<String, Object> dependencies = new HashMap<>();

				EntityPlayerMP player = (EntityPlayerMP) entity;

				dependencies.put("player", player);

				ProcedureMedicalknowledgeFoodEaten.executeProcedure(dependencies);
			}
		}
	}
}
