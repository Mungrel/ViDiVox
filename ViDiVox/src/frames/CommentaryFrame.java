package frames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tools.Audio;
import tools.IOHandler;
import tools.Tools;

import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class CommentaryFrame extends JFrame {
	/*
	 * 
	 * Frame used to add/create 'commentary' (text) into a wave file and add to
	 * the video loaded. This JFrame represents the GUI for it.
	 */
	private JPanel contentPane;
	public static CommentaryFrame cmFrame = new CommentaryFrame();
	private JTextArea textField;
	public static JCheckBox chckbxApplyThisSpeech;
	public static boolean loadNewVideoIsChecked = false;
	public static int currentFestID = 0;
	public JSlider speedSlider;
	JButton btnStop;
	JButton btnPreview;
	JButton btnSave;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cmFrame.setVisible(true);
					cmFrame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CommentaryFrame() {
		setTitle("Add Commentary");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 458, 262);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextArea();
		textField.setLineWrap(true);
		textField.setBounds(12, 22, 287, 134);
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				String text = textField.getText();
				if (text.trim().equals("")){
					btnPreview.setEnabled(false);
					btnSave.setEnabled(false);
				} else {
					btnPreview.setEnabled(true);
					btnSave.setEnabled(true);
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = textField.getText();
				if (text.trim().equals("")){
					btnPreview.setEnabled(false);
					btnSave.setEnabled(false);
				} else {
					btnPreview.setEnabled(true);
					btnSave.setEnabled(true);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = textField.getText();
				if (text.trim().equals("")){
					btnPreview.setEnabled(false);
					btnSave.setEnabled(false);
				} else {
					btnPreview.setEnabled(true);
					btnSave.setEnabled(true);
				}
			}
		});
		contentPane.add(textField);

		// Preview button plays text through festival
		btnPreview = new JButton("Preview");
		btnPreview.setBounds(15, 168, 133, 25);
		btnPreview.setBackground(Color.WHITE);
		btnPreview.setEnabled(false);
		btnPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Preview button clicked
				String textToPreview = textField.getText();
				File f = Audio.generateSCMFestFile(textToPreview, getSliderVal(), false);
				int numWords = textToPreview.split(" ").length;
				// Brief says to limit max number of words to between 20-40
				if (numWords > 30) {
					// Too many words
					Tools.displayError("Number of words should be less than 30");
				} else {
					currentFestID = Audio.speakFestival(f);
				}

			}
		});
		contentPane.add(btnPreview);

		// Button returns to mainFrame without saving
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(311, 168, 133, 25);
		btnBack.setBackground(Color.WHITE);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Back button clicked
				// MainFrame was never set to invisible
				// Simply hide this current frame
				cmFrame.setVisible(false);
				MainFrame.mFrame.requestFocus();

			}
		});
		contentPane.add(btnBack);

		// Button for saving synth speech as MP3 file
		btnSave = new JButton("Save");
		btnSave.setBounds(160, 170, 139, 21);
		btnSave.setBackground(Color.WHITE);
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Save button clicked
				
				String textToSave = textField.getText();

				int numWords = textToSave.split(" ").length;
				// Brief says to limit max number of words to between 20-40
				if (numWords > 30) {
					// Too many words
					Tools.displayError("Number of words should be less than 30");
				} else {
					File scm = Audio.generateSCMFestFile(textToSave, getSliderVal(), true);
					Audio.speakFestival(scm);
					File uttFile = new File(IOHandler.TmpDirectory+"utt.wav");
					Audio.saveFestToMP3(uttFile);
					if (chckbxApplyThisSpeech.isSelected()) {
						// Checkbox was selected upon save click
						cmFrame.setVisible(false);
						MultipleAudioFrame.maFrame.setLocationRelativeTo(cmFrame);
						MultipleAudioFrame.maFrame.setVisible(true);
					}
				}

			}
		});
		contentPane.add(btnSave);

		JLabel lblWriteTextBelow = new JLabel("Text to synthesise (30 word limit)");
		lblWriteTextBelow.setBounds(10, 0, 378, 15);
		contentPane.add(lblWriteTextBelow);

		btnStop = new JButton("Stop Preview");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Stop button clicked
				Audio.killAllFestProc(currentFestID);
			}
		});
		btnStop.setBackground(Color.WHITE);
		btnStop.setBounds(15, 205, 133, 25);
		contentPane.add(btnStop);
		chckbxApplyThisSpeech = new JCheckBox("Apply audio");
		chckbxApplyThisSpeech.setBounds(160, 196, 290, 23);

		contentPane.add(chckbxApplyThisSpeech);
		
		speedSlider = new JSlider();
		Hashtable labeltable = new Hashtable();
		labeltable.put(new Integer("5"), new JLabel("0.5"));
		labeltable.put(new Integer("10"), new JLabel("1.0"));
		labeltable.put(new Integer("15"), new JLabel("1.5"));
		labeltable.put(new Integer("20"), new JLabel("2.0"));
		speedSlider.setLabelTable(labeltable);
		speedSlider.setSnapToTicks(true);
		speedSlider.setMajorTickSpacing(5);
		speedSlider.setMinimum(5);
		speedSlider.setPaintLabels(true);
		speedSlider.setPaintTicks(true);
		speedSlider.setMaximum(20);
		speedSlider.setOrientation(SwingConstants.VERTICAL);
		speedSlider.setBounds(323, 22, 109, 134);
		contentPane.add(speedSlider);
		
		JLabel lblTalkSpeed = new JLabel("Talk Speed");
		lblTalkSpeed.setBounds(311, 0, 121, 14);
		contentPane.add(lblTalkSpeed);
	}
	public double getSliderVal(){
		double val = (double)speedSlider.getValue();
		val /= 10;
		return val;
	}
}
