package ch14.bookshop.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ShopBookDBBean {
	
	private static ShopBookDBBean instance = new ShopBookDBBean();
	
	public static ShopBookDBBean getInstance() {
		return instance;
	}
	
	private ShopBookDBBean() {}
	
	/* 커넥션풀로부터 커넥션 객체를 얻어내는 메소드 */
	private Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/basicjsp");
		return ds.getConnection();
	}
	
	/* 관리자 인증 메소드 */
	public int managerCheck(String id, String passwd) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd = "";
		int x = -1;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select managerPasswd from manager where managerId = ?");
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dbpasswd = rs.getString("managerPasswd");
				if(dbpasswd.equals(passwd))
					x = 1;	//인증성공
				else
					x = 0;	//비밀번호 틀림
			}
			else
				x = -1;		//해당 아이디 없음
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	/* 책 등록 메소드 */
	public void insertBook(ShopBookDataBean book) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("insert into book values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, book.getBook_id());
			pstmt.setString(2, book.getBook_kind());
			pstmt.setString(3, book.getBook_title());
			pstmt.setInt(4, book.getBook_price());
			pstmt.setShort(5, book.getBook_count());
			pstmt.setString(6, book.getAuthor());
			pstmt.setString(7, book.getPublishing_com());
			pstmt.setString(8, book.getPublishing_date());
			pstmt.setString(9, book.getBook_image());
			pstmt.setString(10, book.getBook_content());
			pstmt.setByte(11, book.getDiscount_rate());
			pstmt.setTimestamp(12, book.getReg_date());
			
			pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
	}
	
	/* 전체등록된 책의 수를 얻어내는 메소드 */
	public int getBookCount() throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int x = 0;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select count(*) from book");
			rs = pstmt.executeQuery();
			
			if(rs.next())
				x = rs.getInt(1);
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	/* 분류별 또는 전체 등록된 책의 정보를 얻어내는 메소드 */
	public List<ShopBookDataBean> getBooks(String book_kind) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ShopBookDataBean> bookList = null;
		
		try {
			conn = getConnection();
			String sql1 = "select * from book";
			String sql2 = "select * from book where book_kind = ? order by reg_date desc";
			
			if(book_kind.equals("all")) {
				pstmt = conn.prepareStatement(sql1);
			}
			else {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, book_kind);
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bookList = new ArrayList<ShopBookDataBean>();
				do {
					ShopBookDataBean book = new ShopBookDataBean();
					
					book.setBook_id(rs.getInt("book_id"));
					book.setBook_kind(rs.getString("book_kind"));
					book.setBook_title(rs.getString("book_title"));
					book.setBook_price(rs.getInt("book_price"));
					book.setBook_count(rs.getShort("book_count"));
					book.setAuthor(rs.getString("author"));
					book.setPublishing_com(rs.getString("publishing_com"));
					book.setPublishing_date(rs.getString("publishing_date"));
					book.setBook_image(rs.getString("book_image"));
					book.setDiscount_rate(rs.getByte("discount_rate"));
					book.setReg_date(rs.getTimestamp("reg_date"));
					
					bookList.add(book);
				}while(rs.next());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return bookList;
	}
	
	/* 쇼핑몰 메인에 표시하기 위해서 사용하는 분류별 신간책 목록을 얻어내는 메소드 */
	public ShopBookDataBean[] getBooks(String book_kind, int count) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShopBookDataBean bookList[] = null;
		int i = 0;
		
		try {
			conn = getConnection();
			
			String sql = "select * from book where book_kind = ? order by reg_date desc limit ?, ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, book_kind);
			pstmt.setInt(2, 0);
			pstmt.setInt(3, count);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bookList = new ShopBookDataBean[count];
				
				do {
					ShopBookDataBean book = new ShopBookDataBean();
					book.setBook_id(rs.getInt("book_id"));
					book.setBook_kind(rs.getString("book_kind"));
					book.setBook_title(rs.getString("book_title"));
					book.setBook_price(rs.getInt("book_price"));
					book.setBook_count(rs.getShort("book_count"));
					book.setAuthor(rs.getString("author"));
					book.setPublishing_com(rs.getString("publishing_com"));
					book.setPublishing_date(rs.getString("publishing_date"));
					book.setBook_image(rs.getString("book_image"));
					book.setDiscount_rate(rs.getByte("discount_rate"));
					book.setReg_date(rs.getTimestamp("book_reg_date"));
					
					bookList[i] = book;
					
					i++;
				}while(rs.next());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return bookList;
	}
	
	/* bookId에 해당하는 책의 정보를 얻어내는 메소드로 
	 * 등록된 책을 수정하기 위해 수정폼으로 읽어들이기 위한 메소드 */
	public ShopBookDataBean getBook(int bookId) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShopBookDataBean book = null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select * from book where book_id = ?");
			pstmt.setInt(1, bookId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				book = new ShopBookDataBean();
				
				book.setBook_kind(rs.getString("book_kind"));
				book.setBook_title(rs.getString("book_title"));
				book.setBook_price(rs.getInt("book_price"));
				book.setBook_count(rs.getShort("book_count"));
				book.setAuthor(rs.getString("author"));
				book.setPublishing_com(rs.getString("publishing_com"));
				book.setPublishing_date(rs.getString("publishing_date"));
				book.setBook_image(rs.getString("book_image"));
				book.setDiscount_rate(rs.getByte("discount_rate"));
				book.setReg_date(rs.getTimestamp("book_reg_date"));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)
				try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null)
				try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)
				try {conn.close();}catch(SQLException ex) {}
		}
		return book;
	}
}
