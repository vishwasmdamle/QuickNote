package com.example.vishwasdamle.quickNote.model;

import org.joda.time.DateTime;

/**
 * Created by vishwasdamle on 03/04/15.
 */
public class ExpenseEntry {
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
}
