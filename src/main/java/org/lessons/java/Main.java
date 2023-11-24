package org.lessons.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	private static final String url = "jdbc:mysql://localhost:3306/db-nations";
	private static final String user = "root";
	private static final String pws = "";

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("filtra i paesi per nome: ");
		String input = in.nextLine();
		System.out.println();
		
		try (Connection con = DriverManager.getConnection(url, user, pws)) {  
			  
			final String SQL1 = ""
					+ " SELECT countries.country_id AS 'countryId', countries.name AS 'countryName', regions.name AS 'regionsName', continents.name AS 'continentsName' "
					+ " FROM countries "
					+ " JOIN regions "
					+ " ON countries.region_id = regions.region_id "
					+ " JOIN continents "
					+ " ON regions.continent_id = continents.continent_id "
					+ " WHERE countries.name LIKE ? "
					+ " ORDER BY countries.name "
					+ " ; ";
			
			final String SQL2 = ""
					+ " SELECT countries.name "
					+ " FROM countries "
					+ " WHERE countries.country_id = ? "
					+ " ; ";
			  
			final String SQL3 = ""
					+ " SELECT languages.language "
					+ " FROM countries "
					+ " JOIN country_languages "
					+ " ON countries.country_id = country_languages.country_id "
					+ " JOIN languages "
					+ " ON country_languages.language_id = languages.language_id "
					+ " WHERE countries.country_id = ? "
					+ " ; ";
			
			final String SQL4 = ""
					+ " SELECT country_stats.year, country_stats.population, country_stats.gdp "
					+ " FROM countries "
					+ " JOIN country_stats "
					+ " ON countries.country_id = country_stats.country_id "
					+ " WHERE countries.country_id = ? "
					+ " ORDER BY country_stats.year DESC "
					+ " LIMIT 1 "
					+ " ; ";
				
		  try(PreparedStatement ps = con.prepareStatement(SQL1)){
			  
			  ps.setString(1, "%" + input + "%");
			  
			  System.out.println();
			  
			  try(ResultSet rs = ps.executeQuery()){
		    	
		    		while (rs.next()) {
						
		    			int id = rs.getInt(1);
	    				String countryName = rs.getString(2);
	    				String regionsName = rs.getString(3);
	    				String continentsName = rs.getString(4);
	    				
	    				System.out.println("[" + id + "] " + countryName + " - " + regionsName + " - " + continentsName + "\n");
		    		}
			  }
		  }
		  
		  System.out.println();
		  System.out.print("scegli un paese tramite ID: ");
		  String strInput2 = in.nextLine();
		  int input2 = Integer.valueOf(strInput2);
		  System.out.println();
		  
		  try (PreparedStatement ps = con.prepareStatement(SQL2)) {
			  
			  System.out.print("\n----------------------------------------------------------\n");
			  
			  ps.setInt(1, input2);
			  
			  try (ResultSet rs = ps.executeQuery()) {
				
				  while (rs.next()) {
					  
					  String name = rs.getString(1);
					  
					  System.out.print("Dettagli del paese: " + name);
				}
			}
		  }
		  
		  try(PreparedStatement ps = con.prepareStatement(SQL3)){
			  
			  ps.setInt(1, input2);
			  
			  System.out.print("\n----------------------------------------------------------\n");
			  System.out.print("Linguaggi: ");
			  
			  try (ResultSet rs = ps.executeQuery()) {
				
				  while (rs.next()) {
					  
					  String language = rs.getString(1);
					  
					  if (!rs.isLast()) {
						  
						  System.out.print(language + ", ");
						  
					  } else {
						  
						  System.out.print(language);
					  }
				  }
			}
		  }
		  
		  try(PreparedStatement ps = con.prepareStatement(SQL4)){
			  
			  ps.setInt(1, input2);
			  
			  System.out.print("\n----------------------------------------------------------\n");
			  System.out.println("Statistiche più recenti\n");
			  
			  try (ResultSet rs = ps.executeQuery()) {
				
				  while (rs.next()) {
					  
					  // SELECT country_stats.year, country_stats.population, country_stats.gdp
					  
					  String year = rs.getString(1);
					  String population = rs.getString(2);
					  String gdp = rs.getString(3);
					  
					  System.out.println("Anno statistiche: " + year);
					  System.out.println("Popolazione: " + population);
					  System.out.println("GDP: " + gdp);
				  }
			}
		  }
		  
		} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
		}
		
		in.close();
	}
}
