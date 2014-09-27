package ca.ualberta.taskchecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

//Class used by TaskHolder to save and load an ArrayList to internal storage
public class DataManager {

	private Context context;
	private String fileName;
	
	public DataManager(Context c, String f) {
		context = c;
		fileName = f;
	}
	
	//Converts the ArrayList to JSONArray and saves to storage
	public void saveTasks(ArrayList<Task> tasks, boolean archived) throws JSONException, IOException {
		JSONArray array = new JSONArray();
		for (Task t : tasks) {
			array.put(t.toJSON());
		}
		Writer writer = null;
		
		try {
			OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	//Loads JSONArray from storage and converts to ArrayList
	public ArrayList<Task> loadTasks(boolean archived) throws IOException, JSONException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		BufferedReader reader = null;
		try {
			
			InputStream in = context.openFileInput(fileName);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for (int i = 0; i < array.length(); i++) {
				tasks.add(new Task(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (reader != null)
				reader.close();
		}
		return tasks;
	}
	
	
	
	
}
