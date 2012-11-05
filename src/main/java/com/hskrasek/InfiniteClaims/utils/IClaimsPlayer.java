package com.hskrasek.InfiniteClaims.utils;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.sk89q.worldguard.LocalPlayer;

public class IClaimsPlayer extends LocalPlayer implements Player
{
	private String	name;
	Player			actualPlayer	= null;
	OfflinePlayer	actualOffline	= null;

	public IClaimsPlayer(String name, InfiniteClaims plugin)
	{
		this.name = name;

		if (plugin.getServer().getPlayer(name) != null)
		{
			actualPlayer = plugin.getServer().getPlayer(name);
		}
		else
		{
			actualOffline = plugin.getServer().getOfflinePlayer(name);
		}
	}

	public void closeInventory()
	{

	}

	public GameMode getGameMode()
	{

		return null;
	}

	public PlayerInventory getInventory()
	{

		return null;
	}

	public ItemStack getItemInHand()
	{

		return null;
	}

	public ItemStack getItemOnCursor()
	{

		return null;
	}

	public String getName()
	{

		return name;
	}

	public InventoryView getOpenInventory()
	{

		return null;
	}

	public int getSleepTicks()
	{

		return 0;
	}

	public boolean isBlocking()
	{

		return false;
	}

	public boolean isSleeping()
	{

		return false;
	}

	public InventoryView openEnchanting(Location arg0, boolean arg1)
	{

		return null;
	}

	public InventoryView openInventory(Inventory arg0)
	{

		return null;
	}

	public void openInventory(InventoryView arg0)
	{

	}

	public InventoryView openWorkbench(Location arg0, boolean arg1)
	{

		return null;
	}

	public void setGameMode(GameMode arg0)
	{

	}

	public void setItemInHand(ItemStack arg0)
	{

	}

	public void setItemOnCursor(ItemStack arg0)
	{

	}

	public boolean setWindowProperty(Property arg0, int arg1)
	{

		return false;
	}

	public boolean addPotionEffect(PotionEffect arg0)
	{

		return false;
	}

	public boolean addPotionEffect(PotionEffect arg0, boolean arg1)
	{

		return false;
	}

	public boolean addPotionEffects(Collection<PotionEffect> arg0)
	{

		return false;
	}

	public void damage(int arg0)
	{

	}

	public void damage(int arg0, Entity arg1)
	{

	}

	public Collection<PotionEffect> getActivePotionEffects()
	{

		return null;
	}

	public double getEyeHeight()
	{

		return 0;
	}

	public double getEyeHeight(boolean arg0)
	{

		return 0;
	}

	public Location getEyeLocation()
	{

		return null;
	}

	public int getHealth()
	{

		return 0;
	}

	public Player getKiller()
	{

		return null;
	}

