package org.powerbot.script.rt6;

import java.awt.Color;

import org.powerbot.bot.rt6.client.NpcConfig;
import org.powerbot.script.Actionable;
import org.powerbot.script.Identifiable;
import org.powerbot.script.StringUtils;

/**
 * Npc
 */
public class Npc extends Actor implements Identifiable, Actionable {
	public static final Color TARGET_COLOR = new Color(255, 0, 255, 15);
	private final org.powerbot.bot.rt6.client.Npc npc;

	public Npc(final ClientContext ctx, final org.powerbot.bot.rt6.client.Npc npc) {
		super(ctx);
		this.npc = npc;
	}

	@Override
	protected org.powerbot.bot.rt6.client.Npc getAccessor() {
		return npc;
	}

	@Override
	public String name() {
		final NpcConfig d = npc.getConfig();
		return d.isNull() ? "" : StringUtils.stripHtml(d.getName());
	}

	@Override
	public int combatLevel() {
		final NpcConfig d = npc.getConfig();
		return d.isNull() ? -1 : d.getCombatLevel();
	}

	@Override
	public int id() {
		final NpcConfig d = npc.getConfig();
		return d.isNull() ? -1 : d.getId();
	}

	@Override
	public String[] actions() {
		final NpcConfig d = npc.getConfig();
		return d.isNull() ? new String[0] : d.getActions();
	}

	public int prayerIcon() {
		final int[] a1 = getOverheadArray1();
		final short[] a2 = getOverheadArray2();
		final int len = a1.length;
		if (len != a2.length) {
			return -1;
		}

		for (int i = 0; i < len; i++) {
			if (a1[i] == 440) {
				return a2[i];
			}
		}
		return -1;
	}

	private int[] getOverheadArray1() {
		final NpcConfig d = npc.getConfig();
		final int[] arr1 = npc.getOverhead().getArray1(), arr2 = d.getOverheadArray1();
		return arr1 != null ? arr1 : arr2 != null ? arr2 : new int[0];
	}


	private short[] getOverheadArray2() {
		final NpcConfig d = npc.getConfig();
		final short[] arr1 = npc.getOverhead().getArray2(), arr2 = d.getOverheadArray2();
		return arr1 != null ? arr1 : arr2 != null ? arr2 : new short[0];
	}

	@Override
	public boolean valid() {
		final org.powerbot.bot.rt6.client.Npc npc = getAccessor();
		if (npc.isNull()) {
			return false;
		}
		for (final Npc n : ctx.npcs.select()) {
			if (n.getAccessor().equals(npc)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return Npc.class.getSimpleName() + "[id=" + id() + ",name=" + name() + ",level=" + combatLevel() + "]";
	}
}
