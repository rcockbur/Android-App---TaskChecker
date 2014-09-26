package ca.ualberta.taskchecker;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

//Main data model for application
public class Task {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_COMPLETE = "complete";
	private static final String JSON_ARCHIVED = "archived";
	private UUID id;
	private String title;
	private boolean complete;
	private boolean archived;
	
	//Constructor for new Task
	public Task(){
		id = UUID.randomUUID();
	}
	
	//Constructor when loading from storage
	public Task(JSONObject json) throws JSONException {
		id = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			title = json.getString(JSON_TITLE);
		}
		complete = json.getBoolean(JSON_COMPLETE);
		archived = json.getBoolean(JSON_ARCHIVED);
	}
	
	//getters and setters
	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	//Creates JSON object and saves Task attributes to it
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, id.toString());
		json.put(JSON_TITLE, title);
		json.put(JSON_COMPLETE, complete);
		json.put(JSON_ARCHIVED,  archived);
		return json;
		}
	
}
