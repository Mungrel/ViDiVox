package frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;

import tools.IOHandler;
import tools.Tools;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JSlider;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	public static MainFrame mFrame;
	public final EmbeddedMediaPlayer theVideo = Tools.getMediaPlayerComponent().getMediaPlayer();
	private boolean videoLoaded = false;
	private float videoPlayRate = 1.0f;
	public String chosenVideoPath = null;
	public JButton button;
	final JSlider slider = new JSlider(JSlider.HORIZONTAL);
	JMenuItem mntmOpenAVideo;
	public static String oldVidPath = null;
	public static boolean isBeingPreviewed = false;
	final JButton btnPlay = new JButton("");

	private Component volalignment, timealignment; // this is a 'hack' for
													// flayouts, which will
													// creates the volume button
													// being 'pushed' to the
													// right

	// For toggling pause/play button
	private boolean videoPlaying = false;

	// For the audio controller
	private boolean isMuted = false;
	private boolean reverse = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NativeDiscovery nd = new NativeDiscovery();
					nd.discover();
					mFrame = new MainFrame();
					mFrame.setLocationRelativeTo(null); // centre screen
					mFrame.setVisible(true);
					// Set min dimension to current
					mFrame.setMinimumSize(new Dimension(mFrame.getWidth(), mFrame.getHeight()));
					IOHandler.CheckPaths();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame. Button icons retrieved from:
	 * http://www.tdcurran.com/sites/tdcurran/images/user/Icons-in-iOS-8/audio-
	 * controls.png on 15/09/15 at 11:41 a.m All of this code is self-generated
	 * from the window builder, and is required to be inside the constructor.
	 * Meaning we were un-able to attempt to shorten this method.
	 */
	public MainFrame() {
		setTitle("ViDiVox");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 713, 607);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		JMenuBar menuBar = new JMenuBar();
		// Set min dimension to current
		menuBar.setMinimumSize(new Dimension(getWidth(), menuBar.getHeight()));
		setJMenuBar(menuBar);

		final JLabel currentTime = new JLabel("00:00"), lengthTime = new JLabel("00:00");
		final JLabel lblPlayspeedx = new JLabel("Playspeed: 1.00x");

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		btnPlay.setHorizontalAlignment(SwingConstants.LEFT);
		btnPlay.setBackground(Color.WHITE);
		btnPlay.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/play.jpg")));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Play/Pause button clicked
				if (!videoPlaying) { // was paused or stopped
					if (videoLoaded) {
						theVideo.play();
						theVideo.setRate(1.0f);
						videoPlaying = true;
						btnPlay.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/pause.jpg")));
					} else {
						Tools.displayError("You need to open something to play first!");
					}
				} else { // was playing, so pause it
					theVideo.pause();
					videoPlaying = false; // paused;
					btnPlay.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/play.jpg")));
				}
				reverse = false;
				videoPlayRate = 1;
			}
		});

		mntmOpenAVideo = new JMenuItem("Open a video");
		mntmOpenAVideo.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// Open button clicked
				File chosenFile = Tools.openFile();
				if (chosenFile != null) {
					String mediaPath = chosenFile.getAbsolutePath();
					chosenVideoPath = mediaPath;
					theVideo.prepareMedia(mediaPath);
					videoLoaded = true;
					button.setEnabled(true);
					MultipleAudioFrame.maFrame.hasBeenPreviewed = false;
					isBeingPreviewed = false;
					if (!videoPlaying)
						btnPlay.doClick();
					// Can now click save button as video is loaded
				}
			}
		});
		mnFile.add(mntmOpenAVideo);

		JMenuItem mntmCloseProgram = new JMenuItem("Close program");
		mntmCloseProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmCloseProgram);

		Component video = Tools.getMediaPlayerComponent();
		contentPane.add(video, BorderLayout.CENTER);

		JPanel bottomRowButtonsPanel = new JPanel();
		bottomRowButtonsPanel.setPreferredSize(new Dimension(this.getWidth(), 85));
		contentPane.add(bottomRowButtonsPanel, BorderLayout.SOUTH);
		bottomRowButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		slider.setUI(new MetalSliderUI() {
			protected void scrollDueToClickInTrack(int direction) {
				// this is the default behaviour, let's comment that out
				// scrollByBlock(direction);

				int value = slider.getValue();

				if (slider.getOrientation() == JSlider.HORIZONTAL) {
					value = this.valueForXPosition(slider.getMousePosition().x);
				} else if (slider.getOrientation() == JSlider.VERTICAL) {
					value = this.valueForYPosition(slider.getMousePosition().y);
				}
				slider.setValue(value);
			}
		});
		slider.setValue(0);
		slider.setPreferredSize(new Dimension(this.getWidth() - 20, 20));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JSlider slider = (JSlider) evt.getSource();
				int value = slider.getValue();
				if (slider.getValueIsAdjusting()) {
					theVideo.setTime(value * 100);
				}
			}
		});
		bottomRowButtonsPanel.add(slider);

		// To keep the slider at full width when the window resizes
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				volalignment.setPreferredSize(new Dimension(getWidth() - 640, 1));
				timealignment.setPreferredSize(new Dimension(getWidth() - 295, 1));
				slider.setPreferredSize(new Dimension(getWidth() - 20, 20));
			}
		});

		bottomRowButtonsPanel.add(currentTime);

		timealignment = Box.createHorizontalStrut(getWidth() - 295);
		bottomRowButtonsPanel.add(Box.createHorizontalStrut(54));

		bottomRowButtonsPanel.add(lblPlayspeedx);

		bottomRowButtonsPanel.add(timealignment);
		bottomRowButtonsPanel.add(lengthTime);

		bottomRowButtonsPanel.add(btnPlay);
		bottomRowButtonsPanel.add(Box.createHorizontalStrut(25));

		JButton btnStop = new JButton("");
		btnStop.setActionCommand("");
		btnStop.setIcon(new ImageIcon(((new ImageIcon(MainFrame.class.getResource("/icons/stop.png"))).getImage())
				.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		btnStop.setBackground(Color.WHITE);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				theVideo.setTime(0);
				btnPlay.doClick();
			}
		});

		JButton btnReverse = new JButton("");
		btnReverse.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/rev.jpg")));
		btnReverse.setBackground(Color.WHITE);
		btnReverse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (videoPlaying) {
					btnPlay.doClick();
				}
				if (!reverse) {
					videoPlayRate = 1f;
				}
				videoPlayRate += 1f;
				reverse = true;
			}
		});

		bottomRowButtonsPanel.add(btnReverse);
		bottomRowButtonsPanel.add(btnStop);

		// btnPause has been removed, and merged into btnPlay.

		JButton btnFastforward = new JButton("");
		btnFastforward.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/ff.jpg")));
		btnFastforward.setBackground(Color.WHITE);
		btnFastforward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Fast forward button clicked
				// Continuously fast forward until play button clicked
				if (videoPlaying) {
					btnPlay.doClick();
				}
				if (reverse) {
					reverse = false;
					videoPlayRate = 1f;
				}
				videoPlayRate += 1f;
			}
		}); //
		bottomRowButtonsPanel.add(btnFastforward);
		volalignment = Box.createHorizontalStrut(getWidth() - 640);
		final JButton btnVolume = new JButton("");
		btnVolume.setBackground(Color.WHITE);
		btnVolume.setIcon(new ImageIcon(((new ImageIcon(MainFrame.class.getResource("/icons/volume.png"))).getImage())
				.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		bottomRowButtonsPanel.add(volalignment);

		button = new JButton("...");
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				// "..." button clicked
				if (theVideo.isPlaying()) {
					btnPlay.doClick();
				}
				if (mFrame.isBeingPreviewed){
					//Video they're currently watching is a preview
					RevertOrApplyFrame.raFrame.setLocationRelativeTo(mFrame);
					RevertOrApplyFrame.raFrame.setVisible(true);
				} else {
				OptionsFrame.oFrame.setLocationRelativeTo(MainFrame.mFrame);
				OptionsFrame.oFrame.setVisible(true);
				}
			}
		});
		button.setBackground(Color.WHITE);
		bottomRowButtonsPanel.add(button);
		bottomRowButtonsPanel.add(btnVolume);

		final JSlider volSlider = new JSlider(JSlider.HORIZONTAL);
		volSlider.setUI(new MetalSliderUI() {
			protected void scrollDueToClickInTrack(int direction) {
				// this is the default behaviour, let's comment that out
				// scrollByBlock(direction);
				int value = volSlider.getValue();
				if (volSlider.getOrientation() == JSlider.HORIZONTAL) {
					value = this.valueForXPosition(volSlider.getMousePosition().x);
				} else if (volSlider.getOrientation() == JSlider.VERTICAL) {
					value = this.valueForYPosition(volSlider.getMousePosition().y);
				}
				volSlider.setValue(value);
			}
		});
		volSlider.setValue(100);
		theVideo.setVolume(100);
		volSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JSlider slider = (JSlider) evt.getSource();
				int value = slider.getValue();
				// value here
				if (value == 0) {
					btnVolume.setIcon(
							new ImageIcon(((new ImageIcon(MainFrame.class.getResource("/icons/muted.png"))).getImage())
									.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				} else if (value < 50) {
					btnVolume.setIcon(new ImageIcon(
							((new ImageIcon(MainFrame.class.getResource("/icons/volumelow.png"))).getImage())
									.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				} else {
					btnVolume.setIcon(
							new ImageIcon(((new ImageIcon(MainFrame.class.getResource("/icons/volume.png"))).getImage())
									.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				}
				theVideo.setVolume(value);
			}
		});

		btnVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isMuted) {
					int value = volSlider.getValue();
					if (value == 0) {
						btnVolume.setIcon(new ImageIcon(
								((new ImageIcon(MainFrame.class.getResource("/icons/muted.png"))).getImage())
										.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
					} else if (value < 50) {
						btnVolume.setIcon(new ImageIcon(
								((new ImageIcon(MainFrame.class.getResource("/icons/volumelow.png"))).getImage())
										.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
					} else {
						btnVolume.setIcon(new ImageIcon(
								((new ImageIcon(MainFrame.class.getResource("/icons/volume.png"))).getImage())
										.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
					}
					volSlider.setEnabled(true); // cant change volume anymore
					isMuted = false;
				} else {
					btnVolume.setIcon(
							new ImageIcon(((new ImageIcon(MainFrame.class.getResource("/icons/muted.png"))).getImage())
									.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
					isMuted = true; // toggle the mute..
					volSlider.setEnabled(false); // can change again!
				}
				theVideo.mute(isMuted);
			}
		});

		bottomRowButtonsPanel.add(volSlider);

		Timer t = new Timer(25, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (videoLoaded) {
					DecimalFormat d = new DecimalFormat();
					d.setMinimumFractionDigits(2);
					String t = "Playspeed: ";
					if (reverse)
						t += '-';
					t += d.format(videoPlayRate) + "x";
					lblPlayspeedx.setText(t); // Update the play speed label
					currentTime.setText(Tools.LongToTime(theVideo.getTime()));
					int iPos = (int) (theVideo.getTime() / 100);
					if (iPos + 5 >= slider.getMaximum()) { // Loop back to the
															// start if video
															// has ended
						theVideo.setTime(0);
						btnPlay.doClick();
					}
					if (theVideo.getMediaMeta().getLength() > 0) {
						slider.setValue(iPos);
					}
				}
			}
		});

		t.start();
		// Two different timers as I needed two different 'refresh' rates
		Timer t2 = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (videoLoaded) {
					if (theVideo.getMediaMeta().getLength() > 0) {
						slider.setMinimum(0);
						slider.setMaximum((int) (theVideo.getMediaMeta().getLength() / 100));
					}
					lengthTime.setText(Tools.LongToTime(theVideo.getMediaMeta().getLength()));
					if (videoPlayRate > 1f) {
						if (reverse) { // This reverses or fast forwrads
							theVideo.setTime(theVideo.getTime() - (int) ((videoPlayRate / (float) 5f) * 1000));
							if (theVideo.getTime() <= 0) {
								videoPlayRate = 1f;
								reverse = false;
							}
						} else {
							theVideo.setTime(theVideo.getTime() + (int) ((videoPlayRate / (float) 5f) * 1000));
						}
					}
				}
			}
		});

		t2.start();
	}
	
	public void clickPlay(){
		btnPlay.doClick();
	}
}
