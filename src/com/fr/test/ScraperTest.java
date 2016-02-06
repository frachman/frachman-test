/**
 * 
 */
package com.fr.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Farandy Rachman
 *
 */
public class ScraperTest {

	public void flightScraper(String origin, String destination, String roundtrip, String departDate, String returnDate) {
		
		Document doc = null;
		
		try {
			String url = "https://booking.airasia.com/Flight/Select";
			
			Connection con = Jsoup.connect(url)
			           .data("o1", origin.toUpperCase())
			           .data("d1", destination.toUpperCase())
			           .data("culture", "id-ID")
			           .data("dd1", departDate)
			           //.data("dd2", returnDate)
			           .data("r", "true")
			           .data("ADT", "1")
			           .data("CHD", "0")
			           .data("inl", "0")
			           .data("s", "true")
			           .data("mon", "true")
			           .data("cc", "IDR")
			           .data("c", "false")
			           .timeout(10*5000);
			if(roundtrip.equalsIgnoreCase("y")){
				doc = con.data("dd2", returnDate)
			           .get();
			} else {
				doc = con.get();
			}
			
			List<FlightInformation> departures = new ArrayList<FlightInformation>();
			List<FlightInformation> returns = new ArrayList<FlightInformation>();
			
			Element content = doc.getElementById("availabilityForm");
			Elements availabilities = content.select("div.js_availability_container");
			
			int z=0;
			for(Element available : availabilities){
				
				Element table = available.getElementsByTag("table").get(0);
				Elements trs = table.getElementsByTag("tr");
				
				for(Element tr : trs){
					if(tr.hasClass("fare-light-row") || tr.hasClass("fare-dark-row")){
						Elements tds = tr.getElementsByTag("td");
						FlightInformation data = new FlightInformation();
						for(Element td : tds){
							if(td.getElementsByTag("table").size() > 0) {
								Element innerTbl= td.getElementsByTag("table").get(0);
								Element innerTr = innerTbl.getElementsByTag("tr").get(0);
								Elements innerTds= innerTr.getElementsByTag("td");
								
								data.setDepartTime(innerTds.get(1).select("div.text-center").text());
								data.setArrivalTime(innerTds.get(3).select("div.text-center").text());
								data.setFlightNo(innerTds.get(4).select("div.carrier-hover-bold").first().text());
								data.setDuration(innerTds.get(5).select("div.text-center").text());
								
							} else if(td.select("div.avail-fare-price").size() > 0) {
								String price = td.select("div.avail-fare-price").text().replaceAll("[^\\w]", "");
								data.setPrice(new BigDecimal(price.substring(0, price.length()-3).trim()));
							}
						}
						
						if(z==0 && null != data.getFlightNo()){
							departures.add(data);
						}
						if(z==1 && null != data.getFlightNo()) {
							returns.add(data);
						}
					}
				}
					
				z++;
			}
			
			FlightInformation cheapestDepart = getCheapestPrice(departures);
			FlightInformation cheapestReturn = getCheapestPrice(returns);
			System.out.println("================================================");
			System.out.println("");
			System.out.println("Cheapest flight for "+origin+" to "+destination+" route is");
			System.out.println("Depart time    :"+cheapestDepart.getDepartTime());
			System.out.println("Arrival time   :"+cheapestDepart.getArrivalTime());
			System.out.println("Duration       :"+cheapestDepart.getDuration());
			System.out.println("Flight No      :"+cheapestDepart.getFlightNo());
			System.out.println("Price          :"+cheapestDepart.getPrice());
			System.out.println("");
			
			if(null != cheapestReturn) {
				System.out.println("================================================");
				System.out.println("");
				System.out.println("Cheapest flight for "+destination+" to "+origin+" route is");
				System.out.println("Depart time    :"+cheapestReturn.getDepartTime());
				System.out.println("Arrival time   :"+cheapestReturn.getArrivalTime());
				System.out.println("Duration       :"+cheapestReturn.getDuration());
				System.out.println("Flight No      :"+cheapestReturn.getFlightNo());
				System.out.println("Price          :"+cheapestReturn.getPrice());
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public FlightInformation getCheapestPrice(List<FlightInformation> fis) {
		FlightInformation cheapestPrice = null;
		for(FlightInformation fi : fis){
			if(null == cheapestPrice){
				cheapestPrice = fi;
			} else {
				if(cheapestPrice.getPrice().compareTo(fi.getPrice()) > 0)
					cheapestPrice = fi;
			}
		}
		
		return cheapestPrice;
	}
	
	
	public static void main(String[] args) {
		String origin, destination, roundtrip, departDate, returnDate=null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("Please enter origin airport code: ");
			origin = br.readLine();
			System.out.print("Please enter destination airport code: ");
			destination = br.readLine();
			System.out.print("Do you want to purchase return ticket? (y/n) ");
			roundtrip = br.readLine();
			System.out.print("Please enter departing date (yyyy-mm-dd): ");
			departDate = br.readLine();
			
			if(roundtrip.equalsIgnoreCase("y")){
				System.out.print("Please enter return date (yyyy-mm-dd): ");
				returnDate = br.readLine();
			}
			
			ScraperTest scrapper = new ScraperTest();
			scrapper.flightScraper(origin, destination, roundtrip, departDate, returnDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
