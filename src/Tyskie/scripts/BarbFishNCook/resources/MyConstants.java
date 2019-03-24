package scripts.BarbFishNCook.resources;

import org.powerbot.script.Tile;

/**
 * Created by Thijs on 12-6-2017.
 */
public class MyConstants {
    /**
     * Fishing spot ID's
     */
    public static final int[] FISHING_SPOT_IDS = {1526};

    /**
     * Fish ID's
     */
    public static final int RAW_SALMON_ID = 331;
    public static final int COOKED_SALMON_ID = 329;
    public static final int RAW_TROUT_ID = 335;
    public static final int COOKED_TROUT_ID = 333;

    public static final int[] RAW_FISH_IDS = {RAW_SALMON_ID, RAW_TROUT_ID};
    public static final int[] COOKED_FISH_IDS = {COOKED_SALMON_ID, COOKED_TROUT_ID};

    /**
     * Fire ID's
     */
    public static final int FIRE_ID = 26185;

    /**
     * Fishing Supplies ID's
     */
    public static final int[] FISHING_SUPPLIES_IDS = {309, 314};

    /**
     * Bounds
     */
    public static final int[] FIRE_BOUNDS = {-40, 56, -64, 0, -40, 56};
    public static final int[] FISHING_BOUNDS = {-48, 48, -100, 0, -48, 48};

    /**
     * Animation ID's
     */
    public static final int ANIMATION_IDLE = -1;
    public static final int ANIMATION_COOKING = 897;
    public static final int ANIMATION_FISHING = 623;

    /**
     * Inventory Related
     */
    public static final int INVENTORY_FULL = 28;

    /**
     * Widget skills ID's
     */
    public static final int SKILL_FISHING = 19;
    public static final int SKILL_COOKING = 20;

    /**
     * Paths
     */
    //fishing spot to bank
    public static final Tile[] FISHING_TO_BANK = {new Tile(3109, 3433, 0), new Tile(3106, 3433, 0), new Tile(3103, 3433, 0), new Tile(3100, 3434, 0), new Tile(3097, 3436, 0), new Tile(3094, 3439, 0), new Tile(3094, 3442, 0), new Tile(3091, 3445, 0), new Tile(3091, 3448, 0), new Tile(3093, 3451, 0), new Tile(3093, 3454, 0), new Tile(3092, 3457, 0), new Tile(3089, 3460, 0), new Tile(3088, 3463, 0), new Tile(3091, 3465, 0), new Tile(3094, 3465, 0), new Tile(3097, 3465, 0), new Tile(3098, 3468, 0), new Tile(3098, 3471, 0), new Tile(3098, 3474, 0), new Tile(3098, 3477, 0), new Tile(3098, 3480, 0), new Tile(3098, 3483, 0), new Tile(3095, 3483, 0), new Tile(3092, 3485, 0), new Tile(3090, 3488, 0), new Tile(3093, 3490, 0)};

    //fishing spot to fire
    public static final Tile[] FISHING_TO_FIRE = {new Tile(3103, 3425, 0), new Tile(3101, 3427, 0), new Tile(3101, 3429, 0), new Tile(3103, 3430, 0), new Tile(3105, 3432, 0)};

}
