package cn.superiormc.mythicchanger.objects.changes;

import com.google.common.base.Enums;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RemoveAllEnchants extends AbstractChangesRule {

    public RemoveAllEnchants() {
        super();
    }

    @Override
    public ItemStack setChange(ConfigurationSection section, ItemStack item, Player player) {
        if (!item.hasItemMeta()) {
            return item;
        }
        if (!section.getBoolean("remove-all-enchants")) {
            return item;
        }
        ItemMeta meta = item.getItemMeta();
        for (Enchantment enchant : item.getEnchantments().keySet()) {
            meta.removeEnchant(enchant);
        }
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getWeight() {
        return -198;
    }
}
