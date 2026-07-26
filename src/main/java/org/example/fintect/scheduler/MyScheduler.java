package org.example.fintect.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.fintect.user.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class MyScheduler {
//    @Scheduled(fixedRate = 10000)
//    public void executeTask() {
//        System.out.println("Task executed at: " + LocalDateTime.now());
//    }

//    @Scheduled(cron = "0 8 23 * * *", zone = "Asia/Dhaka")
//    public void executeTask() {
//        System.out.println("Reminder: It's 11:08 PM!");
//        System.out.println("Task executed at: " + LocalDateTime.now());
//    }

//    @Scheduled(fixedDelay = 5000)
//    public void task() {
//        System.out.println("Runs 5 seconds after completion");
//    }


    private final UsersRepository userRepository;

    @Scheduled(fixedDelay = 10000)
    public void checkUsers() {

        System.out.println("---------------------------------------------");
        System.out.println("Scheduler started: " + LocalTime.now());

        long totalUsers = userRepository.count();

        System.out.println("Total Users: " + totalUsers);

        System.out.println("Scheduler finished: " + LocalTime.now());

        System.out.println("---------------------------------------------");
    }
}
