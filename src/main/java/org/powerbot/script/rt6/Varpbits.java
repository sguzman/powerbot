package org.powerbot.script.rt6;

import org.powerbot.bot.rt6.client.Client;

/**
 * Varpbits
 * {@link Varpbits} is a utility which provides raw access to the game's
 * settings, or otherwise known as varpbits.
 */
public class Varpbits extends ClientAccessor {
	
	public Varpbits(final ClientContext factory) {
		super(factory);
	}

	/**
	 * Returns the array of settings for the game.
	 *
	 * @return an array of the game's settings
	 */
	public int[] array() {
		final Client client = ctx.client();
		if (client == null) {
			return null;
		}
		final int[] arr = client.getPlayerFacade().getVarpbits().get();
		return arr != null ? arr.clone() : new int[0];
	}

	/**
	 * Returns the array of a specified index.
	 *
	 * @param index the index of the setting
	 * @return the setting for the specified index
	 */
	public int varpbit(final int index) {
		final int[] arr = array();
		return index > -1 && index < arr.length ? arr[index] : -1;
	}

	/**
	 * Gets the value at a given index and applies a given mask to the value.
	 *
	 * @param index the index in the settings array
	 * @param mask  the bitmask
	 * @return the masked value
	 */
	public int varpbit(final int index, final int mask) {
		return varpbit(index, 0, mask);
	}

	/**
	 * Gets the value at a given index, bit shifts it right by a given number of bits and applies a mask.
	 *
	 * @param index the index in the settings array
	 * @param shift the number of bits to right shift
	 * @param mask  the bitmask
	 * @return the masked value
	 */
	public int varpbit(final int index, final int shift, final int mask) {
		return (varpbit(index) >>> shift) & mask;
	}
}
