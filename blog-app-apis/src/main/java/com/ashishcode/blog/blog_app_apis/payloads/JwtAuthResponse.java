package com.ashishcode.blog.blog_app_apis.payloads;

import lombok.Data;

@Data
public class JwtAuthResponse {

	private String token;
	
	private UserDto user;
}
