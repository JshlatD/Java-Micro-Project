package model;

public class Unit {

    private String name;
    private String category;
    private double conversionFactor;
    private String description;
    private String region;
    private String system;


    public Unit(
            String name,
            String category,
            double conversionFactor
    ) {

        this(
                name,
                category,
                conversionFactor,
                "",
                "",
                ""
        );
    }


    public Unit(
            String name,
            String category,
            double conversionFactor,
            String description,
            String region
    ) {

        this(
                name,
                category,
                conversionFactor,
                description,
                region,
                ""
        );
    }


    public Unit(
            String name,
            String category,
            double conversionFactor,
            String description,
            String region,
            String system
    ) {

        this.name =
                name;

        this.category =
                category;

        this.conversionFactor =
                conversionFactor;

        this.description =
                description;

        this.region =
                region;

        this.system =
                system;
    }


    public String getName() {

        return name;
    }


    public String getCategory() {

        return category;
    }


    public double getConversionFactor() {

        return conversionFactor;
    }


    public String getDescription() {

        return description;
    }


    public String getRegion() {

        return region;
    }


    public String getSystem() {

        return system;
    }


    public double convertToBase(
            double value
    ) {

        return value * conversionFactor;
    }
}