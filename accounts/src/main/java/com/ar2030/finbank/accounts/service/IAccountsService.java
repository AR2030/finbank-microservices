package com.ar2030.finbank.accounts.service;

import com.ar2030.finbank.accounts.dto.CustomerDto;

public interface IAccountsService {


    void createAccount(CustomerDto customerDto);


    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);

    boolean updateCommunicationStatus(Long AccountNumber);

}