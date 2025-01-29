package com.teller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.CrmIdModel;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.ForbiddenException;
import com.teller.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ValidateTokenService {

    private final AccountRepository accountRepository;
    private final TokenUtil tokenUtil;

    public boolean validateToken(String token) throws JsonProcessingException, CommonException, ForbiddenException {
        String crmId = tokenUtil.validateToken(token.replace("Bearer ", ""));
        ObjectMapper objectMapper = new ObjectMapper();
        CrmIdModel crmIdModel = objectMapper.readValue(crmId, CrmIdModel.class);
        Account repositoryByCrmId = accountRepository.findByCrmId(crmIdModel.getCrmId());

        if (!Objects.nonNull(repositoryByCrmId)) {
            throw new ForbiddenException(
                    ResponseCode.INVALID_TOKEN.getCode(),
                    "Invalid token",
                    "teller-service",
                    HttpStatus.FORBIDDEN
            );
        }

        return true;
    }
}
