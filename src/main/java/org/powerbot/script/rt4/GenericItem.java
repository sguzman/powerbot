package org.powerbot.script.rt4;

import org.powerbot.script.Identifiable;
import org.powerbot.script.Nameable;

/**
 * GenericItem
 */
abstract class GenericItem extends Interactive implements Identifiable, Nameable {
	public GenericItem(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	public String name() {
		return CacheItemConfig.load(id()).name;
	}

	/**
	 * Whether or not the item is a members' item.
	 *
	 * @return <ii>true</ii> if it is members only, <ii>false</ii> otherwise.
	 */
	public boolean members() {
		return CacheItemConfig.load(id()).members;
	}

	/**
	 * Whether or not the item is stackable.
	 *
	 * @return <ii>true</ii> if the item can be stacked, <ii>false</ii> otherwise.
	 */
	public boolean stackable() {
		return CacheItemConfig.load(id()).stackable;
	}

	/**
	 * Whether or not the item is in banknote form.
	 *
	 * @return <ii>true</ii> if it's a banknote, <ii>false</ii> otherwise.
	 */
	public boolean noted() {
		return CacheItemConfig.load(id()).noted;
	}

	/**
	 * Whether or not the item is tradeable.
	 *
	 * @return <ii>true</ii> if it is tradeable, <ii>false</ii> otherwise.
	 */
	public boolean tradeable() {
		return CacheItemConfig.load(id()).tradeable;
	}

	/**
	 * Whether or not the item is a cosmetic.
	 *
	 * @return <ii>true</ii> if it is a cosmetic, <ii>false</ii> otherwise.
	 */
	public boolean cosmetic() {
		return CacheItemConfig.load(id()).cosmetic;
	}

	/**
	 * The value of the item which would appear in most shops. This may be
	 * used to also grab the high/low alchemy value, as well.
	 *
	 * @return The item value
	 */
	public int value() {
		return CacheItemConfig.load(id()).value;
	}

	/**
	 * The model ID of the item.
	 *
	 * @return The model id of the item.
	 */
	public int modelId() {
		return CacheItemConfig.load(id()).modelId;
	}

	/**
	 * An array of the possible actions if the item were on the ground.
	 *
	 * @return A String array.
	 */
	public String[] groundActions() {
		return CacheItemConfig.load(id()).groundActions;
	}

	/**
	 * An array of the possible actions if the item were in the inventory.
	 *
	 * @return A String array.
	 */
	public String[] inventoryActions() {
		return CacheItemConfig.load(id()).actions;
	}
}
