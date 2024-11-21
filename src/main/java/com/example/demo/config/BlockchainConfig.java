package com.example.demo.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import com.example.demo.model.Contract;

@Configuration
public class BlockchainConfig {

    @Bean
    public Web3j web3j(@Value("${blockchain.infura-url}") String infuraUrl) {
        return Web3j.build(new HttpService(infuraUrl));
    }

    @Bean
    public Credentials credentials(@Value("${blockchain.private-key}") String privateKey) {
        return Credentials.create(privateKey);
    }

    @Bean
    public ContractGasProvider gasProvider() {
        return new DefaultGasProvider();
    }

    @Bean
    public Contract contract(@Value("${blockchain.contract-address}") String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return Contract.load(contractAddress, web3j, credentials, gasProvider);
    }
}
