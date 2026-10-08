import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL =
            "jdbc:sqlite:../database/indicmeasure.db";


    public static Connection connect()
            throws SQLException {

        return DriverManager.getConnection(URL);
    }


    public static void initializeDatabase() {

        String createTableSql = """
                CREATE TABLE IF NOT EXISTS units (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    conversion_factor REAL NOT NULL,
                    description TEXT,
                    region TEXT,
                    system TEXT
                )
                """;


        try (
            Connection connection = connect();
            Statement statement =
                    connection.createStatement()
        ) {

            statement.execute(createTableSql);

            addColumnIfMissing(
                    connection,
                    "description"
            );

            addColumnIfMissing(
                    connection,
                    "region"
            );

            addColumnIfMissing(
                    connection,
                    "system"
            );


            System.out.println(
                    "Database initialized successfully."
            );


        } catch (SQLException e) {

            System.out.println(
                    "Database error: "
                    + e.getMessage()
            );
        }
    }


    private static void addColumnIfMissing(
            Connection connection,
            String columnName
    ) {

        try {

            String sql =
                    "ALTER TABLE units ADD COLUMN "
                    + columnName
                    + " TEXT";


            try (
                Statement statement =
                        connection.createStatement()
            ) {

                statement.execute(sql);
            }


        } catch (SQLException e) {

            // Column already exists.
        }
    }


    public static void addUnit(
            String name,
            String category,
            double conversionFactor,
            String description,
            String region,
            String system
    ) {

        String checkSql = """
                SELECT id
                FROM units
                WHERE name = ?
                """;


        String insertSql = """
                INSERT INTO units
                (
                    name,
                    category,
                    conversion_factor,
                    description,
                    region,
                    system
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;


        try (
            Connection connection = connect();

            PreparedStatement checkStatement =
                    connection.prepareStatement(
                            checkSql
                    )
        ) {

            checkStatement.setString(
                    1,
                    name
            );


            ResultSet resultSet =
                    checkStatement.executeQuery();


            if (resultSet.next()) {

                System.out.println(
                        "Unit already exists: "
                        + name
                );

                return;
            }


            try (
                PreparedStatement statement =
                        connection.prepareStatement(
                                insertSql
                        )
            ) {

                statement.setString(
                        1,
                        name
                );

                statement.setString(
                        2,
                        category
                );

                statement.setDouble(
                        3,
                        conversionFactor
                );

                statement.setString(
                        4,
                        description
                );

                statement.setString(
                        5,
                        region
                );

                statement.setString(
                        6,
                        system
                );


                statement.executeUpdate();


                System.out.println(
                        "Added unit: "
                        + name
                );
            }


        } catch (SQLException e) {

            System.out.println(
                    "Error adding unit "
                    + name
                    + ": "
                    + e.getMessage()
            );
        }
    }


    public static void addUnitInformation() {

        /*
         * =========================================
         * TRADITIONAL MASS
         * =========================================
         */


        addUnit(
                "Tola",
                "Mass",
                11.6638,
                "Traditional unit of mass used historically in the Indian subcontinent.",
                "Indian subcontinent",
                "Traditional"
        );


        addUnit(
                "Masha",
                "Mass",
                0.9722,
                "Traditional unit of mass used historically in India.",
                "Indian subcontinent",
                "Traditional"
        );


        addUnit(
                "Ratti",
                "Mass",
                0.1215,
                "Traditional unit of mass historically associated with the ratti seed.",
                "Indian subcontinent",
                "Traditional"
        );


        addUnit(
                "Pau",
                "Mass",
                233.275,
                "Traditional fractional unit of mass, commonly treated as one-quarter of a seer in some systems.",
                "India and South Asia",
                "Traditional"
        );


        addUnit(
                "Seer",
                "Mass",
                933.10,
                "Historical unit of mass used in parts of the Indian subcontinent.",
                "Indian subcontinent",
                "Traditional"
        );


        addUnit(
                "Maund",
                "Mass",
                37324.2,
                "Historical unit of mass used for larger quantities of goods and commodities.",
                "Indian subcontinent",
                "Traditional"
        );


        /*
         * =========================================
         * TRADITIONAL LENGTH
         * =========================================
         */


        addUnit(
                "Gaz",
                "Length",
                0.9144,
                "Traditional unit of length with historical regional usage.",
                "India and South Asia",
                "Traditional"
        );


        addUnit(
                "Gaj",
                "Length",
                0.9144,
                "Traditional unit of length used in various parts of India.",
                "India",
                "Traditional"
        );


        addUnit(
                "Kos",
                "Length",
                3200.0,
                "Traditional unit of distance whose value varied considerably by region and historical period.",
                "India and South Asia",
                "Traditional"
        );


        addUnit(
                "Yojana",
                "Length",
                12800.0,
                "Ancient unit of distance referenced in Indian historical and literary sources; its length varied by tradition.",
                "Ancient India",
                "Traditional"
        );


        /*
         * =========================================
         * METRIC MASS
         * Base unit = gram
         * =========================================
         */


        addUnit(
                "Milligram",
                "Mass",
                0.001,
                "Metric unit of mass equal to one-thousandth of a gram.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Gram",
                "Mass",
                1.0,
                "Metric base unit of mass.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Kilogram",
                "Mass",
                1000.0,
                "Metric unit of mass equal to one thousand grams.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Metric Tonne",
                "Mass",
                1000000.0,
                "Metric unit of mass equal to one thousand kilograms.",
                "International metric system",
                "Metric"
        );


        /*
         * =========================================
         * METRIC LENGTH
         * Base unit = metre
         * =========================================
         */


        addUnit(
                "Millimetre",
                "Length",
                0.001,
                "Metric unit of length equal to one-thousandth of a metre.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Centimetre",
                "Length",
                0.01,
                "Metric unit of length equal to one-hundredth of a metre.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Metre",
                "Length",
                1.0,
                "Metric base unit of length.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Kilometre",
                "Length",
                1000.0,
                "Metric unit of length equal to one thousand metres.",
                "International metric system",
                "Metric"
        );


        /*
         * =========================================
         * METRIC VOLUME
         * Base unit = millilitre
         * =========================================
         */


        addUnit(
                "Millilitre",
                "Volume",
                1.0,
                "Metric unit of volume commonly used for small quantities of liquid.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Litre",
                "Volume",
                1000.0,
                "Metric unit of volume equal to one thousand millilitres.",
                "International metric system",
                "Metric"
        );


        /*
         * =========================================
         * METRIC AREA
         * Base unit = square metre
         * =========================================
         */


        addUnit(
                "Square Centimetre",
                "Area",
                0.0001,
                "Metric unit of area equal to one ten-thousandth of a square metre.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Square Metre",
                "Area",
                1.0,
                "Metric base unit of area.",
                "International metric system",
                "Metric"
        );


        addUnit(
                "Square Kilometre",
                "Area",
                1000000.0,
                "Metric unit of area equal to one million square metres.",
                "International metric system",
                "Metric"
        );


        System.out.println(
                "Unit information added successfully."
        );
    }


    public static void showUnits() {

        String sql = """
                SELECT
                    id,
                    name,
                    category,
                    conversion_factor,
                    description,
                    region,
                    system
                FROM units
                ORDER BY id
                """;


        try (
            Connection connection = connect();

            Statement statement =
                    connection.createStatement();

            ResultSet resultSet =
                    statement.executeQuery(sql)
        ) {

            System.out.println();

            System.out.println(
                    "Units in database:"
            );

            System.out.println(
                    "--------------------------------"
            );


            int count = 0;


            while (resultSet.next()) {

                count++;


                System.out.println(
                        resultSet.getInt("id")
                        + " | "
                        + resultSet.getString("name")
                        + " | "
                        + resultSet.getString("category")
                        + " | "
                        + resultSet.getDouble(
                                "conversion_factor"
                        )
                        + " | "
                        + resultSet.getString(
                                "system"
                        )
                );


                System.out.println(
                        "Description: "
                        + resultSet.getString(
                                "description"
                        )
                );


                System.out.println(
                        "Region: "
                        + resultSet.getString(
                                "region"
                        )
                );


                System.out.println();
            }


            System.out.println(
                    "Total units: "
                    + count
            );


        } catch (SQLException e) {

            System.out.println(
                    "Error reading units: "
                    + e.getMessage()
            );
        }
    }


    public static void main(String[] args) {

        initializeDatabase();

        addUnitInformation();

        showUnits();
    }
}