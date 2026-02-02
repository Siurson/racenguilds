package me.hectogram.racenguilds.race;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EmblemFactory {

    private final NamespacedKey emblemKey;
    private final NamespacedKey raceKey;

    public EmblemFactory(JavaPlugin plugin) {
        this.emblemKey = new NamespacedKey(plugin, "race_emblem");
        this.raceKey = new NamespacedKey(plugin, "race_type");
    }

    public ItemStack createEmblem(RaceType race) {
        Material material = getEmblemMaterial(race);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§6Emblemat " + race.getDisplayName() + "§6a");
        meta.setLore(List.of("§7Przyda się u kowala"));

        meta.getPersistentDataContainer().set(emblemKey, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(raceKey, PersistentDataType.STRING, race.name());

        item.setItemMeta(meta);
        return item;
    }

    private Material getEmblemMaterial(RaceType race) {
        return switch (race) {
            case ELF -> Material.BORDURE_INDENTED_BANNER_PATTERN;
            case GOBLIN -> Material.CREEPER_BANNER_PATTERN;
            case DWARF -> Material.FIELD_MASONED_BANNER_PATTERN;
            case ORC -> Material.GUSTER_BANNER_PATTERN;
            case GIANT -> Material.SKULL_BANNER_PATTERN;
            case TRADER -> Material.GLOBE_BANNER_PATTERN;
            case HUMAN -> Material.FLOWER_BANNER_PATTERN;
        };
    }
}
