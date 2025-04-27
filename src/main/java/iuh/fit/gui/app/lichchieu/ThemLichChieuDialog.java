package iuh.fit.gui.app.lichchieu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;
import com.raven.swing.TimePicker;

import entity.LichChieu;
import entity.Phim;
import entity.Phong;
import dao.LichChieuDAO;
import dao.PhimDAO;
import dao.PhongDAO;
import service.*;

public class ThemLichChieuDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final IdGeneratorService idGeneratorService;
    private JTextField txtMaLichChieu;
    private JTextField txtMaPhong;
    private JTextField txtMaPhim;
    private JTextField txtGioBatDau;
    private JTextField txtGioKetThuc;
    private JTextField txtGiaMotGhe;
    private JButton btnThem;
    private JButton btnHuy;
    private LichChieuService lichChieuDAO;
    private boolean dataChanged;
	private JComboBox<Phong> cboPhong;
	private JComboBox<Phim> cboPhim;
	private PhongService daoPhong;
	private ArrayList<Phong> dsPhong;
	private PhimService daoPhim;
	private ArrayList<Phim> dsPhim;
	private TimePicker chonGio;
	private JButton nutChonGio;
	private AbstractButton nutChonNgayBD;
	private DateChooser chonNgayBD;
	private JTextField txtNgayBD;

    public ThemLichChieuDialog(JFrame parent) throws MalformedURLException, NotBoundException, RemoteException {
        super(parent, "Thêm Lịch Chiếu", true);
        this.lichChieuDAO = (LichChieuService) Naming.lookup("rmi://XXXXXX:9090/lichChieuService");
        this.dataChanged = false;
        daoPhong = (PhongService) Naming.lookup("rmi://XXXXXX:9090/phongService");
        daoPhim = (PhimService) Naming.lookup("rmi://XXXXXX:9090/phimService");
        idGeneratorService = (IdGeneratorService) Naming.lookup("rmi://XXXXXX:9090/idGeneratorService");

        setSize(600, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel pnlContent = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlContent.add(new JLabel("Phòng:"));
        cboPhong = new JComboBox<Phong>();
        dsPhong = (ArrayList<Phong>) daoPhong.getAll();
        for (Phong phong : dsPhong) {
        	cboPhong.addItem(phong);
        }
        pnlContent.add(cboPhong);

        pnlContent.add(new JLabel("Phim:"));
        cboPhim = new JComboBox<Phim>();
        dsPhim = (ArrayList<Phim>) daoPhim.getAll();
        for (Phim phim : dsPhim) {
        	if (phim.getTrangThai().equals("Đang chiếu")) {
        		cboPhim.addItem(phim);
        	}
        }
        pnlContent.add(cboPhim);
        
        JPanel pnlNgay = new JPanel();
        pnlNgay.setLayout(new BoxLayout(pnlNgay, BoxLayout.X_AXIS));
        txtNgayBD = new JTextField(20);
		chonNgayBD = new DateChooser();
		nutChonNgayBD = new JButton();
        FlatSVGIcon icon = new FlatSVGIcon("images/svg/calendar.svg", 18, 18);
		nutChonNgayBD.setIcon(icon);
		nutChonNgayBD.addActionListener(e -> {
			chonNgayBD.showPopup();
		});
		chonNgayBD.setTextRefernce(txtNgayBD);
		chonNgayBD.addEventDateChooser(new EventDateChooser() {
			@Override
			public void dateSelected(SelectedAction action, SelectedDate date) {
				System.out.println(date.getDay() + "-" + date.getMonth() + "-" + date.getYear());
				if (action.getAction() == SelectedAction.DAY_SELECTED) {
					chonNgayBD.hidePopup();
				}
			}
		});
		pnlNgay.add(txtNgayBD);
		pnlNgay.add(nutChonNgayBD);
		pnlContent.add(new JLabel("Ngày chiếu:"));
        pnlContent.add(pnlNgay);

        pnlContent.add(new JLabel("Giờ Bắt Đầu:"));
        JPanel pnlGio = new JPanel();
        pnlGio.setLayout(new BoxLayout(pnlGio, BoxLayout.X_AXIS));
        txtGioBatDau = new JTextField();
        chonGio = new TimePicker();
		nutChonGio = new JButton();
		chonGio.setForeground(new Color(138, 48, 191));
		chonGio.setDisplayText(txtGioBatDau);
		nutChonGio.setIcon(new FlatSVGIcon("images/svg/clock.svg", 18, 18));
		nutChonGio.addActionListener(e -> {
			chonGio.showPopup(this, (getWidth() - chonGio.getPreferredSize().width) / 2,
					(getHeight() - chonGio.getPreferredSize().height) / 2);
		});
		pnlGio.add(txtGioBatDau);
		pnlGio.add(nutChonGio);
        pnlContent.add(pnlGio);

        pnlContent.add(new JLabel("Giá Một Ghế:"));
        txtGiaMotGhe = new JTextField();
        pnlContent.add(txtGiaMotGhe);

        add(pnlContent, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnThem = new JButton("Thêm");
		btnThem.setBackground(Color.decode("#273167"));
		btnThem.setForeground(Color.white);
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(Color.decode("#ff4f4f"));
        btnHuy.setForeground(Color.white);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnHuy);

        add(pnlButtons, BorderLayout.SOUTH);

        btnThem.addActionListener(this);
        btnHuy.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(btnThem)) {
            try {
                themLichChieu();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        } else if (source.equals(btnHuy)) {
            UIManager.put("OptionPane.yesButtonText", "Có");
            UIManager.put("OptionPane.noButtonText", "Không");
            UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát? \nMọi thay đổi sẽ không được lưu.",
                    "Chú ý", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                this.dispose();
            }
            UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
        }
    }

    private void themLichChieu() throws RemoteException {
        String ngayBD = txtNgayBD.getText().trim();
        String gioBD = txtGioBatDau.getText().trim();
        String giaMotGheStr = txtGiaMotGhe.getText().trim();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ngayBDLocalDate;
        try {
            ngayBDLocalDate = LocalDate.parse(ngayBD, dateFormatter);
        } catch (Exception ex) {
            UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Ngày chiếu không hợp lệ. Vui lòng nhập đúng định dạng dd-MM-yyyy", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgayBD.requestFocus();
            UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
            return;
        }

        DateTimeFormatter timeFormatter;
        LocalTime gioBDLocalTime;
        if (gioBD.contains("AM") || gioBD.contains("PM")) {
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        } else {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        }
        try {
            gioBDLocalTime = LocalTime.parse(gioBD, timeFormatter);
        } catch (Exception ex) {
            UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Giờ chiếu không hợp lệ. Vui lòng nhập đúng định dạng HH:mm hoặc hh:mm AM/PM", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGioBatDau.requestFocus();
            UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
            return;
        }

        LocalDateTime ngayGioChieu = LocalDateTime.of(ngayBDLocalDate, gioBDLocalTime);
        if (!ngayGioChieu.isAfter(LocalDateTime.now())) {
            UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Ngày chiếu phải sau ngày hiện tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgayBD.requestFocus();
            UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
            return;
        }

        double giaMotGhe;
        if (giaMotGheStr.isEmpty()) {
            UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Giá vé không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaMotGhe.requestFocus();
            UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
            return;
        }
        try {
            giaMotGhe = Double.parseDouble(giaMotGheStr);
            if (giaMotGhe < 0) {
                UIManager.put("Button.background", Color.decode("#273167"));
                UIManager.put("Button.foreground", Color.WHITE);
                JOptionPane.showMessageDialog(this, "Giá vé phải là một số dương", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGiaMotGhe.requestFocus();
                UIManager.put("Button.background", Color.white);
    			UIManager.put("Button.foreground", Color.black);
                return;
            }
        } catch (Exception ex) {
            UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Giá vé phải là một số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaMotGhe.requestFocus();
            UIManager.put("Button.background", Color.white);
			UIManager.put("Button.foreground", Color.black);
            return;
        }

        Phong phong = (Phong) cboPhong.getSelectedItem();
        Phim phim = (Phim) cboPhim.getSelectedItem();
        LocalDateTime ngayGioKetThuc = ngayGioChieu.plusMinutes(phim.getThoiLuong());

        ArrayList<LichChieu> danhSachLichChieu = lichChieuDAO.getLichChieuTheoPhong(String.valueOf(phong.getMaPhong()));
        for (LichChieu lc : danhSachLichChieu) {
            LocalDateTime batDau = lc.getGioBatDau();
            LocalDateTime ketThuc = lc.getGioKetThuc();
            if ((ngayGioChieu.isBefore(ketThuc.plusMinutes(30)) && ngayGioKetThuc.isAfter(batDau.minusMinutes(30)))) {
            	UIManager.put("Button.background", Color.decode("#273167"));
                UIManager.put("Button.foreground", Color.WHITE);
                JOptionPane.showMessageDialog(this, "Phòng này đã có lịch chiếu trong khoảng thời gian này", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGioBatDau.requestFocus();
                UIManager.put("Button.background", Color.white);
    			UIManager.put("Button.foreground", Color.black);
                return;
            }
        }

        LichChieu lichChieuMoi = new LichChieu(ngayGioChieu, ngayGioKetThuc, giaMotGhe, phong, phim);
        lichChieuMoi.setMaLichChieu(idGeneratorService.getNextId("LichChieu"));
        boolean themThanhCong = lichChieuDAO.add(lichChieuMoi);
        if (themThanhCong) {
        	UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Thêm lịch chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            UIManager.put("Button.background", Color.white);
            UIManager.put("Button.foreground", Color.black);
            dataChanged = true;
            this.dispose();
        } else {
        	UIManager.put("Button.background", Color.decode("#273167"));
            UIManager.put("Button.foreground", Color.WHITE);
            JOptionPane.showMessageDialog(this, "Thêm lịch chiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            UIManager.put("Button.background", Color.white);
            UIManager.put("Button.foreground", Color.black);
        }
    }


    public boolean isDataChanged() {
        return dataChanged;
    }
}