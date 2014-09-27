package ca.ualberta.taskchecker;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

//Container object for holding and managing Tasks
public class TaskHolder {
	
	private static final String FILENAME = "tasks.json"; //file for tasks
	private static TaskHolder taskHolder;
	private ArrayList<Task> tasks; //tasks collection
	private DataManager manager;
	private Context context;
	
	//Constructor with context because DataManager needs context to save and load
	private TaskHolder(Context appContext) {
		context = appContext;
		manager = new DataManager(context, FILENAME);
		try {
			tasks = manager.loadTasks(false);
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

	
	public String numToDoChecked() {
		int count = 0;
		for (Task t : tasks) {
			if (t.isArchived() == false) {
				if (t.isComplete() == true) {
					count = count + 1;
				}
			}
		}
		return Integer.toString(count);
	}
	
	public String numArchivedChecked() {
		int count = 0;
		for (Task t : tasks) {
			if (t.isArchived() == true) {
				if (t.isComplete() == true) {
					count = count + 1;
				}
			}
		}
		return Integer.toString(count);
	}
	public String numToDoUnchecked() {
		int count = 0;
		for (Task t : tasks) {
			if (t.isArchived() == false) {
				if (t.isComplete() == false) {
					count = count + 1;
				}
			}
		}
		return Integer.toString(count);
	}
	
	public String numArchivedUnchecked() {
		int count = 0;
		for (Task t : tasks) {
			if (t.isArchived() == true) {
				if (t.isComplete() == false) {
					count = count + 1;
				}
			}
		}
		return Integer.toString(count);
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
			manager.saveTasks(tasks, false);
			return true;
		} catch (Exception e) {
			return false;
		}
	}	
}
