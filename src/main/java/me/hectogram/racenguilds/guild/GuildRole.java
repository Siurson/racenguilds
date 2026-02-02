package me.hectogram.racenguilds.guild;

public enum GuildRole {
    KING("§6Król"),
    DIPLOMAT("§bDyplomata");

    private final String display;

    GuildRole(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public static GuildRole fromString(String s) {
        try {
            return GuildRole.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
