package ca.ualberta.taskchecker;

import java.util.UUID;

import android.app.Fragment;

//This activity hosts TaskFragment
public class TaskActivity extends SingleFragmentActivity {

	//Method for creating fragment with taskId as an EXTRA
	@Override
	protected Fragment createFragment() {
		UUID taskId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		return TaskFragment.newInstance(taskId);
		
	}
}

