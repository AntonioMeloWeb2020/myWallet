package com.genesis.coincapcheck.demo.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesis.coincapcheck.demo.dto.WalletResultDTO;
import com.genesis.coincapcheck.demo.service.WalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@Api(value = "Wallet", tags = "Wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @ApiOperation(value = "Check the current wallet in csv wallet over data on coincap and check the updated values.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Coin Cap Check Response successfully", response = WalletResultDTO.class),
        @ApiResponse(code = 204, message = "Coin Cap Check could not be executed")
    })
    @GetMapping("/api/v1/wallet")
    ResponseEntity<WalletResultDTO> wallet() throws FileNotFoundException, IOException, InterruptedException, ExecutionException, TimeoutException {

        WalletResultDTO wallet = walletService.checkMyPositionOnCoinCap();

        return ResponseEntity.ok().body(wallet);
    }
}