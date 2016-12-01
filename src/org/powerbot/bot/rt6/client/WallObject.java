package org.powerbot.bot.rt6.client;

import org.powerbot.bot.Reflector;

public class WallObject extends RenderableEntity {
	private static final Reflector.FieldCache a = new Reflector.FieldCache(),
			b = new Reflector.FieldCache();

	public WallObject(final Reflector reflector, final Object obj) {
		super(reflector, obj);
	}

	public byte getOrientation() {
		return reflector.accessByte(this, a);
	}

	public int getId() {
		return reflector.accessInt(this, b);
	}
}
