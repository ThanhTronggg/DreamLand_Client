package iuh.fit.gui.app.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;

import entity.NhanVien;
import iuh.fit.gui.app.Main;
import iuh.fit.gui.app.hoso.DoiMatKhau;
import iuh.fit.gui.app.hoso.HoSoForm;
import iuh.fit.gui.app.khachhang.QuanLyKhachHangGUI;
import iuh.fit.gui.app.khuyenmai.QuanLyKhuyenMaiGUI;
import iuh.fit.gui.app.lichchieu.QuanLyLichChieuGUI;
import iuh.fit.gui.app.nhanvien.NhanVienGUI;
import iuh.fit.gui.app.phim.QuanLyPhimGUI;
import iuh.fit.gui.app.sanpham.doan.QuanLyDoAnGUI;
import iuh.fit.gui.app.sanpham.nuocuong.QuanLyNuocUongGUI;
import iuh.fit.gui.app.thongke.ThongKeDoanhThu;
import iuh.fit.gui.app.thongke.ThongKeKhachHang;
import iuh.fit.gui.app.thongke.ThongKePhim;
import iuh.fit.gui.app.thongke.ThongKeSanPham;
import iuh.fit.gui.menu.Menu;
import iuh.fit.gui.menu.MenuAction;

public class MainForm extends JLayeredPane {
	private static final long serialVersionUID = 1L;
	private Menu menu;
	private JPanel panelBody;
	private JButton menuButton;

	public MainForm(NhanVien employee) {
		init(employee);
	}

	private void init(NhanVien employee) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MainFormLayout());
		menu = new Menu(employee.getVaiTro());
		panelBody = new JPanel(new BorderLayout());
		initMenuArrowIcon();
		menuButton.putClientProperty(FlatClientProperties.STYLE,
				"" + "background:$Menu.button.background;" + "arc:999;" + "focusWidth:0;" + "borderWidth:0");
		menuButton.addActionListener((ActionEvent e) -> {
			setMenuFull(!menu.isMenuFull());
		});
		initMenuEvent(employee);
		setLayer(menuButton, JLayeredPane.POPUP_LAYER);
		add(menuButton);
		add(menu);
		add(panelBody);
	}

	@Override
	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		initMenuArrowIcon();
	}

	private void initMenuArrowIcon() {
		if (menuButton == null) {
			menuButton = new JButton();
		}
		String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
		menuButton.setIcon(new FlatSVGIcon("images/svg/" + icon, 0.8f));
		System.out.println("images/svg/" + icon);
	}

	private void initMenuEvent(NhanVien employee) {
		menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
			if (employee.getVaiTro().equalsIgnoreCase("Nhân viên quản lý")) {
				switch (index) {
					case 0: //Quản lý nhân viên
						Main.showMainForm(new NhanVienGUI(employee));
						break;
					case 1: //Quản lý khuyến mãi
						Main.showMainForm(new QuanLyKhuyenMaiGUI());
						// System.out.println(employee);
						break;
					case 2: //Quản lý Phim
						Main.showMainForm(new QuanLyPhimGUI());
						break;
					case 3: //Quản lý Đồ ăn & uống
						switch (subIndex) {
							case 1: //Đồ ăn
								Main.showMainForm(new QuanLyDoAnGUI());
								break;
							case 2: //Nước uống
								Main.showMainForm(new QuanLyNuocUongGUI());
								break;
							default:
								action.cancel();
								break;
						}
						break;
					case 4: // Thống kê
						switch (subIndex) {
							case 1: //Thống kê doanh thu
								Main.showMainForm(new ThongKeDoanhThu());
								break;
							case 2: //khách hàng
								Main.showMainForm(new ThongKeKhachHang());
								break;
							case 3: //phim
								Main.showMainForm(new ThongKePhim());
								break;
							case 4: //Sản phẩm
								Main.showMainForm(new ThongKeSanPham());
								break;
							default:
								action.cancel();
								break;
						}
						break;
					case 5:
						switch (subIndex) {
							case 1:
								 Main.showMainForm(new HoSoForm(employee));
								break;
							case 2:
								Main.showMainForm(new DoiMatKhau(employee));
								break;
							default:
								action.cancel();
								break;
						}
						break;
					case 6:
						Main.logout();
						break;
					default:
						action.cancel();
						break;
				}
			} else {
				switch (index) {
					case 0:
						Main.showMainForm(new QuanLyLichChieuGUI(employee));
						break;
					case 1:
						Main.showMainForm(new QuanLyKhachHangGUI());
						break;
					case 2: // Thống kê
						switch (subIndex) {
							case 1: //Thống kê doanh thu
								Main.showMainForm(new ThongKeDoanhThu());
								break;
							case 2: //khách hàng
								Main.showMainForm(new ThongKeKhachHang());
								break;
							case 3: //phim
								Main.showMainForm(new ThongKePhim());
								break;
							case 4: //Sản phẩm
								Main.showMainForm(new ThongKeSanPham());
								break;
							default:
								action.cancel();
								break;
						}
						break;
					case 3:
						switch (subIndex) {
							case 1:
								Main.showMainForm(new HoSoForm(employee));
								break;
							case 2:
								Main.showMainForm(new DoiMatKhau(employee));
								break;
							default:
								action.cancel();
								break;
						}
						break;
					case 4:
						Main.logout();
						break;
					default:
						action.cancel();
						break;
				}
			}
		});

	}

	private void setMenuFull(boolean full) {
		String icon;
		if (getComponentOrientation().isLeftToRight()) {
			icon = (full) ? "menu_left.svg" : "menu_right.svg";
		} else {
			icon = (full) ? "menu_right.svg" : "menu_left.svg";
		}
		menuButton.setIcon(new FlatSVGIcon("images/svg/" + icon, 0.8f));
		menu.setMenuFull(full);
		revalidate();
	}

	public void hideMenu() {
		menu.hideMenuItem();
	}

	public void showForm(Component component) {
		panelBody.removeAll();
		panelBody.add(component);
		panelBody.repaint();
		panelBody.revalidate();
	}

	public void setSelectedMenu(int index, int subIndex) throws MalformedURLException, NotBoundException, RemoteException {
		menu.setSelectedMenu(index, subIndex);
	}

	private class MainFormLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			synchronized (parent.getTreeLock()) {
				return new Dimension(5, 5);
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			synchronized (parent.getTreeLock()) {
				return new Dimension(0, 0);
			}
		}

		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				boolean ltr = parent.getComponentOrientation().isLeftToRight();
				Insets insets = UIScale.scale(parent.getInsets());
				int x = insets.left;
				int y = insets.top;
				int width = parent.getWidth() - (insets.left + insets.right);
				int height = parent.getHeight() - (insets.top + insets.bottom);
				int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
				int menuX = ltr ? x : x + width - menuWidth;
				menu.setBounds(menuX, y, menuWidth, height);
				int menuButtonWidth = menuButton.getPreferredSize().width;
				int menuButtonHeight = menuButton.getPreferredSize().height;
				int menubX;
				if (ltr) {
					menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
				} else {
					menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
				}
				menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
				int gap = UIScale.scale(5);
				int bodyWidth = width - menuWidth - gap;
				int bodyHeight = height;
				int bodyx = ltr ? (x + menuWidth + gap) : x;
				int bodyy = y;
				panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
			}
		}
	}
}
