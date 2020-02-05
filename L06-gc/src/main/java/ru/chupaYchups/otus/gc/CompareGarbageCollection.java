package ru.chupaYchups.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareGarbageCollection {

    private enum GenerationType {
        OLD("major GC"),
        YOUNG("minor GC");
        private String logToken;
        GenerationType(String logToken) {
            this.logToken = logToken;
        }
        public String getLogToken() {
            return logToken;
        }
        public static GenerationType getByLogString(String logString) {
            for (GenerationType type : GenerationType.values()) {
                if (logString.contains(type.getLogToken())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("There is no generation type for string : " + logString);
        }
    }

    private static class GarbageCollectionStat {
        private long duration;
        private int buildCounter;
        public long getDuration() {
            return duration;
        }
        public int getBuildCounter() {
            return buildCounter;
        }
        public void addToStat(long duration) {
            buildCounter++;
            this.duration += duration;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting pid : " + ManagementFactory.getRuntimeMXBean().getName() );
        Map<GenerationType, GarbageCollectionStat> statistic = new HashMap<>();
        statistic.put(GenerationType.OLD, new GarbageCollectionStat());
        statistic.put(GenerationType.YOUNG, new GarbageCollectionStat());
        switchOnMonitoring(statistic);
        MemoryTerror terror = new MemoryTerror();
        long startTime = System.currentTimeMillis();
        try {
            terror.start();
        }
        finally {
            Thread.sleep(5000);
            System.out.print("Work time : " +  (System.currentTimeMillis() - startTime) + ", quantity of added objects : " + terror.getAddingElementsCounter());
            statistic.forEach((generationType, garbageCollectionStat) -> System.out.print(", " + generationType.name() + " : (buildCount = " + garbageCollectionStat.getBuildCounter() +", duration = " + garbageCollectionStat.getDuration() + ")"));
        }
    }

    private static void switchOnMonitoring(final Map<GenerationType, GarbageCollectionStat> statistic) {
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
                    GarbageCollectionStat stat = statistic.get(GenerationType.getByLogString(gcAction));
                    stat.addToStat(duration);
                    System.out.println( "start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)" );
                }
            };
            emitter.addNotificationListener(listener, null, null);
        });
    }
}
