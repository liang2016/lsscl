package com.serotonin.mango.rt.maint;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;

import com.serotonin.mango.Common;
import com.serotonin.monitor.IntegerMonitor;
import com.serotonin.timer.FixedRateTrigger;
import com.serotonin.timer.TimerTask;

public class WorkItemMonitor extends TimerTask {
    private static final long TIMEOUT = 1000 * 10; // Run every ten seconds.

    /**
     * This method will set up the memory checking job. It assumes that the corresponding system setting for running
     * this job is true.
     */
    public static void start() {
        Common.timer.schedule(new WorkItemMonitor());
    }

    private final IntegerMonitor mediumPriorityServiceQueueSize = new IntegerMonitor(getClass().getName()
            + ".mediumPriorityServiceQueueSize");
    private final IntegerMonitor scheduledTimerTaskCount = new IntegerMonitor(getClass().getName()
            + ".scheduledTimerTaskCount");
    private final IntegerMonitor highPriorityServiceQueueSize = new IntegerMonitor(getClass().getName()
            + ".highPriorityServiceQueueSize");
    private final IntegerMonitor maxStackHeight = new IntegerMonitor(getClass().getName() + ".maxStackHeight");
    private final IntegerMonitor threadCount = new IntegerMonitor(getClass().getName() + ".threadCount");

    private WorkItemMonitor() {
        super(new FixedRateTrigger(TIMEOUT, TIMEOUT));

        Common.MONITORED_VALUES.addIfMissingStatMonitor(mediumPriorityServiceQueueSize);
        Common.MONITORED_VALUES.addIfMissingStatMonitor(scheduledTimerTaskCount);
        Common.MONITORED_VALUES.addIfMissingStatMonitor(highPriorityServiceQueueSize);
        Common.MONITORED_VALUES.addIfMissingStatMonitor(maxStackHeight);
        Common.MONITORED_VALUES.addIfMissingStatMonitor(threadCount);
    }

    @Override
    public void run(long fireTime) {
        BackgroundProcessing bp = Common.ctx.getBackgroundProcessing();

        mediumPriorityServiceQueueSize.setValue(bp.getMediumPriorityServiceQueueSize());
        scheduledTimerTaskCount.setValue(Common.timer.size());
        highPriorityServiceQueueSize
                .setValue(((ThreadPoolExecutor) Common.timer.getExecutorService()).getActiveCount());

        // Check the stack heights
        int max = 0;
        Collection<StackTraceElement[]> stacks = Thread.getAllStackTraces().values();
        threadCount.setValue(stacks.size());
        for (StackTraceElement[] stack : stacks) {
            if (max < stack.length)
                max = stack.length;
        }
        maxStackHeight.setValue(max);
    }
}
