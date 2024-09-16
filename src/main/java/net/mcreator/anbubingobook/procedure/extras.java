package net.mcreator.anbubingobook.procedure;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.command.AdvancementCommand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.narutomod.procedure.ProcedureUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class extras {

    @Nullable
    public static List<String> CheckAdvancements (EntityPlayerMP player){
        List<String> advancements = new ArrayList<>();
        if (ProcedureUtils.advancementAchieved(player, "narutomod:bakuton_acquired")){advancements.add("narutomod:bakuton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:byakuganopened")){advancements.add( "narutomod:byakuganopened");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:futton_acquired")){advancements.add("narutomod:futton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:hyoton_acquired")){advancements.add("narutomod:hyoton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:jiton_acquired")){advancements.add("narutomod:jiton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:kekkei_tota_awakened")){advancements.add("narutomod:kekkei_tota_awakened");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:mokuton_acquired")){advancements.add("narutomod:mokuton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:ranton_acquired")){advancements.add("narutomod:ranton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:shakuton_acquired")){advancements.add("narutomod:shakuton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:shikotsumyaku_acquired")){advancements.add("narutomod:shikotsumyaku_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:yooton_acquired")){advancements.add("narutomod:yooton_acquired");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:eternalmangekyoachieved")){advancements.add("narutomod:eternalmangekyoachieved");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:mangekyosharinganopened")){advancements.add("narutomod:mangekyosharinganopened");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:rinneganawakened")){advancements.add("narutomod:rinneganawakened");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:rinnesharinganactivated")){advancements.add("narutomod:rinnesharinganactivated");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:tensei_byakugan_activated")){advancements.add("narutomod:tensei_byakugan_activated");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:tenseigan_achieved")){advancements.add("narutomod:tenseigan_achieved");}
        if (ProcedureUtils.advancementAchieved(player, "narutomod:sharinganopened")){advancements.add("narutomod:sharinganopened");}
        if (advancements.isEmpty()) {return null;}
        return advancements;
    }

    public static void RemoveAdvancement (EntityPlayerMP player, String ID){
        AdvancementManager manager = ((WorldServer)player.world).getAdvancementManager();
        Advancement adv = manager.getAdvancement(new ResourceLocation(ID));
        AdvancementProgress progress = player.getAdvancements().getProgress(adv);
        for (String s : progress.getCompletedCriteria()){
            player.getAdvancements().revokeCriterion(adv, s);

        }


    }

}
