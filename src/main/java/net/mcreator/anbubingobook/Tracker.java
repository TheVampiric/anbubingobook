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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.narutomod.ElementsNarutomodMod;
import net.narutomod.NarutomodModVariables;
import net.narutomod.item.ItemEightGates;
import net.narutomod.item.ItemJutsu;
import net.narutomod.procedure.ProcedureSync;
import net.narutomod.procedure.ProcedureUtils;
import net.mcreator.anbubingobook.ModConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.narutomod.PlayerTracker.getBattleXp;

@ElementsAnbubingobookMod.ModElement.Tag
public class Tracker extends ElementsAnbubingobookMod.ModElement {



    private static final String BATTLEXP = NarutomodModVariables.BATTLEXP;
    private static final String KEEPXP_RULE = "keepNinjaXp";
    public static final String FORCE_DOJUTSU_DROP_RULE = "forceDojutsuDropOnDeath";
    private static final String FORCE_SEND = "forceSendBattleXP2self";
    private static final String UPDATE_HEALTH = "forceUpdateHealth";

    /**
     * Do not remove this constructor
     */
    public Tracker(ElementsAnbubingobookMod instance) {
        super(instance, 3);
    }




    public static boolean keepNinjaXp(World world) {
        return world.getGameRules().getBoolean(KEEPXP_RULE);
    }

    public static boolean isNinja(EntityPlayer player) {
        return player.getEntityData().getDouble(BATTLEXP) > 0.0d;
    }

    public static double getBattleXp(EntityPlayer player) {
        return player.getEntityData().getDouble(BATTLEXP);
    }

    public static double getNinjaLevel(EntityPlayer player) {
        return MathHelper.sqrt(getBattleXp(player));
    }

    public static void addBattleXp(EntityPlayer entity, double xp) {
        addBattleXp(entity, xp, true);
    }

    private static void addBattleXp(EntityPlayer entity, double xp, boolean sendMessage) {
        entity.getEntityData().setDouble(BATTLEXP, Math.min(getBattleXp(entity) + xp, 100000.0d));
        if (entity instanceof EntityPlayerMP) {
            sendBattleXPToTracking((EntityPlayerMP)entity);
            if (sendMessage) {
                entity.sendStatusMessage(new TextComponentString(
                        net.minecraft.util.text.translation.I18n.translateToLocal("chattext.ninjaexperience")+
                                String.format("%.1f", getBattleXp(entity))), true);
            }
        }
    }

