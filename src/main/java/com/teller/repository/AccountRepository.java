package com.teller.repository;

import com.teller.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByAccountId(String accountId);
    Account findByCrmId(String crmId);
    Account deleteByAccountId(String accountId);
}
