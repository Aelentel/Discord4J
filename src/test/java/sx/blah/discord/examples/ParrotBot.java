/*
 *     This file is part of Discord4J.
 *
 *     Discord4J is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Discord4J is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */

package sx.blah.discord.examples;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * An example to demonstrate event listening and message sending.
 * It will repeat everything said in a channel.
 */
public class ParrotBot extends BaseBot implements IListener<MessageReceivedEvent> {

	public ParrotBot(IDiscordClient discordClient) {
		super(discordClient);
		EventDispatcher dispatcher = discordClient.getDispatcher(); // Gets the client's event dispatcher
		dispatcher.registerListener(this); // Registers this bot as an event listener
	}

	/**
	 * Called when the client receives a message.
	 */
	@Override
	public void handle(MessageReceivedEvent event) {
		IMessage message = event.getMessage(); // Gets the message from the event object NOTE: This is not the content of the message, but the object itself
		IChannel channel = message.getChannel(); // Gets the channel in which this message was sent.
		try {
			// Builds (sends) and new message in the channel that the original message was sent with the content of the original message.
			new MessageBuilder(this.client).withChannel(channel).withContent(message.getContent()).build();
		} catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
			System.err.print("Sending messages too quickly!");
			e.printStackTrace();
		} catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
			System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
			e.printStackTrace();
		} catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
			System.err.print("Missing permissions for channel!");
			e.printStackTrace();
		}
	}
}
