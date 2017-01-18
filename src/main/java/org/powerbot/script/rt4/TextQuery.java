package org.powerbot.script.rt4;

import org.powerbot.script.AbstractQuery;
import org.powerbot.script.Textable;

/**
 * TextQuery
 *
 * @param <K>
 */
public abstract class TextQuery<K extends Textable> extends AbstractQuery<TextQuery<K>, K, ClientContext>
		implements Textable.Query<TextQuery<K>> {
	public TextQuery(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	protected TextQuery<K> getThis() {
		return this;
	}

	@Override
	public TextQuery<K> text(final String... texts) {
		return select(new Textable.Matcher(texts));
	}
}
