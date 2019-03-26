package org.magic.gui.components.dialog;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;
import org.magic.api.beans.MagicDeck;
import org.magic.api.interfaces.MTGCardsExport;
import org.magic.api.interfaces.MTGCardsIndexer;
import org.magic.gui.abstracts.AbstractBuzyIndicatorComponent;
import org.magic.gui.abstracts.AbstractDelegatedImporter;
import org.magic.gui.components.editor.JTagsPanel;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;
import org.magic.services.ThreadManager;
import org.magic.services.workers.DeckImportWorker;

public class ManualImportDialog extends AbstractDelegatedImporter {

	
	private static final long serialVersionUID = 1L;
	private JTextPane editorPane;
	private JTagsPanel tagsPanel;
	private transient Logger logger = MTGLogger.getLogger(this.getClass());
	private int start;
	private int position;
	private AbstractBuzyIndicatorComponent lblLoading;
	private MagicDeck importedDeck;
	
	
	
	public String getStringDeck() {
		return editorPane.getText();
	}

	public ManualImportDialog() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(400, 400));
		setTitle(MTGControler.getInstance().getLangService().getCapitalize("MANUAL_IMPORT"));
		setIconImage(MTGConstants.ICON_TAB_IMPORT.getImage());
		setModal(true);
		JPanel panel = new JPanel();
		tagsPanel = new JTagsPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		JButton btnImport = new JButton(MTGConstants.ICON_SAVE);
		btnImport.setToolTipText(MTGControler.getInstance().getLangService().getCapitalize("IMPORT"));
		panel.add(btnImport);

		JButton btnCancel = new JButton(MTGConstants.ICON_CANCEL);
		btnCancel.setToolTipText(MTGControler.getInstance().getLangService().getCapitalize("CANCEL"));
		btnCancel.addActionListener(e -> {
			editorPane.setText("");
			dispose();
		});
		panel.add(btnCancel);
		
		lblLoading = AbstractBuzyIndicatorComponent.createLabelComponent();
		panel.add(lblLoading);

		JLabel lblPastYourDeck = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("IMPORT_HELP"));
		
		getContentPane().add(lblPastYourDeck, BorderLayout.NORTH);
		
		JPanel panelCenter = new JPanel();
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		editorPane = new JTextPane();
		
		editorPane.setPreferredSize(new Dimension(106, 300));
		panelCenter.add(new JScrollPane(editorPane));
		panelCenter.add(tagsPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		
		tagsPanel.setEditable(false);
		tagsPanel.setFontSize(10);
		tagsPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = tagsPanel.getTagAt(e.getPoint());
				try {
					editorPane.getDocument().remove(start,position-start);
					editorPane.getDocument().insertString(start, " "+name, null);
					editorPane.requestFocus();
					Robot r = new Robot();
					r.keyPress(KeyEvent.VK_ENTER);
					start=0;
					
				} catch (BadLocationException e1) {
					logger.error("error editing at s:" +start +" e:"+(position-start),e1);
				} catch (AWTException e1) {
					logger.error("Error loading key enter",e1);
				}
				
			} 
		});
		
		
		editorPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent ke) {
				position = editorPane.getCaretPosition();
				try {
					
					if(ke.getKeyCode()==KeyEvent.VK_SPACE && start<=0)
						start=position-1;
					
					if(ke.getKeyCode()==KeyEvent.VK_ENTER)
						start=0;
					
					
					
					if(start>=0)
					{
						String currentName=editorPane.getText(start, (position-start)).trim();
						if(currentName.length()>=4)
							tagsPanel.bind(MTGControler.getInstance().getEnabled(MTGCardsIndexer.class).suggestCardName(currentName));
					
					}
				}
				catch(Exception e)
				{
					logger.error("error",e);
				}
				
			}
		});
		
		btnImport.addActionListener(e ->{
			
			DeckImportWorker sw = new DeckImportWorker(MTGControler.getInstance().getPlugin(MTGConstants.MANUAL_IMPORT_SYNTAX, MTGCardsExport.class), lblLoading,null)
										{
			
											@Override
											protected MagicDeck doInBackground() {
												
												try {
													importedDeck= exp.importDeck(editorPane.getText(),"manual");
												} catch (Exception e) {
													err=e;
													logger.error("error export with " + exp,e);
												}
												return importedDeck;
											}
											
											@Override
											protected void done()
											{
												super.done();
												dispose();
											}
											
										};
									lblLoading.start();
									ThreadManager.getInstance().runInEdt(sw);
			
			
			
			
		});

		
		
	}


	public MagicDeck getSelectedDeck() {

		return importedDeck;
	}

}
