package iuh.fit.gui.app.phim;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import entity.Phim;
import iuh.fit.gui.app.EnvironmentVariable;
import net.miginfocom.swing.MigLayout;
import service.PhimService;

public class QuanLyPhimGUI extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
    private JLabel lblTitle;
    private JTextField txtTim;
    private JButton btnThem;
    private JButton btnSua;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> cboLoaiHienThi;
    private JButton btnXoa;
    private PhimService phim_dao;

    public QuanLyPhimGUI() throws MalformedURLException, NotBoundException, RemoteException {
        phim_dao = (PhimService) Naming.lookup("rmi://"+ EnvironmentVariable.IP.getValue()+":"+Integer.parseInt(EnvironmentVariable.PORT_SERVER.getValue())+"/phimService");
       

        setLayout(new BorderLayout());

        // Panel bao gồm lblTitle và pnlTimKiem
        JPanel pnlNor = new JPanel();
        pnlNor.setLayout(new BorderLayout());

        // Tiêu đề
        lblTitle = new JLabel("THÔNG TIN PHIM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlNor.add(lblTitle, BorderLayout.NORTH);

        // Panel tìm kiếm và loại hiển thị
        JPanel pnlTimKiem = new JPanel();
        pnlTimKiem.setLayout(new MigLayout("", "[][]push[]", ""));

        txtTim = new JTextField();
        txtTim.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm theo tên phim");
        txtTim.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSVGIcon("images/svg/search.svg", 0.35f));
        pnlTimKiem.add(txtTim, "w 200!");

        cboLoaiHienThi = new JComboBox<>(new String[]{ "Phim đang chiếu", "Phim chưa chiếu", "Toàn bộ"});
        pnlTimKiem.add(cboLoaiHienThi);

        // Thêm panel tìm kiếm vào dưới tiêu đề
        pnlNor.add(pnlTimKiem, BorderLayout.SOUTH);
        add(pnlNor, BorderLayout.NORTH);

        // Bảng hiển thị phim
        String[] header = {"Mã phim", "Tên phim", "Trạng Thái", "Thời lượng [phút]"};
        tableModel = new DefaultTableModel(header, 0);
        table = new JTable(tableModel);
     // Add MouseListener for row click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the movie code (maPhim) from the selected row
                    String maPhim = (String) tableModel.getValueAt(selectedRow, 0);
                    
                    // Retrieve the movie details from the DAO
                    Phim selectedPhim = null;
                    try {
                        selectedPhim = phim_dao.findById(maPhim);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Show the movie details in a dialog
                    ChiTietPhimDialog dialog = new ChiTietPhimDialog(selectedPhim);
                    dialog.setModal(true);
                    dialog.setVisible(true);
                }
            }
        });

        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        

        // Panel chứa các nút chức năng
        JPanel pnlChucNang = new JPanel(new MigLayout("insets 0, align right", "[][][]", ""));
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        btnThem = new JButton("Thêm mới");
        btnThem.setIcon(new FlatSVGIcon("images/svg/add.svg", 20, 20));
        btnThem.setBackground(new Color(9, 87, 208));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(buttonFont);
        pnlChucNang.add(btnThem, "gapright 15");

        btnSua = new JButton("Cập nhật");
        btnSua.setIcon(new FlatSVGIcon("images/svg/update.svg", 20, 20));
        btnSua.setBackground(new Color(237, 203, 37));
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(buttonFont);
        pnlChucNang.add(btnSua, "gapright 15");

        btnXoa = new JButton("Xóa");
        btnXoa.setIcon(new FlatSVGIcon("images/svg/delete.svg", 15, 18));
        btnXoa.setBackground(new Color(255, 0, 0));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(buttonFont);
        pnlChucNang.add(btnXoa);

        // Thêm pnlChucNang vào phía dưới cùng của giao diện
        add(pnlChucNang, BorderLayout.SOUTH);

        // Gắn sự kiện
        cboLoaiHienThi.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);

        // Hiển thị dữ liệu
        hienThi();

        // Lọc khi nhập tìm kiếm
        txtTim.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    hienThi();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    hienThi();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    hienThi();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
   

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(cboLoaiHienThi)) {
            try {
                hienThi();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (o.equals(btnThem)) {
            try {
                them();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            } catch (NotBoundException ex) {
                throw new RuntimeException(ex);
            }
        } else if (o.equals(btnSua)) {
            try {
                sua();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (o.equals(btnXoa)) {
            try {
                xoa();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void hienThi() throws RemoteException {
        String selectedOption = cboLoaiHienThi.getSelectedItem().toString(); // Lấy lựa chọn từ ComboBox
        docData(selectedOption); // Gọi docData với tham số là trạng thái chọn
    }

    public void docData(String option) throws RemoteException {
        String textTimKiem = txtTim.getText();

        // Xóa tất cả hàng hiện tại trong bảng
        tableModel.setRowCount(0);

        // Lấy dữ liệu từ DAO và cập nhật bảng
        ArrayList<Phim> list = (ArrayList<Phim>) phim_dao.getAll(); // Lấy tất cả phim từ DAO

        for (Phim phim : list) {
            // Điều kiện lọc phim theo trạng thái
            boolean matches = false;

            // Lọc theo trạng thái phim
            if (option.equals("Phim đang chiếu") && phim.getTrangThai().equals("Đang chiếu")) {
                matches = true;
            } else if (option.equals("Phim chưa chiếu") && phim.getTrangThai().equals("Sắp chiếu")) {
                matches = true;
            } else if (option.equals("Toàn bộ")) {
                matches = true;
            }

            // Nếu phim thỏa mãn điều kiện tìm kiếm và trạng thái, thêm vào bảng
            if (matches && (String.valueOf(phim.getMaPhim()).contains(textTimKiem) || phim.getTenPhim().toLowerCase().contains(textTimKiem.toLowerCase()))) {
                tableModel.addRow(new String[]{
                    String.valueOf(phim.getMaPhim()),
                    phim.getTenPhim(),
                    phim.getTrangThai(),
                    Integer.toString(phim.getThoiLuong())
                });
            }
        }
    }



    private void them() throws RemoteException, MalformedURLException, NotBoundException {
        ThemPhimDialog themPhim = new ThemPhimDialog();
        themPhim.setModal(true);
        themPhim.setVisible(true);
        Phim newPhim = themPhim.getPhim(); // Get the movie from the dialog
        if (newPhim != null) {
            if (phim_dao.exists(String.valueOf(newPhim.getMaPhim()))) {
               // JOptionPane.showMessageDialog(this, "Mã phim đã tồn tại. Vui lòng nhập mã khác!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean isSaved = phim_dao.add(newPhim);
			if (isSaved) {
			    tableModel.addRow(new String[]{
                    String.valueOf(newPhim.getMaPhim()),
			        newPhim.getTenPhim(),
			        newPhim.getTrangThai(),
			        Integer.toString(newPhim.getThoiLuong())
			    });
			    JOptionPane.showMessageDialog(this, "Thêm phim thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			} else {
			    JOptionPane.showMessageDialog(this, "Lỗi khi thêm phim vào cơ sở dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			}
        } else {
            JOptionPane.showMessageDialog(this, "Không có phim mới được thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    private void sua() throws MalformedURLException, NotBoundException, RemoteException {
        // Kiểm tra xem có hàng nào được chọn không
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy thông tin phim từ hàng đã chọn
        String maPhim = (String) tableModel.getValueAt(selectedRow, 0);
        Phim phimToUpdate = phim_dao.findById(maPhim); // Lấy phim theo mã

        // Mở hộp thoại để cập nhật thông tin phim
        CapNhatPhimDialog capNhatPhimDialog = new CapNhatPhimDialog(phimToUpdate);
        capNhatPhimDialog.setModal(true);
        capNhatPhimDialog.setVisible(true);

        // Lấy phim đã cập nhật từ hộp thoại
        Phim updatedPhim = capNhatPhimDialog.getPhim();

        // Kiểm tra nếu updatedPhim không phải là null (người dùng không nhấn hủy)
        if (updatedPhim != null) {
            // Cập nhật lại dữ liệu trong cơ sở dữ liệu
            boolean isUpdated = phim_dao.update(updatedPhim);
            if (isUpdated) {
                // Cập nhật lại hàng trong bảng
                tableModel.setValueAt(updatedPhim.getTenPhim(), selectedRow, 1);
                tableModel.setValueAt(updatedPhim.getTrangThai(), selectedRow, 2);
                tableModel.setValueAt(updatedPhim.getThoiLuong(), selectedRow, 3);
               // JOptionPane.showMessageDialog(this, "Cập nhật phim thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phim vào cơ sở dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Nếu updatedPhim là null, tức là người dùng đã hủy
            JOptionPane.showMessageDialog(this, "Không có thông tin phim nào được cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    
    //Xóa_phim 
    private void xoa() throws RemoteException {
        // Kiểm tra xem có hàng nào được chọn không
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy mã phim từ hàng đã chọn
        String maPhim = (String) tableModel.getValueAt(selectedRow, 0);

        // Xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phim này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Gọi phương thức xóa phim trong DAO
            boolean isDeleted = phim_dao.delete(maPhim);
            if (isDeleted) {
                // Xóa hàng trong bảng
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa phim khỏi cơ sở dữ liệu!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 