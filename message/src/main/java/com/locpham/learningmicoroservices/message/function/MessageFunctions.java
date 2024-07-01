package com.locpham.learningmicoroservices.message.function;

import com.locpham.learningmicoroservices.message.dto.AccountsMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

    public Function<AccountsMsgDto, AccountsMsgDto> email() {
        return accountsMsgDto -> {
            logger.info("Email to accounts " + accountsMsgDto.email());
            return accountsMsgDto;
        };
    }

    public Function<AccountsMsgDto, AccountsMsgDto> sms() {
        return accountsMsgDto -> {
            logger.info("SMS to accounts " + accountsMsgDto.toString());
            return accountsMsgDto;
        };
    }
}
