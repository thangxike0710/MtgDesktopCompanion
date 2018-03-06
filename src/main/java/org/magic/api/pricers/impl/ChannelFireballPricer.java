package org.magic.api.pricers.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.MagicPrice;
import org.magic.api.interfaces.MTGCardsProvider.STATUT;
import org.magic.api.interfaces.abstracts.AbstractMagicPricesProvider;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class ChannelFireballPricer extends AbstractMagicPricesProvider {
	@Override
	public STATUT getStatut() {
		return STATUT.BETA;
	}
	
	
	public ChannelFireballPricer() {
		super();
	

	}

	@Override
	public List<MagicPrice> getPrice(MagicEdition me, MagicCard card) throws IOException {
	
		
		String keyword=card.getName();
		String url = getString("URL");
		
		
		keyword=URLEncoder.encode(keyword,getString("ENCODING"));
		
		setProperty("KEYWORD", keyword);
		
		if(me!=null)
			keyword += "&setname=" + URLEncoder.encode(me.getSet(),getString("ENCODING"));
		
		
		String link=url.replaceAll("%CARDNAME%", keyword);
		
		
		logger.info(getName()+ " Looking for price " + link);
		JsonReader reader = new JsonReader(new InputStreamReader(new URL(link).openStream(), getString("ENCODING")));
		JsonElement root = new JsonParser().parse(reader);
		
		String value = root.getAsJsonArray().get(0).getAsString();
		
		MagicPrice mp = new MagicPrice();
			mp.setUrl("http://store.channelfireball.com/products/search?query="+URLEncoder.encode(card.getName(),getString("ENCODING")));
			mp.setSite(getName());
			mp.setCurrency(value.substring(0, 1));
			mp.setValue(Double.parseDouble(value.substring(1).replaceAll(",", "")));
			
			
		ArrayList<MagicPrice> list = new ArrayList<>();
							list.add(mp);
							
							
		logger.info(getName() +" found " + list.size() +" item(s)" );
							
		return list;
	}

	@Override
	public String getName() {
		return "Channel Fireball";
	}

	@Override
	public void alertDetected(List<MagicPrice> p) {
		// do nothing
		
	}


	@Override
	public void initDefault() {
		setProperty("MAX", "5");
		setProperty("URL", "http://magictcgprices.appspot.com/api/cfb/price.json?cardname=%CARDNAME%");
		setProperty("WEBSITE", "http://store.channelfireball.com/");
		setProperty("ENCODING", "UTF-8");
		setProperty("KEYWORD", "");
		
	}
	
	
	
}
