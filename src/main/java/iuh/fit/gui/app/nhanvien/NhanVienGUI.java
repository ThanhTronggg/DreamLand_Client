package iuh.fit.gui.app.nhanvien;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;
import net.miginfocom.swing.MigLayout;

public class NhanVienGUI extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField timKiemTextField;
    private JButton capNhatButton;
    private JButton xoaButton;
    private JPanel container0;
    private JPanel container1;

    private NhanVienDAO nhanVienDAO;
    private JButton themMoiButton;
    private NhanVienTableModel nhanVienTableModel;
    private JTable nhanVienTable;
    private ThemNhanVien themNhanVienDialog;
    private CapNhatNhanVien capNhatNhanVienDialog;
    private NhanVien nhanVienHienTai;

    public NhanVienGUI(NhanVien nhanVienHienTai) {
        this.nhanVienHienTai = nhanVienHienTai;
        nhanVienDAO = new NhanVienDAO(NhanVien.class);

        setLayout(new BorderLayout());
        initUI();
        setupTable();
        setupListeners();

        System.out.println("visible");

        this.setVisible(true);
    }

    private void initUI() {
        container0 = new JPanel();
        container1 = new JPanel();
        timKiemTextField = new JTextField();
        timKiemTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm");

        themMoiButton = new JButton("Thêm mới");
        capNhatButton = new JButton("Cập nhật");
        xoaButton = new JButton("Xóa");

        container1.setLayout(new MigLayout("", "[]push[][][]", ""));
        container1.add(timKiemTextField, "w 200!");
        container1.add(themMoiButton);
        container1.add(capNhatButton);
        container1.add(xoaButton);

        // Reduce the scaling factor of each icon
        themMoiButton.setIcon(new FlatSVGIcon("images/svg/add.svg", 0.03f));
        capNhatButton.setIcon(new FlatSVGIcon("images/svg/edit.svg", 0.03f));
        xoaButton.setIcon(new FlatSVGIcon("images/svg/delete.svg", 0.03f));
        timKiemTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
                new FlatSVGIcon("images/svg/search.svg", 0.03f));

        container0.setLayout(new MigLayout("wrap, fill, insets 15", "[fill]", "[grow 0][fill]"));
        container0.add(container1);
        add(container0, BorderLayout.CENTER);
    }

    private void setupTable() {
        List<NhanVien> nhanVienList = nhanVienDAO.getAll();
        nhanVienTableModel = new NhanVienTableModel(nhanVienList); // Initialize with data
        nhanVienTable = new JTable(nhanVienTableModel);

        JScrollPane scrollPane = new JScrollPane(nhanVienTable);
        container0.add(scrollPane);

        if (nhanVienTable.getColumnModel().getColumnCount() > 0) {
            nhanVienTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            nhanVienTable.getColumnModel().getColumn(4).setPreferredWidth(300);
        }

        customizeScrollPane(scrollPane);
        customizeTableAlignment();
    }

    private void customizeScrollPane(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "background:$Table.background;" + "track:$Table.background;" + "trackArc:999");
    }

    private void customizeTableAlignment() {
        nhanVienTable.getTableHeader()
                .setDefaultRenderer(getAlignmentCellRender(nhanVienTable.getTableHeader().getDefaultRenderer(), true));
        nhanVienTable.setDefaultRenderer(Object.class,
                getAlignmentCellRender(nhanVienTable.getDefaultRenderer(Object.class), false));
    }

    private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                if (com instanceof JLabel) {
                    JLabel label = (JLabel) com;
                    label.setHorizontalAlignment(SwingConstants.LEADING);
                    if (!header) {
                        com.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                    }
                }
                return com;
            }
        };
    }

    private void setupListeners() {
        timKiemTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                xuLyTimKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                xuLyTimKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                xuLyTimKiem();
            }
        });

        themMoiButton.addActionListener(this);
        capNhatButton.addActionListener(this);
        xoaButton.addActionListener(this);

        // Add a mouse listener to handle row click
        nhanVienTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    int row = nhanVienTable.getSelectedRow();
                    if (row != -1) {
                        showNhanVienDetail(row);
                    }
                }
            }
        });
    }

    private void showNhanVienDetail(int rowIndex) {
        // Retrieve NhanVien object based on selected row index
        NhanVien nhanVien = nhanVienTableModel.getNhanVienList().get(rowIndex);

        // Create and show the detail dialog
        XemThongTinNhanVien xemThongTinNhanVienDialog = new XemThongTinNhanVien(nhanVien);
        xemThongTinNhanVienDialog.setModal(true);
        xemThongTinNhanVienDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(themMoiButton)) {
            openThemNhanVienDialog();
        } else if (e.getSource().equals(capNhatButton)) {
            updateNhanVien();
        } else if (e.getSource().equals(xoaButton)) {
            deleteNhanVien();
        }
    }

    private void openThemNhanVienDialog() {
        SwingUtilities.invokeLater(() -> {
            themNhanVienDialog = new ThemNhanVien();
            themNhanVienDialog.setVisible(true);

            themNhanVienDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshNhanVienTable();
                }
            });
        });
    }

    private void updateNhanVien() {
        SwingUtilities.invokeLater(() -> {
            int rowIndex = nhanVienTable.getSelectedRow(); // Lấy chỉ mục dòng được chọn
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để cập nhật.");
            } else {
                String maNhanVien = (String) nhanVienTable.getValueAt(rowIndex, 0); // Lấy mã nhân viên
                maNhanVien = maNhanVien.trim(); // Xóa khoảng trắng ở đầu và cuối chuỗi
                NhanVien nhanVien = nhanVienDAO.findById(maNhanVien); // Lấy thông tin từ cơ sở dữ liệu
                nhanVien.setMaNhanVien(maNhanVien); // Gán mã nhân viên cho đối tượng nhân viên
                // Mở cửa sổ cập nhật nhân viên
                capNhatNhanVienDialog = new CapNhatNhanVien(nhanVien);
                capNhatNhanVienDialog.setVisible(true);
                capNhatNhanVienDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        // Lấy lại danh sách nhân viên sau khi thêm nhân viên
                        refreshNhanVienTable();
                    }
                });
            }
        });
    }

    private void refreshNhanVienTable() {
        List<NhanVien> nhanVienList = nhanVienDAO.getAll(); // Lấy lại danh sách nhân viên từ cơ
                                                                    // sở
                                                                    // dữ liệu

        // Kiểm tra xem danh sách có rỗng không
        if (nhanVienList != null && !nhanVienList.isEmpty()) {
            nhanVienTableModel.setNhanVienList(nhanVienList); // Cập nhật lại dữ liệu trong TableModel
            nhanVienTableModel.fireTableDataChanged(); // Làm mới bảng
        } else {
            System.out.println("Danh sách nhân viên không có dữ liệu!");
        }
    }

    private void deleteNhanVien() {
        SwingUtilities.invokeLater(() -> {
            int rowIndex = nhanVienTable.getSelectedRow();
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để xóa.");
            } else {
                String maNhanVien = (String) nhanVienTable.getValueAt(rowIndex, 0);
                String soDienThoai = (String) nhanVienTable.getValueAt(rowIndex, 2);
                if (String.valueOf(nhanVienHienTai.getMaNhanVien()).equals(maNhanVien)) {
                    JOptionPane.showMessageDialog(this, "Bạn không thể xóa chính mình!!!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này?", "Cảnh báo",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean success = nhanVienDAO.delete(maNhanVien);
                    if (success) {
                        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO(TaiKhoan.class);
                        boolean isDeleteTaiKhoan = taiKhoanDAO.delete(soDienThoai);

                        if (isDeleteTaiKhoan) {
                            JOptionPane.showMessageDialog(this, "Xóa thành công.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại.", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                        refreshNhanVienTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void xuLyTimKiem() {
        String keyword = timKiemTextField.getText();
        List<NhanVien> nhanVienList = nhanVienDAO.searchNhanVien(keyword);
        nhanVienTableModel.setNhanVienList(nhanVienList); // Update the table model
        nhanVienTableModel.fireTableDataChanged(); // Notify table to refresh
    }
}
