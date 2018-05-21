package org.beta;

import java.io.IOException;

import org.magic.api.beans.MTGNotification;
import org.magic.api.beans.MTGNotification.FORMAT_NOTIFICATION;
import org.magic.api.interfaces.abstracts.AbstractMTGNotifier;

public class DiscordNotifier extends AbstractMTGNotifier {

	@Override
	public void send(MTGNotification notification) throws IOException {
		
	}

	@Override
	public FORMAT_NOTIFICATION getFormat() {
		return FORMAT_NOTIFICATION.TEXT;
	}

	@Override
	public String getName() {
		return "Discord";
	}
	
	@Override
	public void initDefault() {
		setProperty("BOT_ID", "");
		setProperty("TOKEN","");
		setProperty("CHANNELID", "");
	}

}