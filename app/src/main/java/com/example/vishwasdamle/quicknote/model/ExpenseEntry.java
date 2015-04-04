package com.example.vishwasdamle.quickNote.model;

import org.joda.time.DateTime;

/**
 * Created by vishwasdamle on 03/04/15.
 */
public class ExpenseEntry {
    private static final String SEPARATOR = ",";
    public static final String QUOTE = "\"";
    public static final String ESCAPED_QUOTE = "\"\"";
    public static final String NEWLINE_SEPARATOR = "\n";
    DateTime timeStamp;
    ExpenseType expenseType;
    Double amount;
    String description;

    public ExpenseEntry(DateTime timestamp, ExpenseType expenseType, Double amount, String description) {
        this.timeStamp = timestamp;
        this.expenseType = expenseType;
        this.amount = amount;
        this.description = description;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseEntry(ExpenseType expenseType, Double amount, String description) {
        this.timeStamp = new DateTime();
        this.expenseType = expenseType;
        this.amount = amount;
        this.description = description;
    }

    public String getPrintable() {
        return timeStamp + "\t" + expenseType.toString() + "\t" + amount + "\t" + description;
    }

    @Override
    public String toString() {
        return timeStamp.toString() + "\t" + expenseType.toString() + "\t" + amount + "\t" + description;
    }

    public String getCSVPrintable() {
        return (QUOTE + timeStamp + QUOTE)
                .concat(SEPARATOR).concat(QUOTE + expenseType + QUOTE)
                .concat(SEPARATOR).concat(String.valueOf(amount))
                .concat(SEPARATOR).concat(QUOTE + description.replaceAll(QUOTE, ESCAPED_QUOTE) + QUOTE)
                .concat(NEWLINE_SEPARATOR);

    }
}
