package Tyskie.scripts.TRuneCrafting.resources;

import org.powerbot.script.Tile;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class MyConstants {
    /**
     * Skill ID's
     */
    public static final int SKILL_RUNECRAFTING = 7;

    /**
     * Essence ID's
     */
    public static final int PURE_ESSENCE = 7936;
    public static final int RUNE_ESSENCE = 1436;

    /**
     * Animation ID's
     */
    public static final int ANIMATION_IDLE = -1;

    /**
     * Rune ID's
     */
    public static final int AIR_RUNE = 556;
    public static final int MIND_RUNE = 558;
    public static final int WATER_RUNE = 555;
    public static final int EARTH_RUNE = 557;
    public static final int FIRE_RUNE = 554;
    public static final int BODY_RUNE = 559;

    /**
     * Inventory Related
     */
    public static final int INVENTORY_FULL = 28;
    public static final int INVENTORY_EMPTY = 0;
    public static final int INVENTORY_ONLY_RUNES = 1;

    /**
     * Altar related ID's
     */
    public static final int AIR_MYSTERIOUS_RUINS_ID = 14399;
    public static final int AIR_PORTAL_ID = 14841;
    public static final int AIR_ALTAR_ID = 14897;

    public static final int MIND_MYSTERIOUS_RUINS_ID = 14401;
    public static final int MIND_PORTAL_ID = 14842;
    public static final int MIND_ALTAR_ID = 14898;

    public static final int WATER_MYSTERIOUS_RUINS_ID = 14403;
    public static final int WATER_PORTAL_ID = 14843;
    public static final int WATER_ALTAR_ID = 14899;

    public static final int EARTH_MYSTERIOUS_RUINS_ID = 14405;
    public static final int EARTH_PORTAL_ID = 14844;
    public static final int EARTH_ALTAR_ID = 14900;

    public static final int FIRE_MYSTERIOUS_RUINS_ID = 14407;
    public static final int FIRE_PORTAL_ID = 14845;
    public static final int FIRE_ALTAR_ID = 14901;

    public static final int BODY_MYSTERIOUS_RUINS_ID = 14409;
    public static final int BODY_PORTAL_ID = 14846;
    public static final int BODY_ALTAR_ID = 14902;

    /**
     * Paths
     */
    public static final Tile[] FALLY_BANK_AIR_ALTAR = {new Tile(3012, 3355, 0), new Tile(3012, 3359, 0), new Tile(3008, 3359, 0), new Tile(3008, 3355, 0), new Tile(3008, 3351, 0), new Tile(3008, 3347, 0), new Tile(3008, 3343, 0), new Tile(3008, 3339, 0), new Tile(3008, 3335, 0), new Tile(3008, 3329, 0), new Tile(3006, 3325, 0), new Tile(3006, 3321, 0), new Tile(3006, 3317, 0), new Tile(3006, 3313, 0), new Tile(3006, 3309, 0), new Tile(3003, 3305, 0), new Tile(2999, 3304, 0), new Tile(2995, 3303, 0), new Tile(2993, 3299, 0), new Tile(2991, 3295, 0), new Tile(2987, 3294, 0)};
    public static final Tile[] AIR_PORTAL_TO_ALTAR = {new Tile(2841, 4830, 0), new Tile(2842, 4830, 0), new Tile(2842, 4831, 0), new Tile(2842, 4832, 0), new Tile(2842, 4833, 0)};

    public static final Tile[] FALLY_BANK_MIND_ALTAR = {new Tile(2946, 3368, 0), new Tile(2946, 3372, 0), new Tile(2949, 3375, 0), new Tile(2953, 3378, 0), new Tile(2957, 3379, 0), new Tile(2961, 3381, 0), new Tile(2962, 3385, 0), new Tile(2962, 3389, 0), new Tile(2965, 3392, 0), new Tile(2966, 3396, 0), new Tile(2966, 3400, 0), new Tile(2966, 3404, 0), new Tile(2966, 3408, 0), new Tile(2970, 3409, 0), new Tile(2974, 3411, 0), new Tile(2979, 3415, 0), new Tile(2982, 3418, 0), new Tile(2986, 3421, 0), new Tile(2987, 3425, 0), new Tile(2987, 3429, 0), new Tile(2987, 3433, 0), new Tile(2984, 3437, 0), new Tile(2983, 3441, 0), new Tile(2983, 3445, 0), new Tile(2981, 3449, 0), new Tile(2981, 3453, 0), new Tile(2979, 3457, 0), new Tile(2976, 3460, 0), new Tile(2975, 3464, 0), new Tile(2975, 3468, 0), new Tile(2975, 3472, 0), new Tile(2973, 3476, 0), new Tile(2973, 3480, 0), new Tile(2975, 3484, 0), new Tile(2973, 3488, 0), new Tile(2976, 3492, 0), new Tile(2977, 3496, 0), new Tile(2978, 3500, 0), new Tile(2978, 3504, 0), new Tile(2979, 3508, 0), new Tile(2980, 3512, 0)};
    public static final Tile[] MIND_PORTAL_TO_ALTAR = {new Tile(2793, 4829, 0), new Tile(2793, 4831, 0), new Tile(2791, 4833, 0), new Tile(2790, 4835, 0), new Tile(2788, 4837, 0), new Tile(2787, 4839, 0)};

    public static final Tile[] LUMBRIDGE_BANK_WATER_ALTAR = {new Tile(3208, 3220, 2), new Tile(3206, 3216, 2), new Tile(3206, 3212, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3210, 3209, 0), new Tile(3214, 3210, 0), new Tile(3215, 3214, 0), new Tile(3215, 3218, 0), new Tile(3219, 3218, 0), new Tile(3223, 3218, 0), new Tile(3227, 3218, 0), new Tile(3231, 3218, 0), new Tile(3232, 3214, 0), new Tile(3233, 3210, 0), new Tile(3234, 3206, 0), new Tile(3237, 3202, 0), new Tile(3239, 3198, 0), new Tile(3241, 3194, 0), new Tile(3244, 3191, 0), new Tile(3244, 3187, 0), new Tile(3243, 3183, 0), new Tile(3240, 3180, 0), new Tile(3238, 3176, 0), new Tile(3238, 3172, 0), new Tile(3235, 3169, 0), new Tile(3231, 3167, 0), new Tile(3228, 3164, 0), new Tile(3224, 3164, 0), new Tile(3220, 3164, 0), new Tile(3216, 3164, 0), new Tile(3212, 3164, 0), new Tile(3208, 3164, 0), new Tile(3204, 3164, 0), new Tile(3200, 3164, 0), new Tile(3196, 3164, 0), new Tile(3192, 3166, 0), new Tile(3188, 3167, 0)};
    public static final Tile[] WATER_PORTAL_TO_ALTAR = {new Tile(2725, 4832, 0), new Tile(2723, 4834, 0), new Tile(2721, 4836, 0), new Tile(2719, 4836, 0)};

    public static final Tile[] VARROCK_BANK_EARTH_ALTAR = {new Tile(3253, 3420, 0), new Tile(3253, 3423, 0), new Tile(3254, 3426, 0), new Tile(3257, 3428, 0), new Tile(3260, 3428, 0), new Tile(3263, 3428, 0), new Tile(3266, 3428, 0), new Tile(3269, 3428, 0), new Tile(3272, 3428, 0), new Tile(3275, 3428, 0), new Tile(3278, 3428, 0), new Tile(3281, 3428, 0), new Tile(3284, 3431, 0), new Tile(3284, 3434, 0), new Tile(3284, 3437, 0), new Tile(3284, 3440, 0), new Tile(3284, 3443, 0), new Tile(3284, 3446, 0), new Tile(3284, 3449, 0), new Tile(3284, 3452, 0), new Tile(3284, 3455, 0), new Tile(3287, 3459, 0), new Tile(3289, 3462, 0), new Tile(3292, 3465, 0), new Tile(3295, 3465, 0), new Tile(3298, 3467, 0), new Tile(3301, 3469, 0), new Tile(3303, 3472, 0), new Tile(3306, 3472, 0)};
    public static final Tile[] EARTH_PORTAL_TO_ALTAR = {new Tile(2657, 4830, 0), new Tile(2657, 4831, 0), new Tile(2657, 4832, 0), new Tile(2657, 4833, 0), new Tile(2657, 4834, 0), new Tile(2657, 4835, 0), new Tile(2658, 4836, 0), new Tile(2658, 4837, 0), new Tile(2658, 4838, 0), new Tile(2658, 4839, 0)};

    public static final Tile[] ALKHARID_BANK_FIRE_ALTAR = {new Tile(3269, 3166, 0), new Tile(3272, 3166, 0), new Tile(3273, 3169, 0), new Tile(3275, 3172, 0), new Tile(3275, 3175, 0), new Tile(3278, 3177, 0), new Tile(3281, 3179, 0), new Tile(3284, 3179, 0), new Tile(3287, 3179, 0), new Tile(3288, 3182, 0), new Tile(3290, 3185, 0), new Tile(3291, 3188, 0), new Tile(3293, 3191, 0), new Tile(3294, 3194, 0), new Tile(3296, 3197, 0), new Tile(3298, 3200, 0), new Tile(3297, 3203, 0), new Tile(3297, 3206, 0), new Tile(3298, 3209, 0), new Tile(3298, 3212, 0), new Tile(3298, 3215, 0), new Tile(3298, 3218, 0), new Tile(3298, 3221, 0), new Tile(3297, 3224, 0), new Tile(3297, 3227, 0), new Tile(3297, 3230, 0), new Tile(3297, 3233, 0), new Tile(3300, 3236, 0), new Tile(3302, 3239, 0), new Tile(3304, 3242, 0), new Tile(3305, 3245, 0), new Tile(3308, 3248, 0), new Tile(3310, 3251, 0)};
    public static final Tile[] FIRE_PORTAL_TO_ALTAR = {new Tile(2576, 4848, 0), new Tile(2576, 4846, 0), new Tile(2577, 4844, 0), new Tile(2579, 4842, 0), new Tile(2581, 4840, 0), new Tile(2583, 4839, 0)};

    public static final Tile[] EDGEVILLE_BANK_BODY_ALTAR = {new Tile(3094, 3491, 0), new Tile(3091, 3491, 0), new Tile(3088, 3490, 0), new Tile(3085, 3487, 0), new Tile(3082, 3484, 0), new Tile(3081, 3481, 0), new Tile(3081, 3478, 0), new Tile(3080, 3475, 0), new Tile(3080, 3472, 0), new Tile(3080, 3469, 0), new Tile(3083, 3467, 0), new Tile(3086, 3464, 0), new Tile(3084, 3461, 0), new Tile(3083, 3458, 0), new Tile(3084, 3455, 0), new Tile(3083, 3452, 0), new Tile(3080, 3450, 0), new Tile(3077, 3448, 0), new Tile(3074, 3447, 0), new Tile(3073, 3444, 0), new Tile(3073, 3441, 0), new Tile(3070, 3441, 0), new Tile(3067, 3441, 0), new Tile(3064, 3441, 0), new Tile(3061, 3438, 0), new Tile(3058, 3438, 0), new Tile(3055, 3441, 0)};
    public static final Tile[] BODY_PATH_ALTAR = {new Tile(2522, 4847, 0), new Tile(2521, 4845, 0), new Tile(2521, 4843, 0), new Tile(2521, 4841, 0), new Tile(2521, 4839, 0)};
    public static final Tile[] BODY_PATH_PORTAL = {new Tile(2521, 4839, 0), new Tile(2521, 4837, 0), new Tile(2521, 4835, 0)};

}
