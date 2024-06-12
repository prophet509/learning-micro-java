package com.locpham.learning.springmicroservices.accounts.services.impl;

import com.locpham.learning.springmicroservices.accounts.dto.AccountsDto;
import com.locpham.learning.springmicroservices.accounts.dto.CardsDto;
import com.locpham.learning.springmicroservices.accounts.dto.CustomerDetailsDto;
import com.locpham.learning.springmicroservices.accounts.dto.LoansDto;
import com.locpham.learning.springmicroservices.accounts.entity.Accounts;
import com.locpham.learning.springmicroservices.accounts.entity.Customer;
import com.locpham.learning.springmicroservices.accounts.exception.ResourceNotFoundException;
import com.locpham.learning.springmicroservices.accounts.mapper.AccountsMapper;
import com.locpham.learning.springmicroservices.accounts.mapper.CustomerMapper;
import com.locpham.learning.springmicroservices.accounts.repository.AccountsRepository;
import com.locpham.learning.springmicroservices.accounts.repository.CustomerRepository;
import com.locpham.learning.springmicroservices.accounts.services.ICustomersService;
import com.locpham.learning.springmicroservices.accounts.services.client.CardsFeignClient;
import com.locpham.learning.springmicroservices.accounts.services.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
