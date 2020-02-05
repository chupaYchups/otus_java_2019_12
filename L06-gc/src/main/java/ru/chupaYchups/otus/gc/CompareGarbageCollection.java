package ru.chupaYchups.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class CompareGarbageCollection {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        MemoryTerror terror = new MemoryTerror();
        try {
            System.out.println("Starting pid : " + ManagementFactory.getRuntimeMXBean().getName() );
            switchOnMonitoring();
            terror.start();
        }
        finally {
            System.out.println("Efficiency : " + terror.getAddingElementsCounter() / (System.currentTimeMillis() - startTime));
        }
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcMbeans = ManagementFactory.getGarbageCollectorMXBeans();
        gcMbeans.forEach(gcMbean -> {
            System.out.println("GC name:" +  gcMbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcMbean;
            NotificationListener listener = (notification, o) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();
                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();
                    System.out.println( "start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)" );
                }
            };
            emitter.addNotificationListener(listener, null, null);
        });
    }
}
