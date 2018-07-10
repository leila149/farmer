package com.janet.farmersclub;

/**
 * Created by anableila on 7/10/18.
 */

public class MyFarms {
    private static String farmname;
    private static String farmdesc;
    private static String location;

    public MyFarms(){}

    public MyFarms(String farmname, String farmdesc, String location){
        this.farmname = farmname;
        this.farmdesc = farmdesc;
        this.location = location;
    }



    public static String getName() {
        return farmname;
    }

    public void setName(String farm_name) {
        this.farmname = farmname;
    }

    public static String getDescription() {
        return farmdesc;
    }

    public void setDescription(String farmdesc) {
        this.farmdesc = farmdesc;
    }


    public static String getLocation() {
        return location;
    }

    public void setLocation(String farm_location) {
        this.location = location;
    }


}
