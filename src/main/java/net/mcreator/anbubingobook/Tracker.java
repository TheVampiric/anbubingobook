/**
 * This mod element is always locked. Enter your code in the methods below.
 * If you don't need some of these methods, you can remove them as they
 * are overrides of the base class ElementsAnbubingobookMod.ModElement.
 * <p>
 * You can register new events in this class too.
 * <p>
 * As this class is loaded into mod element list, it NEEDS to extend
 * ModElement class. If you remove this extend statement or remove the
 * constructor, the compilation will fail.
 * <p>
 * If you want to make a plain independent class, create it in
 * "Workspace" -> "Source" menu.
 * <p>
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 */
package net.mcreator.anbubingobook;

import com.google.common.collect.Maps;
import net.mcreator.anbubingobook.procedure.procedureevolve;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.narutomod.NarutomodModVariables;
import net.narutomod.item.ItemEightGates;
import net.narutomod.item.ItemSharingan;
import net.narutomod.procedure.ProcedureSync;
import net.narutomod.procedure.ProcedureUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.Entity;


@ElementsAnbubingobookMod.ModElement.Tag
public class Tracker extends ElementsAnbubingobookMod.ModElement {


    private static final String BATTLEXP = NarutomodModVariables.BATTLEXP;

    private static final String FORCE_SEND = "forceSendBattleXP2self";
    private static final String UPDATE_HEALTH = "forceUpdateHealth";

    /**
     * Do not remove this constructor
     */
    public Tracker(ElementsAnbubingobookMod instance) {
        super(instance, 3);
    }




    public static double getBattleXp(EntityPlayer player) {
        return player.getEntityData().getDouble(BATTLEXP);
    }

    public static double getNinjaLevel(EntityPlayer player) {
        return MathHelper.sqrt(getBattleXp(player));
    }

    public static void addBattleXp(EntityPlayer entity, double xp) {
        addBattleXp(entity, (xp * ModConfig.Ninja_XP_MULTI), true);
    }

    private static void addBattleXp(EntityPlayer entity, double xp, boolean sendMessage) {
        entity.getEntityData().setDouble(BATTLEXP, Math.min(getBattleXp(entity) + xp, ModConfig.Max_Ninja_XP));
        if (entity instanceof EntityPlayerMP) {
            sendBattleXPToTracking((EntityPlayerMP) entity);
            if (sendMessage) {
                entity.sendStatusMessage(new TextComponentString(
                        net.minecraft.util.text.translation.I18n.translateToLocal("chattext.ninjaexperience") +
                                String.format("%.1f", getBattleXp(entity))), true);
            }
        }
    }

    private static void logBattleExp(EntityPlayer entity, double xp) {
        if (entity instanceof EntityPlayerMP
                && ProcedureUtils.advancementAchieved((EntityPlayerMP) entity, "narutomod:ninjaachievement")) {
            addBattleXp(entity, xp);
            ItemEightGates.logBattleXP(entity);
            JXP.logBattleXP(entity);
            //EntityTracker.getOrCreate(entity).lastLoggedXpTime = entity.ticksExisted;
            entity.getEntityData().setInteger("lastLoggedXpTime", entity.ticksExisted);
        }
    }

    private static void sendBattleXPToSelf(EntityPlayerMP player) {
        ProcedureSync.EntityNBTTag.sendToSelf(player, BATTLEXP, getBattleXp(player));
    }


    private static void sendBattleXPToTracking(EntityPlayerMP player) {
        ProcedureSync.EntityNBTTag.sendToTracking(player, BATTLEXP, getBattleXp(player));
    }


    private boolean isOffCooldown(Entity entity) {
        return true;
    }


    public class PlayerHook {
        private boolean isOffCooldown(Entity entity) {
            return true;
        }


