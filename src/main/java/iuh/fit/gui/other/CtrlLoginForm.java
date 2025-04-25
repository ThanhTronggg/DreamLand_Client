package iuh.fit.gui.other;

import org.mindrot.jbcrypt.BCrypt;

import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class CtrlLoginForm {

	private TaiKhoanDAO tkDAO;

	public CtrlLoginForm() {
		tkDAO = new TaiKhoanDAO(TaiKhoan.class);
	}

	public boolean checkCredentials(String username, String password) {
		TaiKhoan tk = tkDAO.getTaiKhoanTheoUsername(username);
		if (tk == null || !BCrypt.checkpw(password, tk.getMatKhau())) {
			return false;
		}
		return true;
	}

	public NhanVien getEmployeeByAccount(String username, String password) {
		return tkDAO.getNhanVienTheoTaiKhoan(username, checkCredentials(username, password));
	}

}