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

import Jarno.coremod.BattleProgressionConfig;
import Jarno.coremod.ranks.IRank;
import Jarno.coremod.ranks.RankProvider;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.narutomod.*;
import net.narutomod.entity.EntitySusanooBase;
import net.narutomod.entity.EntitySusanooClothed;
import net.narutomod.entity.EntitySusanooSkeleton;
import net.narutomod.entity.EntitySusanooWinged;
import net.narutomod.item.ItemEightGates;
import net.narutomod.item.ItemRinnegan;
import net.narutomod.item.ItemSharingan;
import net.narutomod.procedure.ProcedureSync;
import net.narutomod.procedure.ProcedureUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import org.lwjgl.Sys;


@ElementsAnbubingobookMod.ModElement.Tag
public class Tracker extends ElementsAnbubingobookMod.ModElement {


    private static final double[] DEFAULT_DRAIN = {-30d, -30d, -60d, -70d, -90d};
    private static final double[] STOLEN_DEFAULT_DRAIN = {-60d, -60d, -120d, -140d, -180d};
    private static int DEFAULT_TIME = 40;
    private static int REGEN_TIMER = 0;


    private static final String BATTLEXP = NarutomodModVariables.BATTLEXP;

    private static final String FORCE_SEND = "forceSendBattleXP2self";
    private static final String UPDATE_HEALTH = "forceUpdateHealth";

    int susanootime = 0;

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


    private static void addBattleXp(EntityPlayer entity, double xp, boolean sendMessage) {
        if (Loader.isModLoaded("ninjaxpadjuster")) {
            IRank rank = (IRank) entity.getCapability(RankProvider.RANK_CAP, null);
            if (rank != null && !rank.getRank().isEmpty()) {
                int value = rank.getValue();
                entity.getEntityData().setDouble(BATTLEXP, Math.min(getBattleXp(entity) + xp, value));
            } else {
                entity.getEntityData().setDouble(BATTLEXP, Math.min(getBattleXp(entity), BattleProgressionConfig.maxNxp));
            }
        } else {
            entity.getEntityData().setDouble(BATTLEXP, Math.min(getBattleXp(entity) + xp, ModConfig.Max_Ninja_XP));
        }
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
            addBattleXp(entity, xp, true);
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


        @SubscribeEvent(priority = EventPriority.HIGHEST)
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

            if (ModConfig.solo_MS && event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntity() instanceof EntityWolf) {


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
            if (event.getEntity() instanceof EntityPlayer && ModConfig.RESPAWN_AMOUNT > 0) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                if (!player.world.isRemote) {
                    Chakra.pathway(player).consume(ModConfig.RESPAWN_AMOUNT * -1);
                    Chakra.pathway(player).consume(10.0d);
                }
            }
        }


        @SubscribeEvent
        public void onJoin(PlayerLoggedInEvent event) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            if (!ProcedureUtils.advancementAchieved(player, "narutomod:ninjaachievement") && ModConfig.NINJA_START) {
                ProcedureUtils.grantAdvancement(player, "narutomod:ninjaachievement", true);
            }
        }


        @SubscribeEvent(priority = EventPriority.HIGH)
        public void RegenAndDrain(TickEvent.PlayerTickEvent event) {
            EntityPlayer player = event.player;
            if (PlayerTracker.isNinja(player) && !player.world.isRemote && event.phase == TickEvent.Phase.END) {
                int time;

                if (ModConfig.SUSANOO_TICKS != DEFAULT_TIME) {
                    time = ModConfig.SUSANOO_TICKS;
                } else {
                    time = DEFAULT_TIME;
                }
                if (player.getRidingEntity() instanceof EntitySusanooBase) {
                    Entity susanoo = player.getRidingEntity();
                        if (ModConfig.CUSTOM_SUSANOO_DRAIN) {
                        if (susanoo.ticksExisted % time == 0) {

                            ItemStack MS = player.inventory.armorInventory.get(3);
                            boolean owner = ProcedureUtils.isOriginalOwner(player, MS);
                            boolean legs;

                            if (susanoo instanceof EntitySusanooWinged.EntityCustom) {
                                if (owner) {
                                    Chakra.pathway(player).consume(DEFAULT_DRAIN[4]);
                                    Chakra.pathway(player).consume(ModConfig.SUSANOO_DRAIN[4]);
                                } else {
                                    Chakra.pathway(player).consume(STOLEN_DEFAULT_DRAIN[4]);
                                    Chakra.pathway(player).consume(ModConfig.STOLEN_SUSANOO_DRAIN[4]);
                                }

                            } else if (susanoo instanceof EntitySusanooClothed.EntityCustom) {
                                legs = ((EntitySusanooClothed.EntityCustom) susanoo).hasLegs();
                                if (legs) {
                                    if (owner) {
                                        Chakra.pathway(player).consume(DEFAULT_DRAIN[3]);
                                        Chakra.pathway(player).consume(ModConfig.SUSANOO_DRAIN[3]);
                                    } else {
                                        Chakra.pathway(player).consume(STOLEN_DEFAULT_DRAIN[3]);
                                        Chakra.pathway(player).consume(ModConfig.STOLEN_SUSANOO_DRAIN[3]);
                                    }
                                } else {
                                    if (owner) {
                                        Chakra.pathway(player).consume(DEFAULT_DRAIN[2]);
                                        Chakra.pathway(player).consume(ModConfig.SUSANOO_DRAIN[2]);
                                    } else {
                                        Chakra.pathway(player).consume(STOLEN_DEFAULT_DRAIN[2]);
                                        Chakra.pathway(player).consume(ModConfig.STOLEN_SUSANOO_DRAIN[2]);
                                    }
                                }

                            } else if (susanoo instanceof EntitySusanooSkeleton.EntityCustom) {
                                legs = ((EntitySusanooSkeleton.EntityCustom) susanoo).isFullBody();
                                if (legs) {
                                    if (owner) {
                                        Chakra.pathway(player).consume(DEFAULT_DRAIN[1]);
                                        Chakra.pathway(player).consume(ModConfig.SUSANOO_DRAIN[1]);
                                    } else {
                                        Chakra.pathway(player).consume(STOLEN_DEFAULT_DRAIN[1]);
                                        Chakra.pathway(player).consume(ModConfig.STOLEN_SUSANOO_DRAIN[1]);
                                    }
                                } else {
                                    if (owner) {
                                        Chakra.pathway(player).consume(DEFAULT_DRAIN[0]);
                                        Chakra.pathway(player).consume(ModConfig.SUSANOO_DRAIN[0]);
                                    } else {
                                        Chakra.pathway(player).consume(STOLEN_DEFAULT_DRAIN[0]);
                                        Chakra.pathway(player).consume(ModConfig.STOLEN_SUSANOO_DRAIN[0]);
                                    }
                                }
                            }

                        }
                    }

                        if (!ModConfig.RINNEGAN_INF_SUSANOO && susanoo.ticksExisted >= ModConfig.SUSANOO_ALIVE_TICKS) {
                            Item rinnegan = ItemRinnegan.helmet;
                            if (ProcedureUtils.hasItemInMainInventory(player, rinnegan)) {
                                susanoo.setDead();
                            }
                        }
                }
                if (ModConfig.PASSIVE_REGEN_AMOUNT > 0){
                    if(REGEN_TIMER >= ModConfig.REGEN_TICKS){
                        REGEN_TIMER = 0;
                        Chakra.pathway(player).consume(-ModConfig.PASSIVE_REGEN_AMOUNT);
                    }else {
                        REGEN_TIMER++;
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



