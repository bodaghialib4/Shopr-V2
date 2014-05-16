package com.uwetrottmann.shopr.eval;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import com.uwetrottmann.shopr.ui.basic.MainActivityBasic;
import com.uwetrottmann.shopr.ui.explanation.MainActivityExplanation;

public class VariantTask {
	private Task task;
	private Variant variant;
	private TaskListener listener;

	private VariantTask(Task task, Variant variant, TaskListener listener) {
		this.task = task;
		this.variant = variant;
		this.listener = listener;
	}

	public static VariantTask[] all() {
		return new VariantTask[] { ScrutabilityForExplained(),
				ScrutabilityForBase() };
	}

	public static VariantTask ScrutabilityForExplained() {
		return new VariantTask(Task.SCRUTABILITY, Variant.EXPLAINED,
				new TaskListener() {

					@Override
					public void onStart(Context context) {
						context.startActivity(new Intent(context,
								MainActivityExplanation.class));
					}
				});
	}

	public static VariantTask ScrutabilityForBase() {
		return new VariantTask(Task.SCRUTABILITY, Variant.BASE,
				new TaskListener() {
					@Override
					public void onStart(Context context) {
						context.startActivity(new Intent(context,
								MainActivityBasic.class));
					}
				});
	}

	public static String[] allTaskNames() {
		ArrayList<String> taskNames = new ArrayList<String>();
		for (VariantTask taskVar : all()) {
			taskNames.add(taskVar.toString());
		}
		return taskNames.toArray(new String[all().length]);
	}

	public TaskListener getTaskListener() {
		return listener;
	}

	public String toString() {
		return task.getName() + "-" + variant.getName();
	}

	public enum Task {
		SCRUTABILITY("Scrutability");

		private String name;

		Task(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum Variant {
		EXPLAINED("Explained"), BASE("Base");

		private String name;

		Variant(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public interface TaskListener {
		public void onStart(Context context);
	}

}
