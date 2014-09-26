package ca.ualberta.taskchecker;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

//Fragment for displaying list of Tasks with CheckBox's indicating 
//state of 'complete' attribute. Task items respond to short and long clicks
public class TaskListFragment extends ListFragment {
	
	private ArrayList<Task> tasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.tasks_title);
		tasks = TaskHolder.get(getActivity()).getTasks();
		TaskAdapter adapter = new TaskAdapter(tasks);
		setListAdapter(adapter);
	}
	
	//Inflates views and creates listener for context menu
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	View v = super.onCreateView(inflater, parent, savedInstanceState);
	ListView listView = (ListView)v.findViewById(android.R.id.list);  
	listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);          //Allows for multiple item selection
	listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {   //Creates listView listener
		
		public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {} //not used
		
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.task_list_item_context, menu);
			return true;
		}
		
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {  //not used
			return false;
		}
		
		//deletes selected Tasks when clicked
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
				case R.id.menu_item_delete_task:
					TaskAdapter adapter = (TaskAdapter)getListAdapter();
					TaskHolder taskHolder = TaskHolder.get(getActivity());
					for (int i = adapter.getCount() - 1; i >= 0; i--) {
						if (getListView().isItemChecked(i)) {
							taskHolder.deleteTask(adapter.getItem(i));
						}
					}
					mode.finish();
					adapter.notifyDataSetChanged(); //Update listView
					return true;
				default:
					return false;
			}
		}
		
		public void onDestroyActionMode(ActionMode mode) { //not used
		}
	});
	return v;
	}
	
	//Updates listView when resumed
	@Override
	public void onResume() {
		super.onResume();
		((TaskAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	//inflates optionsMenu
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.fragment_task_list, menu);
	}
	
	//Creates new task when optionsMenu item is clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_task:
				Task crime = new Task();
				TaskHolder.get(getActivity()).addTask(crime);
				Intent i = new Intent(getActivity(), TaskActivity.class);
				i.putExtra(TaskFragment.EXTRA_TASK_ID, crime.getId());
				startActivityForResult(i, 0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	//Creates TaskListActivity with an EXTRA containing selected item's Id
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task t = ((TaskAdapter)getListAdapter()).getItem(position);
		Intent i = new Intent(getActivity(), TaskActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, t.getId());
		startActivity(i);
	}
	
	//Custom adapter for linking Task objects to listView
	private class TaskAdapter extends ArrayAdapter<Task> {
		
		public TaskAdapter(ArrayList<Task> tasks) {
			super(getActivity(), 0, tasks);
		}
		
		//If a View object is passed in, it modifies that one instead of
		//creating a new one.
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task, null);
			}
			Task t = getItem(position);
			
			TextView titleTextView = (TextView)convertView.findViewById(R.id.task_list_item_titleTextView);
			titleTextView.setText(t.getTitle());
			
			CheckBox completeCheckBox = (CheckBox)convertView.findViewById(R.id.task_list_item_completeCheckBox);
			completeCheckBox.setChecked(t.isComplete());
			
			return convertView;	
		}
	}
}
