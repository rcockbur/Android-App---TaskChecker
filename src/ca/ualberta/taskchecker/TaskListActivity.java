package ca.ualberta.taskchecker;

import android.app.Fragment;

//This activity hosts TaskListFragment
public class TaskListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new TaskListFragment();
	}

}
