package com.pst.ag.dsv_converter;

import com.pst.ag.dsv_converter.service.FileConverterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class DsvConverterApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DsvConverterApplication.class, args);
		FileConverterService service = new FileConverterService();
		service.execute(args);
	}

}
