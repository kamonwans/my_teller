package com.teller.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "account")
public class Account {
    @Id
    @Field("_id")
    private String id;
    @Field("crmId")
    private String crmId;
    @Field("accountId")
    private String accountId;
    @Field("accountName")
    private String accountName;
    @Field("amount")
    private double amount;
    @Field("createDate")
    private Date createDate;
    @Field("updateDate")
    private Date updateDate;
}
