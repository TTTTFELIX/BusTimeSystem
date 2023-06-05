import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class lab4 {

    private static void Menu(){

        System.out.println("01:\tDisplay a Schedule");
        System.out.println("02:\tDelete a TripOffering");
        System.out.println("03:\tAdd a TripOffering");
        System.out.println("04:\tChange a Driver");
        System.out.println("05:\tChange a Bus");
        System.out.println("06:\tDisplay Trip Stops");
        System.out.println("07:\tDisplay Weekly Schedule for Driver");
        System.out.println("08:\tAdd a Driver");
        System.out.println("09:\tAdd a Bus");
        System.out.println("10:\tDelete a Bus");
        System.out.println("11:\tInsert Actual Trip Info");
        System.out.println("q:\tExit program");
        System.out.println("Please enter the 'number' or 'q'");

    }

    public static void main(String[] args) {
        String input;



        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab4", "root", "NSCnsc3244");

            Statement statement = connection.createStatement();

            Menu();


            Scanner scan = new Scanner(System.in);
            while(true){

                System.out.print("Enter: ");
                input = scan.nextLine();

                if(input.trim().equals("01"))

                    ShowSchedule(statement);

                else if(input.trim().equals("02"))

                    DeleteTripOffering(statement);

                else if(input.trim().equals("03"))

                    AddTripOffering(statement);

                else if(input.trim().equals("04"))

                    ChangeDriver(statement);

                else if(input.trim().equals("05"))

                    ChangeBus(statement);

                else if(input.trim().equals("06"))

                    ShowTripStops(statement);

                else if(input.trim().equals("07"))

                    ShowWeekly(statement);

                else if(input.trim().equals("08"))

                    AddDriver(statement);

                else if(input.trim().equals("09"))

                    AddBus(statement);

                else if(input.trim().equals("10"))

                    DeleteBus(statement);

                else if(input.trim().equals("11"))

                    EditTripData(statement);

                else if(input.trim().charAt(0) == 'q')

                    System.exit(0);

                else
                    Menu();
                scan.close();

            }


        }catch(Exception e){

            e.printStackTrace();

        }



    }

    public static void ShowSchedule(Statement stmt) throws SQLException {

        Scanner sc = new Scanner(System.in);

        System.out.print("Start Location Name: ");

        String StartLocation = sc.nextLine().trim();

        System.out.print("Destination Name: ");

        String Destiantion = sc.nextLine().trim();

        System.out.print("Date: ");

        String date = sc.nextLine().trim();

        try{

            ResultSet resultSet = stmt.executeQuery("SELECT T.ScheduledStartTime, T.ScheduledArrivalTime, T.DriverName, T.BusID " +

                    "FROM TripOffering T, Trip T1 " +

                    "WHERE Tr.StartLocationName LIKE '" + StartLocation + "' AND " +

                    "Tr.DestinationName LIKE '" + Destiantion + "' AND " +

                    "T.Date = '" + date + "' AND " +

                    "Tr.TripNumber = T.TripNumber " +

                    "Order by ScheduledStartTime ");

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int colCount = resultSetMetaData.getColumnCount();

            for(int i = 1; i <= colCount; i++){

                System.out.print(resultSetMetaData.getColumnName(i) + "\t");

            }

            System.out.println();

            while(resultSet.next()){

                for(int i = 1; i <= colCount; i++)

                    System.out.print(resultSet.getString(i) + "\t\t");

                System.out.println();

            }

            resultSet.close();

        }catch (Exception e){

            System.out.println("No schedule from " + StartLocation + " to " + Destiantion + " on " + date);

        }

    }

    public static void DeleteTripOffering(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("Trip Number: ");

        String TripNum = sc.nextLine();

        System.out.print("Date: ");

        String Date = sc.nextLine().trim();

        System.out.print("Scheduled Start Time: ");

        String StartTime = sc.nextLine().trim();

        sc.close();

        try{

            if(stmt.executeUpdate("DELETE TripOffering " +

                    "WHERE TripNumber = '" + TripNum + "' AND " +

                    "Date = '" + Date + "' AND " +

                    "ScheduledStartTime = '" + StartTime + "'") == 1){

                System.out.println("Successfully deleted Trip Offering");

            }else
                System.out.println("No Trip Offering with Trip Number: " + TripNum + " on "+ Date + " starting at "+ StartTime);

        }catch (Exception e){
            System.out.println("No Trip Offering with Trip Number: " + TripNum + " on "+ Date + " starting at "+ StartTime);
        }

    }

    public static void AddTripOffering(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        while(true){

            System.out.print("Enter Trip Number: ");

            String TripNum = sc.nextLine().trim();

            System.out.print("Date: ");

            String Date = sc.nextLine().trim();

            System.out.print("Scheduled Start Time: ");

            String StartTime = sc.nextLine().trim();

            System.out.print("Scheduled Arrival Time: ");

            String ArrivalTime = sc.nextLine().trim();

            System.out.print("Driver Name: ");

            String Driver = sc.nextLine().trim();

            System.out.print("Bus ID: ");

            String Bus = sc.nextLine().trim();

            sc.close();

            try{

                stmt.execute("INSERT INTO TripOffering VALUES ('" + TripNum + "', '" + Date + "', '" + StartTime + "', '" + ArrivalTime + "', '" + Driver + "', '" + Bus + "')");

                System.out.print("Successfully added a new Trip Offering");

            }catch (Exception e){

                System.out.println("Check input formatting");

            }

            System.out.print("Add another Trip Offering? (y/n): ");

            String inputy = sc.nextLine();

            sc.close();

            if(inputy.trim().charAt(0) == 'y'){

            }else{

                break;

            }

        }

    }

    public static void ChangeDriver(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("New Driver Name: ");

        String Driver = sc.nextLine().trim();

        System.out.print("Trip Number: ");

        String TripNum = sc.nextLine().trim();

        System.out.print("Date: ");

        String Date = sc.nextLine().trim();

        System.out.print("Scheduled Start Time: ");

        String StartTime = sc.nextLine().trim();

        sc.close();

        try{
            if(stmt.executeUpdate("UPDATE TripOffering " +

                    "SET DriverName = '" + Driver + "' " +

                    "WHERE TripNumber = '" + TripNum + "' AND " +

                    "Date = '" + Date + "' AND " +

                    "ScheduledStartTime = '" + StartTime + "'") == 0){

                System.out.println("No Trip Offering with Trip Number: " + TripNum + " on "+ Date + " starting at "+ StartTime);

            }else

                System.out.println("Successfully updated Driver");

        }catch (Exception e){

            System.out.println("No Driver in database");

        }

    }

    public static void ChangeBus(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("New Bus Number: ");

        String Bus = sc.nextLine().trim();

        System.out.print("Trip Number: ");

        String TripNum = sc.nextLine().trim();

        System.out.print("Date: ");

        String Date = sc.nextLine().trim();

        System.out.print("Scheduled Start Time: ");

        String StartTime = sc.nextLine().trim();

        sc.close();

        try{

            if(stmt.executeUpdate("UPDATE TripOffering " +

                    "SET BusID = '" + Bus + "' " +

                    "WHERE TripNumber = '" + TripNum + "' AND " +

                    "Date = '" + Date + "' AND " +

                    "ScheduledStartTime = '" + StartTime + "'") == 0){

                System.out.println("No Trip Offering with Trip Number: " + TripNum + " on "+ Date + " starting at "+ StartTime);

            }else

                System.out.println("Successfully updated Bus");

        }catch (Exception e){

            System.out.println("No BusNumber in database");

        }

    }

    public static void ShowTripStops(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("Trip Number: ");

        String TripNum = sc.nextLine().trim();

        try{

            ResultSet rs = stmt.executeQuery("SELECT * " +

                    "FROM TripStopInfo " +

                    "WHERE TripNumber = '" + TripNum + "' " +

                    "Order By SequenceNumber ");

            ResultSetMetaData rsmd = rs.getMetaData();

            int colCount = rsmd.getColumnCount();

            for(int i = 1; i <= colCount; i++){

                System.out.print(rsmd.getColumnName(i) + "\t");

            }

            System.out.println();

            while(rs.next()){

                for(int i = 1; i <= colCount; i++)

                    System.out.print(rs.getString(i) + "\t\t\t");

                System.out.println();

            }

            rs.close();

        }catch (Exception e){

            System.out.println("Trip Number: '" + TripNum + "' not found");

        }

    }

    public static void ShowWeekly(Statement stmt) throws ParseException, SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("Driver name: ");

        String Driver = sc.nextLine().trim();

        System.out.print("Date: ");

        String DateStr = sc.nextLine().trim();

        sc.close();

        SimpleDateFormat df = new SimpleDateFormat("MM/DD/yy");

        Calendar Date = new GregorianCalendar();

        Date.setTime(df.parse(DateStr));

        for(int i = 0; i < 7; i++){

            DateStr = df.format(Date.getTime());

            try{

                ResultSet rs = stmt.executeQuery("SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID " +

                        "FROM TripOffering " +

                        "WHERE DriverName LIKE '" + Driver + "' " +

                        "AND Date = '" + DateStr + "' " +

                        "Order By ScheduledStartTime ");

                ResultSetMetaData rsmd = rs.getMetaData();

                int colCount = rsmd.getColumnCount();

                if(i == 0){

                    System.out.println("Day 1");

                    for(int j = 1; j <= colCount; j++){

                        if(j == 1 || j == 3)

                            System.out.print(rsmd.getColumnName(j) + "\t");

                        else

                            System.out.print(rsmd.getColumnName(j) + "\t\t");

                    }

                    System.out.println();

                }

                while(rs.next()){

                    for(int j = 1; j <= colCount; j++)

                        System.out.print(rs.getString(j) + "\t\t");

                    System.out.println();

                }

                rs.close();

            }catch(Exception e){

                System.out.println("No Driver Found");

            }

            Date.add(Calendar.DATE, 1);

            if(i < 6)

                System.out.println("Day " + (i+2));

        }

        System.out.println();

    }

    public static void AddDriver(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("Driver name: ");

        String Driver = sc.nextLine().trim();

        System.out.print("Phone number: ");

        String Phone = sc.nextLine().trim();

        sc.close();

        try{

            stmt.execute("INSERT INTO Driver VALUES ('" + Driver + "', '" + Phone + "')");

            System.out.println("Successfully added a new Driver");

        }catch (Exception e){

            System.out.println(" ADD FAILED ");

        }

    }

    public static void AddBus(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("Bus ID: ");

        String Bus = sc.nextLine().trim();

        System.out.print("Bus model: ");

        String Model = sc.nextLine().trim();

        System.out.print("Bus year: ");

        String Year = sc.nextLine().trim();

        sc.close();

        try{

            stmt.execute("INSERT INTO Bus VALUES ('" + Bus + "', '" + Model + "', '" + Year + "')");

            System.out.println("Successfully added a new Bus");

        }catch (Exception e){

            System.out.println(" ADD FAILED");

        }

    }

    public static void DeleteBus(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("BusID: ");

        String Bus = sc.nextLine().trim();

        sc.close();

        try{

            if(stmt.executeUpdate("DELETE Bus " +

                    "FROM Bus" +

                    "WHERE BusID = '" + Bus + "'") == 0){

                System.out.println("No BusID = " + Bus + "Found");

            }else{

                System.out.println("Successfully Deleted");

            }

        }catch(Exception e){
            System.out.println("No BusID = " + Bus + " Found");

        }

    }

    public static void EditTripData(Statement stmt) throws SQLException{

        Scanner sc = new Scanner(System.in);

        System.out.print("TripNumber: ");

        String TripNum = sc.nextLine().trim();

        System.out.print("Date: ");

        String Date = sc.nextLine().trim();

        System.out.print("ScheduledStartTime: ");

        String StartTime = sc.nextLine().trim();

        System.out.print("StopNumber: ");

        String Stop = sc.nextLine().trim();

        System.out.print("ScheduledArrivalTime: ");

        String ArrivalTime = sc.nextLine().trim();

        System.out.print("ActualStartTime: ");

        String ActualStart = sc.nextLine().trim();

        System.out.print("ActualArrivalTime: ");

        String ActualArrival = sc.nextLine().trim();

        System.out.print("Passengers in: ");

        String PassIn = sc.nextLine().trim();

        System.out.print("Passengers out: ");

        String PassOut = sc.nextLine().trim();

        sc.close();

        try{

            stmt.execute("INSERT INTO ActualTripStopInfo VALUES ('" + TripNum + "', '" + Date + "', '" + StartTime + "', '" + Stop + "', '" + ArrivalTime

                    + "', '" + ActualStart + "', '" + ActualArrival + "', '" + PassIn + "', '" + PassOut + "')");

        }catch(Exception e){

            System.out.println("INSERT FAILED");

        }

        System.out.println("Successfully recorded data");

    }


}
