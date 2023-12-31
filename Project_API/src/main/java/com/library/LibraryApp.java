///////////////////////////////////////////////////////////////////////////////////////////////////////
package com.library;

//  FILE : ApiAppilcation.java
//  AUTHOR : Auto-Generated by using https://start.spring.io/
//  DESCRIPTION: Is the primary file for the application, run this to launch springboot
//               DO NOT change this file unless necessary
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApp {
	public static void main(String[] args){
		try {
			SpringApplication.run(LibraryApp.class, args);	
		} catch (Exception e) {
			System.out.println("ERROR at main! LEVEL SEVERE, PLEASE HELP!\n");
			SpringApplication.run(LibraryApp.class, args);
		}
	}
}