    private static void logBattleExp(EntityPlayer entity, double xp) {
        if (entity instanceof EntityPlayerMP
                && ProcedureUtils.advancementAchieved((EntityPlayerMP)entity, "narutomod:ninjaachievement")) {
            addBattleXp((EntityPlayerMP)entity, xp);
            ItemEightGates.logBattleXP(entity);
            ItemJutsu.logBattleXP(entity);
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

    public static class Deaths {
        private static final List<Deaths> deadPlayers = Lists.newArrayList();
        private final UUID playerId;
        private final double x;
        private final double y;
        private final double z;
        private final long time;
        private final Team team;
        private final double lastXp;

        private Deaths(EntityPlayer player) {
            this.playerId = player.getUniqueID();
            this.x = player.posX;
            this.y = player.posY;
            this.z = player.posZ;
            this.time = player.world.getTotalWorldTime();
            this.team = player.getTeam();
            this.lastXp = getBattleXp(player);
        }

        public static void log(EntityPlayer entity) {
            Iterator<Deaths> iter = deadPlayers.iterator();
            while (iter.hasNext()) {
                Deaths death = iter.next();
                if (death.playerId.equals(entity.getUniqueID())) {
                    iter.remove();
                }
            }
            deadPlayers.add(new Deaths(entity));
            if (!keepNinjaXp(entity.world)) {
                entity.getEntityData().setDouble(BATTLEXP, 0.0D);
                if (entity instanceof EntityPlayerMP) {
                    sendBattleXPToTracking((EntityPlayerMP)entity);
                }
            }
        }

        public static void clear() {
            deadPlayers.clear();
        }

        public static Deaths mostRecent() {
            if (!deadPlayers.isEmpty())
                return deadPlayers.get(deadPlayers.size());
            return null;
        }

        public static boolean hasRecentNearby(EntityPlayer player, double distance, double timeframe) {
            return hasRecentNearby(player, distance, timeframe, true);
        }

        public static boolean hasRecentNearby(EntityPlayer player, double distance, double timeframe, boolean checkTeam) {
            if (deadPlayers.isEmpty())
                return false;
            for (int i = deadPlayers.size(); --i >= 0;) {
                Deaths deadguy = deadPlayers.get(i);
                if (!deadguy.playerId.equals(player.getUniqueID())) {
                    double d0 = deadguy.x - player.posX;
                    double d1 = deadguy.y - player.posY;
                    double d2 = deadguy.z - player.posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    if (d3 < distance * distance && player.world.getTotalWorldTime() - deadguy.time <= timeframe
                            && (!checkTeam || player.isOnScoreboardTeam(deadguy.team))) {
                        return true;
                    }
                }
            }
            return false;
        }

        public static boolean hasRecentMatching(EntityPlayer player, double timeframe) {
            if (!deadPlayers.isEmpty()) {
                for (int i = deadPlayers.size(); --i >= 0;) {
                    Deaths deadguy = deadPlayers.get(i);
                    if (deadguy.playerId.equals(player.getUniqueID()))
                        return true;
                }
            }
            return false;
        }

        public static long mostRecentTime(EntityPlayer player) {
            if (!deadPlayers.isEmpty()) {
                for (int i = deadPlayers.size(); --i >= 0;) {
                    Deaths deadguy = deadPlayers.get(i);
                    if (deadguy.playerId.equals(player.getUniqueID()))
                        return deadguy.time;
                }
            }
            return 0L;
        }

        public static double getXpBeforeDeath(EntityPlayer player) {
            if (!deadPlayers.isEmpty()) {
                for (int i = deadPlayers.size(); --i >= 0;) {
                    Deaths deadguy = deadPlayers.get(i);
                    if (deadguy.playerId.equals(player.getUniqueID()))
                        return deadguy.lastXp;
                }
            }
            return 0.0d;
        }
    }

    public class PlayerHook {
        private final Map<Integer, Map<String, Object>> persistentDataMap = Maps.newHashMap();
        private final UUID hp_uuid = UUID.fromString("84d6711b-c26d-4dfa-b0c5-1ff54395f4de");

        @SubscribeEvent
        public void onTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END && event.player instanceof EntityPlayerMP) {
                double d = getBattleXp(event.player) * 0.005d;
                if (d > 0d) {
                    IAttributeInstance maxHealthAttr = event.player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
                    AttributeModifier attr = maxHealthAttr.getModifier(hp_uuid);
                    if (attr == null || (int)attr.getAmount() / 2 != (int)d / 2) {
                        if (attr != null) {
                            maxHealthAttr.removeModifier(hp_uuid);
                        }
                        maxHealthAttr.applyModifier(new AttributeModifier(hp_uuid, "ninja.maxhealth", d, 0));
                        event.player.setHealth(event.player.getHealth() + 0.1f);
                    }
                }
                if (event.player.getEntityData().getBoolean(FORCE_SEND)) {
                    event.player.getEntityData().removeTag(FORCE_SEND);
                    sendBattleXPToTracking((EntityPlayerMP)event.player);
                }
                if (event.player.getEntityData().getBoolean(UPDATE_HEALTH)) {
                    event.player.getEntityData().removeTag(UPDATE_HEALTH);
                    event.player.setHealth(event.player.getHealth());
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onDeath(LivingDeathEvent event) {
            EntityLivingBase entity = event.getEntityLiving();
            if (entity instanceof EntityPlayerMP) {
                Deaths.log((EntityPlayer) entity);
                entity.clearActivePotions();
            }
        }

        private boolean isOffCooldown(Entity entity) {
            return true;
        }

        @SubscribeEvent(priority = EventPriority.HIGH)
        public void onDamaged(LivingDamageEvent event) {
            Entity targetEntity = event.getEntity();
            Entity sourceEntity = event.getSource().getTrueSource();
            float amount = event.getAmount();
            if (!targetEntity.equals(sourceEntity) && sourceEntity instanceof EntityLivingBase && amount > 0f) {
                if (this.isOffCooldown(targetEntity) && targetEntity instanceof EntityPlayer && amount < ((EntityPlayer)targetEntity).getHealth()) {
                    double bxp = getBattleXp((EntityPlayer)targetEntity);
                    logBattleExp((EntityPlayer)targetEntity, bxp < 1d ? 1d : ((amount / MathHelper.sqrt(MathHelper.sqrt(bxp))))*ModConfig.Ninja_XP_MULTI);
                }
                if (sourceEntity instanceof EntityPlayer) {
                    double xp = 0.0d;
                    if ((targetEntity instanceof EntityPlayer || (targetEntity instanceof EntityLiving && !((EntityLiving)targetEntity).isAIDisabled()))
                            && this.isOffCooldown(sourceEntity)) {
                        EntityLivingBase target = (EntityLivingBase)targetEntity;
                        int resistance = target.isPotionActive(MobEffects.RESISTANCE)
                                ? target.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 2 : 1;
                        double x = MathHelper.sqrt(target.getMaxHealth() * ProcedureUtils.getModifiedAttackDamage(target)
                                * MathHelper.sqrt(ProcedureUtils.getArmorValue(target)+1d) * Math.min(resistance, 6));
                        xp = Math.min(x * Math.min(amount / target.getMaxHealth(), 1f) * ModConfig.Ninja_XP_MULTI, 60d);
                        xp *= sourceEntity.getEntityData().hasKey("VEZx") ? sourceEntity.getEntityData().getDouble("VEZx") : 0.5d;
                    }
                    if (xp > 0d) {
                        logBattleExp((EntityPlayer)sourceEntity, xp);
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