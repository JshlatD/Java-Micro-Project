let allUnits = [];


/* ================================
   LOAD UNITS
================================ */

async function loadUnits() {

    try {

        const response =
            await fetch(
                "http://localhost:8080/api/units"
            );


        allUnits =
            await response.json();


        updateFromUnits();

        updateToUnits();


    } catch (error) {

        console.error(
            "Could not load units:",
            error
        );


        document.getElementById(
            "fromUnit"
        ).innerHTML =
            '<option value="">Could not load units</option>';


        document.getElementById(
            "toUnit"
        ).innerHTML =
            '<option value="">Could not load units</option>';
    }
}


/* ================================
   FILTER UNITS
================================ */

function getMatchingUnits(
    system,
    category
) {

    return allUnits.filter(
        unit =>
            unit.system === system &&
            unit.category === category
    );
}


/* ================================
   UPDATE FROM UNITS
================================ */

function updateFromUnits() {

    const system =
        document.getElementById(
            "fromSystem"
        ).value;


    const category =
        document.getElementById(
            "fromCategory"
        ).value;


    const select =
        document.getElementById(
            "fromUnit"
        );


    const units =
        getMatchingUnits(
            system,
            category
        );


    populateUnitSelect(
        select,
        units
    );


    showUnitInformation();
}


/* ================================
   UPDATE TO UNITS
================================ */

function updateToUnits() {

    const system =
        document.getElementById(
            "toSystem"
        ).value;


    const category =
        document.getElementById(
            "toCategory"
        ).value;


    const select =
        document.getElementById(
            "toUnit"
        );


    const units =
        getMatchingUnits(
            system,
            category
        );


    populateUnitSelect(
        select,
        units
    );


    showUnitInformation();
}


/* ================================
   POPULATE SELECT
================================ */

function populateUnitSelect(
    select,
    units
) {

    select.innerHTML = "";


    if (units.length === 0) {

        const option =
            document.createElement(
                "option"
            );


        option.value = "";

        option.textContent =
            "No units available";


        select.appendChild(
            option
        );


        return;
    }


    units.forEach(unit => {

        const option =
            document.createElement(
                "option"
            );


        option.value =
            unit.name.toLowerCase();


        option.textContent =
            unit.name;


        select.appendChild(
            option
        );

    });
}


/* ================================
   UNIT INFORMATION
================================ */

function showUnitInformation() {

    const unitInfo =
        document.getElementById(
            "unitInfo"
        );


    const fromUnitName =
        document.getElementById(
            "fromUnit"
        ).value;


    const toUnitName =
        document.getElementById(
            "toUnit"
        ).value;


    const selectedUnit =
        allUnits.find(
            unit =>
                unit.name.toLowerCase()
                === fromUnitName
        )
        ||
        allUnits.find(
            unit =>
                unit.name.toLowerCase()
                === toUnitName
        );


    if (!selectedUnit) {

        unitInfo.innerHTML =
            "<p>Select a unit to view its information.</p>";

        return;
    }


    unitInfo.innerHTML = `

        <h3>
            ${selectedUnit.name}
        </h3>

        <p>
            <strong>System:</strong>
            ${selectedUnit.system}
        </p>

        <p>
            <strong>Category:</strong>
            ${selectedUnit.category}
        </p>

        <p>
            <strong>Description:</strong>
            ${selectedUnit.description || "Not available"}
        </p>

        <p>
            <strong>Historical / Regional Usage:</strong>
            ${selectedUnit.region || "Not available"}
        </p>

    `;
}


/* ================================
   CONVERSION
================================ */

async function convert() {

    const value =
        document.getElementById(
            "value"
        ).value;


    const fromUnit =
        document.getElementById(
            "fromUnit"
        ).value;


    const toUnit =
        document.getElementById(
            "toUnit"
        ).value;


    const fromCategory =
        document.getElementById(
            "fromCategory"
        ).value;


    const toCategory =
        document.getElementById(
            "toCategory"
        ).value;


    const resultElement =
        document.getElementById(
            "result"
        );


    if (!value) {

        resultElement.innerText =
            "Please enter a value.";

        return;
    }


    if (!fromUnit || !toUnit) {

        resultElement.innerText =
            "Please select both units.";

        return;
    }


    if (
        fromCategory !== toCategory
    ) {

        resultElement.innerText =
            "Units must belong to the same category.";

        return;
    }


    try {

        const response =
            await fetch(
                `http://localhost:8080/api/convert?value=${encodeURIComponent(value)}&from=${encodeURIComponent(fromUnit)}&to=${encodeURIComponent(toUnit)}`
            );


        const data =
            await response.json();


        if (data.error) {

            resultElement.innerText =
                data.error;

            return;
        }


        resultElement.innerText =
            `${data.value} ${data.fromUnit} = ${data.result} ${data.toUnit}`;


        addToHistory(
            `${data.value} ${data.fromUnit} → ${data.result} ${data.toUnit}`
        );


    } catch (error) {

        console.error(error);


        resultElement.innerText =
            "Could not connect to Java server.";
    }
}


/* ================================
   HISTORY
================================ */

function addToHistory(text) {

    const historyList =
        document.getElementById(
            "historyList"
        );


    const noHistoryMessage =
        historyList.querySelector(
            "p"
        );


    if (noHistoryMessage) {

        noHistoryMessage.remove();
    }


    const item =
        document.createElement(
            "p"
        );


    item.innerText =
        text;


    historyList.prepend(
        item
    );
}


/* ================================
   CLEAR HISTORY
================================ */

function clearHistory() {

    const historyList =
        document.getElementById(
            "historyList"
        );


    historyList.innerHTML =
        "<p>No conversions yet.</p>";
}


/* ================================
   START
================================ */

loadUnits();