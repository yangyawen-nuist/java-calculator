const form = document.querySelector("#calculator-form");
const leftInput = document.querySelector("#left");
const rightInput = document.querySelector("#right");
const resultOutput = document.querySelector("#result");
const statusOutput = document.querySelector("#status");
const calculateButton = document.querySelector(".calculate-button");
const operationButtons = [...document.querySelectorAll(".operation")];

let selectedOperator = "+";

operationButtons.forEach((button) => {
    button.addEventListener("click", () => {
        selectedOperator = button.dataset.operator;
        operationButtons.forEach((item) => item.classList.toggle("active", item === button));
    });
});

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    if (!form.reportValidity()) {
        return;
    }

    calculateButton.disabled = true;
    statusOutput.classList.remove("error");
    statusOutput.textContent = "Calling Java backend...";

    const query = new URLSearchParams({
        left: leftInput.value,
        operator: selectedOperator,
        right: rightInput.value
    });

    try {
        const response = await fetch(`/api/calculate?${query}`);
        const payload = await response.json();

        if (!response.ok) {
            throw new Error(payload.error || "Calculation failed");
        }

        resultOutput.textContent = payload.result;
        statusOutput.textContent = `${payload.left} ${displayOperator(payload.operator)} ${payload.right}`;
    } catch (error) {
        resultOutput.textContent = "Error";
        statusOutput.textContent = error.message;
        statusOutput.classList.add("error");
    } finally {
        calculateButton.disabled = false;
    }
});

function displayOperator(operator) {
    return operator === "*" ? "×" : operator === "/" ? "÷" : operator;
}
