package com.newlecture.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.newlecture.web.entity.Notice;

public class NoticeService {
	private String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
	private String uid = "newlec"; // oracle id
	private String pwd = "1115"; //oracle password
	private String driver = "oracle.jdbc.driver.OracleDriver";
	
	
	public List<Notice> getNoticeList(){
		return getNoticeList("title", "", 1);
	}
	
	public List<Notice> getNoticeList(int page){
		return getNoticeList("title", "", page);
	}
	
	public List<Notice> getNoticeList(String field, String query, int page){
		
		List<Notice> list = new ArrayList<>();
		System.out.print(field + query);
		
		
		 String sql = "	SELECT * FROM ( " + 
				 	"	SELECT ROWNUM NUM, N.* " +
				 	"	FROM ( SELECT * FROM NOTICE where "+field+" LIKE ? ORDER BY REGDATE DESC  ) N ) "+ 
				 	"	WHERE NUM BETWEEN ? AND ?";
		 
				
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			PreparedStatement st = con.prepareStatement(sql); 
			
			st.setString(1, "%"+query+"%");//%"+query+"%
			st.setInt(2, 1+(page-1)*10); 
			st.setInt(3, page*10);
			 
			ResultSet rs = st.executeQuery();
			
			while(rs.next()){
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				Date regdate = rs.getDate("REGDATE");
				String  writerId = rs.getString("WRITER_ID");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				
				Notice notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content
						);
			
				list.add(notice);
			}
			
			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public int getNoticeCount(){
		return getNoticeCount("title", "");
	}
	public int getNoticeCount(String field, String query){
		
		String sql = "select COUNT(ID) count from notice where "+field+" like ? ";
		int count = 0;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			PreparedStatement st = con.prepareStatement(sql); 
			st.setString(1, "%"+query+"%");//%"+query+"%
			ResultSet rs = st.executeQuery();
			
			if(rs.next())
				count = rs.getInt("count");
			
			rs.close();
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	public Notice getNotice(int id){
		String sql = "select * from notice where id = ?";
		Notice notice = null; 
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			
			if(rs.next()) {
				String title = rs.getString("TITLE");
				Date regdate = rs.getDate("REGDATE");
				String  writerId = rs.getString("WRITER_ID");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				
				notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content
						);
			
				//list.add(notice);
			}
			
			rs.close();
			st.close();
			con.close();
			
				
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notice;
	}
	
	public Notice getNextNotice(int id){
		String sql = "SELECT * FROM NOTICE "
				+ "WHERE ID = ( "
				+ "SELECT ID FROM NOTICE "
				+ "WHERE REGDATE > ( SELECT REGDATE FROM NOTICE WHERE ID = ? ) "
				+ "AND ROWNUM = 1"; 
		Notice notice = null; 
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			
			if(rs.next()) {
				String title = rs.getString("TITLE");
				Date regdate = rs.getDate("REGDATE");
				String  writerId = rs.getString("WRITER_ID");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				
				notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content
						);
			
				//list.add(notice);
			}
			
			rs.close();
			st.close();
			con.close();
			
				
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return notice;
	}
	public Notice getPrevNotice(int id){
		String sql = "select * from "
				+ "(select * from notice where id < ? order by regdate desc) "
				+ "where rownum = 1"; 
		Notice notice = null; 
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			
			if(rs.next()) {
				String title = rs.getString("TITLE");
				Date regdate = rs.getDate("REGDATE");
				String  writerId = rs.getString("WRITER_ID");
				String hit = rs.getString("HIT");
				String files = rs.getString("FILES");
				String content = rs.getString("CONTENT");
				
				notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content
						);
			
				//list.add(notice);
			}
			
			rs.close();
			st.close();
			con.close();
			
				
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return notice;
	}
}
