package org.magic.api.notifiers.impl;

import java.io.IOException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.magic.api.beans.MTGNotification;
import org.magic.api.beans.MTGNotification.FORMAT_NOTIFICATION;
import org.magic.api.interfaces.abstracts.AbstractMTGNotifier;

public class EmailNotifier extends AbstractMTGNotifier{

	@Override
	public FORMAT_NOTIFICATION getFormat() {
		return FORMAT_NOTIFICATION.HTML;
	}
	
	@Override
	public String getName() {
		return "Email";
	}

	

	@Override
	public void initDefault() {
		setProperty("SMTP", "smtp.server.com");
		setProperty("PORT", "25");
		setProperty("FROM", "me@server.com");
		setProperty("SEND_TO", "you@server.com");
		setProperty("SMTP_LOGIN", "login");
		setProperty("SMTP_PASS", "password");
		setProperty("SSL", "true");
	}


	@Override
	public void send(MTGNotification notification) throws IOException {
		HtmlEmail email;
		try {
			
			email = new HtmlEmail();
			email.setHtmlMsg("<html>"+notification.getMessage()+"</html>");
			email.setHostName(getString("SMTP"));
			email.setSmtpPort(getInt("PORT"));
			email.setAuthenticator(new DefaultAuthenticator(getString("SMTP_LOGIN"), getString("SMTP_PASS")));
			email.setSSLOnConnect(getBoolean("SSL"));
			email.setFrom(getString("FROM"));
			email.setSubject(notification.getTitle());
			email.setTextMsg(notification.getMessage());
			for(String to : getArray("SEND_TO"))
				email.addTo(to);
			email.send();
			
		}catch(EmailException ex)
		{
			throw new IOException(ex);
		}
		
	}

	
}
