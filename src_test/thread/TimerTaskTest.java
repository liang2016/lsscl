package thread;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.lsscl.app.util.StringUtil;

public class TimerTaskTest {
	
	public void test(){
		new Timer()
		.schedule(
				new TimerTask(){
			@Override
			public void run() {
				System.out.println(StringUtil.formatDate(new Date(), "HH:mm:ss"));
				System.out.println(Thread.currentThread());
			}}, 1000,2000);
	}
	public static void main(String[] args) {
		new TimerTaskTest().test();
	}
}
