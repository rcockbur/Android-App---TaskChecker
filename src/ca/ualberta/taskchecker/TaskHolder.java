package ca.ualberta.taskchecker;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

//Container object for holding and managing Tasks
public class TaskHolder {
	
	private static final String FILENAME = "tasks.json";
	private static TaskHolder taskHolder;
	private ArrayList<Task> tasks; 
	private DataManager manager;
	private Context context;
	
	//Constructor with context because DataManager needs context to save and load
	private TaskHolder(Context appContext) {
		context = appContext;
		manager = new DataManager(context, FILENAME);
		try {
			tasks = manager.loadTasks();
		} catch (Exception e) {
			tasks = new ArrayList<Task>();
		}
	}
	
	public ArrayList<Task> getTasks() {
		return tasks;
	}
	
	public Task getTask(UUID id) {
		for (Task t : tasks) {
			if (t.getId().equals(id))
				return t;
		}
		return null;
	}
	
	public void addTask(Task t) {
		tasks.add(t);
	}
	
	public void deleteTask(Task t) {
		tasks.remove(t);
	}
	
	//Only calls constructor the first time because class is Singleton
	public static TaskHolder get(Context c) {
		if (taskHolder == null) {
			taskHolder = new TaskHolder(c.getApplicationContext());
		}
		return taskHolder;
	}
	
	public boolean saveTasks() {
		try {
			manager.saveTasks(tasks);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	

	
}
