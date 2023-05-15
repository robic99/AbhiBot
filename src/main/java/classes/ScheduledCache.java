package classes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledCache {

    private static ScheduledExecutorService service;

    public ScheduledCache(){

    }
    public static synchronized ScheduledExecutorService getService(){
        if(service == null){
            service = Executors.newScheduledThreadPool(Integer.MAX_VALUE);
        }
        return service;
    }
}
