package com.someguyssoftware.treasure2.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Helpers for items that sync capability data through getNBTShareTag() / readNBTShareTag().
 * <p>
 * The share tag must carry the stack's own tag (name, enchantments, other mods' data) as well as the
 * capability data, otherwise readNBTShareTag() replaces the stack's tag with only the capability data.
 * The capability data is kept under a single sub-key that is removed again when the share tag is read,
 * so it never ends up in the stack's real tag.
 *
 * @author Mark Gottschling on 10/15/2026
 */
public class ShareTagUtil {
	public static final String CAPABILITIES_KEY = "treasure2_caps";

	private ShareTagUtil() {}

	/**
	 * Creates a share tag from a copy of the stack's own tag plus the capability tags.
	 * @param stack
	 * @param capabilities the capability tags, keyed by name
	 * @return
	 */
	public static NBTTagCompound create(ItemStack stack, NBTTagCompound capabilities) {
		NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound().copy() : new NBTTagCompound();
		tag.setTag(CAPABILITIES_KEY, capabilities);
		return tag;
	}

	/**
	 * Adds a capability tag, ignoring null tags (ex. capability not attached).
	 * @param capabilities
	 * @param key
	 * @param tag
	 */
	public static void put(NBTTagCompound capabilities, String key, @Nullable NBTBase tag) {
		if (tag != null) {
			capabilities.setTag(key, tag);
		}
	}

	/**
	 * Returns the capability tags of a share tag, or an empty compound if it has none.
	 * @param shareTag
	 * @return
	 */
	public static NBTTagCompound getCapabilities(@Nullable NBTTagCompound shareTag) {
		if (shareTag != null && shareTag.hasKey(CAPABILITIES_KEY, 10)) {
			return shareTag.getCompoundTag(CAPABILITIES_KEY);
		}
		return new NBTTagCompound();
	}

	/**
	 * Returns the stack's own tag from a share tag - a copy without the capability tags, or null if nothing remains.
	 * @param shareTag
	 * @return
	 */
	@Nullable
	public static NBTTagCompound getStackTag(@Nullable NBTTagCompound shareTag) {
		if (shareTag == null) {
			return null;
		}
		NBTTagCompound tag = shareTag.copy();
		tag.removeTag(CAPABILITIES_KEY);
		return tag.hasNoTags() ? null : tag;
	}
}
