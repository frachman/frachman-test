/**
 * 
 */
package com.fr.test;

import java.math.BigDecimal;

/**
 * @author Farandy Rachman
 *
 */
public class FlightInformation {

	String departTime, arrivalTime, fromAirportCode, 
		toAirportCode, flightNo, duration;

	BigDecimal price;

	public String getDepartTime() {
		return departTime;
	}

	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getFromAirportCode() {
		return fromAirportCode;
	}

	public void setFromAirportCode(String fromAirportCode) {
		this.fromAirportCode = fromAirportCode;
	}

	public String getToAirportCode() {
		return toAirportCode;
	}

	public void setToAirportCode(String toAirportCode) {
		this.toAirportCode = toAirportCode;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
