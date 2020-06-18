package org.seariver.kanbanboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class KanbanBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanbanBoardApplication.class, args);
    }

}
