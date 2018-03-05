package org.magic.api.pictures.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MTGCardsProvider.STATUT;
import org.magic.api.interfaces.abstracts.AbstractPicturesProvider;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.tools.InstallCert;


public class MagicCardInfoPicturesProvider extends AbstractPicturesProvider {

	
	@Override
	public STATUT getStatut() {
		return STATUT.BETA;
	}
	
	
	private int w;
	private int h;
	
	public MagicCardInfoPicturesProvider() {
		super();
		
		try {
   			InstallCert.install("magiccards.info");
    		System.setProperty("javax.net.ssl.trustStore",new File(MTGConstants.CONF_DIR,MTGConstants.KEYSTORE_NAME).getAbsolutePath());
 		} catch (Exception e1) {
			logger.error(e1);
		}
		newW= Integer.parseInt(getProperty("CARD_SIZE_WIDTH"));
		newH= Integer.parseInt(getProperty("CARD_SIZE_HEIGHT"));
		 
		w=223;
		h=311;
	}
	
	@Override
	public BufferedImage getPicture(MagicCard mc,MagicEdition ed) throws IOException {

		if(MTGControler.getInstance().getEnabledCache().getPic(mc,ed)!=null)
		{
			return resizeCard(MTGControler.getInstance().getEnabledCache().getPic(mc,ed),newW,newH);
		}
	
		
		if(ed==null)
			ed=mc.getEditions().get(0);
		
		
		String infocode=ed.getMagicCardsInfoCode();
		
		if(infocode==null)
			infocode=mc.getEditions().get(0).getId().toLowerCase();
		
		URL url;
		//TODO change this function for other edition selection. mciNumber is on the card, not on the selected Edition
		if(mc.getMciNumber()!=null)
		{
			if(mc.getMciNumber().contains("/"))
			{
				String mcinumber=mc.getMciNumber().substring(mc.getMciNumber().lastIndexOf('/')).replaceAll(".html", "");
				url=new URL(getProperty("WEBSITE")+"/"+getProperty("LANG")+"/"+infocode+"/"+mcinumber+".jpg");
			}
			else	
			{
				url=new URL(getProperty("WEBSITE")+"/"+getProperty("LANG")+"/"+infocode+"/"+mc.getMciNumber()+".jpg");
			}
		}
		else
		{
			url=new URL(getProperty("WEBSITE")+"/"+getProperty("LANG")+"/"+infocode+"/"+mc.getEditions().get(0).getNumber().replaceAll("a", "").replaceAll("b", "")+".jpg");
		}
		
		logger.debug("Get card pic from " + url);

		URLConnection connection = url.openConnection();
					  connection.setRequestProperty("User-Agent", getProperty("USER_AGENT"));
					  connection.connect();
					  
		Image img = null;
				
					img = ImageIO.read(connection.getInputStream()).getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
					BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
					        BufferedImage.TYPE_INT_RGB);

					    Graphics g = bufferedImage.createGraphics();
					    g.drawImage(img, 0, 0, null);
					    g.dispose();

					MTGControler.getInstance().getEnabledCache().put(bufferedImage, mc,ed);
						 
					return resizeCard(bufferedImage,newW,newH) ;
	}


	@Override
	public BufferedImage getSetLogo(String set, String rarity) throws IOException {
		URL url = new URL("http://gatherer.wizards.com/Handlers/Image.ashx?type=symbol&set="+set+"&size=medium&rarity="+rarity.substring(0,1));
		return ImageIO.read(url);
	}


	@Override
	public String getName() {
		return "MagicCardInfo";
	}

	@Override
	public BufferedImage extractPicture(MagicCard mc) throws IOException {
		return getPicture(mc,null).getSubimage(15, 34, 184, 132);
	}

	@Override
	public void initDefault() {
		setProperty("WEBSITE", "https://magiccards.info/scans/");
		setProperty("LANG", "en");
		setProperty("CARD_SIZE_WIDTH", "223");
		setProperty("CARD_SIZE_HEIGHT", "310");
		setProperty("USER_AGENT",MTGConstants.USER_AGENT);
		
	}

}