        @SubscribeEvent(priority = EventPriority.HIGH)
        public void onDamaged(LivingDamageEvent event) {
            Entity targetEntity = event.getEntity();
            Entity sourceEntity = event.getSource().getTrueSource();
            float amount = event.getAmount();
            if (!targetEntity.equals(sourceEntity) && sourceEntity instanceof EntityLivingBase && amount > 0f) {
                if (this.isOffCooldown(targetEntity) && targetEntity instanceof EntityPlayer && amount < ((EntityPlayer) targetEntity).getHealth()) {
                    double bxp = getBattleXp((EntityPlayer) targetEntity);
                    logBattleExp((EntityPlayer) targetEntity, bxp < 1d ? 1d : ((amount / MathHelper.sqrt(MathHelper.sqrt(bxp)))));
                }
                if (sourceEntity instanceof EntityPlayer) {
                    double xp = 0.0d;
                    if ((targetEntity instanceof EntityPlayer || (targetEntity instanceof EntityLiving && !((EntityLiving) targetEntity).isAIDisabled()))
                            && this.isOffCooldown(sourceEntity)) {
                        EntityLivingBase target = (EntityLivingBase) targetEntity;
                        int resistance = target.isPotionActive(MobEffects.RESISTANCE)
                                ? target.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 2 : 1;
                        double x = MathHelper.sqrt(target.getMaxHealth() * ProcedureUtils.getModifiedAttackDamage(target)
                                * MathHelper.sqrt(ProcedureUtils.getArmorValue(target) + 1d) * Math.min(resistance, 6));
                        xp = Math.min(x * Math.min(amount / target.getMaxHealth(), 1f), 60d);
                        xp *= sourceEntity.getEntityData().hasKey("VEZx") ? sourceEntity.getEntityData().getDouble("VEZx") : 0.5d;
                    }
                    if (xp > 0d) {
                        logBattleExp((EntityPlayer) sourceEntity, xp);
                    }
                }

            }
        }

        private final Map<Integer, Map<String, Object>> persistentDataMap = Maps.newHashMap();
        private final UUID hp_uuid = UUID.fromString("84d6711b-c26d-4dfa-b0c5-1ff54395f4de");

        @SubscribeEvent
        public void onTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END && event.player instanceof EntityPlayerMP) {
                double d = getBattleXp(event.player) * 0.005d;
                if (d > 0d) {
                    IAttributeInstance maxHealthAttr = event.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
                    AttributeModifier attr = maxHealthAttr.getModifier(hp_uuid);
                    if (attr == null || (int) attr.getAmount() / 2 != (int) d / 2) {
                        if (attr != null) {
                            maxHealthAttr.removeModifier(hp_uuid);
                        }
                        maxHealthAttr.applyModifier(new AttributeModifier(hp_uuid, "ninja.maxhealth", d, 0));
                        event.player.setHealth(event.player.getHealth() + 0.1f);
                    }
                }
                if (event.player.getEntityData().getBoolean(FORCE_SEND)) {
                    event.player.getEntityData().removeTag(FORCE_SEND);
                    sendBattleXPToTracking((EntityPlayerMP) event.player);
                }
                if (event.player.getEntityData().getBoolean(UPDATE_HEALTH)) {
                    event.player.getEntityData().removeTag(UPDATE_HEALTH);
                    event.player.setHealth(event.player.getHealth());
                }
            }
        }


        @SubscribeEvent(priority = EventPriority.LOW)
        public void LivingDeathEvent(LivingDeathEvent event) {

            if (ModConfig.solo_MS) {

                if (event.getSource().getTrueSource() instanceof EntityPlayer) {


                    if (event.getEntity() instanceof EntityWolf) {

                        EntityWolf wolf = (EntityWolf) event.getEntity();
                        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

                        UUID WOLFUU = wolf.getOwnerId();
                        UUID PUUID = player.getUniqueID();


                        ItemStack helmet = player.inventory.armorInventory.get(3);

                        if (WOLFUU == PUUID) {
                            if (helmet.getItem() == ItemSharingan.helmet) {
                                if (ModConfig.Wolf_XP <= player.getEntityData().getDouble(BATTLEXP)) {
                                    helmet.shrink(1);
                                    Map<String, Object> dependencies = new HashMap<>();
                                    dependencies.put("entity", player);
                                    procedureevolve.executeProcedure(dependencies);
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerHook());
    }

}
