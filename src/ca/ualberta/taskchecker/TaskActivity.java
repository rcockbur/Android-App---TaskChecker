package ca.ualberta.taskchecker;

import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.Menu;

public class TaskActivity extends SingleFragmentActivity {

	//Method for creating and returning fragment with a bundle
	@Override
	protected Fragment createFragment() {
		UUID taskId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		return TaskFragment.newInstance(taskId);
		
	}
}

