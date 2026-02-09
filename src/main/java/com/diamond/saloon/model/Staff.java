package com.diamond.saloon.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "staff")
public class Staff {

    @Id
    private String staffId; // _id (ObjectId)

    private String staffName;

    private String phone;

    private String specialization; // Hair, Makeup, Massage, etc.

    private boolean isAvailable;
}
