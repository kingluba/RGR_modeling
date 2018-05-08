package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import process.Dispatcher;
import process.IModelFactory;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import widgets.ChooseData;
import widgets.ChooseRandom;
import widgets.Diagram;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.event.ChangeListener;

import model.TeamModel;

import javax.swing.event.ChangeEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import widgets.stat.StatisticsManager;
import widgets.experiments.ExperimentManager;
import widgets.parmFinder.ParmFinderView;
import widgets.trans.TransProcessManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TeamGUI extends JFrame {

	private static final long serialVersionUID = -1607946854541105786L;
	private JPanel contentPane;
	private JPanel panelL;
	private JPanel panelR;
	/**
	 * ChooseData number of teams
	 */
	private ChooseData ch_numOfTeams;
	/**
	 * Random time of developing project
	 */
	private ChooseRandom rnd_timeOfIngineering;
	/**
	 * Random number of developers in each team
	 */
	private ChooseRandom rnd_numOfDevels;
	/**
	 * ChooseData time of modeling 
	 */
	private ChooseData ch_timeOfModeling;
	/**
	 * The diagram of queue of customers
	 */
	private Diagram qCustomersD;
	/**
	 * ChooseData The probability of a man to become ill
	 */
	private ChooseData ch_illProb;
	/**
	 * ChooseData The probability of a failed test
	 */
	private ChooseData ch_probOfErrTest;
	private JPanel pane_bottom;
	/**
	 * CheckBox print to console
	 */
	private JCheckBox chckbxConsole;
	private JButton buttonTest;
	private Dispatcher dispatcher = null;
	/**
	 * The diagram of total score of a team ( succeed projects - failed projects )
	 */
	private Diagram TeamTotalD;
	private JLabel label;
	/**
	 * Slider to choose the number of teams to paint the score
	 */
	private JSlider sliderTeamNumToDiagram;

	//the count of team's total results to be drawn
	public int graphsToDraw = 1;
	/**
	 * ChooseData Max queue of customers size
	 */
	private ChooseData ch_MaxQ;
	private ChooseRandom rnd_numOfTesters;
	private ChooseRandom rnd_illTime;
	private ChooseRandom ch_customerHoldTime;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	private Diagram diagramTeamFail;
	private Diagram diagramCustomersWaiting;
	private StatisticsManager statisticsManager;
	private ExperimentManager experimentManager1;
	private ExperimentManager experimentManager2;
	private ExperimentManager experimentManager3;
	private JPanel panel;
	private TransProcessManager transProcessManager;
	private JButton btnParmfinder;

	public int getGraphsToDraw() {
		return graphsToDraw;
	}

	public static void main(String[] args) {
		TeamGUI application = new TeamGUI();
		application.setVisible(true);	
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeamGUI frame = new TeamGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}

	public TeamGUI() {
		super();
		initialize();
	}

	private void startTest() {
		getButtonTest().setEnabled(false);
		getDiagramCustomersWaitingProject().clear();
		getDiagramCustomersWaitingTeam().clear();
		getDiagramTeamTotal().clear();
		getDiagramTeamFail().clear();
		dispatcher = new Dispatcher();		
		dispatcher.addDispatcherFinishListener(
				()->getButtonTest().setEnabled(true));
		IModelFactory factory = (d)-> new TeamModel(d, this);
		TeamModel model =(TeamModel) factory.createModel(dispatcher);
		model.initForTest();
		dispatcher.start();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 653, 520);
		this.setContentPane(_getContentPane());
		this.setTitle("Моделювання командної розробки програмного забезпечення");
	}

	private Container _getContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentShown(ComponentEvent arg0) {
					//to update the diagram content
					getCh_timeOfModeling().select(0, 1);
					getCh_numOfTeams().select(0, 1);
					getCh_MaxQ().select(0, 1);
				}
			});
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridBagLayout gbl_contentPane = new GridBagLayout();
			gbl_contentPane.columnWidths = new int[]{236, 0, 0};
			gbl_contentPane.rowHeights = new int[]{0, 0};
			gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			contentPane.setLayout(gbl_contentPane);

			GridBagConstraints gbc_panelL = new GridBagConstraints();
			gbc_panelL.insets = new Insets(0, 0, 0, 5);
			gbc_panelL.fill = GridBagConstraints.BOTH;
			gbc_panelL.gridx = 0;
			gbc_panelL.gridy = 0;

			GridBagConstraints gbc_panelR = new GridBagConstraints();
			gbc_panelR.fill = GridBagConstraints.BOTH;
			gbc_panelR.gridx = 1;
			gbc_panelR.gridy = 0;


			contentPane.add(getPanelL(), gbc_panelL);
			GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
			gbc_tabbedPane.fill = GridBagConstraints.BOTH;
			gbc_tabbedPane.gridx = 1;
			gbc_tabbedPane.gridy = 0;
			contentPane.add(getTabbedPane(), gbc_tabbedPane);
			//contentPane.add(getPanelR(), gbc_panelR);
		}
		return contentPane;
	}

	private Component getPanelR() {
		if(panelR == null){
			panelR = new JPanel();
			panelR.setBorder(new TitledBorder(null, "\u0414\u0456\u0430\u0433\u0440\u0430\u043C\u0438", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_panelR = new GridBagLayout();
			gbl_panelR.columnWidths = new int[]{0, 0, 0};
			gbl_panelR.rowHeights = new int[]{167, 172, 0, 0, 0};
			gbl_panelR.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_panelR.rowWeights = new double[]{1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
			panelR.setLayout(gbl_panelR);
			GridBagConstraints gbc_qCustomersD = new GridBagConstraints();
			gbc_qCustomersD.insets = new Insets(0, 0, 5, 5);
			gbc_qCustomersD.fill = GridBagConstraints.BOTH;
			gbc_qCustomersD.gridx = 0;
			gbc_qCustomersD.gridy = 0;
			panelR.add(getDiagramCustomersWaitingTeam(), gbc_qCustomersD);
			GridBagConstraints gbc_diagramCustomersWaiting = new GridBagConstraints();
			gbc_diagramCustomersWaiting.insets = new Insets(0, 0, 5, 0);
			gbc_diagramCustomersWaiting.fill = GridBagConstraints.BOTH;
			gbc_diagramCustomersWaiting.gridx = 1;
			gbc_diagramCustomersWaiting.gridy = 0;
			panelR.add(getDiagramCustomersWaitingProject(), gbc_diagramCustomersWaiting);

			GridBagConstraints gbc_TeamTotalD = new GridBagConstraints();
			gbc_TeamTotalD.insets = new Insets(0, 0, 5, 5);
			gbc_TeamTotalD.fill = GridBagConstraints.BOTH;
			gbc_TeamTotalD.gridx = 0;
			gbc_TeamTotalD.gridy = 1;
			panelR.add(getDiagramTeamTotal(), gbc_TeamTotalD);

			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.insets = new Insets(0, 0, 5, 0);
			gbc_label.gridx = 0;
			gbc_label.gridy = 2;
			panelR.add(getLabel(), gbc_label);
			GridBagConstraints gbc_diagramTeamFail = new GridBagConstraints();
			gbc_diagramTeamFail.insets = new Insets(0, 0, 5, 0);
			gbc_diagramTeamFail.fill = GridBagConstraints.BOTH;
			gbc_diagramTeamFail.gridx = 1;
			gbc_diagramTeamFail.gridy = 1;
			panelR.add(getDiagramTeamFail(), gbc_diagramTeamFail);

			GridBagConstraints gbc_sliderTeamNumToDiagram = new GridBagConstraints();
			gbc_sliderTeamNumToDiagram.gridwidth = 2;
			gbc_sliderTeamNumToDiagram.anchor = GridBagConstraints.SOUTH;
			gbc_sliderTeamNumToDiagram.fill = GridBagConstraints.HORIZONTAL;
			gbc_sliderTeamNumToDiagram.insets = new Insets(0, 0, 5, 0);
			gbc_sliderTeamNumToDiagram.gridx = 0;
			gbc_sliderTeamNumToDiagram.gridy = 2;
			panelR.add(getSliderTeamNumToDiagram(), gbc_sliderTeamNumToDiagram);
			GridBagConstraints gbc_pane_bottom = new GridBagConstraints();
			gbc_pane_bottom.gridwidth = 2;
			gbc_pane_bottom.fill = GridBagConstraints.BOTH;
			gbc_pane_bottom.gridx = 0;
			gbc_pane_bottom.gridy = 3;
			panelR.add(getPane_bottom(), gbc_pane_bottom);
		}
		return panelR;
	}

	private Component getPanelL() {
		if(panelL == null){
			panelL = new JPanel();
			panelL.setBorder(new TitledBorder(null, "\u041D\u0430\u043B\u0430\u0448\u0442\u0443\u0432\u0430\u043D\u043D\u044F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_panelL = new GridBagLayout();
			gbl_panelL.columnWidths = new int[]{0, 0};
			gbl_panelL.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gbl_panelL.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panelL.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
			panelL.setLayout(gbl_panelL);
			GridBagConstraints gbc_ch_numOfTeams = new GridBagConstraints();
			gbc_ch_numOfTeams.insets = new Insets(0, 0, 5, 0);
			gbc_ch_numOfTeams.fill = GridBagConstraints.HORIZONTAL;
			gbc_ch_numOfTeams.gridx = 0;
			gbc_ch_numOfTeams.gridy = 0;
			panelL.add(getCh_numOfTeams(), gbc_ch_numOfTeams);
			GridBagConstraints gbc_rnd_timeOfIngineering = new GridBagConstraints();
			gbc_rnd_timeOfIngineering.insets = new Insets(0, 0, 5, 0);
			gbc_rnd_timeOfIngineering.fill = GridBagConstraints.BOTH;
			gbc_rnd_timeOfIngineering.gridx = 0;
			gbc_rnd_timeOfIngineering.gridy = 1;
			panelL.add(getRnd_timeOfIngineering(), gbc_rnd_timeOfIngineering);
			GridBagConstraints gbc_rnd_numOfDevels = new GridBagConstraints();
			gbc_rnd_numOfDevels.insets = new Insets(0, 0, 5, 0);
			gbc_rnd_numOfDevels.fill = GridBagConstraints.BOTH;
			gbc_rnd_numOfDevels.gridx = 0;
			gbc_rnd_numOfDevels.gridy = 2;
			panelL.add(getRnd_numOfDevels(), gbc_rnd_numOfDevels);
			GridBagConstraints gbc_rnd_numOfTesters = new GridBagConstraints();
			gbc_rnd_numOfTesters.insets = new Insets(0, 0, 5, 0);
			gbc_rnd_numOfTesters.fill = GridBagConstraints.BOTH;
			gbc_rnd_numOfTesters.gridx = 0;
			gbc_rnd_numOfTesters.gridy = 3;
			panelL.add(getRnd_numOfTesters(), gbc_rnd_numOfTesters);
			GridBagConstraints gbc_ch_illProb = new GridBagConstraints();
			gbc_ch_illProb.insets = new Insets(0, 0, 5, 0);
			gbc_ch_illProb.fill = GridBagConstraints.HORIZONTAL;
			gbc_ch_illProb.gridx = 0;
			gbc_ch_illProb.gridy = 4;
			panelL.add(getCh_illProb(), gbc_ch_illProb);
			GridBagConstraints gbc_rnd_illTime = new GridBagConstraints();
			gbc_rnd_illTime.insets = new Insets(0, 0, 5, 0);
			gbc_rnd_illTime.fill = GridBagConstraints.BOTH;
			gbc_rnd_illTime.gridx = 0;
			gbc_rnd_illTime.gridy = 5;
			panelL.add(getRnd_illTime(), gbc_rnd_illTime);
			GridBagConstraints gbc_ch_probOfErrTest = new GridBagConstraints();
			gbc_ch_probOfErrTest.insets = new Insets(0, 0, 5, 0);
			gbc_ch_probOfErrTest.fill = GridBagConstraints.HORIZONTAL;
			gbc_ch_probOfErrTest.gridx = 0;
			gbc_ch_probOfErrTest.gridy = 6;
			panelL.add(getCh_probOfSuccessTest(), gbc_ch_probOfErrTest);
			GridBagConstraints gbc_ch_MaxQ = new GridBagConstraints();
			gbc_ch_MaxQ.insets = new Insets(0, 0, 5, 0);
			gbc_ch_MaxQ.fill = GridBagConstraints.HORIZONTAL;
			gbc_ch_MaxQ.gridx = 0;
			gbc_ch_MaxQ.gridy = 7;
			panelL.add(getCh_MaxQ(), gbc_ch_MaxQ);
			GridBagConstraints gbc_ch_timeOfModelling = new GridBagConstraints();
			gbc_ch_timeOfModelling.insets = new Insets(0, 0, 5, 0);
			gbc_ch_timeOfModelling.fill = GridBagConstraints.HORIZONTAL;
			gbc_ch_timeOfModelling.gridx = 0;
			gbc_ch_timeOfModelling.gridy = 8;
			panelL.add(getCh_timeOfModeling(), gbc_ch_timeOfModelling);
			GridBagConstraints gbc_ch_customerHoldTime = new GridBagConstraints();
			gbc_ch_customerHoldTime.fill = GridBagConstraints.BOTH;
			gbc_ch_customerHoldTime.gridx = 0;
			gbc_ch_customerHoldTime.gridy = 9;
			panelL.add(getCh_customerHoldTime(), gbc_ch_customerHoldTime);
		}
		return panelL;
	}

	public ChooseData getCh_numOfTeams() {
		if (ch_numOfTeams == null) {
			ch_numOfTeams = new ChooseData();
			ch_numOfTeams.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent arg0) {
					int c;
					if(ch_numOfTeams.getText().isEmpty())
						c = 1;
					else
						c = Integer.parseInt(ch_numOfTeams.getText());
					getSliderTeamNumToDiagram().setMaximum(c);
					Hashtable<Integer, JComponent> labelTable = new Hashtable<>();
					for(int i = 1; i <= c; i++){
						labelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
					}
					getSliderTeamNumToDiagram().setLabelTable(labelTable);
				}
			});
			ch_numOfTeams.setText("2");
			ch_numOfTeams.setTitle("\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u043A\u043E\u043C\u0430\u043D\u0434");
		}
		return ch_numOfTeams;
	}
	public ChooseRandom getRnd_timeOfIngineering() {
		if (rnd_timeOfIngineering == null) {
			rnd_timeOfIngineering = new ChooseRandom();
			rnd_timeOfIngineering.setTitle("\u0427\u0430\u0441 \u0440\u043E\u0437\u0440\u043E\u0431\u043A\u0438 \u043F\u0440\u043E\u0435\u043A\u0442\u0443(\u0412\u0410\u0416\u041B\u0418\u0412\u041E >e)");
		}
		return rnd_timeOfIngineering;
	}
	public ChooseRandom getRnd_numOfDevels() {
		if (rnd_numOfDevels == null) {
			rnd_numOfDevels = new ChooseRandom();
			rnd_numOfDevels.setTitle("\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u0440\u043E\u0437\u0440\u043E\u0431\u043D\u0438\u043A\u0456\u0432 \u0443 \u043A\u043E\u043C\u0430\u043D\u0434\u0456");
		}
		return rnd_numOfDevels;
	}
	public ChooseData getCh_timeOfModeling() {
		if (ch_timeOfModeling == null) {
			ch_timeOfModeling = new ChooseData();
			ch_timeOfModeling.setText("100");
			ch_timeOfModeling.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent arg0) {
					getDiagramCustomersWaitingTeam().setHorizontalMaxText(ch_timeOfModeling.getText());
					getDiagramTeamTotal().setHorizontalMaxText(ch_timeOfModeling.getText());
					getDiagramTeamFail().setHorizontalMaxText(ch_timeOfModeling.getText());
					getDiagramCustomersWaitingProject().setHorizontalMaxText(ch_timeOfModeling.getText());
				}
			});
			ch_timeOfModeling.setTitle("\u0427\u0430\u0441 \u043C\u043E\u0434\u0435\u043B\u044E\u0432\u0430\u043D\u043D\u044F");
		}
		return ch_timeOfModeling;
	}
	public Diagram getDiagramCustomersWaitingTeam() {
		if (qCustomersD == null) {
			qCustomersD = new Diagram();
			qCustomersD.setVerticalMaxText("16");
			qCustomersD.setHorizontalMaxText("100");
			qCustomersD.setTitleText("\u0427\u0435\u0440\u0433\u0430 \u0437\u0430\u043C\u043E\u0432\u043D\u0438\u043A\u0456\u0432, \u044F\u043A\u0456 \u0447\u0435\u043A\u0430\u044E\u0442\u044C \u0447\u0435\u0440\u0433\u0438 \u043F\u043E\u0434\u0430\u0442\u0438 \u043F\u0440\u043E\u0435\u043A\u0442");
			qCustomersD.setPainterColor(new Color(255, 0, 0));
		}
		return qCustomersD;
	}
	public ChooseData getCh_illProb() {
		if (ch_illProb == null) {
			ch_illProb = new ChooseData();
			ch_illProb.setText("0.05");
			ch_illProb.setTitle("\u0412\u0456\u0440\u043E\u0433\u0456\u0434\u043D\u0456\u0441\u0442\u044C \u0445\u0432\u043E\u0440\u043E\u0431\u0438");
		}
		return ch_illProb;
	}
	public ChooseData getCh_probOfSuccessTest() {
		if (ch_probOfErrTest == null) {
			ch_probOfErrTest = new ChooseData();
			ch_probOfErrTest.setText("0.95");
			ch_probOfErrTest.setTitle("\u0412\u0456\u0440\u043E\u0433\u0456\u0434\u043D\u0456\u0441\u0442\u044C \u0443\u0441\u043F\u0456\u0445\u0443 \u043D\u0430 \u0435\u0442\u0430\u043F\u0456 \u0442\u0435\u0441\u0442\u0443");
		}
		return ch_probOfErrTest;
	}
	private JPanel getPane_bottom() {
		if (pane_bottom == null) {
			pane_bottom = new JPanel();
			GridBagLayout gbl_pane_bottom = new GridBagLayout();
			gbl_pane_bottom.columnWidths = new int[]{0, 0, 0, 0, 87, 113, 0};
			gbl_pane_bottom.rowHeights = new int[]{0, 0};
			gbl_pane_bottom.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_pane_bottom.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			pane_bottom.setLayout(gbl_pane_bottom);
			GridBagConstraints gbc_chckbxConsole = new GridBagConstraints();
			gbc_chckbxConsole.gridwidth = 5;
			gbc_chckbxConsole.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxConsole.gridx = 0;
			gbc_chckbxConsole.gridy = 0;
			pane_bottom.add(getChckbxConsole(), gbc_chckbxConsole);
			GridBagConstraints gbc_buttonStart = new GridBagConstraints();
			gbc_buttonStart.gridx = 5;
			gbc_buttonStart.gridy = 0;
			pane_bottom.add(getButtonTest(), gbc_buttonStart);
		}
		return pane_bottom;
	}
	private JCheckBox getChckbxConsole() {
		if (chckbxConsole == null) {
			chckbxConsole = new JCheckBox("\u0420\u0435\u0437\u0443\u043B\u044C\u0442\u0430\u0442 \u043D\u0430 \u043A\u043E\u043D\u0441\u043E\u043B\u044C");
		}
		return chckbxConsole;
	}
	public Boolean getProtocolToConcole() {
		return getChckbxConsole().isSelected();
	}
	private JButton getButtonTest() {
		if (buttonTest == null) {
			buttonTest = new JButton("\u0421\u0442\u0430\u0440\u0442");
			buttonTest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startTest();
				}
			});
		}
		return buttonTest;
	}
	public Diagram getDiagramTeamTotal() {
		if (TeamTotalD == null) {
			TeamTotalD = new Diagram();
			TeamTotalD.setVerticalMaxText("20");
			TeamTotalD.setHorizontalMaxText("100");
			TeamTotalD.setTitleText("\u0423\u0441\u043F\u0456\u0448\u043D\u0456 \u043F\u0440\u043E\u0435\u043A\u0442\u0438");
		}
		return TeamTotalD;
	}
	public JLabel getLabel() {
		if(label == null)
			label = new JLabel("\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u043A\u043E\u043C\u0430\u043D\u0434");
		return label;
	}
	public JSlider getSliderTeamNumToDiagram() {
		if(sliderTeamNumToDiagram == null){
			sliderTeamNumToDiagram = new JSlider(1, getCh_numOfTeams().getInt(), 1);
			sliderTeamNumToDiagram.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					graphsToDraw = sliderTeamNumToDiagram.getValue();
					//getDiagramTeamTotal().setHorizontalMaxText(String.valueOf(graphsToDraw));
				}
			});
			sliderTeamNumToDiagram.setPaintTicks(true);
			sliderTeamNumToDiagram.setMajorTickSpacing(1);
			sliderTeamNumToDiagram.setSnapToTicks(true);
			sliderTeamNumToDiagram.setToolTipText("Кількість команд для відображення на графіку");
		}
		return sliderTeamNumToDiagram;
	}
	public ChooseData getCh_MaxQ() {
		if (ch_MaxQ == null) {
			ch_MaxQ = new ChooseData();
			ch_MaxQ.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent e) {
					getDiagramCustomersWaitingTeam().setVerticalMaxText(String.valueOf((Integer.parseInt(ch_MaxQ.getText()) + 1)));
					getDiagramCustomersWaitingProject().setVerticalMaxText(String.valueOf((Integer.parseInt(ch_MaxQ.getText()) + 1)));
				}
			});
			ch_MaxQ.setTitle("\u041D\u0430\u0439\u0431\u0456\u043B\u044C\u0448\u0438\u0439 \u0440\u043E\u0437\u043C\u0456\u0440 \u0447\u0435\u0440\u0433\u0438 \u0437\u0430\u043C\u043E\u0432\u043D\u0438\u043A\u0456\u0432");
			ch_MaxQ.setText("15");
		}
		return ch_MaxQ;
	}
	/**
	 * returns the entered time of modeling
	 * @return double
	 */
	public double getFinishTime() {
		return getCh_timeOfModeling().getDouble();
	}
	public ChooseRandom getRnd_numOfTesters() {
		if (rnd_numOfTesters == null) {
			rnd_numOfTesters = new ChooseRandom();
			GridBagLayout gbl_rnd_numOfTesters = (GridBagLayout) rnd_numOfTesters.getLayout();
			gbl_rnd_numOfTesters.rowWeights = new double[]{1.0};
			gbl_rnd_numOfTesters.rowHeights = new int[]{0};
			gbl_rnd_numOfTesters.columnWeights = new double[]{0.0, 1.0};
			gbl_rnd_numOfTesters.columnWidths = new int[]{32, 0};
			rnd_numOfTesters.setTitle("\u041A\u0456\u043B\u044C\u043A\u0456\u0441\u0442\u044C \u0442\u0435\u0441\u0442\u0435\u0440\u0456\u0432 \u0443 \u043A\u043E\u043C\u0430\u043D\u0434\u0456");
		}
		return rnd_numOfTesters;
	}
	public ChooseRandom getRnd_illTime() {
		if (rnd_illTime == null) {
			rnd_illTime = new ChooseRandom();
			rnd_illTime.setTitle("\u0427\u0430\u0441 \u0445\u0432\u043E\u0440\u043E\u0431\u0438");
		}
		return rnd_illTime;
	}
	public ChooseRandom getCh_customerHoldTime() {
		if (ch_customerHoldTime == null) {
			ch_customerHoldTime = new ChooseRandom();
			ch_customerHoldTime.setTitle("\u0417\u0430\u0442\u0440\u0438\u043C\u043A\u0430 \u0434\u043E \u043F\u0440\u0438\u0445\u043E\u0434\u0443 \u043D\u0430\u0441\u0442\u0443\u043F\u043D\u043E\u0433\u043E \u0437\u0430\u043C\u043E\u0432\u043D\u0438\u043A\u0430");
		}
		return ch_customerHoldTime;
	}
	public JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("TZ", null, getScrollPane(), null);
			tabbedPane.addTab("Test", null, getPanelR(), null);
			tabbedPane.addTab("Stat", null, getStatisticsManager(), null);
			tabbedPane.addTab("Regres1-teams", null, getExperimentManager1(), null);
			tabbedPane.addTab("Regres2-ill", null, getExperimentManager2(), null);
			tabbedPane.addTab("Regres3-test", null, getExperimentManager3(), null);
			tabbedPane.addTab("Transient", null, getPanel(), null);
		}
		return tabbedPane;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextPane());
		}
		return scrollPane;
	}
	private JTextPane getTextPane() {
		if (textPane == null) {
			textPane = new JTextPane();
			try {
				textPane.setPage(getClass().getResource("tz.html"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return textPane;
	}
	public Diagram getDiagramTeamFail() {
		if (diagramTeamFail == null) {
			diagramTeamFail = new Diagram();
			GridBagLayout gbl_diagramTeamFail = (GridBagLayout) diagramTeamFail.getLayout();
			gbl_diagramTeamFail.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
			gbl_diagramTeamFail.rowHeights = new int[]{0, 0, 0, 0};
			gbl_diagramTeamFail.columnWeights = new double[]{0.0, 0.0, 0.0};
			gbl_diagramTeamFail.columnWidths = new int[]{0, 0, 0};
			diagramTeamFail.setVerticalMaxText("20");
			diagramTeamFail.setTitleText("\u041F\u0440\u043E\u0432\u0430\u043B\u0435\u043D\u0456 \u043F\u0440\u043E\u0435\u043A\u0442\u0438");
			diagramTeamFail.setPainterColor(Color.RED);
			diagramTeamFail.setHorizontalMaxText("100");
		}
		return diagramTeamFail;
	}
	public Diagram getDiagramCustomersWaitingProject() {
		if (diagramCustomersWaiting == null) {
			diagramCustomersWaiting = new Diagram();
			GridBagLayout gbl_diagramCustomersWaiting = (GridBagLayout) diagramCustomersWaiting.getLayout();
			gbl_diagramCustomersWaiting.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
			gbl_diagramCustomersWaiting.rowHeights = new int[]{0, 0, 0, 0};
			gbl_diagramCustomersWaiting.columnWeights = new double[]{0.0, 0.0, 0.0};
			gbl_diagramCustomersWaiting.columnWidths = new int[]{0, 0, 0};
			diagramCustomersWaiting.setVerticalMaxText("16");
			diagramCustomersWaiting.setTitleText("\u0417\u0430\u043C\u043E\u0432\u043D\u0438\u043A\u0438, \u044F\u043A\u0456 \u0447\u0435\u043A\u0430\u044E\u0442\u044C \u043D\u0430 \u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043D\u043D\u044F \u043F\u0440\u043E\u0435\u043A\u0442\u0443");
			diagramCustomersWaiting.setPainterColor(Color.RED);
			diagramCustomersWaiting.setHorizontalMaxText("100");
		}
		return diagramCustomersWaiting;
	}
	private StatisticsManager getStatisticsManager() {
		if (statisticsManager == null) {
			statisticsManager = new StatisticsManager();
			statisticsManager.setFactory((d)-> new TeamModel(d, this));
		}
		return statisticsManager;
	}
	private ExperimentManager getExperimentManager1() {
		if (experimentManager1 == null) {
			experimentManager1 = new ExperimentManager();
			experimentManager1.setFactory((d)-> new TeamModel(d, this));
		}
		return experimentManager1;
	}

	public void setCh_numOfTeams(String val) {
		getCh_numOfTeams().setDouble(Double.valueOf(val));
	}
	private ExperimentManager getExperimentManager2() {
		if (experimentManager2 == null) {
			experimentManager2 = new ExperimentManager();
			experimentManager2.setFactory((d)-> new TeamModel(d, this));
		}
		return experimentManager2;
	}
	private ExperimentManager getExperimentManager3() {
		if (experimentManager3 == null) {
			experimentManager3 = new ExperimentManager();
			experimentManager3.setFactory((d)-> new TeamModel(d, this));
		}
		return experimentManager3;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0};
			gbl_panel.rowHeights = new int[]{0, 0, 0};
			gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_transProcessManager = new GridBagConstraints();
			gbc_transProcessManager.insets = new Insets(0, 0, 5, 0);
			gbc_transProcessManager.fill = GridBagConstraints.BOTH;
			gbc_transProcessManager.gridx = 0;
			gbc_transProcessManager.gridy = 0;
			panel.add(getTransProcessManager(), gbc_transProcessManager);
			GridBagConstraints gbc_btnParmfinder = new GridBagConstraints();
			gbc_btnParmfinder.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnParmfinder.gridx = 0;
			gbc_btnParmfinder.gridy = 1;
			panel.add(getBtnParmfinder(), gbc_btnParmfinder);
		}
		return panel;
	}
	public TransProcessManager getTransProcessManager() {
		if (transProcessManager == null) {
			transProcessManager = new TransProcessManager();
			GridBagLayout gridBagLayout_1 = (GridBagLayout) transProcessManager.getLayout();
			gridBagLayout_1.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0};
			gridBagLayout_1.rowHeights = new int[]{13, 54, 54, 54, 26};
			gridBagLayout_1.columnWeights = new double[]{0.0, 1.0};
			gridBagLayout_1.columnWidths = new int[]{150, 0};
			GridBagLayout gridBagLayout = (GridBagLayout) transProcessManager.getDiagram().getLayout();
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0};
			gridBagLayout.columnWidths = new int[]{0, 0, 0};
			transProcessManager.setFactory((d)-> new TeamModel(d, this));
		}
		return transProcessManager;
	}
	private JButton getBtnParmfinder() {
		if (btnParmfinder == null) {
			btnParmfinder = new JButton("ParmFinder");
			btnParmfinder.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					btnParmfinderMouseClicked(arg0);
				}
			});
		}
		return btnParmfinder;
	}
	protected void btnParmfinderMouseClicked(MouseEvent arg0) {
		ParmFinderView pf = new ParmFinderView();
		pf.setDiagram(getTransProcessManager().getDiagram());
		pf.setIRegresable(getTransProcessManager());
		JFrame f = new JFrame("PARM");
		f.setContentPane(pf);
		f.setBounds(200, 200, 400, 300);
		f.setResizable(false);
		f.setVisible(true);
		
	}
}
