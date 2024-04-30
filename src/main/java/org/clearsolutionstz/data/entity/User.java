package org.clearsolutionstz.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 50, message = "Invalid field size. first_name must be from 2 to 50 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, max = 200, message = "Invalid field size. first_name must be from 2 to 50 characters")
    private String lastName;

    @Column(name = "birth_date")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Column
    @NotNull
    @Email(message = "Invalid email format")
    private String email;

    @Column
    @Nullable
    @NumberFormat(pattern = "pattern = ###-###-####")
    private String phone;

    @Column
    @Nullable
    @Size(max = 2000, message = "Restriction field size: max 2000")
    private String address;
}
