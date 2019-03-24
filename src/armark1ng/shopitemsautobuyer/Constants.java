package armark1ng.shopitemsautobuyer;

public class Constants {

	public static int AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
			BODY_RUNE = 559, CHAOS_RUNE = 562, NATURE_RUNE = 561, DEATH_RUNE = 560, LAW_RUNE = 563, BLOOD_RUNE = 565,
			SOUL_RUNE = 566, FIRE_RUNE_PACK = 12734, WATER_RUNE_PACK = 12730, AIR_RUNE_PACK = 12728,
			EARTH_RUNE_PACK = 12732, MIND_RUNE_PACK = 12736, CHAOS_RUNE_PACK = 12738, BATTLE_STAFF = 1391,
			STAFF_OF_FIRE = 1387, STAFF_OF_WATER = 1383, STAFF_OF_AIR = 1381, STAFF_OF_EARTH = 1385;

	public static int TYPE_STACKABLE = 0, TYPE_PACK = 1, TYPE_UNSTACKABLE = 2;
	public static String[] WORLDS = new String[] { "World 1 - Free to play - Trade - Free",
			"World 2 - Members - Trade - Mem...", "World 3 - Members - -", "World 4 - Members - Trouble Brew...",
			"World 5 - Members - Falador Part...", "World 6 - Members - Barbarian A...",
			"World 8 - Free to play - Wilderness ...", "World 9 - Members - Wintertodt",
			"World 10 - Members - Kourend Gro...", "World 11 - Members - -", "World 12 - Members - -",
			"World 13 - Members - -", "World 14 - Members - Dorgesh-Kaa...", "World 16 - Free to play - Wilderness ...",
			"World 17 - Members - LMS Competi...", "World 18 - Members - Bounty World", "World 19 - Members - -",
			"World 20 - Members - -", "World 21 - Members - -", "World 22 - Members - Duel Arena",
			"World 26 - Free to play - LMS Casual", "World 27 - Members - Ourania Altar", "World 28 - Members - -",
			"World 29 - Members - Clan Wars - ...", "World 30 - Members - House Party...",
			"World 33 - Members - Games Room,...", "World 34 - Members - Castle Wars 1", "World 35 - Free to play - -",
			"World 36 - Members - Running - na...", "World 38 - Members - -", "World 41 - Members - Running - la...",
			"World 42 - Members - Role-playing", "World 43 - Members - -", "World 44 - Members - Pest Control",
			"World 46 - Members - Agility training", "World 49 - Members - 2000 skill to...",
			"World 50 - Members - TzHaar Fight ...", "World 51 - Members - -", "World 52 - Members - -",
			"World 53 - Members - 1250 skill total", "World 54 - Members - Castle Wars 2", "World 57 - Members - -",
			"World 58 - Members - Blast Furnace", "World 59 - Members - -", "World 60 - Members - -",
			"World 61 - Members - 2000 skill to...", "World 62 - Members - Pyramid Plun...",
			"World 66 - Members - 1500 skill total", "World 67 - Members - Group Questi...", "World 68 - Members - -",
			"World 69 - Members - Wilderness ...", "World 70 - Members - Fishing Trawl...",
			"World 73 - Members - 1750 skill total", "World 74 - Members - -", "World 75 - Members - Barbarian Fi...",
			"World 76 - Members - -", "World 77 - Members - Mort'ton tem...", "World 78 - Members - -",
			"World 81 - Free to play - 500 skill total", "World 82 - Free to play - -", "World 83 - Free to play - -",
			"World 84 - Free to play - -", "World 85 - Free to play - 750 skill total",
			"World 86 - Members - Blast Furnace", "World 93 - Free to play - Clan Wars - ...",
			"World 94 - Free to play - -" };

	public static boolean hasBanking(int npcId) {
		switch (npcId) {
		case 3247:// magic store npc
			return true;
		case 3837:// lunar
			return true;
		}
		return false;
	}

	public static boolean isMembers(int npcId) {
		switch (npcId) {
		case 3247:// magic store npc
			return true;
		case 3837:// lunar
			return true;
		}
		return false;
	}

}
