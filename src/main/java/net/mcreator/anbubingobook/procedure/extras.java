package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ModConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.command.AdvancementCommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.narutomod.NarutomodMod;
import net.narutomod.NarutomodModVariables;
import net.narutomod.item.ItemBakuton;
import net.narutomod.item.ItemHyoton;
import net.narutomod.item.ItemJutsu;
import net.narutomod.item.ItemMokuton;
import net.narutomod.procedure.ProcedureUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class extras {
    public static String modid = "narutomod:";

    @Nullable
    public static List<String> CheckAdvancements(EntityPlayerMP player) {
        List<String> advancements = new ArrayList<>();


        if (ProcedureUtils.advancementAchieved(player, modid + "bakuton_acquired")) {
            advancements.add(modid + "bakuton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "byakuganopened")) {
            advancements.add(modid + "byakuganopened");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "futton_acquired")) {
            advancements.add(modid + "futton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "hyoton_acquired")) {
            advancements.add(modid + "hyoton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "jiton_acquired")) {
            advancements.add(modid + "jiton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "kekkei_tota_awakened")) {
            advancements.add(modid + "kekkei_tota_awakened");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "ranton_acquired")) {
            advancements.add(modid + "ranton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "shakuton_acquired")) {
            advancements.add(modid + "shakuton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "shikotsumyaku_acquired")) {
            advancements.add(modid + "shikotsumyaku_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "yooton_acquired")) {
            advancements.add(modid + "yooton_acquired");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "eternalmangekyoachieved")) {
            advancements.add(modid + "eternalmangekyoachieved");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "mangekyosharinganopened")) {
            advancements.add(modid + "mangekyosharinganopened");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "rinneganawakened")) {
            advancements.add(modid + "rinneganawakened");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "rinnesharinganactivated")) {
            advancements.add(modid + "rinnesharinganactivated");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "tensei_byakugan_activated")) {
            advancements.add(modid + "tensei_byakugan_activated");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "tenseigan_achieved")) {
            advancements.add(modid + "tenseigan_achieved");
        }
        if (ProcedureUtils.advancementAchieved(player, modid + "sharinganopened")) {
            advancements.add(modid + "sharinganopened");
        }
        if (ModConfig.WOOD_REROLL && ProcedureUtils.advancementAchieved(player, modid + "mokuton_acquired") ){
            advancements.add(modid + "mokuton_acquired");
        }
        if (advancements.isEmpty()) {
            return null;
        }
        return advancements;
    }

    public static void RemoveAdvancement(EntityPlayerMP player, String ID) {
        AdvancementManager manager = ((WorldServer) player.world).getAdvancementManager();
        Advancement adv = manager.getAdvancement(new ResourceLocation(ID));
        AdvancementProgress progress = player.getAdvancements().getProgress(adv);
        for (String s : progress.getCompletedCriteria()) {
            player.getAdvancements().revokeCriterion(adv, s);

        }
    }

    public static void Removeitem(EntityPlayer player, Item item) {
        if (ProcedureUtils.hasItemInInventory(player, item)) {
            ItemStack stack = ProcedureUtils.getMatchingItemStack(player, item);
            stack.shrink(1);
        }
        if (ModConfig.WOOD_REROLL && ProcedureUtils.hasItemInInventory(player, ItemMokuton.block)){
            ItemStack wood = ProcedureUtils.getMatchingItemStack(player, ItemMokuton.block);
            wood.shrink(1);
        }
    }
}
