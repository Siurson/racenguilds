package me.hectogram.racenguilds.race;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RaceEffectsManager {

    private final JavaPlugin plugin;
    private final Set<UUID> raceSetup = new HashSet<>();
    private final NamespacedKey giantArmorKey;
    private final NamespacedKey orcHelmetKey;

    public RaceEffectsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.giantArmorKey = new NamespacedKey(plugin, "giant_armor");
        this.orcHelmetKey = new NamespacedKey(plugin, "orc_helmet");
    }


    public void beginRaceSetup(Player player) {
        raceSetup.add(player.getUniqueId());
    }

    public void endRaceSetup(Player player) {
        raceSetup.remove(player.getUniqueId());
    }

    public boolean isInRaceSetup(Player player) {
        return raceSetup.contains(player.getUniqueId());
    }


    private boolean isLeatherArmor(ItemStack item) {
        Material type = item.getType();
        return type == Material.LEATHER_HELMET
                || type == Material.LEATHER_CHESTPLATE
                || type == Material.LEATHER_LEGGINGS
                || type == Material.LEATHER_BOOTS;
    }

    public void validateArmor(Player player, RaceType race) {
        if (isInRaceSetup(player)) return;

        boolean sendMessage = false;

        switch (race) {
            case GIANT -> {
                ItemStack[] armor = player.getInventory().getArmorContents();
                boolean destroyed = false;

                for (ItemStack item : armor) {
                    if (item == null) continue;


                    if (!isLeatherArmor(item)) destroyed = true;
                }

                if (destroyed) {
                    player.getInventory().setArmorContents(new ItemStack[4]);
                    player.updateInventory();
                    sendMessage = true;
                }

                if (sendMessage) {
                    player.sendMessage("§cPróbowałeś zmieścić na siebie zbroję dla człowieka.");
                    player.sendMessage("§cNiestety twoja wielka sylwetka zniszczyła ją na kawałki.");
                }
            }

            case ORC -> {
                ItemStack helmet = player.getInventory().getHelmet();

                if (helmet == null || helmet.getType() == Material.AIR) return;




                player.getInventory().setHelmet(null);
                player.updateInventory();

                player.sendMessage("§6Twoja orcza fizjonomia nie pozwala nosić ludzkich hełmów.");
                player.sendMessage("§6Hełm pęka i rozpada się na kawałki.");
            }

        }
    }


    public boolean isGiantArmor(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(giantArmorKey, PersistentDataType.BYTE);
    }


    public void applyPerks(Player player, RaceType race) {

        resetPlayer(player);

        switch (race) {
            case HUMAN:
                player.setWalkSpeed(0.2f);
                player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.1);
                player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(1.0);
                player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4.0);
                player.setMaxHealth(20);
                player.getAttribute(Attribute.SCALE).setBaseValue(1.0);
                break; // brak bonusów
            case DWARF:
                player.setWalkSpeed(0.18f); // -10%
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, 1, false, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false, false));
                player.getAttribute(Attribute.SCALE).setBaseValue(
                        player.getAttribute(Attribute.SCALE).getBaseValue() * 0.9
                );
                break;
            case ORC:

                player.setWalkSpeed(0.19f); // -5%
                player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(4.0);
                player.getAttribute(Attribute.SCALE).setBaseValue(
                        player.getAttribute(Attribute.SCALE).getBaseValue() * 1.1
                );
                break;
            case GOBLIN:
                player.setWalkSpeed(0.26f); // +30%
                player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(
                        player.getAttribute(Attribute.ATTACK_SPEED).getBaseValue() * 1.5
                );
                player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(
                        player.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() * 0.5
                );
                player.setMaxHealth(14);
                player.getAttribute(Attribute.SCALE).setBaseValue(
                        player.getAttribute(Attribute.SCALE).getBaseValue() * 0.8
                );
                break;
            case ELF:
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 0, false, false, false));
                player.setWalkSpeed(0.22f); // +10%
                break;
            case GIANT:
                player.setWalkSpeed(0.15f);
                player.setMaxHealth(80);
                player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_SPEED)
                        .setBaseValue(3.0);
                player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(2.0);
                player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED)
                        .setBaseValue(0.07);

                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false, false));

                try {
                    player.getAttribute(Attribute.SCALE).setBaseValue(
                            player.getAttribute(Attribute.SCALE).getBaseValue() * 1.5
                    );
                } catch (NoSuchMethodError e) {
                    Bukkit.getLogger().warning("setScale() unavailable! Update to Paper 1.20+ to scale giants.");
                }
                break;
            case TRADER:
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 4, false, false, false));
                player.setMaxHealth(10);
                player.setWalkSpeed(0.25f); // speed II

                break;
        }
    }

    public void resetPlayer(Player player) {

        player.setWalkSpeed(0.2f);
        player.setMaxHealth(20);
        if (player.getHealth() > 20) player.setHealth(20);


        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));


        player.getAttribute(Attribute.SCALE)
                .setBaseValue(1.0);

        player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE)
                .setBaseValue(1.0);
        player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_SPEED)
                .setBaseValue(4.0);
        player.getAttribute(org.bukkit.attribute.Attribute.MOVEMENT_SPEED)
                .setBaseValue(0.1);
    }

}
