import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MemberDAO {
	// DAO : Database Access Object
	// 데이터 베이스에 접근하기 위한 객체를 만들 수 있는 클래스
	Scanner sc = new Scanner(System.in);
	// 레퍼런스형은 기본값을 null이 들어가므로 선언만 해줘도된다.
	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;

	// 드라이버 로딩과 커넥션 객체를 가져오는 메소드
	private void getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String db_url = "jdbc:oracle:thin:@localhost:1521:xe";
			String db_id = "hr";
			String db_pw = "hr";
			conn = DriverManager.getConnection(db_url, db_id, db_pw);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// DataBase와 연결을 끊어주는 메소드
	private void close() {
		try {
			if (rs != null) { // null처리를 했으므로 함께써도됨
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 로그인 기능
	public String login(String id, String pw) {

		getConnection();
		String nick = null;
		try {
			// * JDBC
			// 0. JDBC를 사용하는 프로젝트에 Driver 파일 넣기
			// 프로젝트 오른쪽마우스 build path -> configure

			// 1. Driver 로딩 ( Oracle Driver ) -> 동적로딩
			// 내가 사용하는 DBMS의 드라이버 로딩
			// 예외처리 = try ~ catch
//			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection 연결
			// Connection을 연결하기 위해서는
			// DB에 접속가능한 url, id, pw 가 필요하다.
//			String db_url = "jdbc:oracle:thin:@localhost:1521:xe";
//			String db_id = "hr";
//			String db_pw = "hr";
//			conn = DriverManager.getConnection(db_url, db_id, db_pw);

			// 3. SQL문 작성 및 실행
			String sql = "select * from bigmember where id = ? and pw = ?";

			psmt = conn.prepareStatement(sql); // sql 실행
			psmt.setString(1, id);// 첫번째 물음표
			psmt.setString(2, pw);

			rs = psmt.executeQuery(); // 커서
			if (rs.next()) { // rs 다음칸의 존재여부
				nick = rs.getString(3); // 3번째 열 또는 컬럼명 "nick"도 가능
			}

//			if (rs.next()) { // 커서를 내릴수있는지 물어보고 된다면 내리고 논리값출력
//				System.out.println("로그인 성공");
//				// System.out.println(rs.getString("id"));
//				// System.out.println(rs.getString("pw"));
//				System.out.println(rs.getString("nick") + "님 환영합니다!");
//			} else {
//				System.out.println("로그인 실패");
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 4. Java와 DataBase 간의 연결을 끊어준다
//			try {
//				if (rs != null) {
//					rs.close();
//				}
//				if (psmt != null) {
//					psmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
			close();
		}
		return nick;
	}

	// 회원 가입 기능
	public int join(String id, String pw, String nick) {

		getConnection();
		int cnt = 0;

		try {

			String sql = "insert into bigmember values (?,?,?)";

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);// 첫번째 물음표
			psmt.setString(2, pw);
			psmt.setString(3, nick);

			cnt = psmt.executeUpdate(); // 실행된 sql수

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}

	// 회원 정보 수정 기능
	public int update(String id, String inputNick) {

		getConnection();
		int cnt = 0;
		try {

			String sql = "update bigmember set nick = ? where id = ?";

			psmt = conn.prepareStatement(sql);

			psmt.setString(1, inputNick);// 첫번째 물음표
			psmt.setString(2, id);
			// update 테이블명 set 컬럼명 = 바꾸고싶은값 where 조건
			// executeQuery -> 테이블에 데이터가 변함 없을때, 보기만할때, 주로 select문
			// ResultSet 로 반환
			// executeUpdate -> 테이블의 내용이 변경될때
			// int 타입으로 반환 -> 실행된 sql문의 수
			cnt = psmt.executeUpdate();	// 문장 실행

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}

	// 회원 정보 출력
	public ArrayList<MemberDTO> selectAll() {

		ArrayList<MemberDTO> list = new ArrayList<MemberDTO>();
		getConnection();

		try {
			String sql = "select * from bigmember";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String nick = rs.getString("nick");
				MemberDTO m = new MemberDTO(id, pw, nick);
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return list;

	}

	// 회원 정보 탈퇴
	public int delete(String id, String pw) {

		getConnection();
		int cnt = 0;

		try {
			String sql = "delete from bigmember where id = ? and pw = ?";

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);

			cnt = psmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return cnt;
	}

	// 관리자 회원 정보 수정
	public int adminUpdate(String change_id, String change_nick) {

		getConnection();
		int cnt = 0;

		try {

			String sql = "update bigmember set nick = ? where id = ?";

			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, change_nick);
			psmt.setString(2, change_id);

			cnt = psmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}
	// 관리자 회원 삭제 ( 오버로딩 해도됨 )
	public int deleteId(String remove_id) {

		getConnection();
		int cnt = 0;

		try {
			String sql = "delete from bigmember where id = ?";

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, remove_id);

			cnt = psmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return cnt;
	}

}
