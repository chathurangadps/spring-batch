package com.example.batch.configuration;

import com.example.batch.entity.Contract;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ItemReaderConfiguration {

    @Bean
    public ItemReader<Contract> itemReader(DataSource dataSource){

        JdbcPagingItemReader<Contract> jdbcPagingItemReader = new JdbcPagingItemReader<>();
        jdbcPagingItemReader.setDataSource(dataSource);
        jdbcPagingItemReader.setPageSize(1000);
        jdbcPagingItemReader.setQueryProvider(this.createQuery());
        jdbcPagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(Contract.class));
        return jdbcPagingItemReader;
    }

    private PagingQueryProvider createQuery(){
        MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
        mySqlPagingQueryProvider.setSelectClause("SELECT * ");
        mySqlPagingQueryProvider.setFromClause("FROM CONTRACT ");
        mySqlPagingQueryProvider.setSortKeys(this.sortByCreationDate());
        return mySqlPagingQueryProvider;
    }

    private  Map<String , Order> sortByCreationDate(){
        Map<String , Order> stringOrderMap = new LinkedHashMap<>();
        stringOrderMap.put("amount" , Order.ASCENDING);
        return stringOrderMap;
    }
}
