package tetris;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

public class JBrainTetris extends JTetris {
	public JBrainTetris(int pixels)
	{
		super(pixels);
		dBrain = new DefaultBrain();
		playedCount = -1;
	}
	
	@Override
	public JComponent createControlPanel() {
		JPanel panel =  (JPanel) super.createControlPanel();
		
		panel.add(new JLabel("Brain:"));
		brainMode = new JCheckBox("Brain active");
		panel.add(brainMode);
		
		// make a little panel, put a JSlider in it. JSlider responds to getValue()
		little = new JPanel();
		little.add(new JLabel("Adversary:"));
		adversary = new JSlider(0, 100, 0); // min, max, current
		adversary.setPreferredSize(new Dimension(100,15));
		little.add(adversary);
		adversaryStatus = new JLabel("Ok");
		little.add(adversaryStatus);
		// now add little to panel of controls
		panel.add(little);
		
		return panel;
	}

	@Override
	public Piece pickNextPiece() {
		if (random.nextInt(99) + 1 >= adversary.getValue()) {
			adversaryStatus.setText("Ok");
			return super.pickNextPiece();
		} else {
			adversaryStatus.setText("*Ok*");
			return adversaryPick();
		}
			
	}

	private Piece adversaryPick() {
		Piece[] pieces = Piece.getPieces();
		Piece worstPiece = null;
		double worstScore = 0;
		for (Piece piece : pieces) {
			Piece current = piece;
			while (true) {
				Brain.Move bestMove = dBrain.bestMove(board, current, HEIGHT, null);
				if (bestMove != null && bestMove.score - worstScore > 1e-6) {
					worstScore = bestMove.score;
					worstPiece = bestMove.piece;
				}
				current = current.fastRotation();
				if (current.equals(piece))
					break;
			}
		}
		return worstPiece;
	}

	@Override
	public void tick(int verb) {
		if (brainMode.isSelected() && currentPiece != null && verb == JTetris.DOWN)
			moveCurrentPieceWisely();
		super.tick(verb);
	}

	private void moveCurrentPieceWisely() {
		if (playedCount < count) {
			playedCount = count;
			board.undo();
			bestMove = dBrain.bestMove(board, currentPiece, HEIGHT, null);
			if (bestMove == null)
				return;
		}
		
		if (!bestMove.piece.equals(currentPiece)) {
			computeNewPosition(JTetris.ROTATE);
			board.undo();
			setCurrent(newPiece, newX, newY);
		}
		if (currentX > bestMove.x) {
			computeNewPosition(JTetris.LEFT);
			board.undo();
			setCurrent(newPiece, newX, newY);
		} else if (currentX < bestMove.x) {
			computeNewPosition(JTetris.RIGHT);
			board.undo();
			setCurrent(newPiece, newX, newY);
		}
	}

	public static void main(String[] args) {
		// Set GUI Look And Feel Boilerplate.
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		JTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);
	}
	
	DefaultBrain dBrain;
	JCheckBox brainMode;
	JPanel little;
	JSlider adversary;
	JLabel adversaryStatus;
	int playedCount;
	Brain.Move bestMove;
}
