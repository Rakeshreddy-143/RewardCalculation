package com.rewards.retail.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class RewardRequest {

    @NotNull(message = "Person ID is required")
    @Positive(message = "Person ID must be a positive number")
    private Long personId;

    @NotNull
    @Min(value = 1, message = "Amount must be greater than 0")
    private int amount;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

}
