package com.csse.restapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="t_cardtasks")
public class CardTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    private Card card;
    @Column(name="task_text")
    private String taskText;
    @Column(name="added_date")
    private String addedDate;
    private boolean done;
}
