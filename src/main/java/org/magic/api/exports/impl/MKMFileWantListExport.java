package org.magic.api.exports.impl;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicDeck;
import org.magic.api.interfaces.abstracts.AbstractFormattedFileCardExport;
import org.magic.tools.FileTools;

public class MKMFileWantListExport extends AbstractFormattedFileCardExport {

	@Override
	public MagicDeck importDeck(String f,String dname) throws IOException {

		MagicDeck deck = new MagicDeck();
		deck.setName(dname);
		
		matches(f,true).forEach(m->{
			Integer qty = Integer.parseInt(m.group(1));
			MagicCard mc = parseMatcherWithGroup(m, 2, 3, false, FORMAT_SEARCH.NAME,FORMAT_SEARCH.NAME);
			if(mc!=null)
			{
				deck.getMap().put(mc, qty);
				notify(mc);
			}
		});
		return deck;

	}


	@Override
	public void exportDeck(MagicDeck deck, File dest) throws IOException {

			StringBuilder temp = new StringBuilder();
			
			for (MagicCard mc : deck.getMap().keySet()) {
				if (mc.getCurrentSet().getMkmName() != null)
					temp.append(deck.getMap().get(mc)).append(getSeparator()).append(mc.getName()).append(getSeparator()).append("(").append(mc.getCurrentSet().getMkmName()).append(")\n");
				else
					temp.append(deck.getMapSideBoard().get(mc)).append(getSeparator()).append(mc.getName()).append(getSeparator()).append("(").append(mc.getCurrentSet().getSet()).append(")\n");
				notify(mc);
			}
			
			for (MagicCard mc : deck.getMapSideBoard().keySet()) 
			{
				if (mc.getCurrentSet().getMkmName() != null)
					temp.append(deck.getMapSideBoard().get(mc)).append(getSeparator()).append(mc.getName()).append(getSeparator()).append("(").append(mc.getCurrentSet().getMkmName()).append(")\n");
				else
					temp.append(deck.getMapSideBoard().get(mc)).append(getSeparator()).append(mc.getName()).append(getSeparator()).append("(").append(mc.getCurrentSet().getSet()).append(")\n");
				notify(mc);
			}
			FileTools.saveFile(dest, temp.toString());
		
	}

	

	@Override
	public String getFileExtension() {
		return ".txt";
	}
	
	
	@Override
	public String getName() {
		return "MKM File WantList";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(MKMFileWantListExport.class.getResource("/icons/plugins/magiccardmarket.png"));
	}

	@Override
	protected boolean skipFirstLine() {
		return false;
	}

	@Override
	protected String[] skipLinesStartWith() {
		return new String[0];
	}

	@Override
	protected String getStringPattern() {
		return "(\\d+)"+getSeparator()+"(.*?)"+getSeparator()+"\\((.*?)\\)";
	}

	@Override
	protected String getSeparator() {
		return " ";
	}

}
