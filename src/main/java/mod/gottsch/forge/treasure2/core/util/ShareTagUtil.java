/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Builds and reads item share tags that carry capability data without replacing the stack's own tag.
 * <p>
 * The share tag sent to the client becomes the client stack's tag (via Item.readShareTag), so a share tag
 * made only of capability data wipes the item's custom name, enchantments and other mods' data on the client.
 * Instead the share tag is a copy of the stack's own tag with the capability data under a single sub-key,
 * which is stripped again before the tag is applied to the client stack.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public final class ShareTagUtil {
	public static final String CAPABILITIES_KEY = "treasure2_caps";

	private ShareTagUtil() {}

	/**
	 * Creates a share tag from a copy of the stack's own tag plus the capability tags.
	 * @param stack
	 * @param capabilities the capability tags, keyed by name
	 * @return
	 */
	public static CompoundTag create(ItemStack stack, CompoundTag capabilities) {
		CompoundTag tag = stack.hasTag() ? stack.getTag().copy() : new CompoundTag();
		tag.put(CAPABILITIES_KEY, capabilities);
		return tag;
	}

	/**
	 * Adds a capability tag, ignoring null tags (ex. capability not attached).
	 * @param capabilities
	 * @param key
	 * @param tag
	 */
	public static void put(CompoundTag capabilities, String key, @Nullable Tag tag) {
		if (tag != null) {
			capabilities.put(key, tag);
		}
	}

	/**
	 * Returns the capability tags of a share tag, or an empty compound if it has none.
	 * @param shareTag
	 * @return
	 */
	public static CompoundTag getCapabilities(@Nullable CompoundTag shareTag) {
		if (shareTag != null && shareTag.contains(CAPABILITIES_KEY, Tag.TAG_COMPOUND)) {
			return shareTag.getCompound(CAPABILITIES_KEY);
		}
		return new CompoundTag();
	}

	/**
	 * Returns the stack's own tag from a share tag - a copy without the capability tags, or null if nothing remains.
	 * @param shareTag
	 * @return
	 */
	@Nullable
	public static CompoundTag getStackTag(@Nullable CompoundTag shareTag) {
		if (shareTag == null) {
			return null;
		}
		CompoundTag tag = shareTag.copy();
		tag.remove(CAPABILITIES_KEY);
		return tag.isEmpty() ? null : tag;
	}
}
