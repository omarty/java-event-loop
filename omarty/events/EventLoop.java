package omarty.events;

import java.util.*;
import java.util.concurrent.*;


public class EventLoop implements Runnable
{
	private  LinkedList<Timer>     _timerList          = new LinkedList<Timer>();
	private  LinkedList<Object[]>  _futureList         = new LinkedList<Object[]>();
	private  boolean               _isStopped          = false;
	private  long                  _time               = 0;
	private  Balancer              _balancer           = null;
	public   int                   loadLevel           = 0;
	public   String                id                  = "?";          

	public EventLoop(Object ... args /* evtBalancer */) {
		if (args.length == 1) {
			_balancer = (Balancer) args[0];
		}
		_updateTime();
	}

	@Override
	public void run() {
		System.out.println("[EventLoop :: ?] run in thread "+ Thread.currentThread().getId());
		id = Long.toString(Thread.currentThread().getId());
		while (_isStopped == false) {
			_updateTime();
			_runTimers();
			_runFutures();
			// ...
			// ...
			_prepare();
			if (_balancer != null) {
				_balancer.update();
			}
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			Thread.yield();
		}
	}

	private void _updateTime() {
		_time = System.currentTimeMillis();
	}

	private void _runTimers() {
		for (int i = 0; i < _timerList.size(); i++) {
			var timer = _timerList.get(i);
			if (timer.destroyed == true) {
				_timerList.remove(i);
			}
			if (_time > timer.dueTime) {
				timer.cb.call();
				if (timer.repeat == true)
					timer.updateDueTime(_time);
				else
					_timerList.remove(i);
			} else {
				break;
			}
		}
	}

	private void _runFutures() {
		for (int i = 0; i < _futureList.size(); i++) {
			var io = _futureList.get(i);
			var f = (Future) io[0];
			if (f.isDone() == true) {
				Object rslt;
				try {
					rslt = f.get();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				((Callback) io[1]).call(rslt);
			}
		}
	}

	private void _prepare() {
		loadLevel = _timerList.size() + _futureList.size();
	}

	/**
	 * Function for stop event loop.
	 */
	public void stop() {
		_isStopped = true;
	}

	/** 
	 * Function for set timeout.
	 * @param cb - Callback of timeout.
	 * @param duration - Timeout.
	 * @return Timer - Returns timer.
	 */
	public Timer setTimeout(TimerCallback cb, long duration) {
		// System.out.println("[EventLoop :: "+ id +"] setTimeout "+ timeout +" "+ _time);
		var timer = new Timer(cb, false, _time, duration);
		_timerList.add(timer);
		return timer;
	}
	
	/**
	 * Function for set timeout.
	 * @param cb - Callback of timeout.
	 * @param duration - Interval.
	 * @return Timer - Returns timer.
	 */
	public Timer setInterval(TimerCallback cb, long duration) {
		// System.out.println("[EventLoop :: "+ id +"] setInterval "+ timeout +" "+ _time);
		var timer = new Timer(cb, true, _time, duration);
		_timerList.add(timer);
		return timer;
	}

	
	/** 
	 * Function for set timeout.
	 * @param f - Instance of Future.
	 * @param cb - Callback.
	 */
	public void setFuture(Future f, Callback cb) {
		var io = new Object[] { f, cb };
		_futureList.push(io);
	}
}