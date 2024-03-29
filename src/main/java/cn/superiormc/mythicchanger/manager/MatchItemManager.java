package cn.superiormc.mythicchanger.manager;

import cn.superiormc.mythicchanger.objects.matchitem.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;

public class MatchItemManager {

    public static MatchItemManager matchItemManager;

    private Collection<AbstractMatchItemRule> rules = new HashSet<>();

    public MatchItemManager() {
        matchItemManager = this;
        registerNewRule(new ContainsLore());
        registerNewRule(new ContainsName());
        registerNewRule(new HasEnchants());
        registerNewRule(new HasLore());
        registerNewRule(new HasName());
        registerNewRule(new Items());
    }

    public void registerNewRule(AbstractMatchItemRule rule) {
        rules.add(rule);
    }

    public boolean getMatch(ConfigurationSection section, ItemStack item) {
        for (AbstractMatchItemRule rule : rules) {
            if (!rule.getMatch(section, item)) {
                return false;
            }
        }
        return true;
    }

}
