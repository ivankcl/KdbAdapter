package main;


//import cn.edu.ruc.adapter.BaseAdapter;
//import cn.edu.ruc.start.TSBM;
import main.kx.c;
import java.io.*;
import main.kx.c.KException;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class KdbAdapter {
	private String host = "34.125.238.1"; //gcp
//	private String host = "47.253.89.109"; //ali
	private int port = 5001;
	private String username = "root";
	private String password = "root";
	private static String price_path = "~/TEMGData/price-3000-4000.csv";
	private static String base_path = "~/TEMGData/base-3000-4000.csv";
	private static String split_path = "~/TEMGData/split-3000-4000.csv";
	static KdbAdapter test = new KdbAdapter();
	private static int numOfTest = 1;
	
	// private boolean useTLS;
	//private String dbName = "ruc_test";
	
	public static void ping() {
		System.out.println("ping");
	}
	
	public c getConnection() {
		try {
			return new c(host, port);
		}catch (IOException | KException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static double query(String q) throws KException {
		
		try {
			c query = test.getConnection();
			long startTime = System.nanoTime();
			Object temp = query.k(q); 
//			query.ks(q);
			long endTime = System.nanoTime();
			query.close();
			temp = null;
			return (endTime - startTime)/1000000.0;
		}catch(IOException e) {
			return 0.0;
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		c kdbServer = test.getConnection();
		ping();
		
		Scanner mode = new Scanner(System.in);
		System.out.println("Enter mode: ('1' for query only; '0' for query and importing the data");
		int modeNum = mode.nextInt();
		
		if(modeNum == 0) {
		
			long startTime = System.nanoTime();
			String line = "";
			
			//price
			LinkedList<String> PidList = new LinkedList<>();
			LinkedList<Timestamp> dateList = new LinkedList<>();
			LinkedList<Double> highList = new LinkedList<>();
			LinkedList<Double> lowList = new LinkedList<>();
			LinkedList<Double> openList = new LinkedList<>();
			LinkedList<Double> closeList = new LinkedList<>();
			LinkedList<Double> volList = new LinkedList<>();
			
			//base
			LinkedList<String> BidList = new LinkedList<>();
			LinkedList<String> ExList = new LinkedList<>();
			LinkedList<String> DescrList = new LinkedList<>();
			LinkedList<String> SICList = new LinkedList<>();
			LinkedList<String> SPRList = new LinkedList<>();
			LinkedList<String> CuList = new LinkedList<>();
			LinkedList<Timestamp> CreateDateList = new LinkedList<>();
			
			//split
			LinkedList<String> SidList = new LinkedList<>();
			LinkedList<Timestamp> SplitDateList = new LinkedList<>();
			LinkedList<Timestamp> EntryDateList = new LinkedList<>();
			LinkedList<Short> FactorList = new LinkedList<>();
			
			System.out.println("Starting to import data to array...");
			//date import
			try {
				//price
				BufferedReader Pbr = new BufferedReader(new FileReader(price_path));
				line = Pbr.readLine();
				while((line = Pbr.readLine()) != null) {
	
					String[] values = line.split(",");
					
					PidList.add(values[0]);
					java.util.Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(values[1]);
					Timestamp tempTime = new java.sql.Timestamp(tempDate.getTime());
					dateList.add(tempTime);
					highList.add((Double.valueOf(values[2])));
					lowList.add((Double.valueOf(values[3])));
					openList.add((Double.valueOf(values[4])));
					closeList.add((Double.valueOf(values[5])));
					volList.add((Double.valueOf(values[6])));
	
	 			}
				System.out.println("Price table imported.");
				
				//base
				BufferedReader Bbr = new BufferedReader(new FileReader(base_path));
				line = Bbr.readLine();
				while((line = Bbr.readLine()) != null) {
	
					String[] values = line.split(",");
					
					BidList.add(values[0]);
					ExList.add(values[1]);
					DescrList.add(values[2]);
					SICList.add(values[3]);
					SPRList.add(values[4]);
					CuList.add(values[5]);
					java.util.Date tempDate = new SimpleDateFormat("dd/MM/yyyy").parse(values[6]);
					Timestamp tempTime = new java.sql.Timestamp(tempDate.getTime());
					CreateDateList.add(tempTime);
	
	 			}
				System.out.println("Base table imported.");
				//split
				
				BufferedReader Sbr = new BufferedReader(new FileReader(split_path));
				line = Sbr.readLine();
				while((line = Sbr.readLine()) != null) {
	
					String[] values = line.split(",");
					
					SidList.add(values[0]);
					java.util.Date tempDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(values[1]);
					Timestamp tempTime1 = new java.sql.Timestamp(tempDate1.getTime());
					SplitDateList.add(tempTime1);
					java.util.Date tempDate2 = new SimpleDateFormat("yyyy-MM-dd").parse(values[2]);
					Timestamp tempTime2 = new java.sql.Timestamp(tempDate2.getTime());
					EntryDateList.add(tempTime2);
					FactorList.add(Short.valueOf(values[3]));
	 			}
				System.out.println("Split table imported.");
				
			}catch(IOException e){
				e.printStackTrace();
				
			}
			//price
			Object[] Pid = PidList.toArray();
			Object[] date = dateList.toArray();
			Object[] high = highList.toArray();
			Object[] low = lowList.toArray();
			Object[] open = openList.toArray();
			Object[] close = closeList.toArray();
			Object[] vol = volList.toArray();
	
			Object[] priceData = new Object[] {Pid, date, high, low, open, close, vol};
			String[] priceColumnNames = new String[] {"id","tradedate", "high", "low", "open", "close", "vol"};
	
			
			c.Dict priceDict = new c.Dict(priceColumnNames, priceData);
			c.Flip priceTable = new c.Flip(priceDict);
			
			//base
			Object[] Bid = BidList.toArray();
			Object[] Ex = ExList.toArray();
			Object[] Descr = DescrList.toArray();
			Object[] SIC = SICList.toArray();
			Object[] SPR = SPRList.toArray();
			Object[] Cu = CuList.toArray();
			Object[] CreateDate = CreateDateList.toArray();
			
			Object[] baseData = new Object[] {Bid, Ex, Descr, SIC, SPR, Cu, CreateDate};
			String[] baseColumnNames = new String[] {"id","ex","descr", "sic", "spr","cu","createdate"};
			
			c.Dict baseDict = new c.Dict(baseColumnNames, baseData);
			c.Flip baseTable = new c.Flip(baseDict);
			
			//split
			Object[] Sid = SidList.toArray();
			Object[] SplitDate = SplitDateList.toArray();
			Object[] EntryDate = EntryDateList.toArray();
			Object[] SplitFactor = FactorList.toArray();
			
			Object[] splitData = new Object[] {Sid, SplitDate, EntryDate, SplitFactor};
			String[] splitColumnNames = new String[] {"id", "splitdate", "entrydate", "factor"};
			
			c.Dict splitDict = new c.Dict(splitColumnNames, splitData);
			c.Flip splitTable = new c.Flip(splitDict);
			
			//import data
			System.out.println("Starting to import data to the server...");
			kdbServer.ks("insert", "price", priceTable);
			System.out.println("Price table imported.");
			kdbServer.ks("insert", "base", baseTable);
			System.out.println("Base table imported.");
			kdbServer.ks("insert", "split", splitTable);
			System.out.println("Split table imported.");
			long endTime = System.nanoTime();
		
		
			System.out.println("Import Time:" + (endTime-startTime)/1000000000.0);
			// Releasing all the memories
			System.out.println("Releasing the memories...");
			
			kdbServer.close();
			PidList.remove(); dateList.remove(); highList.remove(); lowList.remove(); openList.remove(); closeList.remove(); volList.remove();
			BidList.remove(); ExList.remove(); DescrList.remove(); SICList.remove(); SPRList.remove(); CuList.remove(); CreateDateList.remove();
			SidList.remove(); SplitDateList.remove(); EntryDateList.remove(); FactorList.remove();
			
			Pid = null; date = null; high = null; low = null; open = null; close = null; vol = null;
			Bid = null; Ex = null; Descr = null; SIC = null; SPR = null; Cu = null; CreateDate = null;
			Sid = null; SplitDate = null; EntryDate = null; SplitFactor = null;
			
			priceData = null; baseData = null; splitData = null;
			priceColumnNames = null; splitColumnNames = null; baseColumnNames = null;
			priceDict = null; baseDict = null; splitDict = null;
			priceTable = null; baseTable = null; splitTable = null;
			
			System.out.println("Done.");
			
		}
		
		//query and output
		System.out.println("Start sending queries...");
		double query1a = 0.0;
		double query1b = 0.0;
		double query1c = 0.0;
		double query3 = 0.0;
		double query4 = 0.0;
			
		for(int i=0; i<numOfTest;i++) {
//			query1a += query("select avg close, max close, min close, asc id, tradedate.year by id, tradedate.year from price where tradedate.year > 2022 and tradedate.year < 2032");
			System.out.print(query1a + " ");
//	 		query1b += query("select avg close, max close, min close, asc id, tradedate.month by id, tradedate.month from price where tradedate.year > 2022 and tradedate.year < 2032");
	 		System.out.print(query1b + " ");
//	 		query1c += query("select from base");
	 		System.out.print(query1c + " ");
//	 		query3 += query("select price.id, price.tradedate, price.high, price.low from price uj split where price.id ~ split.id, price.tradedate ~ split.splitdate");
	 		System.out.print(query3 + " ");
//	 		query4 += query("select avg price.close from price uj base where price.id ~ base.id, base.sic ~ `COMPUTER");
	 		System.out.println(query4);
		}

 		//end
		System.out.println("Average time for query1a: " + query1a/numOfTest);
		System.out.println("Average time for query1b: " + query1b/numOfTest);
		System.out.println("Average time for query1c: " + query1c/numOfTest);
		System.out.println("Average time for query3: " + query3/numOfTest);
		System.out.println("Average time for query4: " + query4/numOfTest);
 		System.out.println("EOP");
       
	}
}
