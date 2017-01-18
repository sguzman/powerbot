package org.powerbot.script.rt4;

import org.powerbot.script.rt4.Game.Tab;

/**
 * Magic interface
 */
public class Magic extends ClientAccessor {
	public Magic(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Retrieves the current book.
	 *
	 * @return The current book.
	 */
	public Book book() {
		final int varp = ctx.varpbits.varpbit(Constants.SPELLBOOK_VARPBIT) & 0x3;
		for (final Book b : Book.values()) {
			if (varp == b.varp) {
				return b;
			}
		}
		return Book.NIL;
	}

	@Deprecated
	public Spell spell() {
		final Book book = book();
		for (final Spell spell : Spell.values()) {
			if (spell.book() != book) {
				continue;
			}
			if (ctx.widgets.component(spell.book().widget, spell.component()).borderThickness() == 2) {
				return spell;
			}
		}
		return Spell.NIL;
	}

	/**
	 * Manually selects the specified spell to be cast. If the current tab
	 * is not the Magic tab, it will attempt to open the Magic tab.
	 *
	 * @param spell The spell to cast.
	 * @return {@code true} if the spell component was successfully clicked,
	 * {@code false} otherwise.
	 */
	public boolean cast(final MagicSpell spell) {
		if (!ctx.game.tab(Game.Tab.MAGIC)) {
			return false;
		}
		final Component c = component(spell);
		return c.visible() && c.click("Cast");
	}

	/**
	 * Determines whether or not the specified spell is ready to be casted.
	 *
	 * @param spell The spell to validate.
	 * @return {@code true} if it is ready to be cast, {@code false} otherwise.
	 */
	public boolean ready(final MagicSpell spell) {
		return ctx.game.tab() == Tab.MAGIC && component(spell).textureId() != spell.texture();
	}

	/**
	 * Grabs the component for the given MagicSpell.
	 *
	 * @param spell The spell to find the component for.
	 * @return The resulting component
	 */
	public Component component(final MagicSpell spell) {
		final Widget w = ctx.widgets.widget(Book.widget());
		for (final Component c : w.components()) {
			final int texture = c.textureId();
			if (texture == spell.texture() || texture == spell.texture() - 50) {
				return c;
			}
		}
		return w.component(-1);
	}

	public interface MagicSpell {
		/**
		 * Gets the level required to cast this spell.
		 *
		 * @return The level required.
		 */
		int level();

		/**
		 * The texture id of the component when the spell cannot be casted.
		 *
		 * @return Texture ID.
		 */
		int texture();

		/**
		 * The book the spell belongs to.
		 *
		 * @return The {@link Book} instance.
		 */
		Book book();
	}

	/**
	 * Modern (Standard) Spells
	 */
	public enum Spell implements MagicSpell {
		NIL(Integer.MIN_VALUE, Integer.MIN_VALUE),
		HOME_TELEPORT(0, 406),
		WIND_STRIKE(1, 1, 65),
		CONFUSE(3, 2, 66),
		ENCHANT_CROSSBOW_BOLT_OPAL(4, 3),
		WATER_STRIKE(5, 4, 67),
		ENCHANT_LEVEL_1_JEWELLERY(7, 5, 68),
		ENCHANT_CROSSBOW_BOLT_SAPPHIRE(7, 3),
		EARTH_STRIKE(9, 6, 69),
		WEAKEN(11, 7, 70),
		FIRE_STRIKE(13, 8, 71),
		ENCHANT_CROSSBOW_BOLT_JADE(14, 3),
		BONES_TO_BANANAS(15, 9, 72),
		WIND_BOLT(17, 10, 73),
		CURSE(19, 11, 74),
		BIND(20, 12, 369),
		LOW_LEVEL_ALCHEMY(21, 13, 75),
		WATER_BOLT(23, 14, 76),
		ENCHANT_CROSSBOW_BOLT_PEARL(24, 3),
		VARROCK_TELEPORT(25, 15, 77),
		ENCHANT_LEVEL_2_JEWELLERY(27, 16, 78),
		ENCHANT_CROSSBOW_BOLT_EMERALD(27, 3),
		EARTH_BOLT(29, 17, 79),
		ENCHANT_CROSSBOW_BOLT_RED_TOPAZ(29, 3),
		LUMBRIDGE_TELEPORT(31, 18, 80),
		TELEKINETIC_GRAB(33, 19, 81),
		FIRE_BOLT(25, 20, 82),
		FALADOR_TELEPORT(37, 21, 83),
		CRUMBLE_UNDEAD(39, 22, 84),
		TELEPORT_TO_HOUSE(40, 23, 405),
		WIND_BLAST(41, 24, 85),
		SUPERHEAT_ITEM(43, 25, 86),
		CAMELOT_TELEPORT(45, 26, 87),
		WATER_BLAST(47, 27, 88),
		ENCHANT_LEVEL_3_JEWELLERY(49, 28, 89),
		ENCHANT_CROSSBOW_BOLT_RUBY(49, 3),
		IBAN_BLAST(50, 29, 103),
		SNARE(50, 30, 370),
		MAGIC_DART(50, 31, 374),
		ARDOUGNE_TELEPORT(51, 32, 104),
		EARTH_BLAST(51, 33, 90),
		HIGH_ALCHEMY(55, 34, 91),
		CHARGE_WATER_ORB(56, 35, 92),
		ENCHANT_LEVEL_4_JEWELLERY(57, 36, 93),
		ENCHANT_CROSSBOW_BOLT_DIAMOND(57, 3),
		WATCHTOWER_TELEPORT(58, 37, 105),
		FIRE_BLAST(59, 38, 94),
		CHARGE_EARTH_ORB(60, 39, 95),
		BONES_TO_PEACHES(60, 40, 404),
		SARADOMIN_STRIKE(60, 41, 111),
		CLAWS_OF_GUTHIX(60, 42, 110),
		FLAMES_OF_ZAMORAK(60, 43, 109),
		TROLLHEIM_TELEPORT(61, 44, 373),
		WIND_WAVE(62, 45, 96),
		CHARGE_FIRE_ORB(63, 46, 97),
		TELEPORT_APE_ATOLL(64, 47, 407),
		WATER_WAVE(65, 48, 98),
		CHARGE_AIR_ORB(66, 49, 99),
		VULNERABILITY(66, 50, 106),
		ENCHANT_LEVEL_5_JEWELLERY(68, 51, 100),
		ENCHANT_CROSSBOW_BOLT_DRAGONSTONE(68, 3),
		TELEPORT_KOUREND(69, 52, 410),
		EARTH_WAVE(70, 53, 101),
		ENFEEBLE(73, 54, 107),
		TELEOTHER_LUMBRIDGE(74, 55, 399),
		FIRE_WAVE(75, 56, 102),
		ENTANGLE(79, 57, 371),
		STUN(80, 58, 108),
		CHARGE(80, 59, 372),
		TELEOTHER_FALADOR(82, 60, 400),
		TELE_BLOCK(85, 61, 402),
		TELEPORT_TO_BOUNTY_TARGET(90, 62, 409),
		ENCHANT_LEVEL_6_JEWELLERY(87, 63, 403),
		ENCHANT_CROSSBOW_BOLT_ONYX(87, 3),
		TELEOTHER_CAMELOT(90, 64, 401),
		ENCHANT_LEVEL_7_JEWELLERY(93, 65, 411);

		private final int level, component, offTexture;

		Spell(final int level, final int component) {
			this(level, component, Integer.MIN_VALUE);
		}

		Spell(final int level, final int component, final int offTexture) {
			this.level = level;
			this.component = component;
			this.offTexture = offTexture;
		}

		public Book book() {
			return Book.MODERN;
		}

		public int level() {
			return level;
		}

		public int texture() {
			return offTexture;
		}

		@Deprecated
		public int component() {
			return component + 1;
		}
	}

	/**
	 * Spells for Ancient Magicks Spellbook
	 */
	public enum AncientSpell implements MagicSpell {
		HOME_TELEPORT(1, 406),
		SMOKE_RUSH(50, 379),
		SHADOW_RUSH(52, 387),
		PADEWWA_TELEPORT(54, 391),
		BLOOD_RUSH(56, 383),
		ICE_RUSH(58, 375),
		SENNTISTEN_TELEPORT(60, 392),
		SMOKE_BURST(62, 380),
		SHADOW_BURST(64, 388),
		KHARYLL_TELEPORT(66, 393),
		BLOOD_BURST(68, 384),
		ICE_BURST(70, 376),
		LASSAR_TELEPORT(72, 394),
		SMOKE_BLITZ(74, 381),
		SHADOW_BLITZ(76, 389),
		DAREEYAK_TELEPORT(78, 395),
		BLOOD_BLITZ(80, 385),
		ICE_BLITZ(82, 377),
		CARRALLANGAR_TELEPORT(84, 396),
		TELEPORT_TO_BOUNTY_TARGET(85, 409),
		SMOKE_BARRAGE(86, 382),
		SHADOW_BARRAGE(88, 390),
		ANNAKARL_TELEPORT(90, 397),
		BLOOD_BARRAGE(92, 386),
		ICE_BARRAGE(94, 378),
		GHORROCK_TELEPORT(96, 398);

		private final int level, offTexture;

		AncientSpell(final int level, final int offTexture) {
			this.level = level;
			this.offTexture = offTexture;
		}

		@Override
		public int level() {
			return level;
		}

		@Override
		public int texture() {
			return offTexture;
		}

		@Override
		public Book book() {
			return Book.ANCIENT;
		}
	}

	/**
	 * Spells for Lunar Spellbook.
	 */
	public enum LunarSpell implements MagicSpell {
		BAKE_PIE(65, 593),
		CURE_PLANT(66, 617),
		MONSTER_EXAMINE(66, 627),
		NPC_CONTACT(67, 618),
		CURE_OTHER(68, 609),
		HUMIDIFY(68, 628),
		MOONCLAN_TELEPORT(69, 594),
		TELE_GROUP_MOONCLAN(70, 619),
		CURE_ME(71, 612),
		HUNTER_KIT(71, 629),
		WATERBIRTH_TELEPORT(72, 595),
		TELE_GROUP_WATERBIRTH(73, 620),
		CURE_GROUP(74, 615),
		BARBARIAN_TELEPORT(75, 597),
		STAT_SPY(75, 626),
		TELE_GROUP_BARBARIAN(76, 621),
		SUPERGLASS_MAKE(77, 598),
		KHAZARD_TELEPORT(78, 599),
		TELE_GROUP_KHAZARD(79, 622),
		DREAM(79, 630),
		STRING_JEWELLERY(80, 600),
		STAT_RESTORE_POT_SHARE(81, 604),
		MAGIC_IMBUE(82, 602),
		FERTILE_SOIL(83, 603),
		BOOST_POTION_SHARE(84, 601),
		FISHING_GUILD_TELEPORT(85, 605),
		TELE_GROUP_FISHING_GUILD(86, 623),
		PLANK_MAKE(86, 631),
		CATHERBY_TELEPORT(87, 606),
		TELE_GROUP_CATHERBY(88, 624),
		ICE_PLATEAU_TELEPORT(89, 607),
		TELE_GROUP_ICE_PLATEAU(90, 625),
		ENERGY_TRANSFER(91, 608),
		HEAL_OTHER(92, 610),
		VENGEANCE_OTHER(93, 611),
		VENGEANCE(94, 614),
		HEAL_GROUP(95, 616),
		SPELL_BOOK_SWAP(96, 632);

		private final int level, offTexture;

		LunarSpell(final int level, final int offTexture) {
			this.level = level;
			this.offTexture = offTexture;
		}

		@Override
		public int level() {
			return level;
		}

		@Override
		public int texture() {
			return offTexture;
		}

		@Override
		public Book book() {
			return Book.LUNAR;
		}

	}

	public enum Book {
		/**
		 * Standard Spellbook
		 */
		MODERN(Spell.values()),

		/**
		 * Ancient Magicks
		 */
		ANCIENT(AncientSpell.values()),

		/**
		 * Lunar Spellbook
		 */
		LUNAR(LunarSpell.values()),

		/**
		 * Non-existent Spellbook.
		 */
		NIL(new MagicSpell[]{});

		@Deprecated
		public final int widget = widget(); // keep for backwards compatibility

		private final int varp;
		private final MagicSpell[] spells;

		Book(final MagicSpell[] spells) {
			this.varp = ordinal();
			this.spells = spells;
		}

		public static int widget() {
			return 218;
		}

		public final MagicSpell[] spells() {
			return spells;
		}
	}
}
