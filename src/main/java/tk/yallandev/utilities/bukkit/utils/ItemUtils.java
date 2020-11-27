package tk.yallandev.utilities.bukkit.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

	public static void dropAndClear(Player p, List<ItemStack> items, Location l) {
		dropItems(items, l);
		p.closeInventory();
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.getInventory().clear();
		p.setItemOnCursor(null);
	}

	public static void dropItems(List<ItemStack> items, Location l) {
		World world = l.getWorld();
		for (ItemStack item : items) {
			if (item == null || item.getType() == Material.AIR)
				continue;
			if (item.hasItemMeta())
				world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
			else
				world.dropItemNaturally(l, item);
		}
	}

	public static void addItem(Player player, ItemStack item, Location location) {
		int slot = player.getInventory().first(item.getType());

		if (slot == -1)
			slot = player.getInventory().firstEmpty();
		else {
			if (player.getInventory().getItem(slot).getAmount() + item.getAmount() <= 64) {
				player.getInventory().addItem(item);
				return;
			}

			slot = player.getInventory().firstEmpty();
		}

		if (slot == -1) {
			boolean needDrop = true;

			for (ItemStack itemContent : player.getInventory().getContents()) {
				if (itemContent.getType() == item.getType() && itemContent.getDurability() == item.getDurability())
					if (itemContent.getAmount() + item.getAmount() <= 64) {
						player.getInventory().addItem(item);
						needDrop = false;
					} else {

						while (itemContent.getAmount() + item.getAmount() <= 64 && item.getAmount() >= 0) {
							itemContent.setAmount(itemContent.getAmount() + 1);
							item.setAmount(item.getAmount() - 1);
						}

						if (item.getAmount() <= 0) {
							needDrop = false;
						}
					}
			}

			if (needDrop)
				location.getWorld().dropItem(location, item);
		} else {
			player.getInventory().addItem(item);
		}
	}

	public static Stream<ItemStack> getItensInInventory(Player player, ItemStack itemStack) {
		return Arrays.asList(player.getInventory().getContents()).stream().filter(item -> item != null
				&& item.getType() == itemStack.getType() && item.getDurability() == itemStack.getDurability());
	}

	public static int first(Inventory inv, ItemStack item, boolean withAmount) {
		if (item == null) {
			return -1;
		}

		ItemStack[] inventory = inv.getContents();

		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null)
				continue;

			if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
				return i;
			}
		}
		return -1;
	}

	public static HashMap<Integer, ItemStack> removeItem(Inventory inventory, ItemStack... items) {
		Validate.notNull(items, "Items cannot be null");
		HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

		// TODO: optimization

		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			int toDelete = item.getAmount();

			while (true) {
				int first = first(inventory, item, false);

				// Drat! we don't have this type in the inventory
				if (first == -1) {
					item.setAmount(toDelete);
					leftover.put(i, item);
					break;
				} else {
					ItemStack itemStack = inventory.getItem(first);
					int amount = itemStack.getAmount();

					if (amount <= toDelete) {
						toDelete -= amount;
						// clear the slot, all used up
						inventory.clear(first);
					} else {
						// split the stack and store
						itemStack.setAmount(amount - toDelete);
						inventory.setItem(first, itemStack);
						toDelete = 0;
					}
				}

				// Bail when done
				if (toDelete <= 0) {
					break;
				}
			}
		}
		return leftover;
	}

	public static boolean isInventoryFull(Player player, ItemStack itemStack) {
		return false;
	}

}
