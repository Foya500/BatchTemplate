package com.foya.ebill.config;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import com.foya.ebill.listener.GenFileStepListener;
import com.foya.ebill.listener.JobCompletionListener;
import com.foya.ebill.listener.MailSendStepListener;
import com.foya.ebill.mapper.ItemRowMapper;
import com.foya.ebill.step.MailItemWriter;
import com.foya.ebill.step.MailProcessor;
import com.foya.ebill.step.Processor;
import com.foya.ebill.step.Writer;


@Configuration
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;
	@Value("${threadCounts.genFile}")
	private String genFilethreads;
	@Value("${threadCounts.sendMail}")
	private String sendMailthreads;
	
    
    
    @Override
    public void setDataSource(DataSource dataSource) {
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database)
    }
    
    
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository getJobRepo() throws Exception {
        return new MapJobRepositoryFactoryBean(getTransactionManager()).getObject();
    }

    /**
     *  multithread read寫法
     *
     * @return
     * @throws SQLException
     */
    @Bean
    @StepScope
    public ItemReader<Map<String,Object>> reader() throws Exception {
		try{
    		JdbcPagingItemReader<Map<String,Object>> reader = new JdbcPagingItemReader<>();
    		final SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
    		sqlPagingQueryProviderFactoryBean.setDataSource(dataSource);
    		sqlPagingQueryProviderFactoryBean.setSelectClause("SELECT * ");
    		sqlPagingQueryProviderFactoryBean.setFromClause("FROM DUAL");
    		sqlPagingQueryProviderFactoryBean.setWhereClause(" WHERE 1=1 ");
    		sqlPagingQueryProviderFactoryBean.setSortKey("SORT_KEY");
    		reader.setQueryProvider(sqlPagingQueryProviderFactoryBean.getObject());
    		reader.setDataSource(dataSource);
    		reader.setPageSize(200);
    		reader.setRowMapper(new ItemRowMapper());
    		reader.afterPropertiesSet();
    		reader.setSaveState(true);
    		return reader;
    	}catch (Exception e) {
    		throw e;
    	}

    }
    
    @Bean
    public ItemProcessor<Map<String,Object>, Map<String,Object>> processor(){
    	return new Processor();
    }
    
    @Bean
    public ItemWriter<Map<String, Object>> writer(){
    	return new Writer();
    }
    /**
     * 一般JdbcCursorItemReader寫法
     *
     * @return
     * @throws SQLException
     */
    @Bean
    @StepScope
    public JdbcCursorItemReader<Map<String,Object>> mailReader(){
    	JdbcCursorItemReader<Map<String,Object>> reader = new JdbcCursorItemReader<Map<String,Object>>(); 
    	reader.setDataSource(dataSource);
    	StringBuffer sb = new StringBuffer();
    	sb.append("	SELECT SYSDATE FROM DUAL ");
    	reader.setVerifyCursorPosition(false);
    	reader.setSql(sb.toString());
    	reader.setRowMapper(new ItemRowMapper());
    	return reader;
    }
    
    @Bean
    public ItemProcessor<Map<String,Object>, Map<String,Object>> mailProcessor(){
    	return new MailProcessor();
    }
    @Bean
    public ItemWriter<Map<String, Object>> mailWriter(){
    	return new MailItemWriter();
    }    
    
    @Bean
    public Job processJob() throws Exception {
        return jobBuilderFactory.get("processJob")
                .incrementer(new RunIdIncrementer()).listener(listener())
                .flow(orderStep1()).next(orderStep2()).end().build();
    }

    @Bean
    public Step orderStep1() throws Exception {
        return stepBuilderFactory.get("gen file").<Map<String,Object>, Map<String,Object>> chunk(10)
                .reader(reader()).processor(processor())
                .writer(writer()).taskExecutor(taskExecutor1()).listener(stepListener1())
                .throttleLimit(getGenFileThreads()).build();
    }
    @Bean
    public Step orderStep2() throws Exception {
    	return stepBuilderFactory.get("send email").<Map<String,Object>, Map<String,Object>> chunk(10)
    			.reader(mailReader()).processor(mailProcessor())
    			.writer(mailWriter()).taskExecutor(taskExecutor2()).listener(stepListener2())
    			.throttleLimit(getMailSendThreads()).build();
    }
    //multi thread used
    @Bean
    public TaskExecutor taskExecutor1(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("task1 :");
        asyncTaskExecutor.setConcurrencyLimit(getGenFileThreads());
        return asyncTaskExecutor;
    }
    
    private int getGenFileThreads() {
    	return StringUtils.isEmpty(genFilethreads) ? 1 : Integer.valueOf(genFilethreads);
    }
     
    //multi thread used
    @Bean
    public TaskExecutor taskExecutor2(){
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("task2 :");
        asyncTaskExecutor.setConcurrencyLimit(getMailSendThreads());
    	return asyncTaskExecutor;
    }
    
    private int getMailSendThreads() {
    	return StringUtils.isEmpty(sendMailthreads) ? 1 : Integer.valueOf(sendMailthreads);
    }
    
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }
    @Bean
    public StepExecutionListener stepListener1() {
    	return new GenFileStepListener();
    }
    @Bean
    public StepExecutionListener stepListener2() {
    	return new MailSendStepListener();
    }
    
}
