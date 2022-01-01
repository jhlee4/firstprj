package com.newlecture.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

//파일 업로드를 위한 서블릿 설정
//byte단위
@MultipartConfig(
		fileSizeThreshold = 1024*1024,
		maxFileSize = 1024*1024*5, //파일 하나의 최대 크기
		maxRequestSize = 1024*1024*5*5 //전체 요청의 최대 크기
)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp")
		.forward(request, response); //데이터를 가지고 이어서 작성
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		
		System.out.print("title : ");
		System.out.print(title);
		
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");
		
		Collection<Part> parts = request.getParts();
		StringBuilder builder = new StringBuilder();
		for(Part p : parts) {
			if(!p.getName().equals("file")) continue; // 이름이 file인 경우에만 실행
			if(p.getSize() == 0) continue; //비어있는 파일
			
			Part filepart = p;
			String fileName = filepart.getSubmittedFileName();
			builder.append(fileName);
			builder.append(",");
			InputStream fis = filepart.getInputStream();
			
			String realPath = request.getServletContext().getRealPath("/upload");		
			System.out.print(realPath);
			
			File path = new File(realPath);
			if(!path.exists())
				path.mkdirs();
			
			//추가 기능 1.예외처리
			//2. 마감(중복된 파일 표시 img1(2).jpg
			String filePath = realPath + File.separator + fileName; 
			FileOutputStream fos = new FileOutputStream(filePath);
			
			byte[] buf = new byte[1024];
			int size = 0;
			while((size = fis.read(buf))!= -1) {//다 읽었다 표현 : -1
				fos.write(buf,0,size);
			}
			fos.close();
			fis.close();
		}
		builder.delete(builder.length()-1,builder.length()); //index는 0부터시작 a,b a부터 b-1까지 삭제
				
		boolean pub = false;
		if(isOpen != null)
			pub = true;		
		
		Notice notice = new Notice();
		notice.setContent(content);
		notice.setTitle(title);
		notice.setPub(pub);
		notice.setWriterId("newlec");
		notice.setFiles(builder.toString());
		
		NoticeService service = new NoticeService();
		int result = service.insertNotice(notice);
		
		response.sendRedirect("list");
	}
}
