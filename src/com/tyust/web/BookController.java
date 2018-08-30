package com.tyust.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tyust.dao.BookDao;
import com.tyust.entity.Book;
import com.tyust.pager.PageBean;
import com.tyust.service.BookService;

@Controller
@RequestMapping("/book")
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private BookDao bookDao;
	
	public static void main(String[] args) {
		BigDecimal b1 = new BigDecimal(3);
        BigDecimal b2 = new BigDecimal(2);
        System.out.println(b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue());;
	}
	@RequestMapping("/load.do")
	public String load(String bid,ModelMap map){
		Book book = bookService.load(bid);
		Map<String , Object> map1 = new HashMap<String , Object>();
		
		double avg = 0;
		String score = book.getScore();
		if(score != null){
			String[] scores = score.split("\\|");
			int totalAvg = Integer.parseInt(scores[0]);
			int person = Integer.parseInt(scores[1]);
			
			BigDecimal b1 = new BigDecimal(totalAvg);
	        BigDecimal b2 = new BigDecimal(person);
			avg = b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).doubleValue();
		} 
		
		BigDecimal b3 = new BigDecimal(0.7);
		BigDecimal b4 = new BigDecimal(avg);
		double res1 = b3.multiply(b4).doubleValue();
	     
		BigDecimal b5 = new BigDecimal(0.3);
		BigDecimal b6 = new BigDecimal(book.getView() + 1);
		double res2 = b5.multiply(b6).doubleValue();
		
		BigDecimal b7 = new BigDecimal(res1);
		BigDecimal b8 = new BigDecimal(res2);
		double res = b7.add(b8).doubleValue();  
		 
		map1.put("bid", bid);
		map1.put("total", res);
		bookDao.editView(map1);
		map.addAttribute("book", book);
		return "/jsps/book/desc";
	}
	
	private int getPc(HttpServletRequest request){
		int pc = 1;
		String param = request.getParameter("pc");
		if(param!=null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){ }
		}
		return pc;
	}
	
	private String getUrl(HttpServletRequest request){
		String url = request.getRequestURI()+"?"+request.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url = url.substring(0, index);
		}
		return url;
	}
	
	@RequestMapping("/findByCategory.do")
	public String findByCategory(HttpServletRequest request){
		int pc = getPc(request);
		String url = getUrl(request);
		String cid = request.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "/jsps/book/list";
	}
	
	@RequestMapping("/findByAuthor.do")
	public String findByAuthor(HttpServletRequest request){
		int pc = getPc(request);
		String url = getUrl(request);
		String author = request.getParameter("author");
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "/jsps/book/list";
	}
	
	@RequestMapping("/findByPress.do")
	public String findByPress(HttpServletRequest request){
		int pc = getPc(request);
		String url = getUrl(request);
		String press = request.getParameter("press");
		PageBean<Book> pb = bookService.findByPress(press, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "/jsps/book/list";
	}
	
	@RequestMapping("/findByBname.do")
	public String findByBname(HttpServletRequest request){
		int pc = getPc(request);
		String url = getUrl(request);
		String bname = request.getParameter("bname");
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "/jsps/book/list";
	}
	
	@RequestMapping("/findByConbination.do")
	public String findByConbination(HttpServletRequest request,Book criteria){
		int pc = getPc(request);
		String url = getUrl(request);
		
		PageBean<Book> pb = bookService.findByConbination(criteria, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "/jsps/book/list";
	}
	
	
	private String oid;
	private String getOid(){
		return oid;
	}
	private void setOid(String oid){
		this.oid=oid;
		
	}
	
}
