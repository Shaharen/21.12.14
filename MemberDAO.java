import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MemberDAO {
	// DAO : Database Access Object
	// ������ ���̽��� �����ϱ� ���� ��ü�� ���� �� �ִ� Ŭ����
	Scanner sc = new Scanner(System.in);
	// ���۷������� �⺻���� null�� ���Ƿ� ���� ���൵�ȴ�.
	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;

	// ����̹� �ε��� Ŀ�ؼ� ��ü�� �������� �޼ҵ�
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

	// DataBase�� ������ �����ִ� �޼ҵ�
	private void close() {
		try {
			if (rs != null) { // nulló���� �����Ƿ� �Բ��ᵵ��
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

	// �α��� ���
	public String login(String id, String pw) {

		getConnection();
		String nick = null;
		try {
			// * JDBC
			// 0. JDBC�� ����ϴ� ������Ʈ�� Driver ���� �ֱ�
			// ������Ʈ �����ʸ��콺 build path -> configure

			// 1. Driver �ε� ( Oracle Driver ) -> �����ε�
			// ���� ����ϴ� DBMS�� ����̹� �ε�
			// ����ó�� = try ~ catch
//			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection ����
			// Connection�� �����ϱ� ���ؼ���
			// DB�� ���Ӱ����� url, id, pw �� �ʿ��ϴ�.
//			String db_url = "jdbc:oracle:thin:@localhost:1521:xe";
//			String db_id = "hr";
//			String db_pw = "hr";
//			conn = DriverManager.getConnection(db_url, db_id, db_pw);

			// 3. SQL�� �ۼ� �� ����
			String sql = "select * from bigmember where id = ? and pw = ?";

			psmt = conn.prepareStatement(sql); // sql ����
			psmt.setString(1, id);// ù��° ����ǥ
			psmt.setString(2, pw);

			rs = psmt.executeQuery(); // Ŀ��
			if (rs.next()) { // rs ����ĭ�� ���翩��
				nick = rs.getString(3); // 3��° �� �Ǵ� �÷��� "nick"�� ����
			}

//			if (rs.next()) { // Ŀ���� �������ִ��� ����� �ȴٸ� ������ �������
//				System.out.println("�α��� ����");
//				// System.out.println(rs.getString("id"));
//				// System.out.println(rs.getString("pw"));
//				System.out.println(rs.getString("nick") + "�� ȯ���մϴ�!");
//			} else {
//				System.out.println("�α��� ����");
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 4. Java�� DataBase ���� ������ �����ش�
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

	// ȸ�� ���� ���
	public int join(String id, String pw, String nick) {

		getConnection();
		int cnt = 0;

		try {

			String sql = "insert into bigmember values (?,?,?)";

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);// ù��° ����ǥ
			psmt.setString(2, pw);
			psmt.setString(3, nick);

			cnt = psmt.executeUpdate(); // ����� sql��

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}

	// ȸ�� ���� ���� ���
	public int update(String id, String inputNick) {

		getConnection();
		int cnt = 0;
		try {

			String sql = "update bigmember set nick = ? where id = ?";

			psmt = conn.prepareStatement(sql);

			psmt.setString(1, inputNick);// ù��° ����ǥ
			psmt.setString(2, id);
			// update ���̺�� set �÷��� = �ٲٰ������ where ����
			// executeQuery -> ���̺� �����Ͱ� ���� ������, ���⸸�Ҷ�, �ַ� select��
			// ResultSet �� ��ȯ
			// executeUpdate -> ���̺��� ������ ����ɶ�
			// int Ÿ������ ��ȯ -> ����� sql���� ��
			cnt = psmt.executeUpdate();	// ���� ����

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}

	// ȸ�� ���� ���
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

	// ȸ�� ���� Ż��
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

	// ������ ȸ�� ���� ����
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
	// ������ ȸ�� ���� ( �����ε� �ص��� )
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
