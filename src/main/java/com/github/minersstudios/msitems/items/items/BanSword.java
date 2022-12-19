package com.github.minersstudios.msitems.items.items;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.CustomItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BanSword implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;

	public BanSword() {
		this.namespacedKey = new NamespacedKey(Main.getInstance(), "ban_sword");
		this.itemStack = new ItemStack(Material.NETHERITE_SWORD);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledName("Бан-меч"));
		itemMeta.setCustomModelData(12000);
		itemMeta.setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
		itemMeta.addAttributeModifier(
				Attribute.GENERIC_ATTACK_DAMAGE,
				new AttributeModifier(UUID.randomUUID(), "attack_damage", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
		);
		itemMeta.addAttributeModifier(
				Attribute.GENERIC_LUCK,
				new AttributeModifier(UUID.randomUUID(), "luck", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
		);
		this.itemStack.setItemMeta(itemMeta);
	}

	@Override
	public @NotNull NamespacedKey getNamespacedKey() {
		return this.namespacedKey;
	}

	@Override
	public void setNamespacedKey(@NotNull NamespacedKey namespacedKey) {
		this.namespacedKey = namespacedKey;
	}

	@Override
	public @NotNull ItemStack getItemStack() {
		return this.itemStack;
	}

	@Override
	public void setItemStack(@NotNull ItemStack itemStack) {
		this.itemStack = itemStack;
	}
}
