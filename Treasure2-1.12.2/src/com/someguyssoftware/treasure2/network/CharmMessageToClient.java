/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import com.someguyssoftware.treasure2.item.charm.CharmStateFactory;
import com.someguyssoftware.treasure2.item.charm.CharmVitals;
import com.someguyssoftware.treasure2.item.charm.ICharmState;
import com.someguyssoftware.treasure2.item.charm.ICharmVitals;
import com.someguyssoftware.treasure2.item.charm.TreasureCharms;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public class CharmMessageToClient implements IMessage {
	private boolean messageIsValid;
	private String charmName;
	private ICharmVitals vitals;
	// location properties of charm(s) - who, what hand or what pouch slot
	private String playerName;
	private EnumHand hand;
	private Integer slot;
	
	/**
	 * 
	 * @param playerName
	 */
	public CharmMessageToClient(String playerName, ICharmState state, EnumHand hand, Integer slot) {
		messageIsValid = true;
		this.playerName = playerName;
		this.charmName = state.getCharm().getName();
		this.vitals = state.getVitals();
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
	    	vitals = CharmStateFactory.createCharmVitals(TreasureCharms.REGISTRY.get(charmName));
	    	vitals.setDuration(duration);
	    	vitals.setPercent(percent);
	    	vitals.setValue(value);
//	    	vitals = new CharmVitals(value, duration, percent);
	    	String handStr = ByteBufUtils.readUTF8String(buf);
	    	if (!handStr.isEmpty()) {
	    		this.hand = EnumHand.valueOf(handStr);
	    	}
	    	this.slot = buf.readInt();
	      } catch (RuntimeException e) {
	        System.err.println("Exception while reading PoisonMistMessageToServer: " + e);
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
	    buf.writeDouble(vitals.getValue());
	    buf.writeInt(vitals.getDuration());
	    buf.writeDouble(vitals.getPercent());
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
		if (charmName != null && playerName != null && vitals != null) {
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

	public ICharmVitals getVitals() {
		return vitals;
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
		return "CharmMessageToClient [messageIsValid=" + messageIsValid + ", charmName=" + charmName + ", vitals="
				+ vitals + ", playerName=" + playerName + ", hand=" + hand + ", slot=" + slot + "]";
	}
}
