package com.newlecture.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;


@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		List<Notice> list = new ArrayList<>();
		
		String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
		String uid = "newlec"; // oracle id
		String pwd = "1115"; //oracle password
		String driver = "oracle.jdbc.driver.OracleDriver";
		String sql = "select * FROM NOTICE";
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,uid,pwd);
			Statement st = con.createStatement();
			//PreparedStatement st = con.prepareStatement(sql); 
			//st.setString(1, "%"+query+"%");//%"+query+"%
			//st.setInt(2, start);
			//st.setInt(3, end);
			ResultSet rs = st.executeQuery(sql);
			
			
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
		
		//notice를 담은 배열 request에 담아주기
		request.setAttribute("list", list);
		
		//notice/list.jsp에 넘겨주기
		request.getRequestDispatcher("/WEB-INF/view/notice/list.jsp")
		.forward(request, response); //데이터를 가지고 이어서 작성
	}
}
