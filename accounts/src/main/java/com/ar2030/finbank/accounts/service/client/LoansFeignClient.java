package com.ar2030.finbank.accounts.service.client;

import com.ar2030.finbank.accounts.dto.LoansDto;
import com.ar2030.finbank.cards.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("loans") // name of the microservice (in Eureka Server) to call
public interface LoansFeignClient {

    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);

}
