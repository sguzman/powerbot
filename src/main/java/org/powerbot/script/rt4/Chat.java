package org.powerbot.script.rt4;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.powerbot.bot.AbstractBot;
import org.powerbot.bot.EventDispatcher;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.Entry;
import org.powerbot.bot.rt4.client.EntryList;
import org.powerbot.bot.rt4.client.MessageEntry;
import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.PaintListener;

/**
 * Chat
 * A utility class for simplifying interacting with the chat box.
 */
public class Chat extends TextQuery<ChatOption> {
	private final AtomicBoolean registered = new AtomicBoolean(false);

	public Chat(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ChatOption> get() {
		final List<ChatOption> options = new ArrayList<ChatOption>(5);
		final Component parent = ctx.widgets.component(Constants.CHAT_WIDGET, 0);
		for (int i = 0; i < 5; i++) {
			final Component component = parent.component(Constants.CHAT_OPTIONS[i]);
			if (!component.valid() || component.textureId() != -1) {
				continue;
			}
			options.add(new ChatOption(ctx, i, component));
		}
		return options;
	}

	public void register() {
		if (!registered.compareAndSet(false, true)) {
			return;
		}
		final EventDispatcher e = ((AbstractBot) ctx.bot()).dispatcher;
		e.add(new PaintListener() {
			private final AtomicReference<Entry> previous = new AtomicReference<Entry>(null);

			@Override
			public void repaint(final Graphics graphics) {
				final Client client = ctx.client();
				if (client == null) {
					return;
				}
				final EntryList q = client.getLoggerEntries();
				final Entry s = q.getSentinel();
				Entry c = s.getNext();
				final Entry f = c;
				while (!s.equals(c) && !c.isNull() && !c.equals(previous.get())) {
					final MessageEntry m = new MessageEntry(c.reflector, c);
					e.dispatch(new MessageEvent(m));
					c = c.getNext();
				}
				previous.set(f);
			}
		});
	}

	public boolean chatting() {
		if (ctx.widgets.component(Constants.CHAT_WIDGET, 0).valid()) {
			return true;
		}
		for (final int[] arr : Constants.CHAT_CONTINUES) {
			if (ctx.widgets.component(arr[0], 0).valid()) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Determines if the chat is continuable.
	 *
	 * @return <tt>true</tt> if the chat is continuable; otherwise <tt>false</tt>
	 */
	public boolean canContinue() {
		return getContinue() != null;
	}

	@Deprecated
	public List<Component> chatOptions() {
		final List<Component> options = new ArrayList<Component>();
		final Component component = ctx.widgets.component(Constants.CHAT_WIDGET, 0);
		for (int i = 1; i < component.componentCount() - 2; i++) {
			options.add(component.components()[i]);
		}
		return options;
	}

	public boolean continueChat(final String... options) {
		return continueChat(false, options);
	}

	public boolean continueChat(final boolean useKeys, final String... options) {
		if (!chatting()) {
			return false;
		}
		if (canContinue()) {
			return clickContinue(useKeys);
		}
		if (options != null) {
			final ChatOption option = ctx.chat.select().text(options).peek();
			if (option.valid()) {
				return option.select(useKeys);
			}
		}
		return false;
	}

	private Component getContinue() {
		for (final int[] a : Constants.CHAT_CONTINUES) {
			final Component c = ctx.widgets.component(a[0], a[1]);
			if (!c.valid()) {
				continue;
			}
			return c;
		}
		return null;
	}

	/**
	 * Continues the chat.
	 *
	 * @return <tt>true</tt> if the chat was continued; otherwise <tt>false</tt>
	 */
	public boolean clickContinue() {
		return clickContinue(false);
	}

	/**
	 * Continues the chat.
	 *
	 * @param key <tt>true</tt> to press space; <tt>false</tt> to use the mouse.
	 * @return <tt>true</tt> if the chat was continued; otherwise <tt>false</tt>
	 */
	public boolean clickContinue(final boolean key) {
		final Component c = getContinue();
		return c != null && (key && ctx.input.send(" ") || c.click());
	}


	public boolean pendingInput() {
		return inputBox().visible();
	}

	public boolean sendInput(final int input) {
		return sendInput(Integer.toString(input));
	}

	public boolean sendInput(final String input) {
		final Component textBox = inputBox();
		if (!pendingInput()) {
			return false;
		}

		String text = textBox.text().replace("*", "");
		if (text.equalsIgnoreCase(input)) {
			return ctx.input.sendln("");
		}

		for (int i = 0; i <= text.length(); ++i) {
			ctx.input.send("{VK_BACK_SPACE down}");
			Condition.sleep(60);
			ctx.input.send("{VK_BACK_SPACE up}");
			Condition.sleep(60);
		}

		ctx.input.send(input);
		text = textBox.text().replace("*", "");
		return text.equalsIgnoreCase(input) && textBox.visible() && ctx.input.sendln("");
	}

	private Component inputBox() {
		return ctx.widgets.component(Constants.CHAT_INPUT, Constants.CHAT_INPUT_TEXT);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChatOption nil() {
		return new ChatOption(ctx, -1, null);
	}
}
