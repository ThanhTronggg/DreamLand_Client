package iuh.fit.gui.app.phim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Phim; 
import net.miginfocom.swing.MigLayout;

public class ChiTietPhimDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel container;
    private JPanel leftContainer;
    private JPanel rightContainer;
    private JLabel movieName;
    private JLabel movieImage;
    private JPanel leftTopContainer;
    private JPanel leftBottomContainer;
    private JPanel durationContainer;
    private JLabel durationLabel;
    private JLabel duration;
    private JPanel rightMainContainer;
    private JPanel directorContainer;
    private JLabel directorLabel;
    private JLabel director;
    private JPanel statusContainer;
    private JLabel statusLabel;
    private JLabel status;
    private JPanel languageContainer;
    private JLabel languageLabel;
    private JLabel language;
    private JPanel countryContainer;
    private JLabel countryLabel;
    private JLabel country;
    private JPanel genreContainer;
    private JLabel genreLabel;
    private JLabel genre;
    private JPanel releasedDateContainer;
    private JLabel releasedDateLabel;
    private JLabel releasedDate;
    private JPanel startDateContainer;
    private JLabel startDateLabel;
    private JLabel startDate;
    private JPanel importPriceContainer;
    private JLabel importPriceLabel;
    private JLabel importPrice;
    private JPanel trailerContainer;
    private JLabel trailerLabel;
    private JTextField trailer;
    private JPanel descriptionContainer;
    private JLabel descriptionLabel;
    private JLabel description;
    private String labelStyles;
    private JButton closeButton;

    public ChiTietPhimDialog(Phim phim) {
        if (phim == null) {
            // Xử lý trường hợp phim là null, ví dụ: hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(this, "Không thể hiển thị chi tiết phim vì dữ liệu không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        setLayout(new MigLayout());
        container = new JPanel(new MigLayout("wrap, fill", "[fill][fill][fill]", "[fill][]"));
        leftContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[grow 0][fill]"));
        leftTopContainer = new JPanel(new MigLayout("wrap, fill", "[center]", "[fill]"));
        leftBottomContainer = new JPanel(new MigLayout("wrap, fillx", "[fill]", "[]"));
        movieName = new JLabel(phim.getTenPhim());
        leftContainer.add(leftTopContainer);
        leftContainer.add(leftBottomContainer);

        leftTopContainer.add(movieName);

        ImageIcon icon = new ImageIcon(phim.getAnh());
        if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
            icon = new ImageIcon("images/movie-poster-not-found.jpg");
        }
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(300, -1, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImg);
        movieImage = new JLabel(resizedIcon);

        leftBottomContainer.add(movieImage);

        rightContainer = new JPanel(new MigLayout("wrap, fill", "[fill]", "[fill]"));
        rightMainContainer = new JPanel(new MigLayout("wrap, fillx", "[][]", ""));
        rightContainer.add(rightMainContainer);

        // Thời lượng
        durationContainer = new JPanel(new MigLayout("", "[][]", ""));
        durationLabel = new JLabel("Thời gian: ");
        duration = new JLabel(phim.getThoiLuong() + " phút");
        durationContainer.add(durationLabel);
        durationContainer.add(duration);

        // Đạo diễn
        directorContainer = new JPanel(new MigLayout("", "[][]", ""));
        directorLabel = new JLabel("Đạo diễn: ");
        director = new JLabel(phim.getDaoDien());
        directorContainer.add(directorLabel);
        directorContainer.add(director);

        // Trạng thái
        statusContainer = new JPanel(new MigLayout("", "[][]", ""));
        statusLabel = new JLabel("Trạng thái: ");
        status = new JLabel(phim.getTrangThai());
        statusContainer.add(statusLabel);
        statusContainer.add(status);

        // Ngôn ngữ
        languageContainer = new JPanel(new MigLayout("", "[][]", ""));
        languageLabel = new JLabel("Ngôn ngữ: ");
        language = new JLabel(phim.getNgonNgu());
        languageContainer.add(languageLabel);
        languageContainer.add(language);

        // Quốc gia
        countryContainer = new JPanel(new MigLayout("", "[][]", ""));
        countryLabel = new JLabel("Quốc gia: ");
        country = new JLabel(phim.getQuocGia());
        countryContainer.add(countryLabel);
        countryContainer.add(country);

        // Thể loại
        genreContainer = new JPanel(new MigLayout("", "[][]", ""));
        genreLabel = new JLabel("Thể loại: ");
        genre = new JLabel(phim.getTheLoai());
        genreContainer.add(genreLabel);
        genreContainer.add(genre);

        // Ngày công chiếu
        releasedDateContainer = new JPanel(new MigLayout("", "[][]", ""));
        releasedDateLabel = new JLabel("Ngày công chiếu: ");
        releasedDate = new JLabel(phim.getNgayCongChieu().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        releasedDateContainer.add(releasedDateLabel);
        releasedDateContainer.add(releasedDate);

        // Ngày bắt đầu chiếu
        startDateContainer = new JPanel(new MigLayout("", "[][]", ""));
        startDateLabel = new JLabel("Ngày bắt đầu: ");
        startDate = new JLabel(phim.getNgayBatDau().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        startDateContainer.add(startDateLabel);
        startDateContainer.add(startDate);

        // Giá thầu
        importPriceContainer = new JPanel(new MigLayout("", "[][]", ""));
        importPriceLabel = new JLabel("Giá thầu: ");
        importPrice = new JLabel(new DecimalFormat("#0.00").format(phim.getGiaThau()) + " VND");
        importPriceContainer.add(importPriceLabel);
        importPriceContainer.add(importPrice);

        // Đoạn phim giới thiệu
        trailerContainer = new JPanel(new MigLayout("", "[][]", ""));
        trailerLabel = new JLabel("Đoạn giới thiệu: ");
        trailer = new JTextField(phim.getTrailer());
        trailer.setEditable(false);
        trailer.setBorder(null);
        trailerContainer.add(trailerLabel);
        trailerContainer.add(trailer);

     // Tóm tắt
        descriptionContainer = new JPanel(new MigLayout("wrap, fill", "[]", "[][]"));
        descriptionLabel = new JLabel("Tóm tắt: ");
        description = new JLabel(
                "<html>" + wrapTextEveryNWords(phim.getTomTat(), 8).replaceAll("\\n", "<br>") + "</html>");

        // Bọc description trong một JScrollPane
        JScrollPane descriptionScrollPane = new JScrollPane(description);
        descriptionScrollPane.setPreferredSize(new Dimension(400, 150)); // Tăng kích thước tối đa cho JScrollPane
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Thêm các thành phần vào descriptionContainer
        descriptionContainer.add(descriptionLabel);
        descriptionContainer.add(descriptionScrollPane); // Thêm JScrollPane thay vì JLabel

        // Thêm descriptionContainer vào rightMainContainer
        rightMainContainer.add(descriptionContainer);

        // Thêm các thành phần vào panel bên phải
        rightMainContainer.add(durationContainer);
        rightMainContainer.add(directorContainer);
        rightMainContainer.add(statusContainer);
        rightMainContainer.add(languageContainer);
        rightMainContainer.add(countryContainer);
        rightMainContainer.add(genreContainer);
        rightMainContainer.add(releasedDateContainer);
        rightMainContainer.add(startDateContainer);
        rightMainContainer.add(importPriceContainer, "wrap");
        rightMainContainer.add(trailerContainer, "span 2");
        rightMainContainer.add(descriptionContainer);

        // Áp dụng kiểu dáng
        movieName.putClientProperty(FlatClientProperties.STYLE, "font:$h3.font; foreground:$primary");
        labelStyles = "font:$p.font";
        durationLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        directorLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        statusLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        languageLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        countryLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        genreLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        releasedDateLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        startDateLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        importPriceLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        trailerLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);
        descriptionLabel.putClientProperty(FlatClientProperties.STYLE, labelStyles);

     // Tạo và thêm nút đóng vào container mới
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.add(closeButton = new JButton("Đóng"));
        closeButton.addActionListener(e -> dispose());  // Đóng dialog khi nút được nhấn
        
     // Thiết lập màu sắc và kích thước cho nút
        closeButton.setBackground(new Color(255, 0, 0)); // Màu nền đỏ
        closeButton.setForeground(Color.WHITE); // Màu chữ trắng
        closeButton.setPreferredSize(new Dimension(100, 40)); // Kích thước nút
        closeButton.setFont(new Font("Arial", Font.BOLD, 16)); // Kiểu chữ in đậm
        buttonContainer.add(closeButton);
        
     // Thêm container vào dialog
        container.add(leftContainer, "grow");
        container.add(rightContainer, "grow");
        add(container, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.SOUTH);  // Đặt nút ở phía dưới bên phải

        pack();
        setLocationRelativeTo(null);
    }

    private static String wrapTextEveryNWords(String text, int n) {
        StringBuilder wrappedText = new StringBuilder();
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (i % n == 0 && i != 0) {
                wrappedText.append("\n");
            }
            wrappedText.append(words[i]).append(" ");
        }
        return wrappedText.toString().trim();
    }
}
