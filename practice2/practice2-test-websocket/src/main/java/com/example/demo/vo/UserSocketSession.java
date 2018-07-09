package com.example.demo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSocketSession implements Serializable {
	private static final long serialVersionUID = -1616667627336749900L;

	private String userId;
	private String sessionId;
	private DemoPrincipal exchangePrincipal;

}
