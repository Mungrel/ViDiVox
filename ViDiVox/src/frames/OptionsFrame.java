package frames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class OptionsFrame extends JFrame {

	private JPanel contentPane;
	public static OptionsFrame oFrame = new OptionsFrame();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionsFrame frame = new OptionsFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OptionsFrame() {
		setTitle("Choose an option");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 405, 124);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblWhatWouldYou = new JLabel("What would you like to do?");
		lblWhatWouldYou.setBounds(10, 23, 230, 14);
		contentPane.add(lblWhatWouldYou);

		JButton btnAddAudio = new JButton("Add audio");
		btnAddAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add audio button clicked
				MultipleAudioFrame.maFrame.setLocationRelativeTo(MainFrame.mFrame);
				MultipleAudioFrame.maFrame.setVisible(true);
				oFrame.setVisible(false);
			}
		});
		btnAddAudio.setBackground(Color.WHITE);
		btnAddAudio.setBounds(20, 48, 109, 23);
		contentPane.add(btnAddAudio);

		JButton btnCreateCommentary = new JButton("Commentate");
		btnCreateCommentary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Commentate button clicked
				CommentaryFrame.cmFrame.setLocationRelativeTo(MainFrame.mFrame);
				CommentaryFrame.cmFrame.setVisible(true);
				oFrame.setVisible(false);
			}
		});
		btnCreateCommentary.setBackground(Color.WHITE);
		btnCreateCommentary.setBounds(139, 48, 153, 23);
		contentPane.add(btnCreateCommentary);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Back button clicked
				oFrame.setVisible(false);
			}
		});
		btnBack.setBackground(Color.WHITE);
		btnBack.setBounds(304, 48, 89, 23);
		contentPane.add(btnBack);
	}
}
