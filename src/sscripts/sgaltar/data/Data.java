package sscripts.sgaltar.data;

public enum Data {

    NORMAL_BONES("Normal Bones", 526, 527),
    BIG_BONES("Big bones", 532, 533),
    DRAGON_BONES("Dragon bones", 536, 537),
    BABY_DRAGON_BONES("Baby Dragon bones", 534, 535),
    WYVERN_BONES("Wyvern bones", 6812, 6816),
    LAVA_DRAGON_BONES("Lava Dragon Bones", 11943, 11944),
    DAGANNOTH_BONES("Dagannoth bones", 6729, 6730),
    SUPERIOR_DRAGON_BONES("Superior Dragon bones", 22124, 22125);


    private final String bone_name;
    private final int bone_ID;
    private final int bone_NID;

    Data(final String bone_name,final int bone_ID,final int bone_NID){
        this.bone_name = bone_name;
        this.bone_ID = bone_ID;
        this.bone_NID = bone_NID;
    }

    public String getBone_name(){
        return bone_name;
    }

    public int getBone_ID() {
        return bone_ID;
    }

    public int getBone_NID() {
        return bone_NID;
    }
    @Override
    public String toString() {
        String name = name().toLowerCase().replace("_", " ");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

}
