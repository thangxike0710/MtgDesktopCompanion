package org.magic.api.interfaces;

import java.io.IOException;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;

import org.magic.api.beans.CardDominance;
import org.magic.api.beans.CardShake;
import org.magic.api.beans.EditionsShakers;
import org.magic.api.beans.HistoryPrice;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.MagicFormat;
import org.magic.api.beans.Packaging;

public interface MTGDashBoard extends MTGPlugin {

	public List<CardShake> getShakerFor(MagicFormat.FORMATS gameFormat) throws IOException;
	
	public EditionsShakers getShakesForEdition(MagicEdition edition) throws IOException;

	public HistoryPrice<MagicCard> getPriceVariation(MagicCard mc, MagicEdition me) throws IOException;

	public HistoryPrice<Packaging> getPriceVariation(Packaging packaging) throws IOException;
	
	public List<CardDominance> getBestCards(MagicFormat.FORMATS f, String filter) throws IOException;

	public Date getUpdatedDate();

	public String[] getDominanceFilters();
	
	public Icon getIcon();
	
	public Currency getCurrency();
	

}
