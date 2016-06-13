package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;

import java.lang.reflect.Field;

import frames.CommentaryFrame;
import frames.MultipleAudioFrame;

public class Audio {

	public static void killAllFestProc(int pid) {

		if (pid != 0) {
			String cmd = "pstree -p " + pid;
			/*
			 * Using the pstree bash command, we find the process named "aplay"
			 * associated with festival which is what makes the sound. The pid
			 * of this process is found and killed using the "kill -9" command
			 */
			ProcessBuilder pb2 = new ProcessBuilder("/bin/bash", "-c", cmd);
			String line = null;
			String s = "";

			try {
				Process p2 = pb2.start();
				InputStream stdout = p2.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
				while ((line = br.readLine()) != null) {
					s += line;
				}
				int x = s.indexOf("play");
				String sub = s.substring(x);
				int length = sub.length();
				String result = "";
				for (int i = 0; i < length; i++) {
					Character character = sub.charAt(i);
					String s2 = character.toString();
					if (s2.equals(")")) {
						break;
					}
					if (Character.isDigit(character)) {
						result += character;
					}
				}
				cmd = "kill -9 " + result;
				ProcessBuilder pb3 = new ProcessBuilder("/bin/bash", "-c", cmd);
				pb3.start();

			} catch (IOException e) {
				System.out.println("Error killing process: " + pid);
			}
		}
	}

	public static void saveFestToMP3(File utteranceWavFile) {
		IOHandler.CheckPaths();

		String wavFullPath = utteranceWavFile.getAbsolutePath();
		JFileChooser jfc = Tools.ReturnConfirmationChooser(false);
		int result = jfc.showSaveDialog(null);
		File mp3 = jfc.getSelectedFile();
		if (result == JFileChooser.CANCEL_OPTION) {
			return;
		}
		String mp3FullPath = mp3.getAbsolutePath();

		if (!Tools.hasExtension(mp3FullPath)) {
			mp3FullPath += ".mp3";
		}
		String cmd2 = "ffmpeg -i " + wavFullPath + " " + mp3FullPath;

		ProcessBuilder pb2 = new ProcessBuilder("/bin/bash", "-c", cmd2);

		try {
			Process proc2 = pb2.start();
			proc2.waitFor();
		} catch (Exception e) {
			Tools.displayError("Error saving speech to MP3");
		}
		if (CommentaryFrame.chckbxApplyThisSpeech.isSelected()) {
			// Checkbox is selected upon save button click
			MultipleAudioFrame.maFrame.setLocationRelativeTo(CommentaryFrame.cmFrame);
			MultipleAudioFrame.maFrame.textField.setText(mp3FullPath);
			MultipleAudioFrame.maFrame.setVisible(true);
		}
	}

	public static int speakFestival(File SCMFile) {

		String cmd = "festival -b "+SCMFile.getAbsolutePath();

		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		int pid = 0;
		try {

			Process p1 = pb.start();
			Field f = p1.getClass().getDeclaredField("pid");
			f.setAccessible(true);
			pid = f.getInt(p1);

		} catch (Exception e) {
			Tools.displayError("Error using festival speech");
			return pid;
		}
		return pid;
	}
	
	public static File generateSCMFestFile(String text, double talkSpeed, boolean toSave){
		double speed;
		if (talkSpeed == 1.5){
			speed = 0.66;
		} else {
			speed = 1/talkSpeed;
		}
		String cmd = "(Parameter.set 'Duration_Stretch "+speed+")";
		if (!toSave){
		cmd += "\n(SayText \""+text+"\")";
		}
		if (toSave){
			cmd +="\n(utt.save.wave (SayText \""+text+"\") \""+IOHandler.TmpDirectory+"utt.wav\""+" 'riff) ";
		}
		Tools.writeTextToFile(cmd, "FestScheme.scm");
		File f = new File(IOHandler.TmpDirectory+"FestScheme.scm");
		return f;
	}

}
