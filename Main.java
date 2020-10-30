import java.lang.Thread;
import java.util.*;
import java.util.HashMap;
import omarty.events.*;
import omarty.events.Timer;


public class Main
{
	static int i = 0;
	static Timer intrvl = null;

	public static void main(String[] args)
	{
		// System.out.println("Main thread "+ Thread.currentThread().getId());
		System.out.println("Menu -------------------------------------------------------");
		System.out.println("1 - EventEmitter");
		System.out.println("2 - EventLoop");
		System.out.println("q - test 2");
		System.out.println("------------------------------------------------------------");
		
		var scanner = new Scanner(System.in);
		var inputStr = "";
		var needExit = false;

		while (true) {
			inputStr = scanner.nextLine();
			
			switch (inputStr) {
				case "1": {
					System.out.println("Test 1 - EventEmitter");
					test1_EventEmitter();
					break;
				}
				case "2": {
					System.out.println("Test 2 - EventLoop");
					test2_EventLoop();
					break;
				}
				case "q": {
					System.out.println("Exit - Bye bye...");
					needExit = true;
					break;
				}
				default: {
					System.out.println("Not valid input");
					break;
				}
			}
			
			if (needExit == true) {
				scanner.close();
				break;
			}
		}


	
		{//ʭʭʭʭʭʭʭʭʭ Speed test ʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭ
			var timeBefore = System.currentTimeMillis();
			//----------------------------------------------------------------------
			for (int x = 0; x < 1000000; x++) {
				// var a = "abcd1234";
			}
			//----------------------------------------------------------------------
			var timeAfter = System.currentTimeMillis();
			var runTime = timeAfter - timeBefore;
			System.out.println("Run time: "+ runTime + " ms");
		}//ʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭʭ
	}

	static void test1_EventEmitter() {
		var ee = new EventEmitter();

		ee.on("event-1", (_args) -> {
			HashMap<?,?> hm = (HashMap<?,?>) _args[0];
			System.out.println("on event-1   -> "+ hm.get("key1"));
		});

		ee.once("event-1", (_args) -> {
			HashMap<?,?> hm = (HashMap<?,?>) _args[0];
			System.out.println("once event-1 -> "+ hm.get("key2"));
		});

		var hm = new HashMap<>();
		hm.put("key1", "value1");
		hm.put("key2", "value2");

		ee.emit("event-1", hm);
		ee.emit("event-1", hm);
	}

	static void test2_EventLoop() {
		var evtlp = new EventLoop();
		Thread thread = new Thread(evtlp);
		thread.start();

		evtlp.setTimeout(() -> {
			System.out.println("Timeout in thread "+ Thread.currentThread().getId());
		}, 100);

		var interval = evtlp.setInterval(() -> {
			System.out.println("Interval in thread "+ Thread.currentThread().getId());
		}, 300);

		evtlp.setTimeout(() -> {
			System.out.println("Timeout in thread "+ Thread.currentThread().getId());
			interval.destroy();
			evtlp.stop();
		}, 1400);
	}
}
