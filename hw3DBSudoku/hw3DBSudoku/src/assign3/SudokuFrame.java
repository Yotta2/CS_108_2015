package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		setLayout(new BorderLayout(4, 4));
		puzzleText = new JTextArea(15, 20);
		puzzleText.setBorder(new TitledBorder("Puzzle"));
		add(puzzleText, BorderLayout.CENTER);

		solutionText = new JTextArea(15, 20);
		solutionText.setBorder(new TitledBorder("Solution"));
		add(solutionText, BorderLayout.EAST);

		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		checkButton = new JButton("Check");
		checkBox = new JCheckBox("Auto Check", true);
		controlPanel.add(checkButton);
		controlPanel.add(checkBox);
		add(controlPanel, BorderLayout.SOUTH);

		checkButton.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						check();
					}
				}
		);

		puzzleText.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						autoCheck();
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						autoCheck();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						autoCheck();
					}

					private void autoCheck() {
						if (!checkBox.isSelected())
							return;
						check();
					}
				}
		);

		// Could do this:
		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void check() {
		try {
			Sudoku sudoku = new Sudoku(puzzleText.getText());
			int solutions = sudoku.solve();
			long elapsed = sudoku.getElapsed();
			StringBuilder sb = new StringBuilder();
			sb.append(sudoku.getSolutionText());
			sb.append("\n");
			sb.append("Solutions: " + solutions);
			sb.append("\n");
			sb.append("elapsed: " + elapsed + "ms");
			solutionText.setText(sb.toString());
		} catch (Exception ex) {
			solutionText.setText("Parsing problem");
		}
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

	private JTextArea puzzleText;
	private JTextArea solutionText;
	private JPanel controlPanel;
	private JButton checkButton;
	private JCheckBox checkBox;
}
