package com.example.demo.model;


import javax.persistence.*;

import java.time.LocalDateTime;


@Embeddable // klasa która jest do osadzenia (osadzalna
class Audit {

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
        // to jest funkcja która odpali się tuz przed zapisaem bazy danych
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
        //dokłada encje do zarzadzania.
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }

}
