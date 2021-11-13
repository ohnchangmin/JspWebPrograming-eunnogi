package ch14.bookshop.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			pstmt.setString(9, book.getBook_img());
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
}
