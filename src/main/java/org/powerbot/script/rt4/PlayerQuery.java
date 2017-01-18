package org.powerbot.script.rt4;

import java.util.regex.Pattern;

import org.powerbot.script.AbstractQuery;
import org.powerbot.script.Area;
import org.powerbot.script.Filter;
import org.powerbot.script.Locatable;
import org.powerbot.script.Nameable;
import org.powerbot.script.Viewable;

/**
 * PlayerQuery
 *
 * @param <K>
 */
public abstract class PlayerQuery<K extends Locatable & Nameable & Viewable> extends AbstractQuery<PlayerQuery<K>, K, org.powerbot.script.rt4.ClientContext>
		implements Locatable.Query<PlayerQuery<K>>, Nameable.Query<PlayerQuery<K>>, Viewable.Query<PlayerQuery<K>> {
	protected PlayerQuery(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PlayerQuery<K> getThis() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> at(final Locatable l) {
		return select(new Locatable.Matcher(l));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> within(final double radius) {
		return within(ctx.players.local(), radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> within(final Locatable locatable, final double radius) {
		return select(new Locatable.WithinRange(locatable, radius));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> within(final Area area) {
		return select(new Locatable.WithinArea(area));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> nearest() {
		return nearest(ctx.players.local());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> nearest(final Locatable locatable) {
		return sort(new Locatable.NearestTo(locatable));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> name(final String... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> name(final String[]... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> name(final Pattern... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> name(final Nameable... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> viewable() {
		return select(new Filter<K>() {
			@Override
			public boolean accept(final K k) {
				return k.inViewport();
			}
		});
	}
}
