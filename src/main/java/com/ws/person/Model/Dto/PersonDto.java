package com.ws.person.Model.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class PersonDto implements Serializable {
    private String name;
    private String type;
    private String documentNumber;
    private int score;
    private Long id;
}
