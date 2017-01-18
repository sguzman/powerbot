package org.powerbot.script.rt4;

import java.util.Arrays;

import org.powerbot.bot.cache.Block;
import org.powerbot.bot.cache.CacheWorker;
import org.powerbot.bot.cache.JagexStream;

/**
 * CacheObjectConfig
 * An object holding configuration data for a GameObject within Runescape.
 */
class CacheObjectConfig {
	public final int index;
	private final JagexStream stream;
	public String name = "null";
	public String[] actions = new String[5];
	public int xSize = 1;
	public int[] materialPointers;
	public int ySize = 1;
	public int[] meshId;
	public int[] meshType;
	public boolean swapYZ = false;
	public int yScale = 128;
	public int xScale = 128;
	public int zScale = 128;
	public int xTranslate = 0;
	public int yTranslate = 0;
	public int stageOperationId = -1;
	public int stageIndex = -1;
	public int zTranslate = 0;
	public int[] originalColors, modifiedColors;

	public CacheObjectConfig(final Block.Sector sector, final int index) {
		this.index = index;
		stream = new JagexStream(sector.getPayload());
		read();
	}

	static CacheObjectConfig load(final CacheWorker worker, final int id) {
		final Block b = worker.getBlock(2, 6);
		if (b == null) {
			return null;
		}
		final Block.Sector s = b.getSector(id);
		if (s == null) {
			return null;
		}
		return new CacheObjectConfig(s, id);
	}

	private void read() {
		int opcode;
		while ((opcode = stream.getUByte()) != 0) {
			switch (opcode) {
			case 1: {
				final int len = stream.getUByte();
				if (len <= 0) {
					continue;
				}
				if (meshId != null) {
					stream.seek(stream.getLocation() + len * 3);
					continue;
				}
				meshId = new int[len];
				meshType = new int[len];
				for (int i = 0; i < len; i++) {
					meshId[i] = stream.getUShort();
					meshType[i] = stream.getUByte();
				}
				break;
			}
			case 2:
				name = stream.getString();
				break;
			case 5: {
				final int len = stream.getUByte();
				if (len <= 0) {
					continue;
				}
				if (meshId != null) {
					stream.seek(stream.getLocation() + len * 2);
					continue;
				}
				meshId = new int[len];
				meshType = null;
				for (int i = 0; i < len; i++) {
					meshId[i] = stream.getUShort();
				}
				break;
			}
			case 14:
				xSize = stream.getUByte();
				break;
			case 15:
				ySize = stream.getUByte();
				break;
			case 17:
			case 18:
				break;
			case 19:
				stream.getUByte();
				break;
			case 21:
			case 22:
			case 23:
				break;
			case 24:
				stream.getUShort();
				break;
			case 27:
				break;
			case 28:
				stream.getUByte();
				break;
			case 29:
			case 39:
				stream.getByte();
				break;
			case 30:
			case 31:
			case 32:
			case 33:
			case 34:
				actions[opcode - 30] = stream.getString();
				if (actions[opcode - 30].equalsIgnoreCase("Hidden")) {
					actions[opcode - 30] = null;
				}
				break;
			case 40: {
				final int len = stream.getUByte();
				originalColors = new int[len];
				modifiedColors = new int[len];
				for (int i = 0; i < len; i++) {
					originalColors[i] = stream.getUShort();
					modifiedColors[i] = stream.getUShort();
				}
				break;
			}
			case 41: {
				final int len = stream.getUByte();
				final short[] arr1 = new short[len], arr2 = new short[len];
				for (int i = 0; i < len; i++) {
					arr1[i] = (short) stream.getUShort();
					arr2[i] = (short) stream.getUShort();
				}
				break;
			}
			case 60:
				stream.getUShort();
				break;
			case 62:
				swapYZ = true;
				break;
			case 64:
				break;
			case 65:
				xScale = stream.getUShort();
				break;
			case 66:
				yScale = stream.getUShort();
				break;
			case 67:
				zScale = stream.getUShort();
				break;
			case 68:
				stream.getUShort();
				break;
			case 69:
				stream.getUByte();
				break;
			case 70:
				xTranslate = stream.getUShort();
				break;
			case 71:
				yTranslate = stream.getUShort();
				break;
			case 72:
				zTranslate = stream.getUShort();
				break;
			case 73:
			case 74:
				break;
			case 75:
				stream.getUByte();
				break;
			case 77: {
				stageOperationId = stream.getUShort() & 0xFFFF;
				if (65535 == stageOperationId) {
					stageOperationId = -1;
				}
				stageIndex = stream.getUShort() & 0xFFFF;
				if (65535 == stageIndex) {
					stageIndex = -1;
				}
				final int len = stream.getUByte();
				materialPointers = new int[1 + len];
				for (int i = 0; i <= len; i++) {
					materialPointers[i] = stream.getUShort() & 0xFFFF;
					if (65535 != materialPointers[i]) {
						continue;
					}
					materialPointers[i] = -1;
				}
				break;
			}
			case 78:
				stream.getUShort();
				stream.getUByte();
				break;
			case 79:
				stream.getUShort();
				stream.getUShort();
				stream.getUByte();
				final int len = stream.getUByte();
				for (int i = 0; i < len; i++) {
					stream.getUShort();
				}
				break;
			case 81:
				stream.getUByte();
				break;
			}
		}
	}

	@Override
	public String toString() {
		return String.format(
				"ObjectDefinition[index=%s,name=%s,actions=%s,xSize=%d,ySize=%d,ext_links=%s,ext_ids=%s,ext_types=%s,stageIndex=%d,stageId=%d]",
				index, name, Arrays.toString(actions), xSize, ySize,
				Arrays.toString(materialPointers), Arrays.toString(meshId), Arrays.toString(meshType),
				stageIndex, stageOperationId
		);
	}
}