package miniProjectJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.CallableStatement;
import java.util.Properties;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		
		Properties mySQLProps = new Properties();
		mySQLProps.setProperty("user", "root");
		mySQLProps.setProperty("password", "");
		
		String url = "jdbc:mysql://localhost:3306/mydb";
		
		try (
				Connection myConn = DriverManager.getConnection(url, mySQLProps);
				Scanner scan = new Scanner(System.in);
		) {
			
			boolean isDone1 = true;
			
			while(isDone1)
			{
				boolean isDone2 = true;
				System.out.println("\nEnter table name or Exit,\n\t Teacher, Course, Student or Exit: " );
				String tName = scan.nextLine().trim().toLowerCase();
			
				if(tName.equals("exit")) {
					isDone1 = false;
					isDone2 = false;
				}
			
				while(isDone2 && (tName.equals("teacher") || tName.equals("student") || tName.equals("course") )) {
					int answer=0;
					try {
					System.out.println("\nEnter number of action or Exit,"
							+ "\n\t 1. Show all data,"
							+ "\n\t2. Insert new data,"
							+ "\n\t3. Update Data,"
							+ "\n\t4. Delete Data,"
							+ "\n\t------"
							+ "\n\t5. Find teacher, course or student,"
							+ "\n\t6. Find Student by status,"
							+ "\n\t------"
							+ "\n\t7. Show statistic (Count Rows), "
							+ "\n\t8. Show Teacher-Course or Student-Course,"
							+ "\n\t9. Show History (Teacher table),"
							+ "\n\t------"
							+ "\n\t0. Exit: ");
					
					answer = Integer.parseInt(scan.nextLine());	
					}
					catch (NumberFormatException e)
					{
						System.out.println("Illegal number, " + e); 
					}
					
					switch (answer) {
				
					case 1: //Select All
						if(tName.equals("teacher")) showTeacher(myConn);
						if(tName.equals("course")) showCourse(myConn);
						if(tName.equals("student")) showStudent(myConn);
						break;
				
					case 2: //Insert
						if(tName.equals("teacher")) insertTeacher(myConn, scan);
						if(tName.equals("course")) insertCourse(myConn, scan);
						if(tName.equals("student")) insertStudent(myConn, scan);
						break;	
				
					case 3: //Update
						System.out.println("\nEnter ID number: " );
						int tmp = Integer.parseInt(scan.nextLine());
						if(tName.equals("teacher")) updateTeacher(myConn, scan, tmp);
						if(tName.equals("course")) updateCourse(myConn, scan, tmp);
						if(tName.equals("student")) updateStudent(myConn, scan, tmp);
						break;	
									
					case 4: //Delete
						System.out.println("\nEnter ID number: " );
						int idN = Integer.parseInt(scan.nextLine());
						
						if(tName.equals("teacher")) delFromTeacher(myConn, idN);
						if(tName.equals("course")) delFromCourse(myConn,idN);
						if(tName.equals("student")) delFromStudent(myConn,idN);
						break;
				
					case 9: //Show History
						if(tName.equals("teacher")) showTeacherHistory(myConn);
						break;
				
					case 5: //Find Name of teacher, student or course
						if(tName.equals("teacher")) findTeacher(myConn, scan);
						if(tName.equals("course")) findCourse(myConn, scan);
						if(tName.equals("student")) findStudent(myConn, scan);
						break;
				
					case 7: //Get count of table
						if(tName.equals("teacher")) getCountTeacher(myConn);
						if(tName.equals("course")) getCountCourse(myConn);
						if(tName.equals("student")) getCountStudent(myConn);
						break;
						
					case 8: // A list of teacher-course, student-course
						if(tName.equals("teacher")) showTeacherCourse(myConn);
						if(tName.equals("student")) showStudentCourse(myConn);
						break;
				
					case 6: //Find student by status
						if(tName.equals("student")) findStudentByStatus(myConn, scan);
						break;
						
					case 0:
						isDone2 = false;
						break;	
				
					default:
						System.out.println("Incorrect the number, try again");
						break;	
					}
				}	
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		

	}

	public static void showTeacher(Connection con) {

		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM teacher");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------------- Teacher -----------------------");
			System.out.println("id" + "\t" + "tnr" + "\t" + "Fname" + "\t" + "Lname" + "  \t" + "Comment" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("tid") + "\t" + rs.getString("tnr") + "\t" + rs.getString("fname")
					+ "\t" + rs.getString("lname")	+ "  \t" + rs.getString("comment") );
			}
			System.out.println("\n------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
	}
	public static void showCourse(Connection con) {
		
		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM course");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------------- Course -----------------------------------");
			System.out.println("Cid" + "\tCnr" + "\tCname" + "\t\t\t\tDescription" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("cid") + "\t" + rs.getString("cnr") + "\t" + rs.getString("cname").trim()
					+ "\t\t\t\t" + rs.getString("description") );
			}
			System.out.println("\n------------------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		
	}
	public static void showStudent(Connection con) {

		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM student");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------------- Student -----------------------");
			System.out.println("sid" + "\t" + "snr" + "\t" + "Fname" + "\t" + "Lname" + "\t" + "Status" + "\t" + "Comment" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("sid") + "\t" + rs.getString("snr") + "\t" + rs.getString("fname")
					+ "\t" + rs.getString("lname")	+ "\t" + rs.getString("status")	+ "\t" + rs.getString("comment") );
			}
			System.out.println("\n------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		
	}
	
	public static void showTeacherHistory(Connection con)
	{
		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM teacherhistory");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------------- TeacherHistory -----------------------------------------------------");
			System.out.println("id" + "\t" + "tnr" + "\t" + "Fname" + "\t" + "Lname" + "\t" + "Comment" 
					+ "\t\t" + "Action" + "\t\t" + "DateTime");
			
			while(rs.next()){
				
				System.out.println(rs.getString("id") + "\t" + rs.getString("tnr") + "\t" + rs.getString("fname")
					+ "\t" + rs.getString("lname")	+ "  \t" + rs.getString("comment")
					+ "  \t" + rs.getString("action") + "  \t" + rs.getString("date") );
			}
			System.out.println("\n-------------------------------------------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
	}
	
	public static void getCountTeacher(Connection con) {
		
		try(
				CallableStatement myStmt = con.prepareCall("{call get_count_teacher(?)}");
		)
		{
		
		myStmt.registerOutParameter(1, Types.INTEGER);
		
		// Call stored procedure
		System.out.println("Calling stored procedure.  get_count_teacher(?)");
		myStmt.execute();
				
		// Get the value of the OUT parameter
		int theCount = myStmt.getInt(1);
		
		System.out.println("\nThe count of Teacher tabel = " + theCount);

	} catch (Exception exc) {
		exc.printStackTrace();
	} 
	}
	public static void getCountStudent(Connection con) {
		
		try(
				CallableStatement myStmt = con.prepareCall("{call get_count_student(?)}");
		)
		{
		
		myStmt.registerOutParameter(1, Types.INTEGER);
		
		// Call stored procedure
		System.out.println("Calling stored procedure.  get_count_student(?)");
		myStmt.execute();
			
		
		// Get the value of the OUT parameter
		int theCount = myStmt.getInt(1);
		
		System.out.println("\nThe count of Student tabel = " + theCount);

	} catch (Exception exc) {
		exc.printStackTrace();
	} 
	}
	public static void getCountCourse(Connection con) {
	
	try(
			CallableStatement myStmt = con.prepareCall("{call get_count_course(?)}");
	)
	{
	
	myStmt.registerOutParameter(1, Types.INTEGER);
	
	// Call stored procedure
	System.out.println("Calling stored procedure.  get_count_course(?)");
	myStmt.execute();
	
	// Get the value of the OUT parameter
	int theCount = myStmt.getInt(1);
	
	System.out.println("\nThe count of Course tabel = " + theCount);

} catch (Exception exc) {
	exc.printStackTrace();
} 
}
	
	public static void delFromTeacher(Connection con, int id) {
	try (
			PreparedStatement ps = con.prepareStatement("DELETE FROM teacher WHERE tid= ?");
			) 
	{
		ps.setInt(1, id);
		int rs = ps.executeUpdate();
		
		System.out.println("Deleted rows: " + rs);
		
	}
	catch(SQLException e) {
				e.printStackTrace();
	}
	}
	public static void delFromCourse(Connection con, int id) {
		try (
				PreparedStatement ps = con.prepareStatement("DELETE FROM course WHERE cid= ?");
				) 
		{
			ps.setInt(1, id);
			int rs = ps.executeUpdate();
			
			System.out.println("Deleted rows: " + rs);
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		
		
	}
	public static void delFromStudent(Connection con, int id) {

		try (
				PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE sid= ?");
				) 
		{
			ps.setInt(1, id);
			int rs = ps.executeUpdate();
			
			System.out.println("Deleted rows: " + rs);
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		
	}
	
	public static void insertTeacher(Connection con, Scanner sca) {
		
		
		String sql = "INSERT INTO teacher (tnr, fname, lname, comment) VALUES (?,?,?,?)";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql);
				)
		{
			
			System.out.println("\nEnter Teacher number: " );
			int tNr = Integer.parseInt(sca.nextLine());	
			System.out.println("\nEnter First Name: " );
			String  tFname = sca.nextLine();
			System.out.println("\nEnter Last Name: " );
			String  tLname = sca.nextLine();
			System.out.println("\nEnter comment: " );
			String  tComment = sca.nextLine();
		
						 
		 ps.setInt(1, tNr);
		 ps.setString(2,tFname);
		 ps.setString(3, tLname);
		 ps.setString(4, tComment);
		 
		 int rs = ps.executeUpdate();
		 System.out.println("Inserted rows: " + rs);
		
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	public static void insertStudent(Connection con, Scanner sca) {
		
		String sql = "INSERT INTO student (snr, fname, lname, comment, status) VALUES (?,?,?,?,?)";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql);
				)
		{
			
			System.out.println("\nEnter Student number: " );
			int sNr = Integer.parseInt(sca.nextLine());	
			System.out.println("\nEnter First Name: " );
			String  sFname = sca.nextLine();
			System.out.println("\nEnter Last Name: " );
			String  sLname = sca.nextLine();
			System.out.println("\nEnter comment: " );
			String  sComment = sca.nextLine();
			System.out.println("\nEnter Status(active, ledigt ...): " );
			String  sStatus = sca.nextLine();
		
			ps.setInt(1, sNr);
			ps.setString(2, sFname);
			ps.setString(3, sLname);
			ps.setString(4, sComment);
			ps.setString(5, sStatus);
		 
			int rs = ps.executeUpdate();
			System.out.println("Inserted rows: " + rs);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void insertCourse(Connection con, Scanner sca) {
		
		String sql = "INSERT INTO course (cnr, cname, description) VALUES (?,?,?)";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql);
				)
		{
			
			System.out.println("\nEnter Course number: " );
			int cNr = Integer.parseInt(sca.nextLine());	
			System.out.println("\nEnter Course name: " );
			String  cName = sca.nextLine();
			System.out.println("\nEnter Course description: " );
			String  cDesc = sca.nextLine();
			
			ps.setInt(1, cNr);
			ps.setString(2,cName);
			ps.setString(3, cDesc);
		
			int rs = ps.executeUpdate();
			System.out.println("Inserted rows: " + rs);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateTeacher(Connection con, Scanner sca, int id) {
		
		String sql = "SELECT * FROM teacher WHERE tid=?";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			)
		{
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			System.out.println("Teacher Number: " + rs.getInt("tnr"));
			System.out.println("\nEnter Teacher number: " );
			int tNr = Integer.parseInt(sca.nextLine());	
			System.out.println("\nEnter First Name: " + rs.getString("fname") );
			String  tFname = sca.nextLine();
			System.out.println("\nEnter Last Name: " + rs.getString("lname") );
			String  tLname = sca.nextLine();
			System.out.println("\nEnter comment: " + rs.getString("comment"));
			String  tComment = sca.nextLine();
		
			rs.updateInt("tnr", tNr);
			rs.updateString("fname", tFname);
			rs.updateString("lname", tLname);
			rs.updateString("comment", tComment);
			rs.updateRow();
	
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updateStudent(Connection con, Scanner sca, int id) {
		
		String sql = "SELECT * FROM student WHERE sid=?";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			)
		{
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			System.out.println("Student Number: " + rs.getInt("snr"));
			System.out.println("\nEnter Student number: " );
			int sNr = Integer.parseInt(sca.nextLine());	
			System.out.println("\nEnter First Name: " + rs.getString("fname") );
			String  sFname = sca.nextLine();
			System.out.println("\nEnter Last Name: " + rs.getString("lname") );
			String  sLname = sca.nextLine();
			System.out.println("\nEnter comment: " + rs.getString("comment"));
			String  sComment = sca.nextLine();
			System.out.println("\nEnter Status: " + rs.getString("status"));
			String  sStatus = sca.nextLine();
		
			rs.updateInt("snr", sNr);
			rs.updateString("fname", sFname);
			rs.updateString("lname", sLname);
			rs.updateString("comment", sComment);
			rs.updateString("status", sStatus);
			rs.updateRow();
	
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updateCourse(Connection con, Scanner sca, int id) {
		
		String sql = "SELECT * FROM course WHERE cid=?";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			)
		{
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			System.out.println("Course Number: " + rs.getInt("cnr"));
			System.out.println("\nEnter Course number: " );
			int cNr = Integer.parseInt(sca.nextLine());	
			System.out.println("\nEnter Course Name: " + rs.getString("cname") );
			String  cName = sca.nextLine();
			System.out.println("\nEnter Description: " + rs.getString("description") );
			String  cDesc = sca.nextLine();
					
			rs.updateInt("cnr", cNr);
			rs.updateString("cname", cName);
			rs.updateString("description", cDesc);
			rs.updateRow();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void findTeacher(Connection con, Scanner sca) {
		
		String sql = "SELECT * FROM teacher WHERE fname LIKE ? OR lname LIKE ?";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql);
			)
		{
			System.out.println("\nEnter searching symbol to find First or Last name: ");
			String  fStr = sca.nextLine();
			
			
			ps.setString(1, "%" + fStr + "%");
			ps.setString(2, "%" + fStr + "%");
			ResultSet rs = ps.executeQuery();
						
			while(rs.next()) {
				System.out.println("\n\t" + rs.getString("tid") + "\t" + rs.getString("tnr") + 
						"\t" + rs.getString("fname") + "\t" + rs.getString("lname") + "\t" + rs.getString("comment"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void findStudent(Connection con, Scanner sca) {
	
		String sql = "SELECT * FROM student WHERE fname LIKE ? OR lname LIKE ?";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql);
			)
		{
			System.out.println("\nEnter searching symbol to find First or Last name: ");
			String  fStr = sca.nextLine();
			
			ps.setString(1, "%" + fStr + "%");
			ps.setString(2, "%" + fStr + "%");
			ResultSet rs = ps.executeQuery();
						
			while(rs.next()) {
				System.out.println("\n\t" + rs.getString("sid") + "\t" + rs.getString("snr") + 
						"\t" + rs.getString("fname") + "\t" + rs.getString("lname") + "\t" + rs.getString("comment")
						+ "\t" + rs.getString("status"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void findCourse(Connection con, Scanner sca) {
		
		String sql = "SELECT * FROM course WHERE cname LIKE ? OR description LIKE ?";
		
		try (
			PreparedStatement ps = con.prepareStatement(sql);
			)
		{
			System.out.println("\nEnter searching symbol to find course name or description: ");
			String  fStr = sca.nextLine();
			
			ps.setString(1, "%" + fStr + "%");
			ps.setString(2, "%" + fStr + "%");
			ResultSet rs = ps.executeQuery();
						
			while(rs.next()) {
				System.out.println("\n\t" + rs.getString("cid") + "\t" + rs.getString("cnr") + 
						"\t" + rs.getString("cname") + "\t" + rs.getString("description"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void showTeacherCourse(Connection con) {

		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT a.tnr, a.fname, a.lname, COUNT(c.cname) as TotalCourses FROM teacher AS a "
						+ "JOIN teachercourse as b ON a.tid=b.tid JOIN course as c ON c.cid=b.cid GROUP BY a.tnr");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------- Total Courses by Teacher ------");
			System.out.println("tNr" + "\t" + "Fname" + "\t" + "Lname" + "  \t" + "Total" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("a.tnr") + "\t" +  rs.getString("a.fname")
					+ "\t" + rs.getString("a.lname")	+ "  \t" + rs.getString("TotalCourses") );
			}
			System.out.println("\n-------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		
		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT a.tnr, a.fname, a.lname, c.cname FROM teacher AS a JOIN teachercourse as b ON a.tid=b.tid JOIN course as c ON c.cid=b.cid;\r\n");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------------- Teacher Course -----------------");
			System.out.println("tNr" + "\t" + "Fname" + "\t" + "Lname" + "  \t" + "Course Name" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("a.tnr") + "\t" +  rs.getString("a.fname")
					+ "\t" + rs.getString("a.lname")	+ "  \t" + rs.getString("c.cname") );
			}
			System.out.println("\n------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		
		
	}
	public static void showStudentCourse(Connection con) {

		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT a.snr, a.fname, a.lname, COUNT(c.cname) as TotalCourse FROM student AS a JOIN studentcourse as b ON a.sid=b.sid "
						+ "JOIN course as c ON c.cid=b.cid GROUP BY a.snr");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------- Total Course by Student -------------");
			System.out.println("sNr" + "\t" + "Fname" + "\t" + "Lname" + "  \t" + "Total" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("a.snr") + "\t" +  rs.getString("a.fname")
					+ "\t" + rs.getString("a.lname")	+ "  \t" + rs.getString("TotalCourse") );
			}
			System.out.println("\n------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
		try (
				Statement statement = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT a.snr, a.fname, a.lname, c.cname FROM student AS a JOIN studentcourse as b ON a.sid=b.sid JOIN course as c ON c.cid=b.cid");
				) 
		{
			rs.beforeFirst();
			System.out.println("\n---------------- Student-Course -----------------------");
			System.out.println("sNr" + "\t" + "Fname" + "\t" + "Lname" + "  \t" + "CourseName" );
			
			while(rs.next()){
				
				System.out.println(rs.getString("a.snr") + "\t" +  rs.getString("a.fname")
					+ "\t" + rs.getString("a.lname")	+ "  \t" + rs.getString("c.cname") );
			}
			System.out.println("\n------------------------------------------------");
			
		}
		catch(SQLException e) {
					e.printStackTrace();
		}
	}

	public static void findStudentByStatus(Connection con, Scanner sca)
	{
		String sql = "SELECT * FROM student WHERE status LIKE ? ";
		ResultSet rs = null;
		try (
			PreparedStatement ps = con.prepareStatement(sql);
			)
		{
			System.out.println("\nEnter student Status (active, ledig, sjuk): ");
			String  fStr = sca.nextLine().trim().toLowerCase();
			
			if(fStr.equals("active") || fStr.equals("ledig") || fStr.equals("sjuk")) {
				ps.setString(1, fStr);
				rs = ps.executeQuery();
			}
			
					
			while(rs.next()) {
				System.out.println("\n\t" + rs.getString("sid") + "\t" + rs.getString("snr") + 
						"\t" + rs.getString("fname") + "\t" + rs.getString("lname") + "\t" + rs.getString("comment")
						+ "\t" + rs.getString("status"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
