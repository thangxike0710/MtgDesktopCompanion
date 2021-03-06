package org.magic.gui.dashlet;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.magic.api.beans.CardShake;
import org.magic.api.beans.EditionsShakers;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MTGCardsProvider;
import org.magic.api.interfaces.MTGDashBoard;
import org.magic.api.interfaces.abstracts.AbstractJDashlet;
import org.magic.gui.abstracts.AbstractBuzyIndicatorComponent;
import org.magic.gui.models.CardShakerTableModel;
import org.magic.gui.renderer.CardShakeRenderer;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.services.threads.ThreadManager;
import org.magic.tools.UITools;

public class EditionsDashlet extends AbstractJDashlet {

	private static final long serialVersionUID = 1L;
	private JXTable table;
	private AbstractBuzyIndicatorComponent lblLoading;
	private JComboBox<MagicEdition> cboEditions;
	private CardShakerTableModel modEdition;

	@Override
	public Icon getIcon() {
		return MTGConstants.ICON_EURO;
	}
	
	
	public void initGUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		modEdition = new CardShakerTableModel();
		cboEditions = UITools.createComboboxEditions();
		panel.add(cboEditions);
		lblLoading = AbstractBuzyIndicatorComponent.createLabelComponent();
		panel.add(lblLoading);
		table = new JXTable(modEdition);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

		table.getColumnModel().getColumn(3).setCellRenderer(new CardShakeRenderer());
		table.getColumnModel().getColumn(5).setCellRenderer(new CardShakeRenderer());

		

		cboEditions.addItemListener(ie -> {
			
			if(ie.getStateChange()==ItemEvent.SELECTED)
				init();	
		});

		if (getProperties().size() > 0) {
			Rectangle r = new Rectangle((int) Double.parseDouble(getString("x")),
					(int) Double.parseDouble(getString("y")), (int) Double.parseDouble(getString("w")),
					(int) Double.parseDouble(getString("h")));

			MagicEdition ed;
			try {
				ed = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).getSetById(getString("EDITION"));
				cboEditions.setSelectedItem(ed);
			} catch (Exception e) {
				logger.error("Error retrieve editions", e);
			}

			setBounds(r);
		}

		try {
			table.packAll();
		} catch (Exception e) {
			// do nothing
		}
		
		UITools.initTableFilter(table);
		UITools.initCardToolTipTable(table, 0, 1, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				try {
					CardShake cs = UITools.getTableSelection(table, 0);
					Desktop.getDesktop().browse(new URI(cs.getLink()));
				
				}catch(Exception ex)
				{
					logger.error("error", ex);
				}
				return null;
			}
		});
	}

	@Override
	public String getName() {
		return "Editions Prices";
	}

	@Override
	public void init() {

		if (cboEditions.getSelectedItem() != null)
		{	
			lblLoading.start();
			SwingWorker<EditionsShakers, Void> sw = new SwingWorker<>()
			{
				@Override
				protected EditionsShakers doInBackground(){
					MagicEdition ed = (MagicEdition) cboEditions.getSelectedItem();
					setProperty("EDITION", ed.getId());
					try {
						return MTGControler.getInstance().getEnabled(MTGDashBoard.class).getShakesForEdition(ed);
					} catch (IOException e) {
						logger.error(e);
						return new EditionsShakers();
					}
				}
				
				@Override
				protected void done() {
					try {
						modEdition.init(get().getShakes());
						table.packAll();
						table.setRowSorter(new TableRowSorter<>(modEdition));
						
					} catch (Exception e) {
						logger.error("error parsing",e);
					}
					lblLoading.end();
				}
				
				
			};
			
			ThreadManager.getInstance().runInEdt(sw,"loading " + cboEditions.getSelectedItem() + " in editionDashlet");
		}

	}

}
