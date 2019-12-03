package org.magic.gui;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTable;
import org.magic.api.beans.Packaging;
import org.magic.api.beans.SealedStock;
import org.magic.api.interfaces.MTGCardsProvider;
import org.magic.api.interfaces.MTGDao;
import org.magic.gui.abstracts.MTGUIComponent;
import org.magic.gui.components.PackagesBrowserPanel;
import org.magic.gui.models.SealedStockModel;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.tools.UITools;

public class SealedStockGUI extends MTGUIComponent {

	private static final long serialVersionUID = 1L;
	private PackagesBrowserPanel packagePanel;
	private SealedStockModel model;
	private Packaging selectedItem;
	
	public SealedStockGUI() {
		initGUI();
	}
	
	public static void main(String[] args) throws SQLException 
	{
		MTGControler.getInstance().getEnabled(MTGCardsProvider.class).init();
		MTGControler.getInstance().getEnabled(MTGDao.class).init();
		SealedStockGUI s = new SealedStockGUI();
		MTGUIComponent.createJDialog(s, true, false).setVisible(true);
		s.onFirstShowing();
	}
	
	private void initGUI() {
		model = new SealedStockModel();
		JXTable table = new JXTable(model);
		packagePanel = new PackagesBrowserPanel(false);
		JPanel toolsPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JTabbedPane panneauDetail = new JTabbedPane();
		
		
		JButton buttonNew = new JButton(MTGConstants.ICON_NEW);
		JButton buttonDelete = new JButton(MTGConstants.ICON_DELETE);
		JButton buttonUpdate = new JButton(MTGConstants.ICON_REFRESH);
		
		centerPanel.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		
		toolsPanel.add(buttonNew);
		toolsPanel.add(buttonDelete);
		toolsPanel.add(buttonUpdate);
		
		add(packagePanel,BorderLayout.WEST);
		centerPanel.add(new JScrollPane(table),BorderLayout.CENTER);
		centerPanel.add(panneauDetail,BorderLayout.SOUTH);
		add(centerPanel,BorderLayout.CENTER);
		add(toolsPanel,BorderLayout.NORTH);
		
		
		packagePanel.getTree().addTreeSelectionListener(e-> {
			
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)packagePanel.getTree().getLastSelectedPathComponent();
			if(selectedNode!=null && (selectedNode.getUserObject() instanceof Packaging))
			{
				selectedItem = (Packaging)selectedNode.getUserObject();
			}
		});
		
		buttonDelete.addActionListener(el->{
			SealedStock it = UITools.getTableSelection(table, 0);
			
			int res = JOptionPane.showConfirmDialog(null, MTGControler.getInstance().getLangService().get("CONFIRM_DELETE", it.getProduct()), MTGControler.getInstance().getLangService().get("DELETE"),JOptionPane.YES_NO_OPTION);
			
			if(res==JOptionPane.YES_OPTION)
			{
				try {
					MTGControler.getInstance().getEnabled(MTGDao.class).deleteStock(it);
					model.removeItem(it);
				} catch (SQLException e1) {
					MTGControler.getInstance().notify(e1);
				}
			}
			
			
		});
		
		buttonUpdate.addActionListener(el->{
			try {
				model.init(MTGControler.getInstance().getEnabled(MTGDao.class).listSeleadStocks());
			} catch (SQLException e1) {
				MTGControler.getInstance().notify(e1);
			}			
			
		});
		
		
		buttonNew.addActionListener(el->{
			try {
				
				SealedStock s = new SealedStock(selectedItem,1);
				MTGControler.getInstance().getEnabled(MTGDao.class).saveOrUpdateStock(s);
				model.addItem(s);
			} catch (SQLException e) {
				MTGControler.getInstance().notify(e);
			}
		});
	}

	@Override
	public String getTitle() {
		return MTGControler.getInstance().getLangService().getCapitalize("PACKAGES");
	}

	@Override
	public ImageIcon getIcon() {
		return MTGConstants.ICON_PACKAGE;
	}
	
	@Override
	public void onFirstShowing() {
		packagePanel.initTree();
		
		try {
			model.init(MTGControler.getInstance().getEnabled(MTGDao.class).listSeleadStocks());
		} catch (SQLException e) {
			MTGControler.getInstance().notify(e);
		}
	}
}