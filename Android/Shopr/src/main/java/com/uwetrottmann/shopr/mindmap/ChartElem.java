package com.uwetrottmann.shopr.mindmap;

public class ChartElem {
	private String name;
	private int color;
	private double value;
	
	public String name() {
		return name;
	}
	public ChartElem name(String name) {
		this.name = name;
		return this;
	}
	public int color() {
		return color;
	}
	public ChartElem color(int color) {
		this.color = color;
		return this;
	}
	public double value() {
		return value;
	}
	public ChartElem value(double value) {
		this.value = value;
		return this;
	}
}
