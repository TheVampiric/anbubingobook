package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.items.ItemHandlerHelper;
import net.narutomod.item.ItemMangekyoSharingan;
import net.narutomod.item.ItemMangekyoSharinganObito;


import java.util.Map;
import java.util.UUID;
import java.util.Random;

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

        long UUIDMost = entity.getUniqueID().getMostSignificantBits();
        long UUIDLeast = entity.getUniqueID().getLeastSignificantBits();
        long color = (long) (Math.random() * 888888888 + 111111111);


        ItemStack mangekyo = ItemStack.EMPTY;




        if ((Math.random() < 0.5)) {
            mangekyo = new ItemStack(ItemMangekyoSharingan.helmet, (int) (1));
        } else {
            mangekyo = new ItemStack(ItemMangekyoSharinganObito.helmet, (int) (1));
        }
        ItemStack _setstack = (mangekyo);

        _setstack.setCount(1);
        _setstack.setTagInfo("player_idMost", new NBTTagLong(UUIDMost));
        _setstack.setTagInfo("player_idLeast", new NBTTagLong(UUIDLeast));
        _setstack.setTagInfo("color", new NBTTagLong(color));
        ItemHandlerHelper.giveItemToPlayer(((EntityPlayer) entity), _setstack);









    }
}
