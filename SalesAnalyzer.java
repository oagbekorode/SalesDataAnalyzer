/*
 * @author Oluwapamilerin Agbekorode
 * Start Date: 9/2/2025
 * Sales Data Analyzer Project:
 * Reads sales transactions from a CSV file and provides basic analytics:
 * total revenue, top products, sales by region, and repeat customer info.
 */


import java.io.File; //Import the File Class
import java.io.FileNotFoundException; //Import class to handle errors
import java.io.PrintWriter;
import java.util.Scanner; //Import the scanner class to read text files
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat; //for rounding to normal currency


public class SalesAnalyzer {
    public static void main(String[] args){
        String SalesRecord = "SalesRecord.csv";
        String OutputFile = "AnalysisReport.txt"; //new file to write results 

        
        //The metrics to calculate 
        double grandRevenue = 0;
        double grandCost = 0;
        double grandProfit = 0;

        Map<String, Integer> unitsPerItem = new HashMap<>();
        Map<String, Double> profitPerRegion = new HashMap<>();

        DecimalFormat money_format = new DecimalFormat();

        try(Scanner SalesReader = new Scanner(new File(SalesRecord));
            PrintWriter SalesWriter = new PrintWriter(OutputFile)
        ) {
            //to skip reading the header line 
            if(SalesReader.hasNextLine()) SalesReader.nextLine();

            while(SalesReader.hasNextLine()) {
                String Line = SalesReader.nextLine();
                String[] values = Line.split(",");

                //Columns needed based on the headers
                String region = values[0];
                String itemType = values[2];
                int unitsSold = Integer.parseInt(values[8]);
                double totalRevenue = Double.parseDouble(values[11]);
                double totalCost = Double.parseDouble(values[12]);
                double totalProfit = Double.parseDouble(values[13]);

                //Update grand totals
                grandRevenue += totalRevenue;
                grandCost += totalCost;
                grandProfit += totalProfit;

                // Update units sold per item type
                unitsPerItem.put(itemType, unitsPerItem.getOrDefault(itemType, 0) + unitsSold);
                
                // Update profit per region
                profitPerRegion.put(region, profitPerRegion.getOrDefault(region, 0.0) + totalProfit);            
            }

            // Print results
            SalesWriter.println("=================== Grand Totals ===================");
            SalesWriter.println("Revenue: $" + money_format.format(grandRevenue));
            SalesWriter.println("Cost: $" + money_format.format(grandCost));
            SalesWriter.println("Profit: $" + money_format.format(grandProfit));

            SalesWriter.println("\n=================== Units Sold per Item Type ===================");
            for(String item : unitsPerItem.keySet()) {
                SalesWriter.println(item + ": " + unitsPerItem.get(item));
            }
            
            SalesWriter.println("\n=================== Profit per Region ===================");

            for(String reg : profitPerRegion.keySet()) {
                SalesWriter.println(reg + ": $" + money_format.format((profitPerRegion.get(reg))));
            }
            // =================== Key Insights ===================
            String topItem = null;
            int maxUnits = 0;
            for (Map.Entry<String, Integer> entry : unitsPerItem.entrySet()) {
                if (entry.getValue() > maxUnits) {
                    topItem = entry.getKey();
                    maxUnits = entry.getValue();
                }
            }

            String topRegion = null;
            double maxProfit = 0.0;
            for (Map.Entry<String, Double> entry : profitPerRegion.entrySet()) {
                if (entry.getValue() > maxProfit) {
                    topRegion = entry.getKey();
                    maxProfit = entry.getValue();
                }
            }

            SalesWriter.println("\n=================== Key Insights ===================");
            SalesWriter.println("Top-Selling Product: " + topItem + " (" + maxUnits + " units)");
            SalesWriter.println("Most Profitable Region: " + topRegion + " ($" + money_format.format(maxProfit) + ")");
        }
        catch(FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}

