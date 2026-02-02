package me.hectogram.racenguilds.race;


public enum RaceType {
    HUMAN("Człowiek"),
    DWARF("Krasnolud"),
    ORC("Ork"),
    GOBLIN("Goblin"),
    ELF("Elf"),
    GIANT("Gigant"),
    TRADER("Podróżny Kupiec");


    private final String displayName;


    RaceType(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }
}