package com.example.amey.signin;

/*
*
* Project Name: 	Smart Bus Tracking System
* Author List: 		Amey Karmarkar
* Filename: 		Location.java
* Functions: 		Location(), getlatitude(), getlongitude()
* Global Variables:	latitude, longitude
*
*/
public class Location {

    private double latitude;
    private double longitude;



    public Location() {
        /*
*
* Function Name: 	Location
* Input: 		none
* Output: 		none
* Logic: 		Constructor Declaration with no Parameter
* Example Call:		Called when object is created.
*
*/
        System.out.println("in Constructuor");
    }

    public Location(double Latitude, double Longitude) {
               /*
*
* Function Name: 	Location
* Input: 		Latitude, Longitude
* Output: 		none
* Logic: 		Constructor Declaration with Parameter
* Example Call:		Called when object is created with parameters.
*
*/
        System.out.println("in parameterised Constructuor");
        this.latitude = Latitude;
        this.longitude = Longitude;

    }

    public double getlatitude() {
               /*
*
* Function Name: 	getLatitude
* Input: 		none
* Output: 		float value of latitude
* Logic: 		Function which returns latitude value of location
* Example Call:		double l = location.getLatitude();
*
*/

        return  latitude;
    }


    public double getlongitude() {
                 /*
*
* Function Name: 	getLongitude
* Input: 		none
* Output: 		float value of latitude
* Logic: 		Function which returns longitude value of location
* Example Call:		double l = location.getLongitude();
*
*/

        return  longitude;


    }

}
