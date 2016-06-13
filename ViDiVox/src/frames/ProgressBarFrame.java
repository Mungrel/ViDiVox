package frames;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ProgressBarFrame extends JFrame {

	private JPanel contentPane;

	public static ProgressBarFrame pbFrame = new ProgressBarFrame();
	public static String[] sa = { "Reticulating splines...", "Solving world hunger...", "Dividing by 0",
			"Coming up with other witty loading screen lines...", "Understanding Inception plot..." };
	public static Random rand = new Random();
	public static JLabel lblGiveUsA;
	public static Timer timer;

	/**
	 * Launch the application. A simple progress bar GUI for when threads are
	 * working in the background to show the user things are happening.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pbFrame.setLocationRelativeTo(null);
					pbFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProgressBarFrame() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		contentPane.setLayout(null);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(38, 61, 163, 20);
		progressBar.setIndeterminate(true);
		progressBar.setValue(1);
		progressBar.setVisible(true);
		contentPane.add(progressBar);
		setTitle("Working...");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 263, 176);

		lblGiveUsA = new JLabel("Give us a few seconds...");
		lblGiveUsA.setBounds(38, 22, 250, 15);
		contentPane.add(lblGiveUsA);
		timer = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblGiveUsA.setText(getRandomMessage());

			}
		});
		timer.start();

		setContentPane(contentPane);
	}

	public static String getRandomMessage() {
		return sa[rand.nextInt(sa.length)];
	}
}
