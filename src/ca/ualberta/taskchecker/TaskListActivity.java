package ca.ualberta.taskchecker;

import android.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new TaskListFragment();
	}

}
