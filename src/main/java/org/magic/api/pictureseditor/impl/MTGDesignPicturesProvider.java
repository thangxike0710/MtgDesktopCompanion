package org.magic.api.pictureseditor.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.abstracts.AbstractPicturesEditorProvider;
import org.magic.game.model.abilities.LoyaltyAbilities;
import org.magic.game.model.factories.AbilitiesFactory;
import org.magic.services.MTGConstants;
import org.magic.tools.ColorParser;
import org.magic.tools.URLTools;

//TODO using URLToolClient instead httpClient apache
public class MTGDesignPicturesProvider extends AbstractPicturesEditorProvider{

	private static final String ACCENT = "ACCENT";
	private static final String CENTER = "CENTER";
	private static final String INDICATOR = "INDICATOR";
	private static final String CARD_ACCENT = "card-accent";
	private static final String CARD_TEMPLATE = "card-template";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String DESIGNER ="designer";
	private static final String BASE_URI="https://mtg.design";
	
	private BasicHttpContext httpContext;
	private BasicCookieStore cookieStore;
	private HttpClient httpclient;
	
	public MTGDesignPicturesProvider() throws IOException {
		super();
		cookieStore = new BasicCookieStore();
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	}
	
	@Override
	public MOD getMode() {
		return MOD.URI;
	}
	
	
	private void connect() throws IOException {
		String u = BASE_URI+"/login";
		httpclient = HttpClients.custom().setUserAgent(MTGConstants.USER_AGENT).setRedirectStrategy(new LaxRedirectStrategy()).build();

		HttpEntity p = httpclient.execute(new HttpGet(u), httpContext).getEntity();
		String token = URLTools.toHtml(EntityUtils.toString(p)).select("input[name=_token]").first().attr("value");
		HttpPost login = new HttpPost(u);
		List<NameValuePair> nvps = new ArrayList<>();
							nvps.add(new BasicNameValuePair("email", getString("LOGIN")));
							nvps.add(new BasicNameValuePair("password", getString("PASS")));
							nvps.add(new BasicNameValuePair("remember", "on"));
							nvps.add(new BasicNameValuePair("_token", token));
		
		login.setEntity(new UrlEncodedFormEntity(nvps));
		login.addHeader("Referer", u);
		login.addHeader("Upgrade-Insecure-Requests", "1");
		login.addHeader("Origin", BASE_URI);
		
		HttpResponse resp = httpclient.execute(login, httpContext);
		
		logger.debug("Connection : " +  resp.getStatusLine().getReasonPhrase());
		
	}


	@Override
	public STATUT getStatut() {
		return STATUT.BETA;
	}
	
