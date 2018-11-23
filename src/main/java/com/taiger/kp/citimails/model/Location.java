package com.taiger.kp.citimails.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {

	private int x;
	private int y;
	private int width;
	private int height;
	private int page;
	private int pageW;
	private int pageH;
	
}
