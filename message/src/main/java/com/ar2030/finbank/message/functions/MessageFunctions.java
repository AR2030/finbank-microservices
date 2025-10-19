package com.ar2030.finbank.message.functions;

import com.ar2030.finbank.message.dto.AccountsMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountsMsgDto,AccountsMsgDto> email(){
        return accountsMsgDto -> {
            log.info("Sending email with the details: {}", accountsMsgDto.toString());
            return accountsMsgDto;
        };
    }

    @Bean
    public Function<AccountsMsgDto,Long> sms(){
        return accountsMsgDto -> {
            log.info("Sending sms with the details: {}", accountsMsgDto.toString());
            return accountsMsgDto.accountNumber();
        };
    }

    @Bean
    public Function<AccountsMsgDto, Long> emailsms() {
        return email().andThen(sms());
    }
}
