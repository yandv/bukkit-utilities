package tk.yallandev.utilities.bukkit.menu;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.yallandev.utilities.bukkit.menu.click.MenuClickHandler;

public class MenuItem {

	private ItemStack stack;
	private MenuClickHandler handler;

	public MenuItem(ItemStack itemstack) {
		this.stack = itemstack;
		this.handler = new MenuClickHandler() {
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
			}

		};
	}

	public MenuItem(ItemStack itemstack, MenuClickHandler clickHandler) {
		this.stack = itemstack;
		this.handler = clickHandler;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setLore(List<String> loreList) {
		ItemStack s = getStack();
		ItemMeta itemMeta = s.getItemMeta();
		itemMeta.setLore(loreList);
		s.setItemMeta(itemMeta);
	}

	public MenuClickHandler getHandler() {
		return handler;
	}

	public void destroy() {
		stack = null;
		handler = null;
	}
}
