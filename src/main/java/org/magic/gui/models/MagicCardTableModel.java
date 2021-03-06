package org.magic.gui.models;

import java.util.List;

import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCardNames;
import org.magic.gui.abstracts.GenericTableModel;
import org.magic.services.MTGControler;

public class MagicCardTableModel extends GenericTableModel<MagicCard> {

	private static final long serialVersionUID = 1L;
	
	public MagicCardTableModel() {
		columns=new String[] { "NAME",
				"CARD_LANGUAGE",
				"CARD_MANA",
				"CARD_TYPES",
				"CARD_POWER",
				"CARD_RARITY",
				"CARD_EDITIONS",
				"CARD_NUMBER",
				"CARD_COLOR",
				"RL"};

	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0: return MagicCard.class;
			case 6: return List.class;
			case 8: return List.class;
			case 9: return Boolean.class;
			default:return String.class;
		}
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		try {
			MagicCard mc = items.get(row);
			switch (column) {
			case 0:
				return mc;
			case 1:
				return getName(mc.getForeignNames());
			case 2:
				return mc.getCost();
			case 3:
				return mc.getFullType();
			case 4:
				return powerorloyalty(mc);
			case 5:
				return (mc.getCurrentSet() != null) ? mc.getCurrentSet().getRarity() : "";
			case 6:
				return mc.getEditions();
			case 7:
				return (mc.getCurrentSet() != null) ? mc.getCurrentSet().getNumber() : "";
			case 8:
				return mc.getColors();
			case 9:
				return mc.isReserved();
			default:
				return mc;
			}
		} catch (Exception e) {
			return null;
		}

	}

	private String powerorloyalty(MagicCard mc) {
		
		if(contains(mc.getTypes(), "creature"))
			return mc.getPower() + "/" + mc.getToughness();
		else if(contains(mc.getTypes(), "planeswalker"))
			return String.valueOf(mc.getLoyalty());
		
		return "";
	}

	private String getName(List<MagicCardNames> foreignNames) {
		for (MagicCardNames name : foreignNames) {
			if (name.getLanguage().equals(MTGControler.getInstance().get("langage")))
				return name.getName();
		}
		return "";
	}

	private boolean contains(List<String> types, String string) {
		for (String s : types)
			if (s.equalsIgnoreCase(string))
				return true;

		return false;

	}

	
}
