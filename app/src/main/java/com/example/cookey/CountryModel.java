package com.example.cookey;

public class CountryModel {
    private final long   id;
    private final String name;
    private final String code;
    private final String flagAssetPath;    // e.g. "flags/us.png"  – null ⇒ placeholder

    public CountryModel(long id, String name, String code, String flagAssetPath) {
        this.id            = id;
        this.name          = name;
        this.code          = code;
        this.flagAssetPath = flagAssetPath;
    }

    public long   getId()           { return id;            }
    public String getName()         { return name;          }
    public String getCode()         { return code;          }
    public String getFlagAssetPath(){ return flagAssetPath; }
}

