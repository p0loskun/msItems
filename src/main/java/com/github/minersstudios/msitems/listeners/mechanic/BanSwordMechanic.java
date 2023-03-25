package com.github.minersstudios.msitems.listeners.mechanic;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.mscore.utils.MSItemUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.register.items.BanSword;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@MSListener
public class BanSwordMechanic implements Listener {

	@EventHandler
	public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
		if (
				!(event.getDamager() instanceof Player damager)
				|| !(event.getEntity() instanceof Player damaged)
				|| !(MSItemUtils.getCustomItem(damager.getActiveItem()) instanceof BanSword)
		) return;
		event.setCancelled(true);
		if (damager.isOp()) {
			try {
				Bukkit.getScheduler().callSyncMethod(MSItems.getInstance(), () ->
						Bukkit.dispatchCommand(damager, "ban " + damaged + " 9999999 Вы были поражены великим Бан-Мечём")
				);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		ItemStack currentItem = event.getCurrentItem();
		if (!(MSItemUtils.getCustomItem(currentItem) instanceof BanSword)) return;
		currentItem.setAmount(0);
		event.setCancelled(true);
		Bukkit.getScheduler().runTask(MSItems.getInstance(), ((Player) event.getWhoClicked())::updateInventory);
	}
}
