package model;

public class LengthUnit extends Unit {

    public LengthUnit(
            String name,
            double conversionFactor
    ) {

        super(
                name,
                "Length",
                conversionFactor
        );
    }


    public LengthUnit(
            String name,
            double conversionFactor,
            String description,
            String region,
            String system
    ) {

        super(
                name,
                "Length",
                conversionFactor,
                description,
                region,
                system
        );
    }
}