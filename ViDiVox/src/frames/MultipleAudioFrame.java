package frames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import tools.Tools;
import tools.AddAudio;
import tools.IOHandler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Color;

import javax.swing.SwingConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("serial")
public class MultipleAudioFrame extends JFrame {

	private JPanel contentPane;
	public static MultipleAudioFrame maFrame = new MultipleAudioFrame();
	public JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	JButton btnPreview;
	JButton btnApply;
	public static boolean hasBeenPreviewed = false;
	JRadioButton overwriteRadio;
	JRadioButton mixRadio;
	String os = System.getProperty("os.name").toLowerCase();

	File file1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MultipleAudioFrame frame = new MultipleAudioFrame();
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
	public MultipleAudioFrame() {
		setTitle("Add Audio");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 439, 243);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(39, 36, 226, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(39, 11, 46, 14);
		contentPane.add(lblFile);

		JButton btnChooseFile = new JButton("Choose");
		btnChooseFile.setBackground(Color.WHITE);
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Choose File 1 button clicked
				hasBeenPreviewed = false;
				File file1 = Tools.openMP3File();
				String s = textField.getText();
				if ((file1 != null) && file1.isFile()) {
					s = file1.getAbsolutePath();
					textField.setText(s);
				}
				
				if (s.equals("")) {
					// File path is empty, do not allow preview and apply
					btnPreview.setEnabled(false);
					btnApply.setEnabled(false);
				} else {
					btnPreview.setEnabled(true);
					btnApply.setEnabled(true);
				}
			}
		});
		btnChooseFile.setBounds(275, 35, 111, 23);
		contentPane.add(btnChooseFile);

		JLabel lblStartTime = new JLabel("Start Time:");
		lblStartTime.setBounds(39, 67, 83, 14);
		contentPane.add(lblStartTime);

		JLabel lblMinutes = new JLabel("Mins:");
		lblMinutes.setBounds(39, 96, 56, 14);
		contentPane.add(lblMinutes);

		JLabel lblSeconds = new JLabel("Sec:");
		lblSeconds.setBounds(165, 96, 46, 14);
		contentPane.add(lblSeconds);

		textField_1 = new JTextField();
		textField_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (textField_1.getText().trim().equals("")) {
					textField_1.setText("0");
				}
			}
		});
		textField_1.setHorizontalAlignment(SwingConstants.TRAILING);
		textField_1.setText("0");
		textField_1.setBounds(105, 93, 46, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (textField_2.getText().trim().equals("")) {
					textField_2.setText("0");
				}
			}
		});
		textField_2.setHorizontalAlignment(SwingConstants.TRAILING);
		textField_2.setText("0");
		textField_2.setBounds(221, 93, 46, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		JButton btnUseCurrentTime = new JButton("Current");
		btnUseCurrentTime.setBackground(Color.WHITE);
		btnUseCurrentTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get current time file 1 button clicked
				int value = MainFrame.mFrame.slider.getValue();
				value /= 10;
				int mins = value / 60;
				int secs = value % 60;
				textField_1.setText("" + mins);
				textField_2.setText("" + secs);
			}
		});
		btnUseCurrentTime.setBounds(275, 92, 111, 23);
		contentPane.add(btnUseCurrentTime);

		JButton btnBack = new JButton("Back");
		btnBack.setBackground(Color.WHITE);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Back button clicked
				maFrame.setVisible(false);
				MainFrame.mFrame.setLocationRelativeTo(maFrame);
				MainFrame.mFrame.setVisible(true);
			}
		});
		btnBack.setBounds(297, 170, 89, 23);
		contentPane.add(btnBack);

		btnApply = new JButton("Apply");
		btnApply.setEnabled(false);
		btnApply.setBackground(Color.WHITE);
		btnApply.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// Apply button clicked
				if (os.equals("linux")){
				if (MultipleAudioFrame.maFrame.hasBeenPreviewed){
					//Edit has been previewed, video already made, simply move
					RevertOrApplyFrame.raFrame.btnSave.doClick();
				} else {
					doAudio(mixRadio.isSelected(), false);
				}} else {
					Tools.displayError("Requires linux!");
				}
			}
		});
		btnApply.setBounds(198, 170, 89, 23);
		contentPane.add(btnApply);

		btnPreview = new JButton("Preview");
		btnPreview.setEnabled(false);
		btnPreview.setBackground(Color.WHITE);
		btnPreview.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// Preview button clicked
				if (os.equals("linux")){
				doAudio(mixRadio.isSelected(), true);
				hasBeenPreviewed = true;
				MainFrame.mFrame.isBeingPreviewed = true;
				} else {
					Tools.displayError("Requires linux!");
				}
			
			}
		});
		btnPreview.setBounds(67, 170, 121, 23);
		contentPane.add(btnPreview);
		
		overwriteRadio = new JRadioButton("Overwrite");
		overwriteRadio.setToolTipText("Overwrites all existing audio");
		overwriteRadio.setSelected(true);
		overwriteRadio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (overwriteRadio.isSelected()){
					mixRadio.setSelected(false);
				}
			}
		});
		overwriteRadio.setBounds(39, 140, 121, 23);
		contentPane.add(overwriteRadio);
		
		mixRadio = new JRadioButton("Mix");
		mixRadio.setToolTipText("Mixes your selected audio track with the existing audio");
		mixRadio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (mixRadio.isSelected()){
					overwriteRadio.setSelected(false);
				} 
			}
		});
		mixRadio.setBounds(198, 140, 149, 23);
		contentPane.add(mixRadio);
		
		JLabel lblMixOptions = new JLabel("Mix Options");
		lblMixOptions.setBounds(39, 117, 112, 14);
		contentPane.add(lblMixOptions);
	}
	
	public void doAudio(boolean isMixing, boolean isPreview){
		if ((!mixRadio.isSelected()) && (!overwriteRadio.isSelected())){
			Tools.displayError("You must select a mix option");
			return;
		}
		String f1TimeMins = textField_1.getText();
		String f1TimeSecs = textField_2.getText();
		long runTime = MainFrame.mFrame.theVideo.getLength() / 1000;
		long selectedTime1 = 0;
		String t1 = textField.getText();
		File f1 = null;
		if (!t1.equals("")) {
			if (Tools.isAllDigits(f1TimeSecs) && Tools.isAllDigits(f1TimeMins)) {
				selectedTime1 = Long.parseLong(f1TimeSecs) + (Long.parseLong(f1TimeMins) * 60);
				if (runTime > selectedTime1) {
					f1 = new File(t1);
				} else {
					Tools.displayError("Selected audio play time starts after video has finished");
				}
			} else {
				Tools.displayError("Invalid time selection");
			}
		}


		if ((f1 != null) && f1.isFile()) {
			// Swingworker goes here
			String offset = Tools.longTimeToString(selectedTime1);
			File f = new File(IOHandler.TmpDirectory+"preview.avi");
			if (!isPreview){
			JFileChooser jfc = Tools.ReturnConfirmationChooser(true);
			jfc.showSaveDialog(null);
			f = jfc.getSelectedFile(); 
			}
			String outVidPath = null;
			if (f != null){
			outVidPath = f.getAbsolutePath();
			}
			if (outVidPath == null) {
				return;
			} else {
				if (!Tools.hasExtension(outVidPath)){
					outVidPath += ".avi";
				}
				ProgressBarFrame.pbFrame.setLocationRelativeTo(null);
				ProgressBarFrame.pbFrame.setVisible(true);
				AddAudio aa = new AddAudio(MainFrame.mFrame.chosenVideoPath, f1.getAbsolutePath(), offset,
						outVidPath, isMixing, isPreview);
				aa.execute();
			}
		}
	}
}
