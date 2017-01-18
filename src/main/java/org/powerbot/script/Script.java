package org.powerbot.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;
import java.util.List;

/**
 * Script
 * The base interface of a script.
 */
public interface Script extends EventListener {
	/**
	 * Returns the execution queue.
	 *
	 * @param state the state being invoked
	 * @return a sequence of {@link java.lang.Runnable} items to process
	 */
	List<Runnable> getExecQueue(State state);

	/**
	 * State
	 * All possible runtime states for a script.
	 */
	enum State {
		START, SUSPEND, RESUME, STOP
	}

	/**
	 * Controller
	 * A controller for a {@link Script} which invokes and determines state changes.
	 */
	interface Controller extends Suspendable, Stoppable {
		/**
		 * Adds a {@link java.lang.Runnable} to the executor.
		 *
		 * @param e a runnable to be executed
		 * @return {@code true} if the runnable was added, otherwise {@code false}
		 */
		boolean offer(Runnable e);

		/**
		 * Returns the primary {@link org.powerbot.script.AbstractScript} running with this {@link org.powerbot.script.Script.Controller}.
		 *
		 * @return the primary {@link org.powerbot.script.AbstractScript}
		 */
		AbstractScript script();
	}

	/**
	 * Manifest
	 * A {@link Script} descriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@interface Manifest {
		/**
		 * The human-friendly name.
		 *
		 * @return the name
		 */
		String name();

		/**
		 * The description, which should be 140 characters or less.
		 *
		 * @return the description
		 */
		String description();

		/**
		 * A series of key=value pairs separated by semicolons (;) or newlines,
		 * e.g. {@code "hidden=true;topic=1234"}.
		 *
		 * @return the properties
		 */
		String properties() default "";
	}
}
