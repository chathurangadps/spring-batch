package com.example.batch.configuration;

import com.example.batch.entity.Contract;
import com.example.batch.entity.ContractHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
public class ItemProcessorConfiguration {

    private final AtomicInteger atomicInteger = new AtomicInteger();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public ItemProcessor<Contract , ContractHistory> itemProcessor() {

        return new ItemProcessor<Contract, ContractHistory>() {
            @Override
            public ContractHistory process(Contract contract) throws Exception {
                log.info("Processing the data : "
                        .concat(contract.getId())
                        .concat(" : ")
                        .concat(Integer.toString(atomicInteger.incrementAndGet())));
                //what ever the data transformation goes here
                return objectMapper.convertValue(contract , ContractHistory.class);
            }
        };
    }
}
