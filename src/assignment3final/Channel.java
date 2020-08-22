package assignment3final;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Channel {

	private String name;
	private List<Message> messages = new ArrayList<Message>();
	private List<Channel> subscriptions = new ArrayList<Channel>();
	
	public Channel(String name) {
		this.name = name;
		this.subscriptions.add(this);
	}

	public void publish(Message msg) {
		messages.add(msg);
	}

	public String getName() {
		return name;
	}

	public void subscribeTo(Channel ch) {
		if (!subscriptions.contains(ch))
				subscriptions.add(ch);
	}

	public void unsubscribeFrom(Channel ch) {
		if (subscriptions.contains(ch))
			subscriptions.remove(ch);
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean isSubscribedTo(String name) {
		return subscriptions.contains(name);
	}

	public List<Message> getAllMessages() {
		return messages;
	}

	public List<Message> getMessagesAfter(int time) {
		List<Message> toReturn = new ArrayList<Message>();
		for (Message m : messages) {
			if (m.getTimeStamp() > time)
				toReturn.add(m);
		}
		return toReturn;
	}
	
	public boolean hasMessageAfter(int time) {
		for (Message m : messages) {
			if (m.getTimeStamp() > time)
				return true;
		}
		return false;
	}
	
	public boolean hasUnreadMessagesAfter(int time) {
		for (Channel ch : subscriptions) {
			if (ch.hasMessageAfter(time))
				return true;
		}
		return false;
	}
	
	public boolean containsSearch(String search) {
		return name.contains(search);
	}
	
	public List<Message> messageSearch(String hash) {
		List<Message> toReturn = new ArrayList<Message>();
		for (Message m : messages) {
			if (m.hasHashtag(hash))
				toReturn.add(m);
		}
		return toReturn;
	}
	
	public List<String> getSubscribedNames() {
		List<String> toReturn = new ArrayList<String>();
		for(Channel ch : subscriptions) {
			if (ch != this)
				toReturn.add(ch.getName());
		}
		return toReturn;
	}
	
	public List<Message> getAllSubscribedMessages(int time) {
		List<Message> toReturn = new ArrayList<Message>();
		for (Channel ch : subscriptions) {
			toReturn.addAll(ch.getMessagesAfter(time));
		}
		Collections.sort(toReturn);
		return toReturn;
	}
	
	public boolean hasSubscribedMessages(int time) {
		for (Channel ch : subscriptions)
			if (ch.hasMessageAfter(time))
				return true;
		return false;
	}
}
