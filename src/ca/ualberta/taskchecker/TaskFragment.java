package ca.ualberta.taskchecker;

import java.util.UUID;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

//Fragment for displaying a single Task and some options for editing it
public class TaskFragment extends Fragment {
	
	public static final String EXTRA_TASK_ID = "ca.ualberta.android.taskchecker.task_id";
	private Task task;
	
	//Views
	private EditText titleField;
	private CheckBox completeCheckBox;
	private CheckBox archivedCheckBox;
	private Button emailButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//gets taskId from bundle and uses it to get appropriate Task
		UUID taskId = (UUID)getArguments().getSerializable(EXTRA_TASK_ID);
		setHasOptionsMenu(true); //tells fragment manager that the fragment will implement menu callbacks
		task = TaskHolder.get(getActivity()).getTask(taskId);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		TaskHolder.get(getActivity()).saveTasks(); //saves to storage when paused
	}

	//navigates up app hierarchy when pressed
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	//inflates views and sets listeners for EditText field and CheckBoxes
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_task, parent, false);
		if (NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		//Sets up EditText field
		titleField = (EditText)v.findViewById(R.id.task_title);
		titleField.setText(task.getTitle());
		titleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				task.setTitle(c.toString());
			}
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {
			}
			public void afterTextChanged(Editable c) {
			}
		});
		//Sets up first checkbox
		completeCheckBox = (CheckBox)v.findViewById(R.id.task_complete);
		completeCheckBox.setChecked(task.isComplete());
		completeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				task.setComplete(isChecked);
			}
		});
		//Sets up second checkbox
		archivedCheckBox = (CheckBox)v.findViewById(R.id.task_archived);
		archivedCheckBox.setChecked(task.isArchived());
		archivedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				task.setArchived(isChecked);
				//TaskHolder.get(getActivity()).toggleArchived(task.getId());
			}
		});
		emailButton = (Button)v.findViewById(R.id.emailButton);
		emailButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//code for email obtained from StackOverflow user fiXedd
				//URL=http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
				
				//Construct message body
				String completeMessage = "is not complete";
				String archivedMessage = "is not archived";
				if (task.isComplete() == true) {
					completeMessage = "is complete";
				}
				if (task.isArchived() == true) {
					archivedMessage = "is archived";
				}
				String emailBody = "Your task is to " + task.getTitle() + ". Task" + completeMessage
						+ "and" + archivedMessage;
				
				//Create intent for starting email client. Pass along task info in EXTRA
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
				i.putExtra(Intent.EXTRA_SUBJECT, "");
				i.putExtra(Intent.EXTRA_TEXT, emailBody);
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		
		
		return v;
	}
	
	//creates a fragment with bundled arguments
	//called by TaskActivity
	public static TaskFragment newInstance(UUID taskId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, taskId);
		TaskFragment fragment = new TaskFragment();
		fragment.setArguments(args);
		return fragment;
	}
}