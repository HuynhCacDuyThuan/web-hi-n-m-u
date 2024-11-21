package com.example.demo.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class EthereumBalanceChecker {
    private Web3j web3j;

    // Constructor để khởi tạo kết nối với mạng Ethereum
    public EthereumBalanceChecker(String rpcUrl) {
        try {
            this.web3j = Web3j.build(new HttpService(rpcUrl));
            System.out.println("Kết nối thành công với mạng tại: " + rpcUrl);
        } catch (Exception e) {
            System.out.println("Không thể kết nối với mạng Ethereum, kiểm tra URL: " + rpcUrl);
            e.printStackTrace();
        }
    }

    // Phương thức lấy số dư của một địa chỉ Ethereum
    public BigDecimal getBalance(String address) throws Exception {
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        BigInteger weiBalance = balance.getBalance();
        if (weiBalance.equals(BigInteger.ZERO)) {
            System.out.println("Địa chỉ " + address + " không có số dư hoặc địa chỉ không hợp lệ.");
        }
        // Chuyển đổi số dư từ Wei sang Ether
        BigDecimal etherBalance = Convert.fromWei(new BigDecimal(weiBalance), Convert.Unit.ETHER);
        return etherBalance; // Trả về số dư ETH dưới dạng Ether
    }

    public static void main(String[] args) {
        try {
            // URL của mạng cục bộ Hardhat
            String rpcUrl = "http://127.0.0.1:8545";
            EthereumBalanceChecker checker = new EthereumBalanceChecker(rpcUrl);

            // Địa chỉ cần kiểm tra số dư (chọn một trong các tài khoản Hardhat)
            String address = "0x8626f6940E2eb28930eFb4CeF49B2d1F2C9C1199";

            // Lấy số dư và in ra
            BigDecimal balance = checker.getBalance(address);
            System.out.println("Số dư của địa chỉ " + address + ": " + balance + " ETH");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
