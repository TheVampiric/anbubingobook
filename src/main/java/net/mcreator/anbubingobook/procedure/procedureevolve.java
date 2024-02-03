package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.ItemHandlerHelper;
import net.narutomod.item.ItemMangekyoSharingan;
import net.narutomod.item.ItemMangekyoSharinganObito;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;


import java.util.Map;
import java.util.UUID;
import java.util.Random;

import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;
import net.narutomod.procedure.ProcedureUtils;

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


        EntityPlayerMP entity = (EntityPlayerMP) dependencies.get("entity");

        long UUIDMost = entity.getUniqueID().getMostSignificantBits();
        long UUIDLeast = entity.getUniqueID().getLeastSignificantBits();
        long color = (long) (Math.random() * 888888888 + 111111111);
        String name = entity.getName();


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
        _setstack.setTagInfo("display", new NBTTagString(name));
        ItemHandlerHelper.giveItemToPlayer(entity, _setstack);

        net.narutomod.procedure.ProcedureUtils.grantAdvancement(entity, "narutomod:mangekyosharinganopened", false);













    }
}
