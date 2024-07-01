package com.locpham.learning.springmicroservices.accounts.dto;

public record AccountsMsgDto(
        Long accountNumber, String name, String email, String mobileNumber) {
}
