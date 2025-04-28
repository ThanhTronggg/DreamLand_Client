package iuh.fit.gui.app.sanpham.doan;
                                                                                                                                                 
                                                                                                                                                 
import javax.swing.*;                                                                                                                            
import java.awt.*;                                                                                                                               
import java.awt.event.*;                                                                                                                         
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.SanPham;
import service.IdGeneratorService;
import service.SanPhamService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Locale;                                                                                                                         
import java.util.ArrayList;                                                                                                                      
                                                                                                                                                 
public class QuanLyDoAnGUI extends JPanel {
    private final IdGeneratorService idGeneratorService;
    private JTextField searchField;
    private JPanel productsPanel;                                                                                                                
    private Color primaryColor = new Color(70, 130, 180);                                                                                        
    private Color secondaryColor = new Color(135, 206, 250);                                                                                     
    private Font titleFont = new Font("Arial", Font.BOLD, 16);                                                                                   
    private Font normalFont = new Font("Arial", Font.PLAIN, 14);                                                                                 
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));                                           
    private ArrayList<SanPham> danhSachSanPham;
	private SanPhamService sanPhamDAO;
	private String pathAnh;
	private AbstractButton lblHinhChon;                                                                                               
                                                                                                                                                 
    public QuanLyDoAnGUI() throws MalformedURLException, NotBoundException, RemoteException {
        setLayout(new BorderLayout(10, 10));                                                                                                     
        setBorder(new EmptyBorder(20, 20, 20, 20));                                                                                              
        setBackground(Color.WHITE);   
        sanPhamDAO = (SanPhamService) Naming.lookup("rmi://172.20.10.14:9090/sanPhamService");
        idGeneratorService = (IdGeneratorService) Naming.lookup("rmi://172.20.10.14:9090/idGeneratorService");

        danhSachSanPham = null;                                                                                                     
                                                                                                                                                 
        // Top panel with search and add button                                                                                                  
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));                                                                                   
        topPanel.setBackground(Color.WHITE);                                                                                                     
                                                                                                                                                 
        // Cập nhật trường tìm kiếm (searchField)                                                                                                
        searchField = new JTextField();                                                                                                          
        searchField.setFont(normalFont);                                                                                                         
        searchField.setPreferredSize(new Dimension(200, 30));  // Giảm chiều rộng của JTextField                                                 
        searchField.setBorder(BorderFactory.createCompoundBorder(                                                                                
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),                                                                          
            BorderFactory.createEmptyBorder(0, 5, 0, 5)));  // Thêm border đẹp hơn cho JTextField                                                
                                                                                                                                                 
        JButton searchButton = createStyledButton("Tìm", primaryColor);                                                                          
        searchButton.addActionListener(e -> searchProducts());                                                                                   
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));                                                                                 
        searchPanel.setBackground(Color.WHITE);                                                                                                  
        searchPanel.add(searchField, BorderLayout.CENTER);                                                                                       
        searchPanel.add(searchButton, BorderLayout.EAST);                                                                                        
                                                                                                                                                 
        JButton addNewProductButton = createStyledButton("Thêm SP", secondaryColor);                                                             
        addNewProductButton.addActionListener(e -> showAddNewProductDialog());                                                                   
                                                                                                                                                 
        topPanel.add(searchPanel, BorderLayout.CENTER);                                                                                          
        topPanel.add(addNewProductButton, BorderLayout.EAST);                                                                                    
                                                                                                                                                 
        // Products panel with grid layout                                                                                                       
        productsPanel = new JPanel(new GridLayout(0, 3, 20, 20));                                                                                
        productsPanel.setBackground(Color.WHITE);                                                                                                
                                                                                                                                                 
        // Add sample products                                                                                                                   
        danhSachSanPham = sanPhamDAO.getSanPhamTheoLoaiSP("Đồ ăn");
        for (SanPham sp : danhSachSanPham) {
        	JPanel card = createProductCard(sp);                                                                                                
            productsPanel.add(card);                                                                                                                 
            productsPanel.revalidate();                                                                                                              
            productsPanel.repaint();     
        }
                                                                                                                                                 
        // Scroll pane for products                                                                                                              
        JScrollPane scrollPane = new JScrollPane(productsPanel);                                                                                 
        scrollPane.setBorder(null);                                                                                                              
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);                                                                                  
                                                                                                                                                 
        // Add components to main panel                                                                                                          
        add(topPanel, BorderLayout.NORTH);                                                                                                       
        add(scrollPane, BorderLayout.CENTER);                                                                                                    
    }                                                                                                                                                                                                                                                                                                                                                                                                                     
                                                                                                                                                 
    private JPanel createProductCard(SanPham sanPham) {                                                                                          
        JPanel card = new JPanel(new BorderLayout(10, 10));                                                                                      
        card.setBorder(new CompoundBorder(new LineBorder(new Color(70, 130, 180), 2), new EmptyBorder(10, 10, 10, 10)));                         
        card.setBackground(Color.WHITE);                                                                                                         
                                                                                                                                                 
        // Product image with improved border                                                                                                    
        ImageIcon originalIcon = new ImageIcon(sanPham.getAnh());                                                                                
        JLabel imageLabel = new JLabel(resizeImage(originalIcon, 150, 150));                                                                     
        imageLabel.setHorizontalAlignment(JLabel.CENTER);                                                                                        
                                                                                                                                                 
        // Improved border for the image                                                                                                         
        imageLabel.setBorder(BorderFactory.createCompoundBorder(                                                                                 
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),                                                                          
            BorderFactory.createCompoundBorder(                                                                                                  
                BorderFactory.createLineBorder(Color.WHITE, 3),                                                                                  
                BorderFactory.createDashedBorder(new Color(70, 130, 180), 2, 2, 1, false)                                                        
            )                                                                                                                                    
        ));                                                                                                                                      
                                                                                                                                                 
        // Product details                                                                                                                       
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 5, 5));                                                                            
        detailsPanel.setBackground(Color.WHITE);                                                                                                 
        JLabel nameLabel = new JLabel(sanPham.getTenSanPham(), JLabel.CENTER);                                                                   
        nameLabel.setFont(titleFont);                                                                                                            
        detailsPanel.add(nameLabel);                                                                                                             
        detailsPanel.add(new JLabel("Số lượng: " + sanPham.getSoLuong(), JLabel.CENTER));                                                        
                                                                                                                                                 
        // Price with coin icon                                                                                                                  
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));                                                                       
        pricePanel.setBackground(Color.WHITE);                                                                                                   
        ImageIcon coinIcon = new ImageIcon("images/xu.jpg"); // Replace with actual path                                                         
        JLabel coinLabel = new JLabel(resizeImage(coinIcon, 16, 16));                                                                            
        JLabel priceLabel = new JLabel(currencyFormatter.format(sanPham.getGiaBan()));                                                           
        pricePanel.add(coinLabel);                                                                                                               
        pricePanel.add(priceLabel);                                                                                                              
        detailsPanel.add(pricePanel);                                                                                                            
                                                                                                                                                 
        // Buttons panel                                                                                                                         
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));                                                               
        buttonsPanel.setBackground(Color.WHITE);                                                                                                 
                                                                                                                                                 
        JButton deleteButton = createStyledButton("Xóa", new Color(220, 53, 69));                                                                
        deleteButton.addActionListener(e -> {
            try {
                showDeleteConfirmDialog(sanPham);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonsPanel.add(deleteButton);                                                                                                          
                                                                                                                                                 
        JButton updateButton = createStyledButton("Cập nhật", new Color(40, 167, 69));                                                           
        updateButton.addActionListener(e -> showUpdateDialog(sanPham));                                                                          
        buttonsPanel.add(updateButton);                                                                                                          
                                                                                                                                                 
        JButton addButton = createStyledButton("Thêm", new Color(0, 123, 255));                                                                  
        addButton.addActionListener(e -> showAddDialog(sanPham));                                                                                
        buttonsPanel.add(addButton);                                                                                                             
                                                                                                                                                 
        card.add(imageLabel, BorderLayout.NORTH);                                                                                                
        card.add(detailsPanel, BorderLayout.CENTER);                                                                                             
        card.add(buttonsPanel, BorderLayout.SOUTH);                                                                                              
                                                                                                                                                 
        return card;                                                                                                                             
    }                                                                                                                                            
                                                                                                                                                 
    private void showAddNewProductDialog() {                                                                                                     
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Sản Phẩm Mới", true);                                 
        dialog.setLayout(new BorderLayout(10, 10));                                                                                              
        dialog.setSize(300, 300);                                                                                                                
        dialog.setLocationRelativeTo(this);                                                                                                      
                                                                                                                                                 
        JPanel contentPanel = new JPanel(new GridLayout(6, 2, 5, 5));                                                                            
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));                                                                                 
                                                                                                                                                 
//        contentPanel.add(new JLabel("Mã Sản Phẩm:"));                                                                                            
//        JTextField maSPField = new JTextField();                                                                                                 
//        contentPanel.add(maSPField);                                                                                                             
                                                                                                                                                 
        contentPanel.add(new JLabel("Tên Sản Phẩm:"));                                                                                           
        JTextField tenSPField = new JTextField();                                                                                                
        contentPanel.add(tenSPField);                                                                                                            
                                                                                                                                                 
        contentPanel.add(new JLabel("Số Lượng:"));                                                                                               
        JTextField soLuongField = new JTextField();                                                                                              
        contentPanel.add(soLuongField);                                                                                                          
                                                                                                                                                 
        contentPanel.add(new JLabel("Giá Mua:"));                                                                                                
        JTextField giaMuaField = new JTextField();                                                                                               
        contentPanel.add(giaMuaField);                                                                                                           
                                                    
        JComboBox<String> cboLoai = new JComboBox<String>();
        cboLoai.addItem("Đồ ăn");
        cboLoai.addItem("Thức uống");
        contentPanel.add(new JLabel("Loại Sản Phẩm:"));                                                                                                                                                                                        
        contentPanel.add(cboLoai);                                                                                                           
                                                                                                                                                 
        contentPanel.add(new JLabel("Đường Dẫn Ảnh:"));

     // Tạo JButton để mở JFileChooser
     JButton btnChonAnh = new JButton("Chọn Ảnh");
     contentPanel.add(btnChonAnh);

     // Thêm sự kiện cho nút "Chọn Ảnh"
     btnChonAnh.addActionListener(e -> {
         JFileChooser fileChooser = new JFileChooser();
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
         fileChooser.setFileFilter(filter);
         
         // Hiển thị JFileChooser và kiểm tra người dùng có chọn file không
         int returnValue = fileChooser.showOpenDialog(contentPanel);
         if (returnValue == JFileChooser.APPROVE_OPTION) {
             pathAnh = fileChooser.getSelectedFile().getAbsolutePath();
             btnChonAnh.setText(pathAnh);
             JOptionPane.showMessageDialog(contentPanel, "Load ảnh thành công");
         }
     });
                                                                                                                                                 
        JButton confirmButton = createStyledButton("Xác nhận", primaryColor);                                                                    
        confirmButton.addActionListener(e -> {                                                                                                   
            try {                                                                                                                                                                                                                              
                String tenSP = tenSPField.getText();                                                                                             
                int soLuong = Integer.parseInt(soLuongField.getText());                                                                          
                double giaMua = Double.parseDouble(giaMuaField.getText());                                                                       
                String loaiSP = cboLoai.getSelectedItem().toString();                                                                                           
                String anh = pathAnh;                                                                                                 
                                                                                                                                                 
//                SanPham newProduct = new SanPham(tenSP, soLuong, giaMua, loaiSP, anh);
                SanPham newProduct = new SanPham();
                newProduct.setTenSanPham(tenSP);
                newProduct.setSoLuong(soLuong);
                newProduct.setGiaMua(giaMua);
                newProduct.setLoaiSanPham(loaiSP);
                newProduct.dinhGiaBan();
                newProduct.setMaSanPham(idGeneratorService.getNextId("SanPham"));
                danhSachSanPham.add(newProduct);
                refreshProductsPanel();
                if(sanPhamDAO.add(newProduct)) {
                	dialog.dispose();                                                                                                                
                	JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm mới thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);                           	
                }
                else {
                	JOptionPane.showMessageDialog(this, "Không thể thêm sản phẩm");
                }
            } catch (NumberFormatException ex) {                                                                                                 
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng định dạng cho số lượng và giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);    
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });                                                                                                                                      
                                                                                                                                                 
        JButton cancelButton = createStyledButton("Hủy", secondaryColor);                                                                        
        cancelButton.addActionListener(e -> dialog.dispose());                                                                                   
                                                                                                                                                 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));                                                                       
        buttonPanel.add(confirmButton);                                                                                                          
        buttonPanel.add(cancelButton);                                                                                                           
                                                                                                                                                 
        dialog.add(contentPanel, BorderLayout.CENTER);                                                                                           
        dialog.add(buttonPanel, BorderLayout.SOUTH);                                                                                             
                                                                                                                                                 
        dialog.setVisible(true);                                                                                                                 
    }                                                                                                                                            
                                                                                                                                                 
    private void showDeleteConfirmDialog(SanPham sanPham) throws RemoteException {
        int option = JOptionPane.showConfirmDialog(this,                                                                                         
                "Bạn có chắc chắn muốn xóa sản phẩm " + sanPham.getTenSanPham() + "?",                                                           
                "Xác nhận xóa",                                                                                                                  
                JOptionPane.YES_NO_OPTION);                                                                                                      
        if (option == JOptionPane.YES_OPTION) {                                                                                                  
            if(sanPhamDAO.delete(sanPham.getMaSanPham())) {
            	danhSachSanPham.remove(sanPham); 
            	refreshProductsPanel();                                                                                                              
            	JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);                                	
            }
            else {
            	JOptionPane.showMessageDialog(this, "Không thể xóa sản  phẩm này");
            }
        }                                                                                                                                        
    }                                                                                                                                            
                                                                                                                                                 
    private void showUpdateDialog(SanPham sanPham) {                                                                                             
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập Nhật Sản Phẩm", true);                                 
        dialog.setLayout(new BorderLayout(10, 10));                                                                                              
        dialog.setSize(300, 250);                                                                                                                
        dialog.setLocationRelativeTo(this);                                                                                                      
                                                                                                                                                 
        JPanel contentPanel = new JPanel(new GridLayout(5, 2, 5, 5));                                                                            
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));                                                                                 
                                                                                                                                                 
        contentPanel.add(new JLabel("Mã Sản Phẩm:"));                                                                                            
        JLabel maSPLabel = new JLabel(String.valueOf(sanPham.getMaSanPham()));
        contentPanel.add(maSPLabel);                                                                                                             
                                                                                                                                                 
        contentPanel.add(new JLabel("Tên Sản Phẩm:"));                                                                                           
        JTextField tenSPField = new JTextField(sanPham.getTenSanPham());                                                                         
        contentPanel.add(tenSPField);                                                                                                            
                                                                                                                                                 
        contentPanel.add(new JLabel("Số Lượng:"));                                                                                               
        JTextField soLuongField = new JTextField(String.valueOf(sanPham.getSoLuong()));                                                          
        contentPanel.add(soLuongField);                                                                                                          
                                                                                                                                                 
        contentPanel.add(new JLabel("Giá Mua:"));                                                                                                
        JTextField giaMuaField = new JTextField(String.valueOf(sanPham.getGiaMua()));                                                            
        contentPanel.add(giaMuaField);   
        
        JComboBox<String> cboLoai = new JComboBox<String>();
        cboLoai.addItem("Đồ ăn");
        cboLoai.addItem("Thức uống");
        cboLoai.setSelectedItem(sanPham.getLoaiSanPham());
        contentPanel.add(new JLabel("Loại Sản Phẩm:"));                                                                                                                                                                                        
        contentPanel.add(cboLoai);                                                                                                           
                                                                                                                                                 
        JButton confirmButton = createStyledButton("Xác nhận", primaryColor);                                                                    
        confirmButton.addActionListener(e -> {                                                                                                   
            try {                                                                                                                                
                sanPham.setTenSanPham(tenSPField.getText());                                                                                     
                sanPham.setSoLuong(Integer.parseInt(soLuongField.getText()));                                                                    
                sanPham.setGiaMua(Double.parseDouble(giaMuaField.getText()));                                                                    
                sanPham.setLoaiSanPham(cboLoai.getSelectedItem().toString());                                                                                   
                sanPham.dinhGiaBan();  
                sanPhamDAO.update(sanPham);
                refreshProductsPanel();                                                                                                          
                dialog.dispose();                                                                                                                
                JOptionPane.showMessageDialog(this, "Đã cập nhật sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);           
            } catch (NumberFormatException ex) {     
            	ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng định dạng cho số lượng và giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);    
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });                                                                                                                                      
                                                                                                                                                 
        JButton cancelButton = createStyledButton("Hủy", secondaryColor);                                                                        
        cancelButton.addActionListener(e -> dialog.dispose());                                                                                   
                                                                                                                                                 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));                                                                       
        buttonPanel.add(confirmButton);                                                                                                          
        buttonPanel.add(cancelButton);                                                                                                           
                                                                                                                                                 
        dialog.add(contentPanel, BorderLayout.CENTER);                                                                                           
        dialog.add(buttonPanel, BorderLayout.SOUTH);                                                                                             
                                                                                                                                                 
        dialog.setVisible(true);                                                                                                                 
    }                                                                                                                                            
                                                                                                                                                 
    private void showAddDialog(SanPham sanPham) {                                                                                                
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Số Lượng", true);                                     
        dialog.setLayout(new BorderLayout(10, 10));                                                                                              
        dialog.setSize(300, 200);                                                                                                                
        dialog.setLocationRelativeTo(this);                                                                                                      
                                                                                                                                                 
        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 5, 5));                                                                            
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));                                                                                 
                                                                                                                                                 
        contentPanel.add(new JLabel("Sản phẩm:"));                                                                                               
        contentPanel.add(new JLabel(sanPham.getTenSanPham()));                                                                                   
                                                                                                                                                 
        contentPanel.add(new JLabel("Số lượng hiện tại:"));                                                                                      
        contentPanel.add(new JLabel(String.valueOf(sanPham.getSoLuong())));                                                                      
                                                                                                                                                 
        contentPanel.add(new JLabel("Số lượng thêm:"));                                                                                          
        JTextField quantityField = new JTextField();                                                                                             
        contentPanel.add(quantityField);                                                                                                         
                                                                                                                                                 
        JButton confirmButton = createStyledButton("Xác nhận", primaryColor);                                                                    
        confirmButton.addActionListener(e -> {                                                                                                   
            try {                                                                                                                                
                int quantity = Integer.parseInt(quantityField.getText());
                if (sanPhamDAO.tangSoLuongSanPham(String.valueOf(sanPham.getMaSanPham()), quantity)) {
                	sanPham.setSoLuong(sanPham.getSoLuong() + quantity);                                                                             
                	refreshProductsPanel();                                                                                                          
                	dialog.dispose();                                                                                                                
                	JOptionPane.showMessageDialog(dialog,                                                                                            
                			"Đã thêm " + quantity + " " + sanPham.getTenSanPham() + " vào kho.",                                                         
                			"Thêm thành công",                                                                                                           
                			JOptionPane.INFORMATION_MESSAGE);                                                                                                            	
                }
            } catch (NumberFormatException ex) {   
            	ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog,                                                                                            
                    "Vui lòng nhập số lượng hợp lệ.",                                                                                            
                    "Lỗi",                                                                                                                       
                    JOptionPane.ERROR_MESSAGE);                                                                                                  
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });                                                                                                                                      
                                                                                                                                                 
        JButton cancelButton = createStyledButton("Hủy", secondaryColor);                                                                        
        cancelButton.addActionListener(e -> dialog.dispose());                                                                                   
                                                                                                                                                 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));                                                                       
        buttonPanel.add(confirmButton);                                                                                                          
        buttonPanel.add(cancelButton);                                                                                                           
                                                                                                                                                 
        dialog.add(contentPanel, BorderLayout.CENTER);                                                                                           
        dialog.add(buttonPanel, BorderLayout.SOUTH);                                                                                             
                                                                                                                                                 
        dialog.setVisible(true);                                                                                                                 
    }                                                                                                                                            
                                                                                                                                                 
    private void refreshProductsPanel() {                                                                                                        
        productsPanel.removeAll();                                                                                                               
        for (SanPham sp : danhSachSanPham) {                                                                                                     
            productsPanel.add(createProductCard(sp));                                                                                            
        }                                                                                                                                        
        productsPanel.revalidate();                                                                                                              
        productsPanel.repaint();                                                                                                                 
    }                                                                                                                                            
                                                                                                                                                 
    private JButton createStyledButton(String text, Color bgColor) {                                                                             
        JButton button = new JButton(text);                                                                                                      
        button.setFont(normalFont);                                                                                                              
        button.setForeground(Color.WHITE);                                                                                                       
        button.setBackground(bgColor);                                                                                                           
        button.setBorderPainted(false);                                                                                                          
        button.setFocusPainted(false);                                                                                                           
        button.setPreferredSize(new Dimension(100, 30));                                                                                         
        button.addMouseListener(new MouseAdapter() {                                                                                             
            public void mouseEntered(MouseEvent e) {                                                                                             
                button.setBackground(bgColor.darker());                                                                                          
            }                                                                                                                                    
            public void mouseExited(MouseEvent e) {                                                                                              
                button.setBackground(bgColor);                                                                                                   
            }                                                                                                                                    
        });                                                                                                                                      
        return button;                                                                                                                           
    }                                                                                                                                            
                                                                                                                                                 
    private ImageIcon resizeImage(ImageIcon icon, int width, int height) {                                                                       
        Image img = icon.getImage();                                                                                                             
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);                                                             
        return new ImageIcon(resizedImg);                                                                                                        
    }                                                                                                                                            
                                                                                                                                                 
    private void searchProducts() {                                                                                                              
        String keyword = searchField.getText().trim().toLowerCase();                                                                             
        productsPanel.removeAll();  // Xóa hết các sản phẩm hiện có trên giao diện                                                               
                                                                                                                                                 
        for (SanPham sp : danhSachSanPham) {                                                                                                     
            if (sp.getTenSanPham().toLowerCase().contains(keyword) || String.valueOf(sp.getMaSanPham()).toLowerCase().contains(keyword)) {
                productsPanel.add(createProductCard(sp));                                                                                        
            }                                                                                                                                    
        }                                                                                                                                        
                                                                                                                                                 
        productsPanel.revalidate();                                                                                                              
        productsPanel.repaint();                                                                                                                 
                                                                                                                                                 
        if (productsPanel.getComponentCount() == 0) {                                                                                            
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào!", "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);            
        }                                                                                                                                        
    }                                                                                                                                            
                                                                                                                                                 
    public static void main(String[] args) {                                                                                                     
        SwingUtilities.invokeLater(() -> {                                                                                                       
            try {                                                                                                                                
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());                                                             
            } catch (Exception e) {                                                                                                              
                e.printStackTrace();                                                                                                             
            }                                                                                                                                    
            JFrame frame = new JFrame("Quản Lý Đồ Ăn");                                                                                          
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                                                                                
            frame.setSize(1000, 700);
            try {
                frame.setContentPane(new QuanLyDoAnGUI());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            frame.setLocationRelativeTo(null);                                                                                                   
            frame.setVisible(true);                                                                                                              
        });                                                                                                                                      
    }                                                                                                                                            
}                                                                                                                                                
