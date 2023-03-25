package com.github.minersstudios.msitems.listeners.mechanic;

import com.github.minersstudios.mscore.MSListener;
import com.github.minersstudios.msitems.items.DamageableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@MSListener
public class DamageableItemMechanic implements Listener {

	@EventHandler
	public void onPlayerItemDamage(@NotNull PlayerItemDamageEvent event) {
		ItemStack itemStack = event.getItem();
		DamageableItem damageableItem = DamageableItem.fromItemStack(itemStack);
		if (damageableItem != null) {
			damageableItem.setRealDamage(damageableItem.getRealDamage() + event.getDamage());
			damageableItem.saveForItemStack(itemStack);
		}
	}
}
