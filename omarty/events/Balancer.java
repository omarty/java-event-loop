package omarty.events;

import java.util.LinkedList;


public class Balancer
{
	public  LinkedList<EventLoop>  evtlpList          = new LinkedList<EventLoop>();
	public  EventLoop              evtlpCurnt         = null;

	public Balancer(LinkedList<EventLoop> evtlpList) {
		this.evtlpList = evtlpList;
	}

	public void addEventLoop(EventLoop evtlp) {
		evtlpList.add(evtlp);
		evtlpCurnt = evtlp;
	}

	public void update() {
		// System.out.println("[EventStats :: "+ evtlp.id +"] update loadLevel "+ loadLevel +" curntLoadDegree "+ curntLoadDegree +" thread "+ Thread.currentThread().getId());
		for (EventLoop evtlp : evtlpList) {
			if (evtlpCurnt.loadLevel < evtlp.loadLevel) {
				continue;
			} else {
				evtlpCurnt = evtlp;
			}
		}
	}
}
