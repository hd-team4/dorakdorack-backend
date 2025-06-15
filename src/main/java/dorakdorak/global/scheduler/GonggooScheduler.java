package dorakdorak.global.scheduler;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GonggooScheduler {

    private final GonggooSchedulerService gonggooSchedulerService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void runGonggooScheduler() {
        gonggooSchedulerService.closeGonggooOrders();
    }
}