package com.ar2030.finbank.accounts.mapper;

import com.ar2030.finbank.accounts.dto.AccountsDto;
import com.ar2030.finbank.accounts.entity.Accounts;

public class AccountsMapper {

    public static AccountsDto mapToAccountsDto(Accounts account, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(account.getAccountNumber());
        accountsDto.setAccountType(account.getAccountType());
        accountsDto.setBranchAddress(account.getBranchAddress());
        return accountsDto;
    }

    public static void updateAccountsFromDto(AccountsDto accountsDto, Accounts account) {
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());
    }

}
