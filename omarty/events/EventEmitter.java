package omarty.events;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * EventEmitter
 */
public class EventEmitter implements IEventEmitter
{
	private HashMap<Object, Event> _evttbl = new HashMap<>();

	/** 
	 * @param evtname
	 * @param cb
	 * @return EventEmitter
	 */
	public EventEmitter on(Object evtname, Callback cb) {
		var evt = _evttbl.get(evtname);
		if (evt == null) {
			evt = new Event((String)evtname);
			_evttbl.put(evtname, evt);
		}

		var listener = new Listener(cb, false);
		if (evt.isEmits == false)
			evt.listeners.push(listener);
		else
			evt.listenersToAdd.push(listener);

		return this;
	}

	/** 
	 * @param evtname
	 * @param cb
	 * @return EventEmitter
	 */
	public EventEmitter once(Object evtname, Callback cb) {
		var evt = _evttbl.get(evtname);
		if (evt == null) {
			evt = new Event((String)evtname);
			_evttbl.put(evtname, evt);
		}

		var listener = new Listener(cb, true);
		if (evt.isEmits == false)
			evt.listeners.push(listener);
		else
			evt.listenersToAdd.push(listener);

		return this;
	}

	/** 
	 * @param evtname
	 * @param param
	 * @return Boolean
	 */
	public Boolean emit(Object evtname, Object ... params) {
		var evt = _evttbl.get(evtname);
		if (evt == null) {
			return false;
		}

		evt.isEmits = true;

		for (int i = 0; i < evt.listeners.size(); i++) {
			var listener = evt.listeners.get(i);
			listener.cb.call(params);
			if (listener.isOnce == true) {
				evt.listeners.remove(i);
				i--;
			}
		}

		if (evt.listenersToAdd.size() > 0) {
			while (evt.listenersToAdd.size() > 0) {
				evt.listeners.push(evt.listenersToAdd.poll());
			}
		}

		if (evt.callbacksToDel.size() > 0) {
			while (evt.callbacksToDel.size() > 0) {
				_removeListener(evt, evt.callbacksToDel.poll());
			}
		}

		evt.isEmits = false;

		if (evt.listeners.size() == 0) {
			_evttbl.remove(evtname);
		}

		return true;
	}

	/** 
	 * @param evtname
	 * @param cb
	 * @return EventEmitter
	 */
	public EventEmitter removeListener(Object evtname, Callback cb) {
		var evt = _evttbl.get(evtname);
		if (evt == null) {
			return this;
		}

		if (evt.isEmits == true) {
			evt.callbacksToDel.push(cb);
		} else {
			_removeListener(evt, cb);
		}

		return this;
	}

	/** 
	 * @param evt
	 * @param cb
	 */
	private void _removeListener(Event evt, Callback cb) {
		for (int i = 0; i < evt.listeners.size(); i++) {
			var listener = evt.listeners.get(i);
			if (listener.cb == cb) {
				evt.listeners.remove(i);
				break;
			}
		}
	}
}


/**
 * Event class.
 */
class Event
{
	public LinkedList<Listener>  listeners       = new LinkedList<>();
	public LinkedList<Listener>  listenersToAdd  = new LinkedList<>();
	public LinkedList<Callback>  callbacksToDel  = new LinkedList<>();
	public Boolean               isEmits         = false;
	public String                name            = null;

	public Event(String evtname) {
		this.name = evtname;
	}
}


/**
 * Listener class.
 */
class Listener
{
	Callback  cb      = null;
	Boolean   isOnce  = false;

	public Listener(Callback cb, Boolean isOnce) {
		this.cb = cb;
		this.isOnce = isOnce;
	}
}

