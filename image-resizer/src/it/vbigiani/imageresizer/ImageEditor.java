package it.vbigiani.imageresizer;

import java.awt.AlphaComposite;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.sf.image4j.codec.bmp.BMPEncoder;

public class ImageEditor extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button loadb = new Button("Load Local");
	private Button saveb = new Button("Save");
	private FileDialog fd = new FileDialog(this, "Choose an image file", FileDialog.LOAD);
	private FileDialog fd2 = new FileDialog(this, "Save the image", FileDialog.SAVE);
	private ImagePanel id;
	private final JLabel lblNewLabel = new JLabel("Game Type");
	private final JComboBox<GameType> comboBox = new JComboBox<GameType>();
	private final JLabel lblNewLabel_1 = new JLabel("Small Size");
	private final JLabel lblNewLabel_2 = new JLabel("Medium Size");
	private final JLabel lblNewLabel_3 = new JLabel("Large Size");
	private final JTextField[][] values = new JTextField[3][];
	private JScrollPane scrollFrame;
	private final Button btnLoadUrl = new Button("Load URL");

	public ImageEditor() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		comboBox.addActionListener(this);
		getContentPane().add(comboBox, gbc_comboBox);
		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);

		for (int i = 0; i < 3; i++) {
			values[i] = new JTextField[2];
			for (int j = 0; j < 2; j++) {
				JTextField value = new JTextField();
				value.setEditable(false);
				value.setEnabled(false);
				value.setColumns(10);
				values[i][j] = value;

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.anchor = GridBagConstraints.WEST;
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.gridx = j + 1;
				gbc.gridy = i + 1;
				getContentPane().add(value, gbc);
			}
		}
		for (GameType item: GameType.values()) {
			comboBox.addItem(item);
		}
		
		id = new ImagePanel(this);
		
		GridBagConstraints gbc_ip = new GridBagConstraints();
		gbc_ip.gridwidth = 3;
		gbc_ip.anchor = GridBagConstraints.EAST;
		gbc_ip.insets = new Insets(0, 0, 5, 0);
		gbc_ip.gridx = 0;
		gbc_ip.gridy = 4;
		gbc_ip.weightx = 1.0;
		gbc_ip.weighty = 1.0;
		gbc_ip.fill = GridBagConstraints.BOTH;
		scrollFrame = new JScrollPane(id);
		getContentPane().add(scrollFrame, gbc_ip);
		
		GridBagConstraints gbc_btnLoadUrl = new GridBagConstraints();
		gbc_btnLoadUrl.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoadUrl.gridx = 0;
		gbc_btnLoadUrl.gridy = 5;
		getContentPane().add(btnLoadUrl, gbc_btnLoadUrl);
		btnLoadUrl.addActionListener(this);
		
		GridBagConstraints gbc_loadb = new GridBagConstraints();
		gbc_loadb.anchor = GridBagConstraints.EAST;
		gbc_loadb.insets = new Insets(0, 0, 0, 5);
		gbc_loadb.gridx = 0;
		gbc_loadb.gridy = 6;
		getContentPane().add(loadb, gbc_loadb);
		loadb.addActionListener(this);
		
		GridBagConstraints gbc_saveb = new GridBagConstraints();
		gbc_saveb.anchor = GridBagConstraints.WEST;
		gbc_saveb.insets = new Insets(0, 0, 0, 5);
		gbc_saveb.gridx = 1;
		gbc_saveb.gridy = 6;
		getContentPane().add(saveb, gbc_saveb);
		saveb.addActionListener(this);
		
		setSize(600, 400);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void load() {
		fd.setVisible(true);
		File f = new File(fd.getDirectory() + fd.getFile());
		if (f.exists()) {
			id.loadImage(f);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveb) {
			try {
				fd2.setVisible(true);
				BufferedImage product = id.getImage().getSubimage(id.getImgX(), id.getImgY(), id.getImgW(), id.getImgH());
				String fname = fd2.getFile();
				fname = Pattern.compile(".bmp", Pattern.CASE_INSENSITIVE | Pattern.LITERAL).matcher(fname).replaceAll("");
				save(fd2.getDirectory(), fname, "S", values[0], product);
				save(fd2.getDirectory(), fname, "M", values[1], product);
				save(fd2.getDirectory(), fname, "L", values[2], product);
			} catch (Exception ex) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		} else if (e.getSource() == loadb) {
			load();
		} else if (e.getSource() == btnLoadUrl) {
			String url = JOptionPane.showInputDialog(this, "URL: ");
			try {
				id.loadImage(new URL(url));
			} catch (MalformedURLException ex) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		} else if (e.getSource() == comboBox) {
			GameType gt = (GameType) comboBox.getSelectedItem();
			for (int i = 0; i < values.length; i++) {
				JTextField[] tfs = values[i];
				for (int j = 0; j < tfs.length; j++) {
					JTextField tf = tfs[j];
					if (gt.isEditable()) {
						tf.setEnabled(true);
						tf.setEditable(true);
						tf.setText("");
					} else {
						tf.setEnabled(false);
						tf.setEditable(false);
						tf.setText("" + gt.getValues()[i][j]);
					}
				}
			}
		}
	}
	
	private void save(String directory, String fname, String suffix, JTextField[] jTextFields, BufferedImage image) {
		try {
			File file = new File(directory, fname + suffix + ".bmp");
			int width = Integer.parseInt(jTextFields[0].getText());
			int height = Integer.parseInt(jTextFields[1].getText());
			BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
			Graphics2D g = resizedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setComposite(AlphaComposite.Src);
			g.drawImage(image, 0, 0, width, height, null);
			g.dispose();
			FileOutputStream fos = new FileOutputStream(file);
			BMPEncoder.write(resizedImage, fos);
			fos.close();
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public double getRatio() {
		try {
			int maxX = 0;
			int maxY = 0;
			for (JTextField[] tf: values) {
				String xString = tf[0].getText();
				String yString = tf[1].getText();
				maxX = maxX < Integer.parseInt(xString) ? Integer.parseInt(xString) : maxX;
				maxY = maxY < Integer.parseInt(yString) ? Integer.parseInt(yString) : maxY;
			}
			return ((double) maxX) / ((double) maxY);
		} catch (NumberFormatException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Please fill all possible output formats!");
			return 0;
		}
	}
}