package org.magic.game.gui.components;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.magic.game.model.ManaPool;
import org.magic.game.model.Player;
import org.magic.gui.components.ManaPanel;
import org.utils.patterns.observer.Observable;
import org.utils.patterns.observer.Observer;

public class ManaPoolPanel extends JPanel implements Observer {
	private JSpinner spinW;
	private JSpinner spinU;
	private JSpinner spinB;
	private JSpinner spinR;
	private JSpinner spinG;
	private JSpinner spinC;
	private JSpinner spinE;

	private Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void update(Observable o, Object arg) {
		ManaPool p = (ManaPool) arg;
		init(p);

	}

	public ManaPoolPanel() {

		spinW = new JSpinner();
		spinW.addChangeListener(ce -> player.setMana("{B}", (int) spinW.getValue()));
		setLayout(new GridLayout(0, 2, 0, 0));

		ManaPanel panelW = new ManaPanel();
		FlowLayout flowLayout5 = (FlowLayout) panelW.getLayout();
		flowLayout5.setAlignment(FlowLayout.CENTER);
		panelW.setManaCost("{W}");
		add(panelW);
		spinW.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinW);

		ManaPanel panelU = new ManaPanel();
		FlowLayout flowLayout2 = (FlowLayout) panelU.getLayout();
		flowLayout2.setAlignment(FlowLayout.CENTER);
		panelU.setManaCost("{U}");
		add(panelU);

		spinU = new JSpinner();
		spinU.addChangeListener(ce -> player.setMana("{U}", (int) spinU.getValue()));

		spinU.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinU);

		ManaPanel panelB = new ManaPanel();
		FlowLayout flowLayout4 = (FlowLayout) panelB.getLayout();
		flowLayout4.setAlignment(FlowLayout.CENTER);
		panelB.setManaCost("{B}");
		add(panelB);

		spinB = new JSpinner();
		spinB.addChangeListener(ce -> player.setMana("{B}", (int) spinB.getValue()));

		spinB.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinB);

		ManaPanel panelR = new ManaPanel();
		FlowLayout flowLayout1 = (FlowLayout) panelR.getLayout();
		flowLayout1.setAlignment(FlowLayout.CENTER);
		panelR.setManaCost("{R}");
		add(panelR);

		spinR = new JSpinner();
		spinR.addChangeListener(ce -> player.setMana("{R}", (int) spinR.getValue()));

		spinR.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinR);

		ManaPanel panelG = new ManaPanel();
		FlowLayout flowLayout3 = (FlowLayout) panelG.getLayout();
		flowLayout3.setAlignment(FlowLayout.CENTER);
		panelG.setManaCost("{G}");
		add(panelG);

		spinG = new JSpinner();
		spinG.addChangeListener(ce -> player.setMana("{G}", (int) spinG.getValue()));

		spinG.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinG);

		ManaPanel panelC = new ManaPanel();
		FlowLayout flowLayout = (FlowLayout) panelC.getLayout();
		flowLayout.setAlignment(FlowLayout.CENTER);
		panelC.setManaCost("{C}");
		add(panelC);

		spinC = new JSpinner();
		spinC.addChangeListener(ce -> player.setMana("{C}", (int) spinC.getValue()));
		spinC.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinC);

		ManaPanel panelE = new ManaPanel();
		FlowLayout flowLayout6 = (FlowLayout) panelE.getLayout();
		flowLayout6.setAlignment(FlowLayout.CENTER);
		add(panelE);

		spinE = new JSpinner();
		panelE.setManaCost("{E}");

		spinE.addChangeListener(ce -> player.setMana("{E}", (int) spinE.getValue())

		);
		spinE.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinE);

	}

	public JSpinner getSpinW() {
		return spinW;
	}

	public JSpinner getSpinU() {
		return spinU;
	}

	public JSpinner getSpinB() {
		return spinB;
	}

	public JSpinner getSpinR() {
		return spinR;
	}

	public JSpinner getSpinG() {
		return spinG;
	}

	public JSpinner getSpinC() {
		return spinC;
	}

	public JSpinner getSpinE() {
		return spinE;
	}

	public void clean() {
		GamePanelGUI.getInstance().getPlayer().getManaPool().clean();
		init(GamePanelGUI.getInstance().getPlayer().getManaPool());
	}

	public void init(ManaPool p) {
		spinW.setValue(p.getMana("{W}"));
		spinU.setValue(p.getMana("{U}"));
		spinB.setValue(p.getMana("{B}"));
		spinR.setValue(p.getMana("{R}"));
		spinG.setValue(p.getMana("{G}"));
		spinC.setValue(p.getMana("{C}"));
		spinE.setValue(p.getMana("{E}"));

	}

}
