package me.hectogram.racenguilds.items.cooldown;

import me.hectogram.racenguilds.items.CustomItemType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemCooldownManager {

    private final Map<UUID, Map<CustomItemType, Long>> cooldowns = new HashMap<>();

    public void setCooldown(Player player, CustomItemType type, int seconds) {
        cooldowns
                .computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                .put(type, System.currentTimeMillis() + seconds * 1000L);
    }

    public boolean hasCooldown(Player player, CustomItemType type) {
        return getRemaining(player, type) > 0;
    }

    public long getRemaining(Player player, CustomItemType type) {
        Map<CustomItemType, Long> map = cooldowns.get(player.getUniqueId());
        if (map == null || !map.containsKey(type)) return 0;

        long left = (map.get(type) - System.currentTimeMillis()) / 1000;
        return Math.max(left, 0);
    }
}
