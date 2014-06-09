package eu.sathra.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import eu.sathra.ai.context.AIContext;
import eu.sathra.io.annotations.Defaults;
import eu.sathra.io.annotations.Deserialize;
import eu.sathra.util.Log;
import eu.sathra.util.TreeNode;

public class ConcurrentTask extends Task {

	private final class TaskWrapper implements Callable<TaskResult> {

		private Task mTask;
		private AIContext mContext;
		private long mTime;
		private long mDelta;

		public TaskWrapper(Task task) {
			mTask = task;
		}
		
		public void setArguments(AIContext context, long time, long delta) {
			mContext = context;
			mTime = time;
			mDelta = delta;
		}

		@Override
		public TaskResult call() throws Exception {
			// TODO Auto-generated method stub
			return mTask.execute(mContext, mTime, mDelta);
		}

	}

	private ExecutorService mExecutor;
	private Set<TaskWrapper> mWrappers;

	@Deserialize("children")
	@Defaults(Deserialize.NULL)
	public ConcurrentTask(Task[] children) {
		super(children);
	}
	
	@Override
	public void addChild(TreeNode<Task> child) {
		super.addChild(child);
		updateWrappers();
	}
	
	@Override
	public void addChildren(TreeNode<Task>[] children) {
		super.addChildren(children);
		updateWrappers();
	}
	
	@Override
	public void removeChild(TreeNode<Task> child) {
		super.removeChild(child);
		updateWrappers();
	}
	
	private void updateWrappers() {
		mWrappers = new HashSet<TaskWrapper>();
		mWrappers.clear();
		
		for (TreeNode<Task> child : getChildren()) {
			mWrappers.add(new TaskWrapper((Task) child));
		}
		
		mExecutor = Executors.newFixedThreadPool(mWrappers.size());
	}

	@Override
	public TaskResult execute(AIContext context, long time, long delta) {

		for (TaskWrapper wrapper : mWrappers) {
			wrapper.setArguments(context, time, delta);
		}
		
		try {
			List<Future<TaskResult>> results = mExecutor.invokeAll(mWrappers);

			for (Future<TaskResult> result : results) {
				TaskResult pingResult = result.get();
				// TODO
			}
		} catch (InterruptedException e) {
			Log.error("InterruptedException while executing children."); // executing children ;____;
		} catch(ExecutionException e) {
			e.printStackTrace();
			Log.error("ExecutionException while executing children.");
		}

		return TaskResult.FALSE;
	}

}
