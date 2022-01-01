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

//���� ���ε带 ���� ���� ����
//byte����
@MultipartConfig(
		fileSizeThreshold = 1024*1024,
		maxFileSize = 1024*1024*5, //���� �ϳ��� �ִ� ũ��
		maxRequestSize = 1024*1024*5*5 //��ü ��û�� �ִ� ũ��
)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp")
		.forward(request, response); //�����͸� ������ �̾ �ۼ�
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
			if(!p.getName().equals("file")) continue; // �̸��� file�� ��쿡�� ����
			if(p.getSize() == 0) continue; //����ִ� ����
			
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
			
			//�߰� ��� 1.����ó��
			//2. ����(�ߺ��� ���� ǥ�� img1(2).jpg
			String filePath = realPath + File.separator + fileName; 
			FileOutputStream fos = new FileOutputStream(filePath);
			
			byte[] buf = new byte[1024];
			int size = 0;
			while((size = fis.read(buf))!= -1) {//�� �о��� ǥ�� : -1
				fos.write(buf,0,size);
			}
			fos.close();
			fis.close();
		}
		builder.delete(builder.length()-1,builder.length()); //index�� 0���ͽ��� a,b a���� b-1���� ����
				
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
