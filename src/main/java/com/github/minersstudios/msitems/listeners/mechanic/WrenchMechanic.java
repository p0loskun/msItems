package com.github.minersstudios.msitems.listeners.mechanic;

import com.github.minersstudios.msdecor.customdecor.Sittable;
import com.github.minersstudios.msdecor.customdecor.Typed;
import com.github.minersstudios.msdecor.customdecor.Wrenchable;
import com.github.minersstudios.msdecor.utils.BlockUtils;
import com.github.minersstudios.msdecor.utils.CustomDecorUtils;
import com.github.minersstudios.msdecor.utils.EntityUtils;
import com.github.minersstudios.msitems.items.items.Wrench;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrenchMechanic implements Listener {
	private Player player;
	private Location location;
	private ItemStack itemInMainHand;

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
		if (
				event.getAction() != Action.RIGHT_CLICK_BLOCK
				|| event.getClickedBlock() == null
				|| event.getHand() == null
		) return;
		Block clickedBlock = event.getClickedBlock();
		this.player = event.getPlayer();
		this.itemInMainHand = this.player.getInventory().getItemInMainHand();
		this.location = clickedBlock.getLocation().toCenterLocation();
		if (
				event.getHand() == EquipmentSlot.HAND
				&& ItemUtils.getCustomItem(this.itemInMainHand) instanceof Wrench
		) {
			event.setCancelled(Tag.DIRT.isTagged(clickedBlock.getType()));
			if (
					BlockUtils.isCustomDecorMaterial(clickedBlock.getType())
					&& CustomDecorUtils.getCustomDecorDataByLocation(this.location) instanceof Wrenchable wrenchable
			) {
				if (wrenchable instanceof Sittable && !this.player.isSneaking()) return;
				for (Entity nearbyEntity : clickedBlock.getWorld().getNearbyEntities(this.location, 0.5d, 0.5d, 0.5d)) {
					if (nearbyEntity instanceof ItemFrame itemFrame) {
						this.use(itemFrame, wrenchable);
						break;
					}
				}
				for (Entity nearbyEntity : clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation().clone().add(0.5d, 0.0d, 0.5d), 0.2d, 0.3d, 0.2d)) {
					if (nearbyEntity instanceof ArmorStand armorStand) {
						this.use(armorStand, wrenchable);
						break;
					}
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractAtEntity(@NotNull PlayerInteractAtEntityEvent event) {
		Entity entity = event.getRightClicked();
		if (!EntityUtils.isCustomDecorEntity(entity)) return;
		this.player = event.getPlayer();
		this.itemInMainHand = this.player.getInventory().getItemInMainHand();
		this.location = entity.getLocation();
		if (
				event.getHand() == EquipmentSlot.HAND
				&& ItemUtils.getCustomItem(this.itemInMainHand) instanceof Wrench
				&& CustomDecorUtils.getCustomDecorDataByEntity(entity) instanceof Wrenchable wrenchable
		) {
			this.use(entity, wrenchable);
		}
	}

	private void use(@Nullable Entity entity, @NotNull Wrenchable wrenchable) {
		Typed.Type type = wrenchable.getNextType(wrenchable.getType(wrenchable.getItemStack()));
		if (type == null) return;
		int customModelData = wrenchable.createItemStack(type).getItemMeta().getCustomModelData();
		if (entity instanceof ItemFrame itemFrame) {
			ItemStack itemStack = itemFrame.getItem();
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setCustomModelData(customModelData);
			itemStack.setItemMeta(itemMeta);
			itemFrame.setItem(itemStack);
		} else if (entity instanceof ArmorStand armorStand) {
			ItemStack itemStack = armorStand.getEquipment().getHelmet();
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setCustomModelData(customModelData);
			itemStack.setItemMeta(itemMeta);
			armorStand.getEquipment().setHelmet(itemStack);
		}
		if (
				this.player.getGameMode() == GameMode.SURVIVAL
				&& this.itemInMainHand.getItemMeta() instanceof Damageable damageable
		) {
			damageable.setDamage(damageable.getDamage() + 1);
			this.itemInMainHand.setItemMeta(damageable);
		}
		this.player.swingMainHand();
		this.player.getWorld().playSound(this.location, Sound.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
	}
}
