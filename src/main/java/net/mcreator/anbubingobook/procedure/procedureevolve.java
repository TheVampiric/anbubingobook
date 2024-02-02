package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Map;

import static net.minecraft.command.CommandBase.getItemByText;

import net.narutomod.item.ItemMangekyoSharinganObito;
import net.narutomod.item.ItemMangekyoSharingan;
import net.narutomod.item.ItemSharingan;

@ElementsAnbubingobookMod.ModElement.Tag
public class procedureevolve extends ElementsAnbubingobookMod.ModElement {
    public procedureevolve(ElementsAnbubingobookMod instance) {
        super(instance, 57);
    }

    public static void executeProcedure(Map<String, Object> dependencies) {

        if (dependencies.get("entity") == null) {
            System.err.println("Failed to load entity");
            return;
        }


        Entity entity = (Entity) dependencies.get("entity");
        ItemStack itemstack = (ItemStack) dependencies.get("itemstack");

        ItemStack mangekyo = ItemStack.EMPTY;


        if ((Math.random() < 0.5)) {
            mangekyo = new ItemStack(ItemMangekyoSharingan.helmet, (int) (1));
        } else {
            mangekyo = new ItemStack(ItemMangekyoSharinganObito.helmet, (int) (1));
        }
        ItemStack _setstack = (mangekyo);

        _setstack.setCount(1);
        ItemHandlerHelper.giveItemToPlayer(((EntityPlayer) entity), _setstack);
    }
}
