package org.magic.api.exports.impl;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicDeck;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MTGCardsProvider;
import org.magic.api.interfaces.abstracts.AbstractCardExport;
import org.magic.services.MTGControler;
import org.magic.tools.FilesTools;

public class MTGArenaExport extends AbstractCardExport {
	
	Map<String,String> correpondance;
	
	
	public MTGArenaExport() {
		correpondance = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		correpondance.put("DOM", "DAR");
	}
	
	@Override
	public String getFileExtension() {
		return ".mtgarena";
	}

	@Override
	public void exportDeck(MagicDeck deck, File dest) throws IOException {
		
		StringBuilder temp = new StringBuilder();
		
		for(Map.Entry<MagicCard, Integer> entry : deck.getMap().entrySet())
		{
			temp.append(entry.getValue())
				.append(" ")
				.append(entry.getKey())
				.append(" (")
				.append(translate(entry.getKey().getCurrentSet().getId()).toUpperCase())
				.append(")")
				.append(" ")
				.append(entry.getKey().getCurrentSet().getNumber())
				.append("\r\n");
			notify(entry.getKey());
			
		}
		
		if(!deck.getMapSideBoard().isEmpty())
			for(Map.Entry<MagicCard, Integer> entry : deck.getMapSideBoard().entrySet())
			{
				temp.append("\r\n")
					.append(entry.getValue())
					.append(" ")
					.append(entry.getKey())
					.append(" (")
					.append(translate(entry.getKey().getCurrentSet().getId()).toUpperCase())
					.append(")")
					.append(" ")
					.append(entry.getKey().getCurrentSet().getNumber());
				notify(entry.getKey());
			}

		FilesTools.saveFile(dest, temp.toString());
		
		
		StringSelection selection = new StringSelection(temp.toString());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
		
		logger.debug("saved in clipboard");

	}
	

	private String reverse(String s) {
		
		if(correpondance.containsValue(s))
			for(Entry<String, String> k : correpondance.entrySet())
				if(k.getValue().equalsIgnoreCase(s))
					return k.getKey();
		
		return s;
	}
	
	
	private String translate(String s) {
		
		if(correpondance.get(s)!=null)
			return correpondance.get(s);
		else
			return s;
	}

	@Override
	public MagicDeck importDeck(String f,String dname) throws IOException {
		try (BufferedReader read = new BufferedReader(new StringReader(f))) {
			MagicDeck deck = new MagicDeck();
			deck.setName(dname);
			String line = read.readLine();

			boolean side=false;
			
			while (line != null) {
				if(line.length()==0)
				{
					side=true;
				}
				else
				{
				
					int qte = Integer.parseInt(line.substring(0, line.indexOf(' ')));
					String name = line.substring(line.indexOf(' '), line.indexOf('('));
					String ed =  reverse(line.substring( line.indexOf('(')+1,line.indexOf(')')));
					MagicEdition me = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).getSetById(ed);
				
					MagicCard mc = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).searchCardByName( name.trim(), me, true).get(0);
					notify(mc);
					if(!side)
						deck.getMap().put(mc, qte);
					else
						deck.getMapSideBoard().put(MTGControler.getInstance().getEnabled(MTGCardsProvider.class).searchCardByName( name.trim(), me, true).get(0), qte);
					
				}
				
					line = read.readLine();
			}
			return deck;
		}
	}


	@Override
	public String getName() {
		return "MTGArena";
	}

	@Override
	public STATUT getStatut() {
		return STATUT.DEV;
	}
	

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj ==null)
			return false;
		
		return hashCode()==obj.hashCode();
	}
	


}
