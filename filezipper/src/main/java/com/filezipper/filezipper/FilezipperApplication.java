package com.filezipper.filezipper;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.filezipper.filezipper.model.User;
import com.filezipper.filezipper.repositories.UserRepo;

@SpringBootApplication
public class FilezipperApplication {

	@Autowired
	UserRepo userRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(FilezipperApplication.class, args);
	}

	@Bean
	InitializingBean sendDatabase() {
		System.out.println("inserting sample data");
	    return () -> {
	    	userRepo.save(User.builder()._id((long) 1).username("test1").password("123").authorities(new ArrayList<String>(Arrays.asList("FILE_READ","FILE_WRITE"))).build());
	    	userRepo.save(User.builder()._id((long) 2).username("test2").password("1234").authorities(new ArrayList<String>(Arrays.asList("FILE_READ","FILE_WRITE"))).build());
	    	userRepo.save(User.builder()._id((long) 3).username("test3").password("12345").authorities(new ArrayList<String>(Arrays.asList("FILE_READ"))).build());
	      };
	   }
}