	public int getLastDamage()
	{

		return 0;
	}

	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> arg0, int arg1)
	{

		return null;
	}

	public List<Block> getLineOfSight(HashSet<Byte> arg0, int arg1)
	{

		return null;
	}

	public int getMaxHealth()
	{

		return 0;
	}

	public int getMaximumAir()
	{

		return 0;
	}

	public int getMaximumNoDamageTicks()
	{

		return 0;
	}

	public int getNoDamageTicks()
	{

		return 0;
	}

	public int getRemainingAir()
	{

		return 0;
	}

	public Block getTargetBlock(HashSet<Byte> arg0, int arg1)
	{

		return null;
	}

	public boolean hasPotionEffect(PotionEffectType arg0)
	{

		return false;
	}

	public <T extends Projectile> T launchProjectile(Class<? extends T> arg0)
	{

		return null;
	}

	public void removePotionEffect(PotionEffectType arg0)
	{

	}

	public void setHealth(int arg0)
	{

	}

	public void setLastDamage(int arg0)
	{

	}

	public void setMaximumAir(int arg0)
	{

	}

	public void setMaximumNoDamageTicks(int arg0)
	{

	}

	public void setNoDamageTicks(int arg0)
	{

	}

	public void setRemainingAir(int arg0)
	{

	}

	public Arrow shootArrow()
	{

		return null;
	}

	public Egg throwEgg()
	{

		return null;
	}

	public Snowball throwSnowball()
	{

		return null;
	}

	public boolean eject()
	{

		return false;
	}

	public int getEntityId()
	{

		return 0;
	}

	public float getFallDistance()
	{

		return 0;
	}

	public int getFireTicks()
	{

		return 0;
	}

	public EntityDamageEvent getLastDamageCause()
	{

		return null;
	}

	public Location getLocation()
	{

		return null;
	}

	public int getMaxFireTicks()
	{

		return 0;
	}

	public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2)
	{

		return null;
	}

	public Entity getPassenger()
	{

		return null;
	}

	public Server getServer()
	{

		return null;
	}

	public int getTicksLived()
	{

		return 0;
	}

	public EntityType getType()
	{

		return null;
	}

	public UUID getUniqueId()
	{

		return null;
	}

	public Entity getVehicle()
	{

		return null;
	}

	public Vector getVelocity()
	{

		return null;
	}

	public World getWorld()
	{
		if (this.actualPlayer != null)
		{
			return actualPlayer.getWorld();
		}
		return null;
	}

	public boolean isDead()
	{

		return false;
	}

	public boolean isEmpty()
	{

		return false;
	}

	public boolean isInsideVehicle()
	{

		return false;
	}

	public boolean leaveVehicle()
	{

		return false;
	}

	public void playEffect(EntityEffect arg0)
	{

	}

	public void remove()
	{

	}

	public void setFallDistance(float arg0)
	{

	}

	public void setFireTicks(int arg0)
	{

	}

	public void setLastDamageCause(EntityDamageEvent arg0)
	{

	}

	public boolean setPassenger(Entity arg0)
	{

		return false;
	}

	public void setTicksLived(int arg0)
	{

	}

	public void setVelocity(Vector arg0)
	{

	}

	public boolean teleport(Location arg0)
	{

		return false;
	}

	public boolean teleport(Entity arg0)
	{

		return false;
	}

	public boolean teleport(Location arg0, TeleportCause arg1)
	{

		return false;
	}

	public boolean teleport(Entity arg0, TeleportCause arg1)
	{

		return false;
	}

	public List<MetadataValue> getMetadata(String arg0)
	{

		return null;
	}

	public boolean hasMetadata(String arg0)
	{

		return false;
	}

	public void removeMetadata(String arg0, Plugin arg1)
	{

	}

	public void setMetadata(String arg0, MetadataValue arg1)
	{

	}

	public PermissionAttachment addAttachment(Plugin arg0)
	{

		return null;
	}

	public PermissionAttachment addAttachment(Plugin arg0, int arg1)
	{

		return null;
	}

	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2)
	{

		return null;
	}

	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3)
	{

		return null;
	}

	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{

		return null;
	}

	public boolean hasPermission(String arg0)
	{

		return false;
	}

	public boolean hasPermission(Permission arg0)
	{

		return false;
	}

	public boolean isPermissionSet(String arg0)
	{

		return false;
	}

	public boolean isPermissionSet(Permission arg0)
	{

		return false;
	}

	public void recalculatePermissions()
	{

	}

	public void removeAttachment(PermissionAttachment arg0)
	{

	}

	public boolean isOp()
	{

		return false;
	}

	public void setOp(boolean arg0)
	{

	}

	public void abandonConversation(Conversation arg0)
	{

	}

	public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1)
	{

	}

	public void acceptConversationInput(String arg0)
	{

	}

	public boolean beginConversation(Conversation arg0)
	{

		return false;
	}

	public boolean isConversing()
	{

		return false;
	}

	public void sendMessage(String arg0)
	{

	}

	public void sendMessage(String[] arg0)
	{

	}

	public long getFirstPlayed()
	{

		return 0;
	}

	public long getLastPlayed()
	{

		return 0;
	}

	public Player getPlayer()
	{

		return null;
	}

	public boolean hasPlayedBefore()
	{

		return false;
	}

	public boolean isBanned()
	{

		return false;
	}

	public boolean isOnline()
	{

		return false;
	}

	public boolean isWhitelisted()
	{

		return false;
	}

	public void setBanned(boolean arg0)
	{

	}

	public void setWhitelisted(boolean arg0)
	{

	}

	public Map<String, Object> serialize()
	{

		return null;
	}

	public Set<String> getListeningPluginChannels()
	{

		return null;
	}

	public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2)
	{

	}

	public void awardAchievement(Achievement arg0)
	{

	}

	public boolean canSee(Player arg0)
	{

		return false;
	}

	public void chat(String arg0)
	{

	}

	public InetSocketAddress getAddress()
	{

		return null;
	}

	public boolean getAllowFlight()
	{

		return false;
	}

	public Location getBedSpawnLocation()
	{

		return null;
	}

	public Location getCompassTarget()
	{

		return null;
	}

	public String getDisplayName()
	{

		return null;
	}

	public float getExhaustion()
	{

		return 0;
	}

	public float getExp()
	{

		return 0;
	}

	public int getFoodLevel()
	{

		return 0;
	}

	public int getLevel()
	{

		return 0;
	}

	public String getPlayerListName()
	{

		return null;
	}

	public long getPlayerTime()
	{

		return 0;
	}

	public long getPlayerTimeOffset()
	{

		return 0;
	}

	public float getSaturation()
	{

		return 0;
	}

	public int getTotalExperience()
	{

		return 0;
	}

	public void giveExp(int arg0)
	{

	}

	public void hidePlayer(Player arg0)
	{

	}

	public void incrementStatistic(Statistic arg0)
	{

	}

	public void incrementStatistic(Statistic arg0, int arg1)
	{

	}

	public void incrementStatistic(Statistic arg0, Material arg1)
	{

	}

	public void incrementStatistic(Statistic arg0, Material arg1, int arg2)
	{

	}

	public boolean isFlying()
	{

		return false;
	}

	public boolean isPlayerTimeRelative()
	{

		return false;
	}

	public boolean isSleepingIgnored()
	{

		return false;
	}

	public boolean isSneaking()
	{

		return false;
	}

	public boolean isSprinting()
	{

		return false;
	}

	public void kickPlayer(String arg0)
	{

	}

	public void loadData()
	{

	}

	public boolean performCommand(String arg0)
	{

		return false;
	}

	public void playEffect(Location arg0, Effect arg1, int arg2)
	{

	}

	public <T> void playEffect(Location arg0, Effect arg1, T arg2)
	{

	}

	public void playNote(Location arg0, byte arg1, byte arg2)
	{

	}

	public void playNote(Location arg0, Instrument arg1, Note arg2)
	{

	}

	public void resetPlayerTime()
	{

	}

	public void saveData()
	{

	}

	public void sendBlockChange(Location arg0, Material arg1, byte arg2)
	{

	}

	public void sendBlockChange(Location arg0, int arg1, byte arg2)
	{

	}

	public boolean sendChunkChange(Location arg0, int arg1, int arg2, int arg3, byte[] arg4)
	{

		return false;
	}

	public void sendMap(MapView arg0)
	{

	}

	public void sendRawMessage(String arg0)
	{

	}

	public void setAllowFlight(boolean arg0)
	{

	}
	
	public void setCompassTarget(Location arg0)
	{

	}

	public void setDisplayName(String arg0)
	{

	}

	public void setExhaustion(float arg0)
	{

	}

	public void setExp(float arg0)
	{

	}

	public void setFlying(boolean arg0)
	{

	}

	public void setFoodLevel(int arg0)
	{

	}

	public void setLevel(int arg0)
	{

	}

	public void setPlayerListName(String arg0)
	{

	}

	public void setPlayerTime(long arg0, boolean arg1)
	{

	}

	public void setSaturation(float arg0)
	{

	}

	public void setSleepingIgnored(boolean arg0)
	{

	}

	public void setSneaking(boolean arg0)
	{

	}

	public void setSprinting(boolean arg0)
	{

	}

	public void setTotalExperience(int arg0)
	{

	}

	public void showPlayer(Player arg0)
	{

	}

	public void updateInventory()
	{

	}

	public void ban(String arg0)
	{

	}

	public String[] getGroups()
	{

		return null;
	}

	public com.sk89q.worldedit.Vector getPosition()
	{

		return null;
	}

	public boolean hasGroup(String arg0)
	{

		return false;
	}

	public void kick(String arg0)
	{

	}

	public void printRaw(String arg0)
	{

	}

	@Override
	public int getExpToLevel()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasLineOfSight(Entity arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getFlySpeed()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWalkSpeed()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFlySpeed(float arg0) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setWalkSpeed(float arg0) throws IllegalArgumentException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Inventory getEnderChest()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playSound(Location arg0, Sound arg1, float arg2, float arg3)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveExpLevels(int arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBedSpawnLocation(Location arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBedSpawnLocation(Location arg0, boolean arg1)
	{
		// TODO Auto-generated method stub
		
	}

}
