/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmData;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;
import com.someguyssoftware.treasure2.item.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.item.charm.TreasureCharms;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public class CharmMessageToClient implements IMessage {
	private boolean messageIsValid;
	private String charmName;
	private ICharmData data;
	// location properties of charm(s) - who, what hand or what pouch slot
	private String playerName;
	private EnumHand hand;
	private Integer slot;
	
	/**
	 * 
	 * @param playerName
	 */
	public CharmMessageToClient(String playerName, ICharmInstance instance, EnumHand hand, Integer slot) {
		messageIsValid = true;
		this.playerName = playerName;
		this.charmName = instance.getCharm().getName().toString();
		this.data = instance.getData();
		this.hand = hand;
		if (slot == null) {
			this.slot = -1;
		}
		else {
			this.slot = slot;
		}
	}
	
	/**
	 * 
	 */
	public CharmMessageToClient() {
		messageIsValid = false;
	}
	
    // these methods may also be of use for your code:
    // for Itemstacks - ByteBufUtils.readItemStack()
    // for NBT tags ByteBufUtils.readTag();
    // for Strings: ByteBufUtils.readUTF8String();
	@Override
	public void fromBytes(ByteBuf buf) {
	    try {
	    	this.playerName = ByteBufUtils.readUTF8String(buf);
	    	this.charmName = ByteBufUtils.readUTF8String(buf);
	    	double value = buf.readDouble();
	    	int duration = buf.readInt();
	    	double percent = buf.readDouble();
            // data = CharmStateFactory.createCharmVitals(TreasureCharms.REGISTRY.get(charmName));
//            data = TreasureCharms.REGISTRY.get(charmName).createInstance().getData();
	    	// TODO ensure charmName contains a domain
	    	/// TODO check optional value
	    	Optional<ICharm> optionalCharm = TreasureCharmRegistry.get(ResourceLocationUtil.create(charmName));
	    	if (!optionalCharm.isPresent()) {
	    		throw new RuntimeException(String.format("Unable to find charm %s in registry.", charmName));
	    	}
	    	data = optionalCharm.get().createInstance().getData();
	    	data.setDuration(duration);
	    	data.setPercent(percent);
	    	data.setValue(value);
//	    	data = new CharmVitals(value, duration, percent);
	    	String handStr = ByteBufUtils.readUTF8String(buf);
	    	if (!handStr.isEmpty()) {
	    		this.hand = EnumHand.valueOf(handStr);
	    	}
	    	this.slot = buf.readInt();
	      } catch (RuntimeException e) {
	        Treasure.logger.error("Exception while reading CharmMessageToClient: ", e);
	        return;
	      }
	      messageIsValid = true;
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
	    if (!messageIsValid) {
	    	return;
	    }
	    ByteBufUtils.writeUTF8String(buf, playerName);
	    ByteBufUtils.writeUTF8String(buf, charmName);
	    buf.writeDouble(data.getValue());
	    buf.writeInt(data.getDuration());
	    buf.writeDouble(data.getPercent());
	    String handStr = "";
	    if (hand != null) {
	    	handStr = hand.name();
	    }
	    ByteBufUtils.writeUTF8String(buf, handStr);	    
	    buf.writeInt(this.slot);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMessageValid() {
		if (charmName != null && playerName != null && data != null) {
			return true;
		}
		return false;
	}

	public boolean isMessageIsValid() {
		return messageIsValid;
	}

	public void setMessageIsValid(boolean messageIsValid) {
		this.messageIsValid = messageIsValid;
	}

	public ICharmData getData() {
		return data;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public EnumHand getHand() {
		return hand;
	}

	public Integer getSlot() {
		return slot;
	}

	public String getCharmName() {
		return charmName;
	}

	@Override
	public String toString() {
		return "CharmMessageToClient [messageIsValid=" + messageIsValid + ", charmName=" + charmName + ", data="
				+ data + ", playerName=" + playerName + ", hand=" + hand + ", slot=" + slot + "]";
	}
}
