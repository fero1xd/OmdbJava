package me.fero.attributes;

public enum Type {
    MOVIE("movie"),
    SERIES("series"),
    EPISODE("episode"),
    SEASON("season");

    private final String name;
    private Type(String name) {
        this.name = name;
    }

    public static Type findType(String text) {
        for(Type type : Type.values()) {
            if(type.name.equals(text)){
                return type;
            }
        }

        return null;
    }
}
