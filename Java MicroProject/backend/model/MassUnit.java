package model;

public class MassUnit extends Unit {

    public MassUnit(
            String name,
            double conversionFactor
    ) {

        super(
                name,
                "Mass",
                conversionFactor
        );
    }


    public MassUnit(
            String name,
            double conversionFactor,
            String description,
            String region,
            String system
    ) {

        super(
                name,
                "Mass",
                conversionFactor,
                description,
                region,
                system
        );
    }
}