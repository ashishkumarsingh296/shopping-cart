package com.ashishcode.blog.blog_app_apis.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
	@Data // Lombok for getters/setters
	@NoArgsConstructor
	@AllArgsConstructor
	public class Role {

		@Id
		private Integer id;

		private String name;
	}

