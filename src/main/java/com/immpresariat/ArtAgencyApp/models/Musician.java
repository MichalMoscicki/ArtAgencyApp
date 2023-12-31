package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name must not be blank")
    private String firstName;
    @NotBlank(message = "Name must not be blank")
    private String lastName;
    @Size(max = 500, message = "Notes can not be longer then 500 characters")
    private String notes;
    @NotNull
    @Pattern(regexp = "^(?:[\\\\+]?[(]?[0-9]{2}[)]?-?[0-9]{3}[-\\\\s\\\\.]?[0-9]{4,6}|null)?$",
            message = "Invalid phone number. Proper structure: \"+\"[dial number][number]. Eg: +481111222333")
    private String phone;
    @NotNull
    @Email(message = "Invalid email")
    private String email;
    @ManyToMany
    List<Instrument> instruments;

}
