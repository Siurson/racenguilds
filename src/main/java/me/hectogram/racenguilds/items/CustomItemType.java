package me.hectogram.racenguilds.items;

public enum CustomItemType {

    SLOW_SCEPTER("slow_scepter"),
    VACCINE("szczepionka"),
    KING_SCEPTER("king_scepter"),
    TELEPORT_SCEPTER("teleport_scepter"),
    CHAOS_SCEPTER("chaos_scepter"),
    WIND_SCEPTER("wind_scepter"),
    SIGHT_SCEPTER("sight_scepter"),
    FIRE_SCEPTER("fire_scepter");

    private final String id;

    CustomItemType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static CustomItemType fromId(String id) {
        for (CustomItemType type : values()) {
            if (type.id.equalsIgnoreCase(id)) return type;
        }
        return null;
    }
}
