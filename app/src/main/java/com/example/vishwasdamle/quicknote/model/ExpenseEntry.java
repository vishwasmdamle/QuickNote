package com.example.vishwasdamle.quicknote.model;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class ExpenseEntry {
  private static final String SEPARATOR = ",";
  public static final String QUOTE = "\"";
  public static final String ESCAPED_QUOTE = "\"\"";
  public static final String NEWLINE_SEPARATOR = "\n";
  Long uid;
  DateTime timeStamp;
  ExpenseType expenseType;
  Double amount;
  String description;
  private String account;

  public ExpenseEntry(Long uid, DateTime timestamp, ExpenseType expenseType, Double amount, String description) {
    this.uid = uid;
    this.timeStamp = timestamp;
    this.expenseType = expenseType;
    this.amount = amount;
    this.description = description;
    this.account = "";
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

  public Long getUid() {
    return uid;
  }

  public String getAccount() {
    return account;
  }

  public ExpenseEntry(ExpenseType expenseType, Double amount, String description) {
    this.uid = 0L;
    this.timeStamp = new DateTime();
    this.expenseType = expenseType;
    this.amount = amount;
    this.description = description;
    this.account = "";
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

  public static Double accumulate(ArrayList<ExpenseEntry> expenseEntries) {
    Double acc = 0D;
    for (ExpenseEntry entry : expenseEntries) {
      acc = acc + getSumAmount(entry);
    }
    return acc;
  }

  private static double getSumAmount(ExpenseEntry entry) {
    return entry.amount * (entry.expenseType.equals(ExpenseType.CREDIT) ? 1 : -1);
  }
}
