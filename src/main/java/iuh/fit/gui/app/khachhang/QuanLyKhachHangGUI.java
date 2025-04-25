package iuh.fit.gui.app.khachhang;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import dao.KhachHangDAO;
import entity.KhachHang;
import net.miginfocom.swing.MigLayout;

public class QuanLyKhachHangGUI extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel lblTitle;
    private JTextField txtTim;
    private JButton btnSua, btnXoa;
    private DefaultTableModel tableModel;
    private JTable table;
    private KhachHangDAO khachHangDAO;
    private Timer searchTimer;

    public QuanLyKhachHangGUI() {
        khachHangDAO = new KhachHangDAO(KhachHang.class);
        setLayout(new BorderLayout());
        initComponents();
       // loadInitialData();
        setupSearchTimer();
    }

    private void setupSearchTimer() {
        searchTimer = new Timer(300, e -> performSearch());
        searchTimer.setRepeats(false);
    }

    private void initComponents() {
        // North Panel with Title and Search
        JPanel pnlNorth = createNorthPanel();
        add(pnlNorth, BorderLayout.NORTH);

        // Center Panel with Table
        JPanel pnlCenter = createCenterPanel();
        add(pnlCenter, BorderLayout.CENTER);

        // South Panel with Buttons
        JPanel pnlSouth = createSouthPanel();
        add(pnlSouth, BorderLayout.SOUTH);

        setupEventListeners();
    }

    private JPanel createNorthPanel() {
        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        lblTitle = new JLabel("THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));

        // Search Panel
        JPanel pnlSearch = new JPanel(new MigLayout("", "[][]push[]", ""));
        txtTim = new JTextField();
        txtTim.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm theo số điện thoại");
        txtTim.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, 
            new FlatSVGIcon("images/svg/search.svg", 0.35f));

        pnlSearch.add(txtTim, "w 300!");

        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);

        return pnlNorth;
    }

    private JPanel createCenterPanel() {
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        String[] header = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email"};
        tableModel = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        return pnlCenter;
    }

    private JPanel createSouthPanel() {
        JPanel pnlSouth = new JPanel(new MigLayout("insets 10, align right"));
        
        btnSua = createButton("Cập nhật", "images/svg/update.svg", new Color(237, 203, 37));
        btnXoa = createButton("Xóa", "images/svg/delete.svg", new Color(255, 0, 0));

        pnlSouth.add(btnSua, "gapright 10");
        pnlSouth.add(btnXoa);

        return pnlSouth;
    }

    private JButton createButton(String text, String iconPath, Color color) {
        JButton button = new JButton(text);
        button.setIcon(new FlatSVGIcon(iconPath, 20, 20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupEventListeners() {
        btnSua.addActionListener(e -> handleUpdate());
        btnXoa.addActionListener(e -> handleDelete());

        txtTim.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { restartTimer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { restartTimer(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { restartTimer(); }
        });
    }

    private void restartTimer() {
        searchTimer.restart();
    }

//    private void performSearch() {
//        String searchText = txtTim.getText().trim();
//        tableModel.setRowCount(0);
//        
//        try {
//            ArrayList<KhachHang> results = searchText.isEmpty() ? 
//                khachHangDAO.getAllKhachHang() : 
//                khachHangDAO.timKiemKhachHangTheoSDT(searchText);
//            
//            for (KhachHang kh : results) {
//                tableModel.addRow(new Object[]{
//                    kh.getMaKhachHang(),
//                    kh.getTenKhachHang(),
//                    kh.getSoDienThoai(),
//                    kh.getEmail()
//                });
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, 
//                "Lỗi khi tìm kiếm: " + e.getMessage(), 
//                "Lỗi", 
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }

    private void handleUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một khách hàng để cập nhật!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        KhachHang selectedCustomer = getSelectedCustomer();
        if (showUpdateDialog(selectedCustomer)) {
            performSearch(); // Refresh the table
        }
    }

    private KhachHang getSelectedCustomer() {
        int row = table.getSelectedRow();
        KhachHang khachHang = new KhachHang(
        );
        khachHang.setTenKhachHang(tableModel.getValueAt(row, 1).toString());
        khachHang.setSoDienThoai(tableModel.getValueAt(row, 2).toString());
        khachHang.setEmail(tableModel.getValueAt(row, 3).toString());
        khachHang.setMaKhachHang(tableModel.getValueAt(row, 0).toString());
        return khachHang;
    }

    private boolean showUpdateDialog(KhachHang customer) {
        JTextField txtTen = new JTextField(customer.getTenKhachHang());
        JTextField txtSoDienThoai = new JTextField(customer.getSoDienThoai());
        JTextField txtEmail = new JTextField(customer.getEmail());

        Object[] message = {
            "Tên khách hàng:", txtTen,
            "Số điện thoại:", txtSoDienThoai,
            "Email:", txtEmail
        };

        int option = JOptionPane.showConfirmDialog(this, message, 
            "Cập nhật khách hàng", JOptionPane.OK_CANCEL_OPTION);
            
        if (option == JOptionPane.OK_OPTION) {
            try {
                KhachHang updatedCustomer = new KhachHang();

                updatedCustomer.setMaKhachHang(customer.getMaKhachHang());
                updatedCustomer.setTenKhachHang(txtTen.getText().trim());
                updatedCustomer.setSoDienThoai(txtSoDienThoai.getText().trim());
                updatedCustomer.setEmail(txtEmail.getText().trim());

                if (khachHangDAO.update(updatedCustomer)) {
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật thông tin thành công!", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi cập nhật: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một khách hàng để xóa!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maKhachHang = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa khách hàng này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (khachHangDAO.delete(maKhachHang)) {
                    JOptionPane.showMessageDialog(this, 
                        "Xóa khách hàng thành công!", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                    performSearch(); // Refresh the table
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi xóa khách hàng: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    private void loadInitialData() {
//        try {
//            ArrayList<KhachHang> dsKhachHang = khachHangDAO.getAllKhachHang();
//            for (KhachHang kh : dsKhachHang) {
//                tableModel.addRow(new Object[]{
//                    kh.getMaKhachHang(),
//                    kh.getTenKhachHang(),
//                    kh.getSoDienThoai(),
//                    kh.getEmail()
//                });
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, 
//                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
//                "Lỗi", 
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }
    private void loadInitialData() {
        // Không cần tải dữ liệu mặc định khi khởi tạo giao diện.
        // Xóa tất cả dữ liệu trong bảng trước khi tìm kiếm hoặc không tìm kiếm.
        tableModel.setRowCount(0);
    }

    private void performSearch() {
        String searchText = txtTim.getText().trim();

        // Nếu không có tìm kiếm (trống), chỉ cần xóa toàn bộ bảng
        if (searchText.isEmpty()) {
            tableModel.setRowCount(0); // Xóa toàn bộ bảng khi không tìm kiếm
            return;
        }

        // Nếu có tìm kiếm, tiếp tục thực hiện tìm kiếm theo số điện thoại
        try {
            KhachHang kh = khachHangDAO.timKhachHangTheoSoDienThoai(searchText);

            // Xóa hết dữ liệu cũ trong bảng trước khi thêm dữ liệu tìm kiếm mới
            tableModel.setRowCount(0);

            tableModel.addRow(new Object[]{
                kh.getMaKhachHang(),
                kh.getTenKhachHang(),
                kh.getSoDienThoai(),
                kh.getEmail()});

            // Nếu không có kết quả tìm kiếm, hiển thị thông báo
            if (kh == null) {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy khách hàng nào!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tìm kiếm: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

}