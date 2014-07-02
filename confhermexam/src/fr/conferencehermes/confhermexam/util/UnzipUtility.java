package fr.conferencehermes.confhermexam.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.parser.Answer;
import fr.conferencehermes.confhermexam.parser.Event;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.ExamJsonParser;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.Question;

public class UnzipUtility {
	DatabaseHelper db;

	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified
	 * by destDirectory (will be created if does not exists)
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public void unzip(String zipFilePath, String destDirectory, Context context)
			throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
				zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();

		File file = new File(zipFilePath);
		if (!file.delete()) {
			new FileNotFoundException("Failed to delete file: " + file);
			Log.e("Exception", "File not found. Cannot perform delete process.");
		}

		String fileUrl = getTextFileFromDirectory(destDirectory);
		Event event = null;
		try {
			JSONObject json = getJsonFromTextFile(fileUrl, destDirectory);
			event = ExamJsonParser.parseExamData(json,
					(destDirectory + File.separator));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Store data in SQLite database

		if (event != null) {
			db = new DatabaseHelper(context);
			db.createEvent(event);

			ArrayList<Exam> exams = event.getExams();
			for (int i = 0; i < exams.size(); i++) {
				db.createExam(exams.get(i));

				ArrayList<Exercise> exercises = exams.get(i).getExercises();
				for (int j = 0; j < exercises.size(); j++) {
					db.createExercise(exercises.get(j));
					db.createExerciseFile(exercises.get(j));

					ArrayList<Question> questions = exercises.get(j)
							.getQuestions();
					for (int k = 0; k < questions.size(); k++) {
						db.createQuestion(questions.get(k));
						db.createQuestionFile(questions.get(k));

						ArrayList<Answer> answers = questions.get(k)
								.getAnswers();
						for (int l = 0; l < answers.size(); l++) {
							db.createAnswer(answers.get(l));
						}
					}
				}
			}

			db.closeDB();
		}

	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private void extractFile(ZipInputStream zipIn, String filePath)
			throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();

	}

	private String getTextFileFromDirectory(String directoryUrl)
			throws NullPointerException {
		File directory = new File(directoryUrl);
		String[] txtFiles = directory.list(new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".txt");
			}
		});

		return txtFiles[0];
	}

	private JSONObject getJsonFromTextFile(String filePath, String destDirectory)
			throws IOException, JSONException {
		File file = new File(destDirectory + File.separator + filePath);
		FileInputStream stream = new FileInputStream(file);
		String jString = null;
		FileChannel fc = stream.getChannel();
		MappedByteBuffer bb;
		try {
			try {
				bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
				jString = Charset.defaultCharset().decode(bb).toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} finally {
			stream.close();
			if (!file.delete()) {
				new FileNotFoundException("Failed to delete file: " + file);
			}
		}

		return new JSONObject(jString);
	}
}