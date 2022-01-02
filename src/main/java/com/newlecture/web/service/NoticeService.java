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
import com.newlecture.web.entity.NoticeView;

public class NoticeService {
	private String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
	private String uid = "newlec"; // oracle id
	private String pwd = "1115"; //oracle password
	private String driver = "oracle.jdbc.driver.OracleDriver";
	
	
	public int removeNoticeAll(int[] ids){
		
		return 0;
	}
	
	public int pubNoticeAll(int[] oids, int[] cids){
		
		List<String> oidsList = new ArrayList<>();
		for(int i=0; i<oids.length;i++)
			oidsList.add(String.valueOf(oids[i]));
		
		List<String> cidsList = new ArrayList<>();
		for(int i=0; i<cids.length;i++)
			oidsList.add(String.valueOf(cids[i]));
		
		return pubNoticeAll(oidsList, cidsList);
	}
	
	public int pubNoticeAll(List<String> oids, List<String> cids){
			
		String oidsCSV = String.join(",", oids);
		String cidsCSV = String.join(",", cids);
		
		return pubNoticeAll(oidsCSV, cidsCSV);
	}
	
	public int pubNoticeAll(String oidsCSV, String cidsCSV){
		
		int result = 0;
		
		String sqlOpen = String.format("UPDATE NOTICE SET PUB=1 WHERE ID IN (%s)",oidsCSV);
		String sqlClose = String.format("UPDATE NOTICE SET PUB=0 WHERE ID IN (%s)",cidsCSV);
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			
			Statement stOpen = con.createStatement();
			result += stOpen.executeUpdate(sqlOpen); // i,u,d 에서 사용 변경된 행의 수만큼 반환(int)
			
			Statement stClose = con.createStatement();
			result += stClose.executeUpdate(sqlClose); // i,u,d 에서 사용 변경된 행의 수만큼 반환(int)
			
			
			stOpen.close();
			stClose.close();
			con.close();
			
				
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	public int insertNotice(Notice notice){
		int result = 0;
			
		String sql = "INSERT INTO NOTICE(TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES(?,?,?,?,?) "; 
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, notice.getTitle());
			st.setString(2, notice.getContent());
			st.setString(3, notice.getWriterId());
			st.setBoolean(4, notice.getPub());
			st.setString(5, notice.getFiles());
			
			result = st.executeUpdate(); // i,u,d 에서 사용 변경된 행의 수만큼 반환(int)
			
			st.close();
			con.close();
			
				
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	public int deleteNotice(int id){
		
		return 0;
		
	}
	public int updateNotice(int notice){
		return 0;
	}
	public List<Notice> getNoticeNewestList(){
		return null;
	}
	
	
	
	public List<NoticeView> getNoticeList(){
		return getNoticeList("title", "", 1);
	}
	
	public List<NoticeView> getNoticeList(int page){
		return getNoticeList("title", "", page);
	}
	
	public List<NoticeView> getNoticeList(String field, String query, int page){
		
		List<NoticeView> list = new ArrayList<>();
		System.out.print(field + query);
		
		
		 String sql = "	SELECT * FROM ( " + 
				 	"	SELECT ROWNUM NUM, N.* " +
				 	"	FROM ( SELECT * FROM NOTICE_VIEW where "+field+" LIKE ? ORDER BY REGDATE DESC  ) N ) "+ 
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
				//String content = rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
				
				NoticeView notice = new NoticeView(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						pub,
						//content,
						cmtCount
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
	
	public List<NoticeView> getNoticePubList(String field, String query, int page) {
		List<NoticeView> list = new ArrayList<>();
		System.out.print(field + query);
		
		
		 String sql = "	SELECT * FROM ( " + 
				 	"	SELECT ROWNUM NUM, N.* " +
				 	"	FROM ( SELECT * FROM NOTICE_VIEW where "+field+" LIKE ? ORDER BY REGDATE DESC  ) N ) "+ 
				 	"	WHERE PUB=1 AND NUM BETWEEN ? AND ?";
		 
				
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
				//String content = rs.getString("CONTENT");
				int cmtCount = rs.getInt("CMT_COUNT");
				boolean pub = rs.getBoolean("PUB");
				
				NoticeView notice = new NoticeView(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						pub,
						//content,
						cmtCount
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
				boolean pub = rs.getBoolean("PUB");
				
				notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content,
						pub
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
		String sql = "select * from "
				+ "				(select * from notice where id > ? order by regdate asc) "
				+ "				where rownum = 1"; 
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
				boolean pub = rs.getBoolean("PUB");
				
				notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content,
						pub
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
		
		Notice notice = null; 
		
		String sql = "select * from "
				+ "(select * from notice where id < ? order by regdate desc) "
				+ "where rownum = 1"; 
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
				boolean pub = rs.getBoolean("PUB");
				
				notice = new Notice(
						id, 
						title, 
						regdate, 
						writerId, 
						hit, 
						files, 
						content,
						pub
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
	public int deleteNoticeAll(int[] ids) {
		
		int result = 0;
		
		String params = "";
		
		for(int i=0;i<ids.length;i++) {
			params += ids[i];
			
			if(i < ids.length-1)
				params += ",";
			
		}
		
		
		String sql = "DELETE NOTICE WHERE ID IN ("+params+")"; 
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			Statement st = con.createStatement();
			result = st.executeUpdate(sql); // i,u,d 에서 사용 변경된 행의 수만큼 반환(int)
			
			st.close();
			con.close();
			
				
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}
