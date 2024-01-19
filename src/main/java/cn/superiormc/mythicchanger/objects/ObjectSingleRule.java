package cn.superiormc.mythicchanger.objects;

import cn.superiormc.mythicchanger.manager.ChangesManager;
import cn.superiormc.mythicchanger.manager.MatchItemManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ObjectSingleRule implements Comparable<ObjectSingleRule> {

    private final String id;

    private final YamlConfiguration config;


    public ObjectSingleRule(String id, YamlConfiguration config) {
        this.id = id;
        this.config = config;
    }

    public boolean getMatchItem(ItemStack item) {
        ConfigurationSection section = config.getConfigurationSection("match-item");
        if (section == null) {
            return true;
        }
        return MatchItemManager.matchItemManager.getMatch(section, item);
    }

    public ItemStack setFakeChange(ItemStack item, Player player) {
        if (item == null || item.getType().isAir()) {
            return item;
        }
        ConfigurationSection section = config.getConfigurationSection("fake-changes");
        if (section == null) {
            return item;
        }
        return ChangesManager.changesManager.setFakeChange(section, item, player);
    }

    public ItemStack setRealChange(ItemStack item, Player player) {
        if (item == null || item.getType().isAir()) {
            return item;
        }
        ConfigurationSection section = config.getConfigurationSection("real-changes");
        if (section == null) {
            return item;
        }
        return ChangesManager.changesManager.setRealChange(section, item, player);
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return config.getInt("weight", 0);
    }


    @Override
    public int compareTo(@NotNull ObjectSingleRule otherPrefix) {
        if (getWeight() == otherPrefix.getWeight()) {
            int len1 = getId().length();
            int len2 = otherPrefix.getId().length();
            int minLength = Math.min(len1, len2);

            for (int i = 0; i < minLength; i++) {
                char c1 = getId().charAt(i);
                char c2 = otherPrefix.getId().charAt(i);

                if (c1 != c2) {
                    if (Character.isDigit(c1) && Character.isDigit(c2)) {
                        // 如果字符都是数字，则按照数字大小进行比较
                        return Integer.compare(Integer.parseInt(getId().substring(i)), Integer.parseInt(otherPrefix.getId().substring(i)));
                    } else {
                        // 否则，按照字符的unicode值进行比较
                        return c1 - c2;
                    }
                }
            }

            return len1 - len2;
        }
        return getWeight() - otherPrefix.getWeight();
    }

    @Override
    public String toString() {
        return getId();
    }
}
