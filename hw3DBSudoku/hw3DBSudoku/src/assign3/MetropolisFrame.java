package assign3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class MetropolisFrame extends JFrame {
	public MetropolisFrame(String title) {
		super(title);
		
		JPanel searchTxtPanel = setupSearchTxtPanel();
		add(searchTxtPanel, BorderLayout.NORTH);

		JPanel controlPanel = setupControlPanel();
		add(controlPanel, BorderLayout.EAST);
		
		JScrollPane scrollpane = setupTablePane(); 
		add(scrollpane, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private JScrollPane setupTablePane() {
		model = new MetropolisTableModel();
		table = new JTable(model);
		table.setShowGrid(true);
		table.setGridColor(Color.GRAY);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(500,400));

		return scrollpane;
	}

	private JPanel setupControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.addData(metropolisTxtField.getText(), continentTxtField.getText(), Integer.valueOf(populationTxtField.getText()));
			}
		});
		controlPanel.add(addButton);

		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.search(metropolisTxtField.getText(), continentTxtField.getText(), populationTxtField.getText(), populationPulldown.getSelectedIndex(), matchTypePulldown.getSelectedIndex());
			}
		});
		controlPanel.add(searchButton);
		
		JPanel searchOptionPanel = new JPanel();
		searchOptionPanel.setBorder(new TitledBorder("Search Options"));
		searchOptionPanel.setLayout(new BoxLayout(searchOptionPanel, BoxLayout.Y_AXIS));

		populationPulldown = new JComboBox(MetropolisTableModel.POPULATION_CRITERIA);
		populationPulldown.setSelectedIndex(0);
		searchOptionPanel.add(populationPulldown);

		matchTypePulldown = new JComboBox(MetropolisTableModel.MATCH_TYPES);
		matchTypePulldown.setSelectedIndex(0);
		searchOptionPanel.add(matchTypePulldown);

		
		controlPanel.add(searchOptionPanel);

		return controlPanel;
	}

	private JPanel setupSearchTxtPanel() {
		JPanel searchTxtPanel = new JPanel();
		searchTxtPanel.setLayout(new BoxLayout(searchTxtPanel, BoxLayout.X_AXIS));
		
		searchTxtPanel.add(new JLabel("Metropolis:"));
		metropolisTxtField = new JTextField();
		searchTxtPanel.add(metropolisTxtField);
		
		searchTxtPanel.add(new JLabel("Continent:"));
		continentTxtField = new JTextField();
		searchTxtPanel.add(continentTxtField);
		
		searchTxtPanel.add(new JLabel("Population:"));
		populationTxtField = new JTextField();
		searchTxtPanel.add(populationTxtField);
		
		return searchTxtPanel;
	}

	public static void main(String[] args) {
		new MetropolisFrame("Metropolis Viewer");

	}

	private JTextField metropolisTxtField;
	private JTextField continentTxtField;
	private JTextField populationTxtField;
	private JButton addButton;
	private JButton searchButton;
	private JComboBox populationPulldown;
	private JComboBox matchTypePulldown;
	private JTable table;
	private MetropolisTableModel model;
}
