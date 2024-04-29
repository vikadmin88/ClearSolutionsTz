package org.clearsolutionstz.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@Table(name = "note")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    @NotNull
    @Size(min = 2, max = 200, message = "The length of the field title must be from 2 to 200 characters!")
    private String title;

    @Column
    @NotNull
    @Size(min = 2, max = 2000, message = "The length of the field content must be from 2 to 2000 characters!")
    private String content;
}
