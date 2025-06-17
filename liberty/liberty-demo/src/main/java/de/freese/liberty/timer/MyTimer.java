// Created: 17 Juni 2025
package de.freese.liberty.timer;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Singleton
// @Startup
public class MyTimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTimer.class);

    // @Resource
    // private TimerService timerService;
    //
    // @PostConstruct
    // public void init() {
    //     final ScheduleExpression schedule = new ScheduleExpression()
    //             .minute("*/1");
    //
    //     timerService.createCalendarTimer(schedule, new TimerConfig(MyTimer.class.getSimpleName(), true));
    // }
    // @Timeout
    // public void scheduleProgrammatic() {
    //     LOGGER.info("Hello World");
    // }

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    public void schedule() {
        LOGGER.info("Hello World");
    }
}
