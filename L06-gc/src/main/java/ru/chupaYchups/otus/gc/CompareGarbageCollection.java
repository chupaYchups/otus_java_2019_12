package ru.chupaYchups.otus.gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.*;
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

    public static void main(String[] args) {
        System.out.println("Starting pid : " + ManagementFactory.getRuntimeMXBean().getName() );
        Map<GenerationType, GarbageCollectionStat> statistic = new HashMap<>();
        statistic.put(GenerationType.OLD, new GarbageCollectionStat());
        statistic.put(GenerationType.YOUNG, new GarbageCollectionStat());
        switchOnMonitoring(statistic);
        new MemoryTerror().run();
    }

    private static void switchOnMonitoring(final Map<GenerationType, GarbageCollectionStat> statistic) {
        List<GarbageCollectorMXBean> gcMbeans = ManagementFactory.getGarbageCollectorMXBeans();
        gcMbeans.forEach(gcMbean -> {
            System.out.println("GC name:" +  gcMbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcMbean;
            NotificationListener listener = (notification, o) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcAction = info.getGcAction();
                    long duration = info.getGcInfo().getDuration();
                    GarbageCollectionStat stat = statistic.get(GenerationType.getByLogString(gcAction));
                    stat.addToStat(duration);
                    System.out.println( "Start:" + info.getGcInfo().getStartTime() + " Name:" + info.getGcName() +
                        ", action:" + gcAction + ", gcCause:" + info.getGcCause() + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        });
    }
}
