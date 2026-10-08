package service;

import database.DatabaseConnection;
import model.LengthUnit;
import model.MassUnit;
import model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversionService {

    private final Map<String, Unit> units =
            new HashMap<>();


    public ConversionService() {

        loadUnitsFromDatabase();
    }


    private void loadUnitsFromDatabase() {

        String sql = """
                SELECT
                    name,
                    category,
                    conversion_factor,
                    description,
                    region,
                    system
                FROM units
                """;


        try (
            Connection connection =
                    DatabaseConnection.connect();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery()
        ) {


            while (resultSet.next()) {

                String name =
                        resultSet.getString("name");


                String category =
                        resultSet.getString("category");


                double conversionFactor =
                        resultSet.getDouble(
                                "conversion_factor"
                        );


                String description =
                        resultSet.getString(
                                "description"
                        );


                String region =
                        resultSet.getString(
                                "region"
                        );


                String system =
                        resultSet.getString(
                                "system"
                        );


                Unit unit;


                /*
                 * Create the appropriate
                 * specialized unit.
                 */

                if (category.equals("Mass")) {

                    unit = new MassUnit(
                            name,
                            conversionFactor,
                            description,
                            region,
                            system
                    );


                } else if (category.equals("Length")) {

                    unit = new LengthUnit(
                            name,
                            conversionFactor,
                            description,
                            region,
                            system
                    );


                } else {

                    /*
                     * Volume, Area, and any future
                     * categories use the base Unit class.
                     */

                    unit = new Unit(
                            name,
                            category,
                            conversionFactor,
                            description,
                            region,
                            system
                    );
                }


                units.put(
                        name.toLowerCase(),
                        unit
                );


                System.out.println(
                        "Loaded: "
                        + name
                        + " | "
                        + category
                        + " | "
                        + system
                );
            }


            System.out.println(
                    "Units loaded from database: "
                    + units.size()
            );


        } catch (SQLException e) {

            System.out.println(
                    "Error loading units: "
                    + e.getMessage()
            );
        }
    }


    public double convertToBase(
            double value,
            String unitName
    ) {

        Unit unit =
                units.get(
                        unitName.toLowerCase()
                );


        if (unit == null) {

            throw new IllegalArgumentException(
                    "Unknown unit"
            );
        }


        return unit.convertToBase(value);
    }


    public double convertFromBase(
            double value,
            String unitName
    ) {

        Unit unit =
                units.get(
                        unitName.toLowerCase()
                );


        if (unit == null) {

            throw new IllegalArgumentException(
                    "Unknown unit"
            );
        }


        return value /
                unit.getConversionFactor();
    }


    public String getCategory(
            String unitName
    ) {

        Unit unit =
                units.get(
                        unitName.toLowerCase()
                );


        if (unit == null) {

            throw new IllegalArgumentException(
                    "Unknown unit"
            );
        }


        return unit.getCategory();
    }


    public List<Unit> getAllUnits() {

        return new ArrayList<>(
                units.values()
        );
    }
}