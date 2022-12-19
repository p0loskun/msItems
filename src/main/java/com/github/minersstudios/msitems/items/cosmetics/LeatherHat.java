package com.github.minersstudios.msitems.items.cosmetics;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.Renameable;
import com.github.minersstudios.msitems.items.Wearable;
import com.github.minersstudios.msitems.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LeatherHat implements Renameable, Wearable {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable Recipe recipe;
	private boolean showInCraftsMenu;

	public LeatherHat() {
		this.namespacedKey = new NamespacedKey(Main.getInstance(), "leather_hat");
		this.itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledName("Кожаная шляпа"));
		itemMeta.setCustomModelData(999);
		itemMeta.lore(ChatUtils.PAINTABLE_LORE_COMPONENT);
		itemMeta.addAttributeModifier(
				Attribute.GENERIC_ARMOR,
				new AttributeModifier(UUID.randomUUID(), "armor", 1.0f, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
		);
		this.itemStack.setItemMeta(itemMeta);
		this.recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "leather_hat"), this.itemStack)
				.shape(
						" L ",
						"LLL"
				).setIngredient('L', Material.LEATHER);
		this.showInCraftsMenu = true;
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

	@Override
	public @Nullable Recipe getRecipe() {
		return this.recipe;
	}

	@Override
	public void setRecipe(@Nullable Recipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean isShowInCraftsMenu() {
		return this.showInCraftsMenu;
	}

	@Override
	public void setShowInCraftsMenu(boolean showInCraftsMenu) {
		this.showInCraftsMenu = showInCraftsMenu;
	}

	@Override
	public Item @NotNull [] getRenameableItems() {
		return Item.renameableItems;
	}

	public enum Item implements Renameable.Item {
		//<editor-fold desc="Hats">
		LEATHER_HAT(999, "Кожаная шляпа", ChatUtils.PAINTABLE_LORE),
		THREED_GLASSES(1211, "3Д Очки"),
		NECTAR_BEE(1213, "Пчела с пыльцой"),
		BEE(1212, "Пчела"),
		ANGRY_NECTAR_BEE(1215, "Злая пчела с пыльцой"),
		ANGRY_BEE(1214, "Злая пчела"),
		PHANTOM(1216, "Фантом"),
		BERET(1217, "Берет", ChatUtils.PAINTABLE_LORE),
		CAP(1218, "Кепка", ChatUtils.PAINTABLE_LORE),
		CARDBOARD_CROWN(1219, "Корона из картона", ChatUtils.PAINTABLE_LORE),
		CHEF_HAT(1220, "Колпак шефа", ChatUtils.PAINTABLE_LORE),
		POVAR_HAT(1221, "Колпак повара", ChatUtils.PAINTABLE_LORE),
		ENCHANTER_HAT(1222, "Чародейная шапка"),
		GOAT_HEAD(1223, "Козья голова"),
		MEDICINE_MASK(1224, "Медицинская маска"),
		NEADER_MASK(1225, "Маска неандертальца", ChatUtils.PAINTABLE_LORE),
		NEADER_MASK_O(1226, "Орущая маска неандертальца", ChatUtils.PAINTABLE_LORE),
		NEADER_MASK_LEAVES(1227, "Маска с листьями неандертальца", ChatUtils.PAINTABLE_LORE),
		NEADER_MASK_LEAVES_O(1228, "Орущая маска с листьями неандертальца", ChatUtils.PAINTABLE_LORE),
		GOLDEN_PROTHESIS_LEFT(1229, "Левый золотой глазной протез", ChatUtils.PAINTABLE_LORE),
		GOLDEN_PROTHESIS_RIGHT(1230, "Правый золотой глазной протез", ChatUtils.PAINTABLE_LORE),
		SILVER_PROTHESIS_LEFT(1231, "Левый серебряный глазной протез", ChatUtils.PAINTABLE_LORE),
		SILVER_PROTHESIS_RIGHT(1232, "Правый серебряный глазной протез", ChatUtils.PAINTABLE_LORE),
		RACOON_HAT(1233, "Шапка из енота"),
		SAMURAI(1234, "Кабуто"),
		SAMURAI_MASK(1235, "Кабуто с маской"),
		SCULK(1236, "Усики вардена"),
		SUN_HAT(1237, "Соломенная шляпа", ChatUtils.PAINTABLE_LORE),
		WAITER_CAP(1238, "Официантская шапочка", ChatUtils.PAINTABLE_LORE),
		WESTERN_HAT(1239, "Ковбойская шляпа"),
		WOLF_HEAD(1240, "Волчья голова", ChatUtils.PAINTABLE_LORE),
		//<editor-fold desc="Villager hats">
		ARMORER_HAT(1241, "Очки бронника"),
		BUTCHER_HAT(1242, "Повязка мясника"),
		CARTOGRAPHER_HAT(1243, "Монокль картографа"),
		DESERT_HAT(1244, "Пустынная шляпа"),
		FARMER_HAT(1245, "Шляпа фермера"),
		FISHERMAN_HAT(1246, "Шляпа рыбака"),
		FLETCHER_HAT(1247, "Шляпа лучника"),
		LIBRARIAN_HAT(1248, "Шляпа библиотекаря"),
		SAVANNA_HAT(1249, "Лавровый венок"),
		SHEPHERD_HAT(1250, "Шляпа пастуха"),
		SNOW_HAT(1251, "Зимняя шляпа"),
		SWAMP_HAT(1252, "Болотный лист"),
		WITCH_HAT(1253, "Шляпа ведьмы")
		//</editor-fold>
		;
		//</editor-fold>
		private final int customModelData;
		@NotNull private final String renameText;
		@Nullable private final List<String> lore;
		private final boolean showInRenameMenu;

		private static final Item[] renameableItems = values();

		Item(
				int customModelData,
				@NotNull String renameText,
				@Nullable List<String> lore,
				boolean showInRenameMenu
		) {
			this.customModelData = customModelData;
			this.renameText = renameText;
			this.lore = lore;
			this.showInRenameMenu = showInRenameMenu;
		}

		Item(
				int customModelData,
				@NotNull String renameText
		) {
			this(customModelData, renameText, null, true);
		}

		Item(
				int customModelData,
				@NotNull String renameText,
				@Nullable List<String> lore
		) {
			this(customModelData, renameText, lore, true);
		}

		@Override
		public @NotNull String getKey() {
			return this.name().toLowerCase(Locale.ROOT);
		}

		@Override
		public int getCustomModelData() {
			return this.customModelData;
		}

		@Override
		public @NotNull String getRenameText() {
			return this.renameText;
		}

		@Override
		public @Nullable List<String> getLore() {
			return this.lore;
		}

		@Override
		public boolean isShowInRenameMenu() {
			return this.showInRenameMenu;
		}
	}
}
