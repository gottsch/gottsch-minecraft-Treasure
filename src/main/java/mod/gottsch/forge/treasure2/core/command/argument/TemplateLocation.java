/**
 * 
 */
package mod.gottsch.forge.treasure2.core.command.argument;

/**
 * @author Mark
 *
 */
public class TemplateLocation {
	private String modID;
	private String archeType;
	private String name;

	public TemplateLocation(String modID, String archeType, String name) {
		this.modID = modID;
		this.archeType = archeType;
		this.name = name;
	}
	
	public String getModID() {
		return modID;
	}

	public String getArchetype() {
		return archeType;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "TemplateLocation [modID=" + modID + ", archeType=" + archeType + ", name=" + name + "]";
	}
}
