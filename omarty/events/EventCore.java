package omarty.events;

import java.util.*;


public class EventCore
{
	private  LinkedList<EventLoop>    _evtlpList        = new LinkedList<EventLoop>();
	private  Balancer                 _evtBalancer      = new Balancer(_evtlpList);

	public EventCore(int threadNum) {
		var _threadNum = threadNum <= 0 ? 1 : threadNum;

		for (int i = 0; i < _threadNum; i++) {
			var evtlp = new EventLoop(_evtBalancer);
			_evtBalancer.addEventLoop(evtlp);
			_evtlpList.add(evtlp);
		}

		for (int i = 0; i < threadNum; i++) {
			var evtlp = _evtlpList.get(i);
			var thread = new Thread(evtlp);
			thread.start();
		}
	}

	/** 
	 * Function for set timeout.
	 * @param cb - Callback of timeout.
	 * @param timeout - Timeout.
	 * @return Timer - Returns timer.
	 */
	public Timer setTimeout(TimerCallback cb, long timeout) {
		return _evtBalancer.evtlpCurnt.setTimeout(cb, timeout);
	}
	
	/**
	 * Function for set timeout.
	 * @param cb - Callback of timeout.
	 * @param timeout - Interval.
	 * @return Timer - Returns timer.
	 */
	public Timer setInterval(TimerCallback cb, long timeout) {
		return _evtBalancer.evtlpCurnt.setInterval(cb, timeout);
	}

	// public void createThread() {
	// 	var evtlp = new EventLoop(_evtBalancer);
	// 	_evtlpList.add(evtlp);
	// 	var thread = new Thread(evtlp);
	// 	thread.start();
	// }

	// public void deleteThread() {
	// 	// ...
	// }
}