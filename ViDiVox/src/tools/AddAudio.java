package tools;

import java.awt.Color;
import java.io.File;

import javax.swing.SwingWorker;

import frames.MainFrame;
import frames.ProgressBarFrame;

public class AddAudio extends SwingWorker<Void, Void> {

	String videoPath;
	String audioPath;
	String audioOffset;
	String outVidPath;
	boolean isMixing = false;
	boolean isPreview = false;

	// Constructor

	public AddAudio(String videoPath, String audioPath, String audioOffset, String outVidPath, boolean isMixing,
			boolean isPreview) {
		this.videoPath = videoPath;
		this.audioPath = audioPath;
		this.audioOffset = audioOffset;
		this.outVidPath = outVidPath;
		this.isMixing = isMixing;
		this.isPreview = isPreview;
	}

	@Override
	protected Void doInBackground() throws Exception {
		if (isPreview) {
			outVidPath = IOHandler.TmpDirectory + "preview.avi";
		}
		String tmpMp3Dir = IOHandler.TmpDirectory + "tmp.mp3";
		String tmpVidDir = IOHandler.TmpDirectory + "tmp.avi";
		String cmd1 = "ffmpeg -y -i " + videoPath + " " + tmpMp3Dir;
		String cmd2 = "ffmpeg -y -i " + videoPath + " -itsoffset " + audioOffset + " -i " + audioPath
				+ " -shortest -map 0:0 -map 1:0 -c:v copy -preset ultrafast -async 1 " + tmpVidDir;
		String cmd3 = "ffmpeg -y -i " + tmpVidDir + " -i " + tmpMp3Dir + " -filter_complex amix=inputs=2 " + outVidPath;
		String cmd4 = "ffmpeg -y -i " + videoPath + " -itsoffset " + audioOffset + " -i " + audioPath
				+ " -shortest -map 0:0 -map 1:0 -c:v copy -preset ultrafast -async 1 " + outVidPath;

		ProcessBuilder pb1 = new ProcessBuilder("/bin/bash", "-c", cmd1);
		ProcessBuilder pb2 = new ProcessBuilder("/bin/bash", "-c", cmd2);
		ProcessBuilder pb3 = new ProcessBuilder("/bin/bash", "-c", cmd3);
		ProcessBuilder pb4 = new ProcessBuilder("/bin/bash", "-c", cmd4);

		if (isMixing) {
			Process p1 = pb1.start();
			p1.waitFor();
			Process p2 = pb2.start();
			p2.waitFor();
			Process p3 = pb3.start();
			p3.waitFor();
		} else {
			Process p4 = pb4.start();
			p4.waitFor();
		}

		return null;
	}

	@SuppressWarnings("static-access")
	protected void done() {
		ProgressBarFrame.pbFrame.setVisible(false);
		MainFrame.mFrame.hasFocus();
		MainFrame.mFrame.toFront();
		if (isPreview) {
			MainFrame.mFrame.oldVidPath = MainFrame.mFrame.chosenVideoPath;
		}
		Tools.setPlayingVideo(new File(outVidPath));
		MainFrame.mFrame.clickPlay();
		if (isPreview) {
			MainFrame.mFrame.button.setBackground(Color.CYAN);
		}
	}

}
