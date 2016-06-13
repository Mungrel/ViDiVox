package tools;


import java.io.File;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import frames.MainFrame;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/*
 * A class for arbitrary methods.
 * Keeping button logic clean and simple
 */
public class Tools {

	static EmbeddedMediaPlayerComponent mediaPlayerComponent = null;
	static File lastDir = null;
	static String festID = null;

	public static File openFile() {

		JFileChooser jfc = Tools.ReturnConfirmationChooser(null);
		if (lastDir != null) {
			jfc.setCurrentDirectory(lastDir);
		}
		jfc.showOpenDialog(null);
		File f = jfc.getSelectedFile();
		if (f != null) {
			lastDir = f.getParentFile();
		}
		return f;

	}

	// displays the time in a nice format, no in milliseconds
	public static String LongToTime(long length) {
		length = length / 1000; // as was in ms, get it down to seconds
		String toReturn = "";
		int minutes = (int) (length / 60);
		int seconds = (int) (length % 60);
		if (minutes - 10 < 0)
			toReturn += "0";
		toReturn += minutes + ":";
		if (seconds - 10 < 0)
			toReturn += "0";
		toReturn += seconds;
		return toReturn;
	}

	public static EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		if (mediaPlayerComponent == null) {
			mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		}
		return mediaPlayerComponent;

	}

	public static void displayInfo(String msg) {
		JOptionPane.showMessageDialog(null, msg, "FYI", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void displayError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static File openMP3File() {
		JFileChooser jfc = Tools.ReturnConfirmationChooser(null);
		jfc.setSelectedFile(new File(IOHandler.Mp3Directory + "Audio"));
		if (lastDir != null) {
			jfc.setCurrentDirectory(lastDir);
		}
		// Same as other file chooser, but only allows mp3 files
		FileFilter ff = new FileNameExtensionFilter("MP3 File", "mp3");

		jfc.setFileFilter(ff);
		int res = jfc.showOpenDialog(null);
		File f = jfc.getSelectedFile();
		if (res == JFileChooser.CANCEL_OPTION)
			f = null;
		if (f != null) {
			lastDir = f.getParentFile();
		}
		return f;

	}

	/*
	 * Code for overwrite confirmation within JFileChooser. Retrieved from:
	 * http://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation
	 * -dialog On 23/9/15
	 */
	@SuppressWarnings("serial")
	public static JFileChooser ReturnConfirmationChooser(Boolean isVideo) {
		JFileChooser jfc = new JFileChooser() {
			@Override
			public void approveSelection() {
				File f = getSelectedFile();
				if (f.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
							JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		String saveTo = "";
		if (isVideo == null) {
			// generic file no extension
			return jfc;
		} else if (isVideo) { // saving a video file, thus give it a default
								// name
			// is video
			int len = new File(IOHandler.VideoDirectory).listFiles().length;
			saveTo = IOHandler.VideoDirectory + "myVideo" + len + ".avi";
		} else {
			// is mp3
			int len = new File(IOHandler.Mp3Directory).listFiles().length;
			saveTo = IOHandler.Mp3Directory + "myAudio" + len + ".mp3";
		}
		jfc.setSelectedFile(new File(saveTo));
		return jfc;

	}

	public static void writeTextToFile(String text, String fileName) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(IOHandler.TmpDirectory + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pw.write(text);
		pw.close();

	}

	public static boolean hasExtension(String s) {
		return (s.split(File.separator)[s.split(File.separator).length - 1].contains("."));
	}

	public static boolean doYesNoDialog(String msg) {
		int choice = JOptionPane.showConfirmDialog(null, msg, "Warning!", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	public static void setPlayingVideo(File videoToPlay) {
		String videoPath = videoToPlay.getAbsolutePath();
		MainFrame.mFrame.chosenVideoPath = videoPath;
		MainFrame.mFrame.theVideo.prepareMedia(videoPath);

	}

	public static boolean isAllDigits(String s) {
		if (s.matches("[0-9]+")) {
			return true;
		} else {
			return false;
		}
	}

	public static String longTimeToString(long time) {
		long secs = time;
		long mins = 0;
		long hours = 0;
		if (secs >= 60) {
			secs %= 60;
			mins = secs / 60;
		}
		if (mins >= 60) {
			mins %= 60;
			hours = mins / 60;
		}
		String strHrs;
		String strMins;
		String strSecs;
		if (hours > 9) {
			strHrs = "" + hours;
		} else {
			strHrs = "0" + hours;
		}
		if (mins > 9) {
			strMins = "" + mins;
		} else {
			strMins = "0" + mins;
		}
		if (secs > 9) {
			strSecs = "" + secs;
		} else {
			strSecs = "0" + secs;
		}
		return strHrs + ":" + strMins + ":" + strSecs;

	}
}
