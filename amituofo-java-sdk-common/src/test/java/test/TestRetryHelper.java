package test;

import com.amituofo.common.define.Constants;
import com.amituofo.common.ex.IDConflictException;
import com.amituofo.common.kit.retry.RetryHelper;
import com.amituofo.common.kit.retry.ReturnValue;
import com.amituofo.common.kit.retry.ReturnableTry;
import com.amituofo.common.util.RandomUtils;

public class TestRetryHelper {

	public TestRetryHelper() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException, IDConflictException {
		System.out.println(System.currentTimeMillis());
		System.out.println(Constants.TIME_1981_MILLISECONDS);
		// TODO Auto-generated method stub
		RetryHelper.asyncRetry("a", new ReturnableTry<String>() {

			@Override
			protected ReturnValue<String> execute(int retryTimes, long time) {
				String str = RandomUtils.randomString(10);
				System.out.println(str);
				return ReturnValue.failed(str);
			}

			@Override
			protected void release() {
				System.out.println("end");
				super.release();
			}
		}, 10, 10000, 0);
		
		Thread.sleep(32000);
		
		RetryHelper.stopRetry("a");
		
		Thread.sleep(10000000);
	}

}
