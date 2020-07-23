package ru.bulat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    private Long id;
    private String email;
    private String password;
    private Long nickname_id;
}
