package uk.co.haxyshideout.chococraft2.entities;

import java.util.Random;

/**
 * Created by clienthax on 5/5/2015.
 */
public class DefaultNames {

	private static String maleNames[] =
			{
					"Boco", "Choco", "Patch", "Eddie", "Big Bird", "Chobi", "Horse Bird", "Mr. Yellowpuffs", "Oscar", "Wild",
					"Stitch", "Milo", "Lewis", "Simon", "Steed", "Bocobo", "Chobo", "Butter Fingers", "Caspar", "Chubby",
					"Coco", "Fuzzy", "Hulk", "Flopsy", "Lionel", "Tidus", "Cloud", "Sephiroth", "Butz", "Cecil", "Golbez",
					"Squall", "Zidane", "Garnet", "Kuja", "Locke", "Celes", "Crafty", "Sparky", "Skippy","clienthax","arkio",
					"Whiskers", "Mog's Mount", "Ruffles", "Quistis", "Noctis", "Firecracker", "Ballistic", "Blizzard",
					"Torobo", "Leon", "Firas", "Travis", "Indigo", "Montoya", "Cobalt", "Jinx", "Komet", "Beau", "Bone",
					"Claw", "Duke", "Easy", "Fire", "Fury", "Idol", "Iron", "Jack", "Mars", "Noir", "Snow", "Star", "Zero",
					"Ace", "Air", "Ice", "Max", "Neo", "Ray", "Alpha", "Arrow", "Avian", "Black", "Blade", "Blaze", "Blitz",
					"Chaos", "Dandy", "Jolly", "Omega", "Pluto", "Point", "Quake", "Titan", "Hope", "Ifrit", "Shiva", "Polonium",
					"Radon", "Sparks", "Lunik"

			};

	private static String femaleNames[] =
			{
					"Choco", "Patch", "Chobi", "Wild", "Chubby", "Crystal", "Coco", "Fuzzy", "Flopsy", "Lulu", "Yuna",
					"Cecil", "Kuja", "Terra", "Locke", "Celes", "Rikku", "Yuffie", "Selphie", "Rinoa", "Sparky",
					"Skippy", "Whiskers", "Pupu", "Quistis", "Noctis", "Tranquille", "Twinkling", "Capucine", "Heidi",
					"Danseuse", "Mercedes", "Psyche", "Victory", "Liberty", "Emma", "Fortune", "Soleil", "Luna", "Violet",
					"Lilith", "Lilli", "Jinx", "Coco", "Fleur", "Feder", "Flora", "Kugel", "Bleu", "Blue", "Chic", "Ciel",
					"Face", "Fire", "Fury", "Iris", "Jade", "Joli", "Kiku", "Lady", "Miel", "Momo", "Moon", "Nana",
					"Noir", "Nova", "Rain", "Rose", "Ruby", "Star", "Vega", "Air", "Aki", "Ayu", "Fee", "Sky", "Sun",
					"Amber", "Angel", "Azure", "Belle", "Clair", "Ebony", "Ember", "Fairy", "Flare", "Glory", "Jaune",
					"Jeune", "Jolly", "Lucky", "Olive", "Orange", "Venus", "Lightning", "Galindorf"
			};

	public static String getRandomName(boolean isMale) {
		if(isMale)
			return maleNames[new Random().nextInt(maleNames.length)];
		else
			return femaleNames[new Random().nextInt(femaleNames.length)];
	}

}
