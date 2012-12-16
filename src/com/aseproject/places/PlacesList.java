package com.aseproject.places;

import java.io.Serializable;
import java.util.List;
 
import com.google.api.client.util.Key;
 
/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* * @author John Argyriou 2012
* */
public class PlacesList implements Serializable 
{ 
    @Key
    public String status;
 
    @Key
    public List<Place> results; 
}
