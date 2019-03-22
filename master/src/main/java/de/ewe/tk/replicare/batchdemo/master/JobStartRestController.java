package de.ewe.tk.replicare.batchdemo.master;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobStartRestController {

   private final JobLauncher jobLauncher;
   private final Job remotePartitioningJob;

   public JobStartRestController(JobLauncher jobLauncher, Job remotePartitioningJob) {
      this.jobLauncher = jobLauncher;
      this.remotePartitioningJob = remotePartitioningJob;
   }

   @GetMapping("/startjob")
   public ResponseEntity<String> startJob() {
      JobParameters jobParameters = new JobParametersBuilder()
            // look at https://stackoverflow.com/questions/22455739/spring-batch-a-job-instance-already-exists-jobinstancealreadycompleteexceptio
            .addDate("date", new Date())
            .toJobParameters();

      try {
         jobLauncher.run(remotePartitioningJob, jobParameters);
      } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

      return new ResponseEntity<>("Job executed", HttpStatus.OK);
   }
}
