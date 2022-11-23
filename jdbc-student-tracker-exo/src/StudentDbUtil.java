import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class StudentDbUtil {

	private static StudentDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/student_tracker";
	
	public static StudentDbUtil getInstance() throws Exception {
		System.out.println("in getinstance");
		if (instance == null) {
			instance = new StudentDbUtil();
		}
		
		return instance;
	}
	
	private StudentDbUtil() throws Exception {		
		System.out.println("in constructeur studentdbutil");
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		System.out.println("in getdatasource");
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	
	// CI DESSOUS LES DIFFÉRENTES REQUÊTES À EFFECTUER

	private Connection getConnection() throws SQLException {
		System.out.println("in getconnexion");
		
		Connection myConnection = dataSource.getConnection();
		
		return myConnection;
	}
	
	private void close(Connection connection, Statement stmt, ResultSet res) {
		System.out.println("in close");
		
		try {
			
			if(connection != null) {
				connection.close();
			}
			
			if(stmt != null) {
				stmt.close();
			}
			
			if(res != null) {
				res.close();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// SELECT
	public List<Student> getStudents() throws SQLException {
		System.out.println("StudentDbUtil in get student");
		
		List<Student> students = new ArrayList<>();
			
		try {
			Connection con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM student");
			
			while (rs.next()) {
				Student student = new Student();
				student.setId(rs.getInt("id"));
				student.setFirst_name(rs.getString("first_name"));
				student.setLast_name(rs.getString("last_name"));
				student.setEmail(rs.getString("email"));
				
				students.add(student);
			}
			
			close(con, st ,rs);
			System.out.println("StudentDbUtil getstudent reussite");
			
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("in StudentDbUtil getstudent echec");
		}
		
		return students;
		
	}
	
	public Student getStudent(int id) {
		
		Student student = null;
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(("SELECT * FROM student WHERE id=?"));	
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
		
			if (rs.next()) {
				
			student = new Student();
			student.setId(rs.getInt("id"));
			student.setFirst_name(rs.getString("first_name"));
			student.setLast_name(rs.getString("last_name"));
			student.setEmail(rs.getString("email"));

			}
			
			close(con, ps, rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return student;
	}
	
	
	// INSERT
	public void addElement(Student student) {
		System.out.println("in addstudent");
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO student(first_name, last_name, email) VALUE (?, ?, ?)");
			ps.setString(1, student.getFirst_name());
			ps.setString(2, student.getLast_name());
			ps.setString(3, student.getEmail());
			
			ps.executeUpdate();
			close(con, ps, null);
			System.out.println("StudentDbUtil addstudent reussite");
			
		} catch (Exception e) {
			e.printStackTrace();		
			System.out.println("StudentDbUtil addstudent echec");
		}
	
	}

	// UPDATE
	
	public String updateStudent(Student student) {
			try {
				Connection con = getConnection();			
				PreparedStatement ps = con.prepareStatement("UPDATE student SET first_name=?, last_name=?, email=? WHERE id= ?");
				
				ps.setString(1, student.getFirst_name());
				
				ps.setString(2, student.getLast_name());
				
				ps.setString(3, student.getEmail());
				
				ps.setInt(4, student.getId());
				
				ps.executeUpdate();
				con.close();
				ps.close();
				
				return "list-students";
				
			} catch (Exception e) {
				e.printStackTrace();

				return "modify";
			}
		
	}


	// DELETE
	
	public void deleteStudent(int id) {
		try {
			Connection con = getConnection();			
			PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE id= ?");
			ps.setInt(1, id);
			
			ps.executeUpdate();
			con.close();
			
			System.out.println("Deleted");
			
		} catch (Exception e) {
			e.printStackTrace();
			
			System.out.println("Not deleted");
		}
	}




}