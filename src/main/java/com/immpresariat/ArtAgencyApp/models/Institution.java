package com.immpresariat.ArtAgencyApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "City must not be blank")
    private String city;
    @NotBlank(message= "Category must not be blank")
    private String category;
    @Size(max = 500, message = "Notes can not be longer then 500 characters")
    private String notes;
    private String webPage;
    @Pattern(regexp = "^(?:[\\\\+]?[(]?[0-9]{2}[)]?-?[0-9]{3}[-\\\\s\\\\.]?[0-9]{4,6}|null)?$",
            message = "Invalid phone number. Proper structure: \"+\"[dial number][number]. Eg: +481111222333")
    private String phone;
    @Nullable
    @Email(message = "Invalid email")
    private String email;

}
