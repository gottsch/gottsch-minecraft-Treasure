package com.someguyssoftware.treasure2.printer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.lock.LockState;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

/**
 * 
 * @author Mark Gottschling on Aug 13, 2018
 *
 */
public class ChestNBTPrettyPrinter {
	private static final String div;
	private static final String sub;

	private static String format2 = "**++    %1$-31s: %2$-28s  ++**\n";
	private static String heading2 = "**++  %1$-63s  ++**\n";
	
	static {
		// setup a divider line
		char[] chars = new char[75];
		Arrays.fill(chars, '*');
		div = new String(chars) + "\n";	
		Arrays.fill(chars, '+');
		chars[0] = chars[1] = chars[73] = chars[74] = '*';
		sub = new String(chars) + "\n";
	}
	
	public ChestNBTPrettyPrinter() {}
	
	/**
	 * Print all the properties of a chest NBT in a prettified format out to a file.
	 * @param tag
	 * @param path
	 */
	public String print(NBTTagCompound tag, Path path) {
		return print(tag, path, "CHEST");
	}
	
	/**
	 * 
	 * @param tag
	 * @param path
	 * @param title
	 * @return
	 */
	public String print(NBTTagCompound tag, Path path, String title) {
		String s = print(tag, title);
		try {
			Files.write(path.toAbsolutePath(), s.getBytes());
		} catch (IOException e) {
			Treasure.LOGGER.error("Error writing ChestConfig NBT to dump file", e);
		}
		return s;
	}
	
	/**
	 * Print all the properties of a chest NBT in a prettified format out to a String
	 * @param tag
	 * @return
	 */
	public String print(NBTTagCompound tag, String title) {
		StringBuilder sb = new StringBuilder();
		try {
			// TODO move these to static properties
			String heading = "**  %1$-67s  **\n";
			String format = "**    %1$-33s: %2$-30s  **\n";
			String format3 = "**    %1$-63s  **\n";
			
			
			sb
			.append(div)
			.append(String.format(heading, title))
			.append(div)
			.append(String.format(heading, "[Properties]"));
			
			// get the lock states
			NBTTagList lockStates = null;
			if (tag.hasKey("lockStates")) {
				lockStates = tag.getTagList("lockStates", Constants.NBT.TAG_COMPOUND);
			}
			sb.append(String.format(format, "# of Lock States", (lockStates!=null) ?lockStates.tagCount() : 0));

			// get the items
			NBTTagList items = null;
			if (tag.hasKey("Items")) {
				items = tag.getTagList("Items", 10);
			}
			sb.append(String.format(format, "# of Items", (items!=null) ?items.tagCount() : 0));
			
			// get the custom name
			if (tag.hasKey("CustomName")) {
				sb.append(String.format(format,  "Custom Name", tag.getString("CustomName")));
			}
			
			// get the facing direction
			if (tag.hasKey("facing")) {
				sb.append(String.format(format,  "Facing", EnumFacing.getFront(tag.getInteger("facing"))));
			}
			
			if (tag.hasKey("sealed")) {
				sb.append(String.format(format,  "Sealed", tag.getBoolean("sealed")));
			}

			if (tag.hasKey("genContext")) {
				NBTTagCompound contextTag = tag.getCompoundTag("genContext");
				if (contextTag.hasKey("lootRarity")) {
					sb.append(String.format(format,  "Loot Rarity", contextTag.getString("lootRarity")));
				}
				if (contextTag.hasKey("chestGenType")) {
					sb.append(String.format(format,  "Chest Generation Type", ChestGeneratorType.valueOf(contextTag.getString("chestGenType")).name()));
				}
			}
			
			// print each lock state
			if (lockStates != null) {
				for (int i = 0; i < lockStates.tagCount(); i++) {
					NBTTagCompound c = lockStates.getCompoundTagAt(i);
					LockState lockState = LockState.readFromNBT(c);
					
					sb.append(sub).append(String.format(heading2, "[Lock State]"));
					if (lockState.getSlot() != null)
						sb.append(String.format(format2, "Slot Face", lockState.getSlot().getFace()));
					if (lockState.getLock() != null) {
						sb.append(String.format(format2, "Lock", lockState.getLock().getClass().getSimpleName()))
						.append(String.format(format2, "Registry Name", lockState.getLock().getRegistryName()))
						.append(String.format(format2, "Unlocalized Name", lockState.getLock().getUnlocalizedName()));
					}
				}
			}
			sb.append(div);
			
			// print each item
	        for (int i = 0; i < items.tagCount(); ++i) {
	            NBTTagCompound c = items.getCompoundTagAt(i);
	            int slot = c.getByte("Slot") & 255;
	            if (c.hasKey("id", 8)) {
	            	String item = c.getString("id");
	            	sb.append(String.format(format2, "Slot " + slot, item));
	            }
	        }
	        sb.append(div);
			
			sb.append("\n");			
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error dumping ChestConfig NBT", e);
			return e.getMessage();
		}
		return sb.toString();
	}
}
