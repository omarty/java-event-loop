package omarty.events;


public class Timer
{
	TimerCallback cb;
	long _startTime;
	long _timeout;
	public long dueTime;
	public boolean repeat;
	public boolean destroyed = false;
	
	public Timer(TimerCallback cb, boolean repeat, long startTime, long timeout) {
		this.cb = cb;
		this._startTime = startTime;
		this._timeout = timeout;
		this.dueTime = startTime + timeout;
		this.repeat = repeat;
	}
	
	public void updateDueTime(long startTime) {
		this.dueTime = startTime + this._timeout;
	}
	
	public void destroy() {
		this.destroyed = true;
	}
}