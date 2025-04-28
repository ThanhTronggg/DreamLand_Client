package iuh.fit.gui.other;

import org.mindrot.jbcrypt.BCrypt;

import entity.NhanVien;
import entity.TaiKhoan;
import service.KhachHangThongKeService;
import service.TaiKhoanService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CtrlLoginForm {

	private TaiKhoanService tkDAO;

	public CtrlLoginForm() throws MalformedURLException, NotBoundException, RemoteException {
		tkDAO = (TaiKhoanService) Naming.lookup("rmi://172.20.10.14:9090/taiKhoanService");
	}

	public boolean checkCredentials(String username, String password) throws RemoteException {
		TaiKhoan tk = tkDAO.getTaiKhoanTheoUsername(username);
		if (tk == null || !BCrypt.checkpw(password, tk.getMatKhau())) {
			return false;
		}
		return true;
	}

	public NhanVien getEmployeeByAccount(String username, String password) throws RemoteException {
		return tkDAO.getNhanVienTheoTaiKhoan(username, checkCredentials(username, password));
	}

}