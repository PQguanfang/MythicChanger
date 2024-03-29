package cn.superiormc.mythicchanger.manager;

import cn.superiormc.mythicchanger.MythicChanger;
import cn.superiormc.mythicchanger.objects.changes.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChangesManager {

    public static ChangesManager changesManager;

    private Map<Player, Collection<Integer>> itemCooldown = new ConcurrentHashMap<>();

    private Collection<AbstractChangesRule> rules = new TreeSet<>();

    public ChangesManager() {
        changesManager = this;
        registerNewRule(new AddEnchants());
        registerNewRule(new AddFlags());
        registerNewRule(new AddLoreFirst());
        registerNewRule(new AddLoreLast());
        registerNewRule(new AddLorePrefix());
        registerNewRule(new AddLoreSuffix());
        registerNewRule(new AddNameFirst());
        registerNewRule(new AddNameLast());
        registerNewRule(new DeleteEnchants());
        registerNewRule(new RemoveAllEnchants());
        registerNewRule(new RemoveFlags());
        registerNewRule(new RemoveEnchants());
        registerNewRule(new ReplaceItem());
        registerNewRule(new ReplaceLore());
        registerNewRule(new SetCustomModelData());
        registerNewRule(new SetLore());
        registerNewRule(new SetName());
        registerNewRule(new SetType());
    }

    public void registerNewRule(AbstractChangesRule rule) {
        rules.add(rule);
    }

    public ItemStack setFakeChange(ConfigurationSection section, ItemStack item, Player player) {
        for (AbstractChangesRule rule : rules) {
            item = rule.setChange(section, item, player);
        }
        return item;
    }

    public ItemStack setRealChange(ConfigurationSection section, ItemStack item, Player player) {
        boolean needReturnNewItem = false;
        for (AbstractChangesRule rule : rules) {
            if (rule.getNeedRewriteItem()) {
                item = rule.setChange(section, item, player);
                needReturnNewItem = true;
            } else {
                rule.setChange(section, item, player);
            }
        }
        if (needReturnNewItem) {
            return item;
        }
        return null;
    }

    public boolean getItemCooldown(Player player, int slot) {
        Collection<Integer> result = itemCooldown.get(player);
        if (result == null) {
            return false;
        }
        return result.contains(slot);
    }

    public void addCooldown(Player player, int slot) {
        if (itemCooldown.get(player) != null) {
            itemCooldown.get(player).add(slot);
        }
        Collection<Integer> tempVal1 = new HashSet<>();
        tempVal1.add(slot);
        itemCooldown.put(player, tempVal1);
    }

    public void removeCooldown(Player player, int slot) {
        if (itemCooldown.get(player) != null) {
            itemCooldown.get(player).remove(slot);
        }
    }

}
