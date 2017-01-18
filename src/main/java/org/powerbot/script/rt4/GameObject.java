package org.powerbot.script.rt4;

import java.awt.Color;
import java.awt.Point;

import org.powerbot.bot.rt4.Bot;
import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.Cache;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.Varbit;
import org.powerbot.script.Actionable;
import org.powerbot.script.Identifiable;
import org.powerbot.script.InteractiveEntity;
import org.powerbot.script.Nameable;
import org.powerbot.script.StringUtils;
import org.powerbot.script.Tile;
import org.powerbot.script.Validatable;

/**
 * GameObject
 */
public class GameObject extends Interactive implements Nameable, InteractiveEntity, Identifiable, Validatable, Actionable {
	public static final Color TARGET_COLOR = new Color(0, 255, 0, 20);
	private static final int[] lookup;

	static {
		lookup = new int[32];
		int i = 2;
		for (int j = 0; j < 32; j++) {
			lookup[j] = i - 1;
			i += i;
		}
	}

	private final BasicObject object;
	private final Type type;

	GameObject(final ClientContext ctx, final BasicObject object, final Type type) {
		super(ctx);
		this.object = object;
		this.type = type;
		bounds(-32, 32, -64, 0, -32, 32);
	}

	@Override
	public void bounds(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		boundingModel.set(new BoundingModel(ctx, x1, x2, y1, y2, z1, z2) {
			@Override
			public int x() {
				final int r = relative();
				return r >> 16;
			}

			@Override
			public int z() {
				final int r = relative();
				return r & 0xffff;
			}
		});
	}

	public int mainId() {
		final Client client = ctx.client();
		if (client == null) {
			return -1;
		}
		return object != null ? (object.getUid() >> 14) & 0xffff : -1;
	}

	@Override
	public int id() {
		final Client client = ctx.client();
		if (client == null) {
			return -1;
		}
		final int id = object != null ? (object.getUid() >> 14) & 0xffff : -1;
		if (object == null) {
			return id;
		}
		int index = -1;
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id);
		if (c == null) {
			return id;
		}
		if (c.stageOperationId != -1) {
			final Cache cache = client.getVarbitCache();
			final Varbit varBit = new Varbit(object.object.reflector, HashTable.lookup(cache.getTable(), c.stageOperationId, Varbit.class));
			if (varBit.obj.get() != null) {
				final int mask = lookup[varBit.getEndBit() - varBit.getStartBit()];
				index = ctx.varpbits.varpbit(varBit.getIndex()) >> varBit.getStartBit() & mask;
			}
		} else if (c.stageIndex >= 0) {
			index = ctx.varpbits.varpbit(c.stageIndex);
		}
		if (index >= 0) {
			final int[] configs = c.materialPointers;
			if (configs != null && index < configs.length && configs[index] != -1) {
				return configs[index];
			}
		}
		return id;
	}

	@Override
	public String name() {
		if (object == null) {
			return "";
		}
		final int id = (object.getUid() >> 14) & 0xffff;
		final CacheObjectConfig
				c1 = CacheObjectConfig.load(Bot.CACHE_WORKER, id),
				c2 = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c2 != null) {
			if (c1 != null && c2.name.equals("null")) {
				return StringUtils.stripHtml(c1.name);
			}
			return StringUtils.stripHtml(c2.name);
		} else if (c1 != null) {
			return StringUtils.stripHtml(c1.name);
		}
		return "";
	}

	public int[] colors1() {
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c != null) {
			final int[] s = c.originalColors;
			return s == null ? new int[0] : s;
		}
		return new int[0];
	}

	public int[] colors2() {
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c != null) {
			final int[] s = c.modifiedColors;
			return s == null ? new int[0] : s;
		}
		return new int[0];
	}

	public int width() {
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c != null) {
			return c.xSize;
		}
		return -1;
	}

	public int height() {
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c != null) {
			return c.ySize;
		}
		return -1;
	}

	public int[] meshIds() {
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c != null) {
			int[] meshIds = c.meshId;
			if (meshIds == null) {
				meshIds = new int[0];
			}
			return meshIds;
		}
		return new int[0];
	}

	public String[] actions() {
		final CacheObjectConfig c = CacheObjectConfig.load(Bot.CACHE_WORKER, id());
		if (c != null) {
			return c.actions;
		}
		return new String[0];
	}

	public int orientation() {
		return object != null ? object.getMeta() >> 6 : 0;
	}

	public Type type() {
		/*
		final BasicObject object = this.object.get();
		return object != null ? object.getMeta() & 0x3f : 0;
		 */
		return type;
	}

	public int relative() {
		final int x, z;
		if (object != null) {
			if (object.isComplex()) {
				x = object.getX();
				z = object.getZ();
			} else {
				final int uid = object.getUid();
				x = (uid & 0x7f) << 7;
				z = ((uid >> 7) & 0x7f) << 7;
			}
		} else {
			x = z = 0;
		}
		return (x << 16) | z;
	}

	@Override
	public boolean valid() {
		return !(object == null || object.object.isNull()) && ctx.objects.select(this, 0).contains(this);
	}

	@Override
	public Tile tile() {
		final Client client = ctx.client();
		final int r = relative();
		final int rx = r >> 16, rz = r & 0xffff;
		if (client != null && rx != 0 && rz != 0) {
			return new Tile(client.getOffsetX() + (rx >> 7), client.getOffsetY() + (rz >> 7), client.getFloor());
		}
		return Tile.NIL;
	}

	@Override
	public Point centerPoint() {
		final BoundingModel model = boundingModel.get();
		return model != null ? model.centerPoint() : new Point(-1, -1);
	}

	@Override
	public Point nextPoint() {
		final BoundingModel model = boundingModel.get();
		return model != null ? model.nextPoint() : new Point(-1, -1);
	}

	@Override
	public boolean contains(final Point point) {
		final BoundingModel model = boundingModel.get();
		return model != null && model.contains(point);
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d,name=%s,type=%s,tile=%s]", GameObject.class.getName(), id(), name(), type.name(), tile().toString());
	}

	@Override
	public int hashCode() {
		return object != null ? object.hashCode() : 0;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof GameObject && hashCode() == o.hashCode();
	}

	public enum Type {
		INTERACTIVE, BOUNDARY, WALL_DECORATION, FLOOR_DECORATION, UNKNOWN
	}
}
