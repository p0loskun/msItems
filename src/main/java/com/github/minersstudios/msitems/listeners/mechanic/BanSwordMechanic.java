package com.github.minersstudios.msitems.listeners.mechanic;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.items.BanSword;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BanSwordMechanic implements Listener {

	@EventHandler
	public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
		if (
				!(event.getDamager() instanceof Player damager)
				|| !(event.getEntity() instanceof Player damaged)
				|| !damager.isOp()
				|| !(ItemUtils.getCustomItem(damager.getActiveItem()) instanceof BanSword)
		) return;
		event.setCancelled(true);
		damaged.banPlayer("Вы были поражены великим Бан-Мечём", damager.getName());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		ItemStack currentItem = event.getCurrentItem();
		if (!(ItemUtils.getCustomItem(currentItem) instanceof BanSword)) return;
		currentItem.setAmount(0);
		event.setCancelled(true);
		Bukkit.getScheduler().runTask(Main.getInstance(), ((Player) event.getWhoClicked())::updateInventory);
	}
}
