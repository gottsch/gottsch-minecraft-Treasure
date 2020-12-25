package com.someguyssoftware.treasure2.item;

/**
 * 
 * @author Mark Gottschling on Dec 19, 2020
 *
 */
public interface IAdornment {

    public int getLevel();
    public IAdornment setLevel(int level);

    public int getMaxSlots();
    public IAdornment setMaxSlots(int slots);
}