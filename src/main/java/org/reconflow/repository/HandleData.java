package org.reconflow.repository;

import org.reconflow.model.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HandleData {
    public static List<Transaction> loadData(String path) {
        List<Transaction> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String transactionId = values[0];
                BigDecimal amount = new BigDecimal(values[1]);

                Transaction transaction = new Transaction(transactionId, amount);
                records.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("Error reading ");
            return records;
        }
        return records;
    }
}
