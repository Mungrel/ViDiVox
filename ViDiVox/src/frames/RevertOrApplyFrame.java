package frames;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import tools.Tools;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class RevertOrApplyFrame extends JFrame {

	private JPanel contentPane;
	public static RevertOrApplyFrame raFrame = new RevertOrApplyFrame();
	JButton btnSave;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RevertOrApplyFrame frame = new RevertOrApplyFrame();
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
	public RevertOrApplyFrame() {
		setResizable(false);
		setTitle("Revert?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 271, 97);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRevert = new JButton("Revert");
		btnRevert.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				//Revert button clicked
				Tools.setPlayingVideo(new File(MainFrame.mFrame.oldVidPath));
				MainFrame.mFrame.isBeingPreviewed = false;
				raFrame.setVisible(false);
				MainFrame.mFrame.clickPlay();
				MainFrame.mFrame.button.setBackground(Color.WHITE);
				
			}
		});
		btnRevert.setBackground(Color.WHITE);
		btnRevert.setBounds(12, 39, 117, 25);
		contentPane.add(btnRevert);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Save button clicked
				JFileChooser jfc = Tools.ReturnConfirmationChooser(true);
				jfc.showSaveDialog(null);
				File saveDir = jfc.getSelectedFile();
				if (saveDir == null){
					return;
				} else {
					Path savePath = saveDir.toPath(); 
					Path fileToSave = new File(MainFrame.mFrame.chosenVideoPath).toPath();
					try {
						Files.copy(fileToSave, savePath, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e1) {
						Tools.displayError("Failed to save video to\n"+saveDir.toString());
					}
					
				}
				raFrame.setVisible(false);
				MainFrame.isBeingPreviewed = false;
				MainFrame.mFrame.button.setBackground(Color.WHITE);
			}
		});
		btnSave.setBackground(Color.WHITE);
		btnSave.setBounds(141, 39, 117, 25);
		contentPane.add(btnSave);
		
		JLabel lblDoYouWant = new JLabel("Do you want to save your edit?");
		lblDoYouWant.setBounds(12, 12, 258, 15);
		contentPane.add(lblDoYouWant);
	}
}