	@Override
	public BufferedImage getPicture(MagicCard mc, MagicEdition me) throws IOException {
		if(cookieStore.getCookies().isEmpty())
			connect();

		
		URIBuilder build = new URIBuilder();
		build.setScheme("https").setHost("mtg.design").setPath("render");
		

		if(me!=null)
		{
			build.addParameter("card-number", !mc.getNumber().isEmpty()?mc.getNumber():"1");
			build.addParameter("card-total", String.valueOf(me.getCardCount()));
			build.addParameter("card-set", me.getId());
			build.addParameter("language", "EN");
			build.addParameter("card-border", me.getBorder().toLowerCase());
		}
		else
		{
			build.addParameter("card-number", "1");
			build.addParameter("card-total", "1");
			build.addParameter("card-set", "MTG");
			build.addParameter("language", "EN");
			build.addParameter("card-border", "black");
		}
		build.addParameter("card-title", mc.getName());
		build.addParameter("mana-cost", mc.getCost());
		
		if(!mc.getSupertypes().isEmpty())
			build.addParameter("super-type", String.join(" ", mc.getSupertypes()));
		
		if(!mc.getTypes().isEmpty() && !mc.getSubtypes().isEmpty())
			build.addParameter("type", String.join(" ", mc.getTypes()) + " - "+ String.join(" ", mc.getSubtypes()));
		else
			build.addParameter("type", String.join(" ", mc.getTypes()));

		build.addParameter("text-size", getString("SIZE"));
		
		if(!mc.getRarity().isEmpty())
			build.addParameter("rarity", mc.getRarity().substring(0,1).toUpperCase());
		else
			build.addParameter("rarity", "C");

		if(!mc.getArtist().isEmpty())
			build.addParameter("artist", mc.getArtist());

		if(!mc.getPower().isEmpty())
			build.addParameter("power", mc.getPower());
	
		if(!mc.getToughness().isEmpty())
			build.addParameter("toughness", mc.getToughness());
		
		if(mc.getLoyalty()!=null)
			build.addParameter("loyalty", String.valueOf(mc.getLoyalty()));
		
		if(mc.getImageName()!=null && mc.getImageName().startsWith("http"))
			build.addParameter("artwork", mc.getImageName());
		
		build.addParameter(DESIGNER, getString(DESIGNER));
		
		if(mc.isLand() && !getString(ACCENT).isEmpty())		
			build.addParameter("land-overlay", getString(ACCENT));
		else
			build.addParameter("land-overlay", "C");
		
		build.addParameter("watermark", "0");
		build.addParameter("set-symbol", "0");
		build.addParameter("centered", getString(CENTER));
		
		if(getBoolean("FOIL"))
			build.addParameter("foil", TRUE);
		
		build.addParameter("lighten", FALSE);
		
		
		build.addParameter("card-layout", "regular");

		
		if(!mc.getText().isEmpty())
		{
			if(!mc.isPlaneswalker())
			{
				build.addParameter("rules-text", mc.getText());
			}
			else
			{
				
				List<LoyaltyAbilities> abs = AbilitiesFactory.getInstance().getLoyaltyAbilities(mc);
				if(abs.size()>3)
				{
					logger.error(getName() + " can't generate 4 abitilities planeswalker. removing last ability");
					abs.remove(abs.size()-1);
				}
				
				build.addParameter("pw-size", String.valueOf(abs.size()));
				for(int i=0;i<abs.size();i++)
					build.addParameter( (i==0)?"rules-text":"pw-text"+(i+1), abs.get(i).getCost()+": "+ abs.get(i).getEffect() +'\u00a0');
			}
		}
		
		if(!mc.getFlavor().isEmpty())
			build.addParameter("flavor-text", mc.getFlavor());
		
		if(mc.getColors().size()==1)
		{
			build.addParameter(CARD_TEMPLATE, ColorParser.getCodeByName(mc.getColors(),false).substring(0, 1));
			build.addParameter(CARD_ACCENT, ColorParser.getCodeByName(mc.getColors(),false).substring(0, 1));
		}
		else if(mc.getColors().size()==2)
		{
			build.addParameter(CARD_TEMPLATE, ColorParser.getCodeByName(mc.getColors(),false).substring(0, 2));
			build.addParameter(CARD_ACCENT, ColorParser.getCodeByName(mc.getColors(),false).substring(0, 2));
		}
		else if(mc.getColors().size()>2)
		{
			build.addParameter(CARD_TEMPLATE, "Gld");
			build.addParameter(CARD_ACCENT, "Gld");
		}
		else
		{
			build.addParameter(CARD_TEMPLATE, "C");
			build.addParameter(CARD_ACCENT, "C");
		}

		if(getBoolean(INDICATOR))
		{
			try {
			if(mc.getColors().size()==1)
				build.addParameter("color-indicator",ColorParser.getCodeByName(mc.getColors(),false).substring(0, 1));
			else
				build.addParameter("color-indicator",ColorParser.getCodeByName(mc.getColors(),false).substring(0, 2));
			}
			catch(Exception e)
			{
				logger.error(e);
			}
		}
		
		
		build.addParameter("edit", FALSE);

		try {
			logger.debug("generate " + build.build());
			HttpGet get = new HttpGet(build.build());
			HttpResponse resp = httpclient.execute(get, httpContext);
			logger.debug("generate " + resp.getStatusLine().getReasonPhrase());
			BufferedImage im = ImageIO.read(resp.getEntity().getContent());
			EntityUtils.consume(resp.getEntity());
			return im;
		} catch (Exception e) {
			logger.error("error generate : ",e);
			return null;
		}
		
		
		
	}

	@Override
	public String getName() {
		return "MtgDesign";
	}
	
	@Override
	public void initDefault() {
		setProperty("LOGIN", "");
		setProperty("PASS", "");
		setProperty("FOIL", FALSE);
		setProperty(CENTER, "yes");
		setProperty("SIZE", "36");
		setProperty(INDICATOR,"yes");
		setProperty(DESIGNER,System.getProperty("user.name"));
	}


	@Override
	public void setFoil(Boolean b) {
		setProperty("FOIL", String.valueOf(b));
		
	}


	@Override
	public void setTextSize(int size) {
		setProperty("SIZE", String.valueOf(size));
		
	}


	@Override
	public void setCenter(boolean center) {
		setProperty(CENTER, String.valueOf(center));
		
	}

	@Override
	public void setColorIndicator(boolean selected) {
		setProperty(INDICATOR, String.valueOf(selected));
		
	}


	@Override
	public void setColorAccentuation(String c) {
		setProperty(ACCENT, c);
		
	}

}
