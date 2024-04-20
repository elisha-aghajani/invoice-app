package utils;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;

public final class Constants {

    public static final Color RED = Color.web("0xb00020");
    public static final Color GREEN = Color.web("0x009400");

    public static final ArrayList<String> STATES = new ArrayList<>(
            Arrays.asList("Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware",
                    "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
                    "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
                    "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New " +
                            "York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                    "Rhode Island", "South Carolina", "South Dakota ", "Tennessee", "Texas", "Utah", "Vermont",
                    "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"));

    public static final Map<String, String> STATES_ABRV = Map.ofEntries(
            entry("Alaska", "AK"),
            entry("Alberta", "AB"),
            entry("Arizona", "AZ"),
            entry("Arkansas", "AR"),
            entry("California", "CA"),
            entry("Colorado", "CO"),
            entry("Connecticut", "CT"),
            entry("Delaware", "DE"),
            entry("Florida", "FL"),
            entry("Georgia", "GA"),
            entry("Hawaii", "HI"),
            entry("Idaho", "ID"),
            entry("Illinois", "IL"),
            entry("Indiana", "IN"),
            entry("Iowa", "IA"),
            entry("Kansas", "KS"),
            entry("Kentucky", "KY"),
            entry("Louisiana", "LA"),
            entry("Maine", "ME"),
            entry("Maryland", "MD"),
            entry("Massachusetts", "MA"),
            entry("Michigan", "MI"),
            entry("Minnesota", "MN"),
            entry("Mississippi", "MS"),
            entry("Missouri", "MO"),
            entry("Montana", "MT"),
            entry("Nebraska", "NE"),
            entry("Nevada", "NV"),
            entry("New Hampshire", "NH"),
            entry("New Jersey", "NJ"),
            entry("New Mexico", "NM"),
            entry("New York", "NY"),
            entry("North Carolina", "NC"),
            entry("North Dakota", "ND"),
            entry("Ohio", "OH"),
            entry("Oklahoma", "OK"),
            entry("Ontario", "ON"),
            entry("Oregon", "OR"),
            entry("Pennsylvania", "PA"),
            entry("Rhode Island", "RI"),
            entry("South Carolina", "SC"),
            entry("South Dakota", "SD"),
            entry("Tennessee", "TN"),
            entry("Texas", "TX"),
            entry("Utah", "UT"),
            entry("Vermont", "VT"),
            entry("Virginia", "VA"),
            entry("Washington", "WA"),
            entry("West Virginia", "WV"),
            entry("Wisconsin", "WI"),
            entry("Wyoming", "WY")
    );
}
