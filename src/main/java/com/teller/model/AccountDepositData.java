package com.teller.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "my_teller")
public class AccountDepositData  implements Serializable {
    @Id
    @Field("_id")
    private String id;
    @Field("deposit")
    private List<Account> accountList;

}
