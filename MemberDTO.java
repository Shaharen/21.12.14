
public class MemberDTO {
	// 데이터베이스를 표현하기위한 사용자 임의의 VO클래스이다
	// VO 클래스 -> Value Object ( 사용자 임의의 클래스 )
	private String id;
	private String pw;
	private String nick;

	public MemberDTO(String id, String pw, String nick) {
		this.id = id;
		this.pw = pw;
		this.nick = nick;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", pw=" + pw + ", nick=" + nick + "]";
	}

}
