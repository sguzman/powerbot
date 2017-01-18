package org.powerbot.script.rt4;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Point;

import org.powerbot.bot.AbstractBot;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MenuCommand;
import org.powerbot.script.Tile;

/**
 * Game
 * A utility class used for interacting with game tabs, retrieving miscellaneous game values, and converting points to the viewport.
 */
public class Game extends ClientAccessor {
	private static final int[] ARRAY_SIN = new int[2048];
	private static final int[] ARRAY_COS = new int[2048];

	static {
		for (int i = 0; i < 2048; i++) {
			ARRAY_SIN[i] = (int) (65536d * Math.sin(i * 0.0030679615d));
			ARRAY_COS[i] = (int) (65536d * Math.cos(i * 0.0030679615d));
		}
	}

	public Game(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Attempts to open the specified game tab on the user interface. If the
	 * tab is already opened, it will return <ii>true</ii>.
	 *
	 * @param tab The tab to switch to
	 * @return <ii>true</ii> if the tab is open, <ii>false</ii> otherwise.
	 */
	public boolean tab(final Tab tab) {
		final Component c = getByTexture(tab.textures);
		return tab() == tab || c != null && c.click(new Filter<MenuCommand>() {
			@Override
			public boolean accept(MenuCommand command) {
				for (final String tip : tab.tips) {
					if (command.action.equals(tip)) {
						return true;
					}
				}
				return false;
			}
		}) && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return tab() == tab;
			}
		}, 50, 10);
	}

	/**
	 * Returns the current tab which is opened.
	 *
	 * @return The open game tab.
	 */
	public Tab tab() {
		for (final Tab tab : Tab.values()) {
			final Component c = getByTexture(tab.textures);
			if (c == null) {
				continue;
			}
			try {
				final Component c2 = ctx.widgets.widget(c.widget().id()).component(c.index() - openedTabIndexOffset(tab));
				if (c2.textureId() != -1) {
					return tab;
				}
			} catch (final ArrayIndexOutOfBoundsException ignored) {
			}
		}
		return Tab.NONE;
	}

	private int openedTabIndexOffset(final Tab tab) {
		if (bottomLineTabs()) {
			switch (tab) {
			case LOGOUT:
				return 1;
			case FRIENDS_LIST:
			case IGNORED_LIST:
			case CLAN_CHAT:
			case OPTIONS:
			case EMOTES:
			case MUSIC:
				return 6;
			default:
				return 7;
			}
		}

		return 7;
	}

	private Component getByTexture(final int... textures) {
		final Widget w = ctx.widgets.widget(resizable() ? bottomLineTabs() ? 164 : 161 : 548);
		for (final Component c : w.components()) {
			for (int t : textures) {
				if (c.textureId() == t) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * Whether or not the player is currently logged in.
	 *
	 * @return <ii>true</ii> if logged in, <ii>false</ii> otherwise.
	 */
	public boolean loggedIn() {
		final int c = clientState();
		return c == Constants.GAME_LOADED || c == Constants.GAME_LOADING;
	}

	/**
	 * The dimensions of the Applet.
	 *
	 * @return The dimensions of the applet.
	 */
	public Dimension dimensions() {
		final Applet applet = (Applet) ((AbstractBot) ctx.bot()).chrome.target.get();
		return applet != null ? new Dimension(applet.getWidth(), applet.getHeight()) : new Dimension(-1, -1);
	}

	/**
	 * The current client state.
	 *
	 * @see Constants
	 * @return The current client state.
	 */
	public int clientState() {
		final Client client = ctx.client();
		return client != null ? client.getClientState() : -1;
	}

	/**
	 * The current floor the client is at.
	 *
	 * @return the current floor the player is at, or -1 if the client has yet to be instantiated.
	 */
	public int floor() {
		final Client client = ctx.client();
		return client != null ? client.getFloor() : -1;
	}

	/**
	 * Determines the current {@link Crosshair} displayed.
	 *
	 * @return the displayed {@link Crosshair}
	 */
	public Crosshair crosshair() {
		final Client client = ctx.client();
		final int type = client != null ? client.getCrosshairIndex() : -1;
		if (type < 0 || type > 2) {
			return Crosshair.NONE;
		}
		return Crosshair.values()[type];
	}

	/**
	 * The relative offset tile for the map.
	 *
	 * @return {@link Tile} of where the offset is.
	 */
	public Tile mapOffset() {
		final Client client = ctx.client();
		if (client == null) {
			return Tile.NIL;
		}
		return new Tile(client.getOffsetX(), client.getOffsetY(), client.getFloor());
	}

	/**
	 * Whether or not the 2-dimension point is within the viewport of the applet.
	 *
	 * @param p The 2-dimensional point to check.
	 * @return <ii>true</ii> if it is within bounds, <ii>false</ii> otherwise.
	 */
	public boolean inViewport(final Point p) {
		return pointInViewport(p.x, p.y);
	}

	/**
	 * Whether or not the game client is resizeable.
	 *
	 * @return <ii>true</ii> if it is resizeable, <ii>false</ii> otherwise.
	 */
	public boolean resizable() {
		return ctx.widgets.widget(548).component(10).screenPoint().x != 4;
	}

	/**
	 * Whether or not the game tabs are in a bottom line.
	 *
	 * @return <ii>true</ii> if they are aligned on the bottom, <ii>false</ii> otherwise.
	 */
	public boolean bottomLineTabs() {
		return resizable() && (ctx.varpbits.varpbit(1055) >>> 8 & 0x1) == 1;
	}

	/**
	 * Whether or not the 2-dimension point is within the viewport of the applet.
	 *
	 * @param x The x-axis value
	 * @param y The y-axis value
	 * @return <ii>true</ii> if it is within bounds, <ii>false</ii> otherwise.
	 */
	public boolean pointInViewport(final int x, final int y) {
		if (resizable()) {
			final Dimension d = dimensions();
			return x >= 0 && y >= 0 && (x > 520 || y <= d.height - 170) &&
					(x < d.width - 245 || y < d.height - 340 && y > 170);
		}
		return x >= 4 && y >= 4 && x <= 515 && y <= 337;
	}

	/**
	 * The {@link HintArrow}.
	 *
	 * @return {@link HintArrow}.
	 */
	public HintArrow hintArrow() {
		//TODO: hint arrow
		final HintArrow r = new HintArrow();
		final Client client = ctx.client();
		if (client == null) {
			return r;
		}
		return r;
	}

	/**
	 * Converts the 3-dimensional tile to a 2-dimensional point on the mini-map component.
	 *
	 * @param tile The tile to convert
	 * @return The point on screen of where the tile would be.
	 */
	public Point tileToMap(final Tile tile) {
		final Client client = ctx.client();
		if (client == null) {
			return new Point(-1, -1);
		}
		final int rel = ctx.players.local().relative();
		final int angle = client.getMinimapScale() + client.getMinimapAngle() & 0x7ff;
		final int[] d = {tile.x(), tile.y(), ARRAY_SIN[angle], ARRAY_COS[angle], -1, -1};
		d[0] = (d[0] - client.getOffsetX()) * 4 + 2 - (rel >> 16) / 32;
		d[1] = (d[1] - client.getOffsetY()) * 4 + 2 - (rel & 0xffff) / 32;
		final int offset = client.getMinimapOffset();
		d[2] = d[2] << 8 / (offset + 256);
		d[3] = d[3] << 8 / (offset + 256);
		d[4] = d[1] * d[2] + d[3] * d[0] >> 16;
		d[5] = d[2] * d[0] - d[1] * d[3] >> 16;
		final Point centre = mapComponent().centerPoint();
		return new Point(centre.x + d[4], centre.y + d[5]);
	}

	/**
	 * Returns the tile height of the relative 2-dimensional tile. The
	 * 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being Vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @return The tile height
	 */
	public int tileHeight(final int relativeX, final int relativeZ) {
		final Client client = ctx.client();
		if (client == null) {
			return 0;
		}
		int floor = client.getFloor();
		int x = relativeX >> 7;
		int y = relativeZ >> 7;
		if (x < 0 || y < 0 || x > 103 || y > 103 ||
				floor < 0 || floor > 3) {
			return 0;
		}
		final byte[][][] meta = client.getLandscapeMeta();
		final int[][][] heights = client.getTileHeights();
		if (meta == null) {
			return 0;
		}
		if (floor < 3 && (meta[1][x][y] & 0x2) == 2) {
			floor++;
		}

		x &= 0x7f;
		y &= 0x7f;
		final int heightStart = x * heights[floor][1 + x][y] + heights[floor][x][y] * (128 - x) >> 7;
		final int heightEnd = (128 - x) * heights[floor][x][1 + y] + x * heights[floor][1 + x][y + 1] >> 7;
		return y * heightEnd + heightStart * (128 - y) >> 7;
	}

	/**
	 * Converts a 3-dimensional point within the overworld to a 2-dimensional point on the
	 * screen. The 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @param h The y-axis value, otherwise known as height.
	 * @return The 2-dimensional point on screen.
	 */
	public Point worldToScreen(final int relativeX, final int relativeZ, final int h) {
		final Client client = ctx.client();
		if (client == null) {
			return new Point(-1, -1);
		}
		return worldToScreen(relativeX, tileHeight(relativeX, relativeZ), relativeZ, h);
	}

	/**
	 * Converts a 3-dimensional point within the overworld to a 2-dimensional point on the
	 * screen. The 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeY The y-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @param h The y-axis value, otherwise known as height
	 * @return The 2-dimensional point on screen.
	 */
	public Point worldToScreen(final int relativeX, final int relativeY, final int relativeZ, final int h) {
		final Client client = ctx.client();
		final Point r = new Point(-1, -1);
		if (relativeX < 128 || relativeX > 13056 ||
				relativeZ < 128 || relativeZ > 13056) {
			return r;
		}
		final int floor = client.getFloor();
		if (floor < 0) {
			return r;
		}
		final int height = relativeY - h;
		final int projectedX = relativeX - client.getCameraX(), projectedZ = relativeZ - client.getCameraZ(),
				projectedY = height - client.getCameraY();
		final int pitch = client.getCameraPitch(), yaw = client.getCameraYaw();
		final int[] c = {ARRAY_SIN[yaw], ARRAY_COS[yaw], ARRAY_SIN[pitch], ARRAY_COS[pitch]};
		final int rotatedX = c[0] * projectedZ + c[1] * projectedX >> 16;
		final int rotatedZ = c[1] * projectedZ - c[0] * projectedX >> 16;
		final int rolledY = c[3] * projectedY - c[2] * rotatedZ >> 16;
		final int rolledZ = c[3] * rotatedZ + c[2] * projectedY >> 16;
		if (rolledZ >= 50) {
			int mx = 256, my = 167;
			if (ctx.widgets.widget(548).component(10).screenPoint().x != 4) {
				final Dimension d = dimensions();
				mx = d.width / 2;
				my = d.height / 2;
			}
			final int proj = client.getTileSize();
			return new Point(
					(rotatedX * proj) / rolledZ + mx,
					(rolledY * proj) / rolledZ + my
			);
		}
		return r;
	}

	/**
	 * Returns the component of the mini-map. If the client is not loaded or the mini-map is not
	 * visible, this will return a <ii>nil</ii> component.
	 *
	 * @return The component of the mini-map.
	 */
	public Component mapComponent() {
		final Widget i = ctx.widgets.widget(resizable() ? bottomLineTabs() ? 164 : 161 : 548);
		for (final Component c : i.components()) {
			if (c.contentType() == 1338) {
				return c;
			}
		}
		return new Component(ctx, i, -1);
	}

	/**
	 * Tabs which represent the different interfaces within the user interface of the game.
	 */
	public enum Tab {
		ATTACK("Combat Options", 168),
		STATS("Stats", 898),
		QUESTS(new String[]{"Quest List", "Minigames", "Achievement Diaries", "Kourend Tasks"}, 776, 1052, 1053, 1299),
		INVENTORY("Inventory", 884),
		EQUIPMENT("Worn Equipment", 901),
		PRAYER("Prayer", 902),
		MAGIC("Magic", 903),
		CLAN_CHAT("Clan Chat", 895),
		FRIENDS_LIST("Friends List", 904),
		IGNORED_LIST("Ignore List", 905),
		LOGOUT("Logout", 906, 542),
		OPTIONS("Options", 907),
		EMOTES("Emotes", 908),
		MUSIC("Music Player", 909),
		NONE("", -1);
		public final String[] tips;
		public final int[] textures;

		Tab(final String tip, final int... textures) {
			this(new String[]{tip}, textures);
		}

		Tab(final String[] tips, final int... textures) {
			this.tips = tips;
			this.textures = textures;
		}
	}

	/**
	 * An enumeration of the possible cross-hairs in game.
	 */
	public enum Crosshair implements org.powerbot.script.Crosshair {
		NONE, DEFAULT, ACTION
	}
}
