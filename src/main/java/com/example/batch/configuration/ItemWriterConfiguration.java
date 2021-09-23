package com.example.batch.configuration;

import com.example.batch.entity.ContractHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class ItemWriterConfiguration {


    @Bean
    public ItemWriter<ContractHistory> itemWriter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        String insertQuery = "INSERT INTO CONTRACT_HISTORY (id ,holder_name , duration , amount , creation_date , status) " +
                " values (:id , :holderName , :duration , :amount , :creationDate , :status)";

        JdbcBatchItemWriter<ContractHistory> itemWriter = new JdbcBatchItemWriter<>(){
            @Override
            public void write(List<? extends ContractHistory> items) throws Exception {

                super.write(items);
                log.info("Item processed : " + items.size());
                deleteQuery(items.stream().map(ContractHistory::getId).collect(Collectors.toList()) ,
                        (NamedParameterJdbcTemplate) namedParameterJdbcTemplate);

            }
        };
        itemWriter.setSql(insertQuery);
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.setJdbcTemplate(namedParameterJdbcTemplate);
        itemWriter.setAssertUpdates(false);

        return itemWriter;
    }

    private void deleteQuery(List<String> contractIdList , NamedParameterJdbcTemplate namedParameterJdbcTemplate){

        String deleteQuery = "DELETE FROM CONTRACT WHERE ID IN (:id)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id" ,contractIdList);
        namedParameterJdbcTemplate.update(deleteQuery , mapSqlParameterSource);
    }
}
