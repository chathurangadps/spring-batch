package com.example.batch.controller;

import com.example.batch.entity.Contract;
import com.example.batch.repository.ContractorRepo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class BatchController {

    private final ContractorRepo contractorRepo;
    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public BatchController(ContractorRepo contractorRepo, JobLauncher jobLauncher, Job job) {
        this.contractorRepo = contractorRepo;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @GetMapping("/insert")
    public  String saveDummyData(){

        log.info("Starting the data inserting process>>>>>>>>>>>>>");

        List<Contract> list = new ArrayList<>();

        for (int i =0 ; i < 10000 ; i++){

            Contract contract = new Contract();
            contract.setHolderName("name : ".concat(Integer.toString(i)));
            contract.setDuration(new Random().nextInt());
            contract.setAmount(new Random().nextInt(500000));

            Date date = new Date();
            date.setDate(new Random().nextInt(30));
            date.setMonth(new Random().nextInt(12));
            date.setYear(new Random().nextInt(2020));

            contract.setCreationDate(date);
            contract.setStatus("InProgress");
            list.add(contract);
        }
        this.contractorRepo.saveAll(list);

        log.info("Finished the data inserting process>>>>>>>>>>>>>");
        return "Saved Successfully!";
    }

    @GetMapping("/start-batch")
    @SneakyThrows
    public  String startBatch(){

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time" , System.currentTimeMillis()).toJobParameters();

        jobLauncher.run(job , jobParameters);
        return "Batch Started";
    }



}
