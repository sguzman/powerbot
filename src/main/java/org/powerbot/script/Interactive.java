package org.powerbot.script;

import java.awt.Point;
import java.awt.Polygon;

/**
 * Interactive
 * An entity which can be interacted with through direct clicking and menu options.
 */
public interface Interactive extends Targetable, Validatable, Viewable, Drawable {
	/**
	 * Finds the centroid of the entity within the game screen.
	 *
	 * @return the centroid if the entity is on the screen; null otherwise
	 */
	Point centerPoint();

	/**
	 * Hovers the target and compensates for movement.
	 *
	 * @return <tt>true</tt> if the mouse is within the target; otherwise <tt>false</tt>
	 */
	boolean hover();

	/**
	 * Clicks the target and compensates for movement. Does not check intent or expected result (mouse cross-hair).
	 *
	 * @return <tt>true</tt> if the click was executed; otherwise <tt>false</tt>
	 */
	boolean click();

	/**
	 * Clicks the target and compensates for movement. Does not check intent or expected result (mouse cross-hair).
	 *
	 * @param left <tt>true</tt> to click left, <tt>false</tt> to click right
	 * @return <tt>true</tt> if the click was executed; otherwise <tt>false</tt>
	 */
	boolean click(boolean left);

	/**
	 * Clicks the target and compensates for movement. Does not check intent or expected result (mouse cross-hair).
	 *
	 * @param button the desired mouse button to press
	 * @return <tt>true</tt> if the click was executed; otherwise <tt>false</tt>
	 */
	boolean click(int button);

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean click(String action);

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param option the option to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean click(String action, String option);

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param c the menu command to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean click(Filter<? super MenuCommand> c);

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean interact(String action);

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param option the option to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean interact(String action, String option);

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click (as defined).
	 * When auto is set to false, the method forcibly right clicks before searching for menu options.
	 * This is useful when precision clicking is required and the option is always in the menu (not up-text).
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param auto   <tt>true</tt> is normal behavior, <tt>false</tt> forces right click
	 * @param action the action to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean interact(boolean auto, String action);

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click (as defined).
	 * When auto is set to false, the method forcibly right clicks before searching for menu options.
	 * This is useful when precision clicking is required and the option is always in the menu (not up-text).
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param auto   <tt>true</tt> is normal behavior, <tt>false</tt> forces right click
	 * @param action the action to look for
	 * @param option the option to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean interact(boolean auto, String action, String option);

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param c the menu command to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean interact(Filter<? super MenuCommand> c);

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click (as defined).
	 * When auto is set to false, the method forcibly right clicks before searching for menu options.
	 * This is useful when precision clicking is required and the option is always in the menu (not up-text).
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param auto <tt>true</tt> is normal behavior, <tt>false</tt> forces right click
	 * @param c    the menu command to look for
	 * @return <tt>true</tt> if the mouse was clicked, otherwise <tt>false</tt>
	 */
	boolean interact(boolean auto, Filter<? super MenuCommand> c);

	/**
	 * Clicks the target and compensates for movement. Does not check intent.
	 *
	 * @param result the crosshair to check against after interaction
	 * @return <tt>true</tt> if the click was executed and <code>ctx.game.crosshair() == result</code>; otherwise <tt>false</tt>
	 */
	boolean click(Crosshair result);

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent.
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param result the crosshair to check against after interaction
	 * @return <tt>true</tt> if the mouse was clicked and <code>ctx.game.crosshair() == result</code>, otherwise <tt>false</tt>
	 */
	boolean click(String action, Crosshair result);

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent.
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param option the option to look for
	 * @param result the crosshair to check against after interaction
	 * @return <tt>true</tt> if the mouse was clicked and <code>ctx.game.crosshair() == result</code>, otherwise <tt>false</tt>
	 */
	boolean click(String action, String option, Crosshair result);

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent.
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param c      the menu command to look for
	 * @param result the crosshair to check against after interaction
	 * @return <tt>true</tt> if the mouse was clicked and <code>ctx.game.crosshair() == result</code>, otherwise <tt>false</tt>
	 */
	boolean click(Filter<? super MenuCommand> c, Crosshair result);

	/**
	 * Sets the boundaries of this entity utilizing an array.
	 *
	 * @param arr {x1, x2, y1, y2, z1, z2}
	 */
	void bounds(final int[] arr);

	/**
	 * The translated model triangles.
	 *
	 * @return the translated model triangles
	 */
	Polygon[] triangles();
}
