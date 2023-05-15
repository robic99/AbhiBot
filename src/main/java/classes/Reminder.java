package classes;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Reminder {
    ScheduledExecutorService executorService = ScheduledCache.getService();
    Task myTask;

    public Reminder(Task myTask){
        this.myTask = myTask;
    }

    public void startWithDelay(int hour, int min){
        Runnable taskWrapper = new Runnable() {
            @Override
            public void run() {
                myTask.execute();
            }
        };
        int totalMin = hour*60 + min;
        executorService.schedule(taskWrapper, totalMin, TimeUnit.MINUTES);
    }

}
