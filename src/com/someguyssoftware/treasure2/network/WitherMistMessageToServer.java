/**
 * 
 */
package com.someguyssoftware.treasure2.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author Mark Gottschling on Feb 17, 2020
 *
 */
public class WitherMistMessageToServer implements IMessage {
	private boolean messageIsValid;
	private String playerName;

	/**
	 * 
	 * @param playerName
	 */
	public WitherMistMessageToServer(String playerName) {
		messageIsValid = true;
		this.playerName = playerName;
	}

	public WitherMistMessageToServer() {
		messageIsValid = false;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.playerName = ByteBufUtils.readUTF8String(buf);

			// these methods may also be of use for your code:
			// for Itemstacks - ByteBufUtils.readItemStack()
			// for NBT tags ByteBufUtils.readTag();
			// for Strings: ByteBufUtils.readUTF8String();

		} catch (IndexOutOfBoundsException e) {
			System.err.println("Exception while reading WitherMistMessageToServer: " + e);
			return;
		}
		messageIsValid = true;

	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageIsValid) {
			return;
		}
		ByteBufUtils.writeUTF8String(buf, this.playerName);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMessageValid() {
		if (playerName != null && !playerName.equals("")) {
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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerID(String playerName) {
		this.playerName = playerName;
	}

}
