package com.calculator.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView previousOperandTextView;
    private TextView currentOperandTextView;
    private String currentOperand = "0";
    private String previousOperand = "";
    private String currentOperator = null;
    private boolean shouldResetScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previousOperandTextView = findViewById(R.id.previousOperand);
        currentOperandTextView = findViewById(R.id.currentOperand);

        setupNumberButtons();
        setupOperatorButtons();
        setupSpecialButtons();
    }

    private void setupNumberButtons() {
        View.OnClickListener numberClickListener = v -> {
            Button button = (Button) v;
            appendNumber(button.getText().toString());
        };

        int[] numberIds = {R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                          R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                          R.id.button8, R.id.button9, R.id.buttonDot};

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberClickListener);
        }
    }

    private void setupOperatorButtons() {
        View.OnClickListener operatorClickListener = v -> {
            Button button = (Button) v;
            appendOperator(button.getText().toString());
        };

        int[] operatorIds = {R.id.buttonPlus, R.id.buttonMinus,
                           R.id.buttonMultiply, R.id.buttonDivide};

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(operatorClickListener);
        }

        findViewById(R.id.buttonEquals).setOnClickListener(v -> calculate());
    }

    private void setupSpecialButtons() {
        findViewById(R.id.buttonClear).setOnClickListener(v -> clearDisplay());
        findViewById(R.id.buttonDelete).setOnClickListener(v -> deleteNumber());
    }

    private void appendNumber(String number) {
        if (shouldResetScreen) {
            currentOperand = "0";
            shouldResetScreen = false;
        }

        if (number.equals(".") && currentOperand.contains(".")) return;
        if (currentOperand.equals("0") && !number.equals(".")) {
            currentOperand = number;
        } else {
            currentOperand += number;
        }
        updateDisplay();
    }

    private void appendOperator(String operator) {
        if (currentOperand.equals("")) return;
        if (!previousOperand.equals("")) {
            calculate();
        }
        currentOperator = operator;
        previousOperand = currentOperand;
        currentOperand = "";
        updateDisplay();
    }

    private void calculate() {
        if (currentOperator == null || shouldResetScreen) return;
        
        double prev = Double.parseDouble(previousOperand);
        double current = Double.parseDouble(currentOperand);
        double result = 0;

        switch (currentOperator) {
            case "+":
                result = prev + current;
                break;
            case "-":
                result = prev - current;
                break;
            case "×":
                result = prev * current;
                break;
            case "÷":
                if (current == 0) {
                    showError("Cannot divide by zero!");
                    clearDisplay();
                    return;
                }
                result = prev / current;
                break;
        }

        currentOperand = String.valueOf(result);
        currentOperator = null;
        previousOperand = "";
        shouldResetScreen = true;
        updateDisplay();
    }

    private void clearDisplay() {
        currentOperand = "0";
        previousOperand = "";
        currentOperator = null;
        updateDisplay();
    }

    private void deleteNumber() {
        if (currentOperand.length() > 1) {
            currentOperand = currentOperand.substring(0, currentOperand.length() - 1);
        } else {
            currentOperand = "0";
        }
        updateDisplay();
    }

    private void updateDisplay() {
        currentOperandTextView.setText(currentOperand);
        if (!previousOperand.equals("") && currentOperator != null) {
            previousOperandTextView.setText(previousOperand + " " + currentOperator);
        } else {
            previousOperandTextView.setText("");
        }
    }

    private void showError(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(message)
               .setPositiveButton("OK", null)
               .show();
    }
}