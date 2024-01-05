/**
 * 
 */
package mod.gottsch.forge.treasure2.core.config;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public interface IWellsConfig {
	
	public void init();
	public boolean isEnabled();
	public double getGenProbability();
	
	public List<String> getBiomeWhiteList();
	public List<String> getBiomeBlackList();
}