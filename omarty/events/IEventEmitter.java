package omarty.events;

/**
 * InnerIEventEmitter
 */
public interface IEventEmitter
{
	IEventEmitter on(Object evtname, Callback cb);
	IEventEmitter once(Object evtname, Callback cb);
	Boolean emit(Object evtname, Object ... params);
	IEventEmitter removeListener(Object evtname, Callback cb);
}

