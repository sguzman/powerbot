package org.powerbot.script.rt6;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.powerbot.bot.rt6.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;

/**
 * Widgets
 * {@link Widgets} is a static utility which provides access to the game's {@link Component}s by means of {@link Widget}s.
 * <p/>
 * {@link Widget}s are cached and are available at all times, even when not present in game.
 * {@link Widget}s must be validated before use.
 */
public class Widgets extends IdQuery<Widget> {
	public Widget[] sparseCache;

	public Widgets(final ClientContext factory) {
		super(factory);
		sparseCache = new Widget[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Widget> get() {
		final Client client = ctx.client();
		if (client == null) {
			return new ArrayList<Widget>(0);
		}
		final Object[] cache = client.getWidgets();
		if (cache == null || cache.length == 0) {
			return new ArrayList<Widget>(0);
		}
		final List<Widget> w = new ArrayList<Widget>(cache.length);
		for (int i = 0; i < cache.length; i++) {
			w.add(new Widget(ctx, i));
		}
		return w;
	}

	/**
	 * Retrieves the cached {@link Widget} for the given index.
	 *
	 * @param index the index of the desired {@link Widget}
	 * @return the {@link Widget} respective to the given index
	 */
	public synchronized Widget widget(final int index) {
		if (index < sparseCache.length && sparseCache[index] != null) {
			return sparseCache[index];
		}
		final Widget c = new Widget(ctx, index);
		final int l = sparseCache.length;
		if (index >= l) {
			sparseCache = Arrays.copyOf(sparseCache, index + 1);
			for (int i = l; i < index + 1; i++) {
				sparseCache[i] = new Widget(ctx, i);
			}
		}
		return sparseCache[index] = c;
	}

	/**
	 * Retrieves the cached {@link Component} of the given {@link Widget} index.
	 *
	 * @param index          the index of the desired {@link Widget}
	 * @param componentIndex the index of the desired {@link Component} of the given {@link Widget}
	 * @return the {@link Component} belonging to the {@link Widget} requested
	 */
	public Component component(final int index, final int componentIndex) {
		return widget(index).component(componentIndex);
	}

	/**
	 * Scrolls a component into view.
	 *
	 * @param component the {@link Component} which is desired to be visible
	 * @param bar       the {@link Component} of the scroll bar
	 * @param scroll    the scroll switch
	 * @return {@code true} if visible; otherwise {@code false}
	 */
	public boolean scroll(final Component component, final Component bar, final boolean scroll) {
		if (component == null || !component.valid()) {
			return false;
		}
		final int childrenCount;
		if (bar == null || !bar.valid() || ((childrenCount = bar.childrenCount()) != 6 && childrenCount != 7)) {
			return false;
		}
		Component pane = component;
		int id;
		int l = 0;
		while (pane.scrollHeightMax() == 0 && (id = pane.parentId()) != -1) {
			pane = ctx.widgets.component(id >> 16, id & 0xffff);
			if (++l > Component.RECURSION_DEPTH) {
				break;
			}
		}
		return pane.scrollHeightMax() != 0 && scroll(component, pane, bar, scroll);
	}

	public boolean scroll(final Component component, final Component pane, final Component bar, final boolean scroll) {
		if (component == null || !component.valid()) {
			return false;
		}
		final int childrenCount;
		if (bar == null || !bar.valid() || ((childrenCount = bar.childrenCount()) != 6 && childrenCount != 7)) {
			return false;
		}
		if (pane == null || !pane.valid() || pane.scrollHeight() == 0) {
			return false;
		}
		final Point view = pane.screenPoint();
		final int height = pane.scrollHeight();
		if (view.x < 0 || view.y < 0 || height < 1) {
			return false;
		}
		final Point pos = component.screenPoint();
		final int length = component.height();
		if (pos.y >= view.y && pos.y <= view.y + height - length) {
			return true;
		}
		final Component thumbHolder = bar.component(0);
		final Component thumb = bar.component(1);//TODO: childrenCount == 7, component = 6 not 1?
		final int thumbSize = thumbHolder.scrollHeight();
		int y = (int) ((float) thumbSize / pane.scrollHeightMax() *
				(component.relativePoint().y + Random.nextInt(-height / 2, height / 2 - length)));
		if (y < 0) {
			y = 0;
		} else if (y >= thumbSize) {
			y = thumbSize - 1;
		}
		final Point p = thumbHolder.screenPoint();
		p.translate(Random.nextInt(0, thumbHolder.width()), y);
		if (!scroll) {
			if (!ctx.input.click(p, true)) {
				return false;
			}
			Condition.sleep();
		}
		Point a;
		Component c;
		int tY = thumb.screenPoint().y;
		final long start = System.nanoTime();
		long mark = System.nanoTime();
		int scrolls = 0;
		while ((a = component.screenPoint()).y < view.y || a.y > view.y + height - length) {
			if (System.nanoTime() - start >= TimeUnit.SECONDS.toNanos(20)) {
				break;
			}
			if (scroll) {
				if (ctx.input.scroll(a.y > view.y)) {
					if (++scrolls >= Random.nextInt(5, 9)) {
						Condition.sleep();
						scrolls = 0;
					}
					Condition.sleep(Random.getDelay());
					if (System.nanoTime() - mark > TimeUnit.SECONDS.toNanos(2)) {
						final int l = thumb.screenPoint().y;
						if (tY == l) {
							return scroll(component, pane, bar, false);
						} else {
							mark = System.nanoTime();
							tY = l;
						}
					}
				} else {
					break;
				}
			} else {
				c = bar.component(a.y < view.y ? 4 : 5);
				if (c == null) {
					break;
				}
				if (c.hover()) {
					ctx.input.press(MouseEvent.BUTTON1);
					if (!Condition.wait(new Condition.Check() {
						@Override
						public boolean poll() {
							final Point a = component.screenPoint();
							return a.y >= view.y && a.y <= view.y + height - length;
						}
					}, 500, 10)) {
						++scrolls;
					}
					ctx.input.release(MouseEvent.BUTTON1);
				}
				if (scrolls >= 3) {
					return false;
				}
			}
		}
		return a.y >= view.y && a.y <= height + view.y + height - length;
	}

	@Override
	public Widget nil() {
		return new Widget(ctx, -1);
	}
}
