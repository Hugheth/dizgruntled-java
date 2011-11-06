package com.hugheth.dizgruntled.ui;

import javax.swing.JFrame;

import java.awt.Component;

import javax.swing.JPanel;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JCheckBoxMenuItem;

@SuppressWarnings("serial")
public class EditorFrame extends JFrame {
	
	private JTextField nameField;
	private JTextField authorField;
	private JTextField websiteField;
	private JTextField textField;
	private JTextField textField_1;
	private JTable table;
	private JTextField textField_2;
	
	public EditorFrame() {
		setTitle("dizGruntled Level Editor");
		setSize(567, 700);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JLabel lblStandardDefault = new JLabel("standard default 1");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(452, Short.MAX_VALUE)
					.addComponent(lblStandardDefault)
					.addContainerGap())
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblStandardDefault)
					.addGap(6))
		);
		
		JPanel levelTab = new JPanel();
		tabbedPane.addTab("Level", null, levelTab, null);
		
		JLabel lblName = new JLabel("Name");
		
		nameField = new JTextField();
		nameField.setColumns(10);
		
		JLabel lblAuthor = new JLabel("Author");
		
		authorField = new JTextField();
		authorField.setColumns(10);
		
		JLabel lblWebsite = new JLabel("Website");
		
		websiteField = new JTextField();
		websiteField.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description");
		
		JLabel lblPlayers = new JLabel("Players");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JLabel lblRooms = new JLabel("Rooms");
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JLabel lblStartXY = new JLabel("Start XY");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		
		JButton btnEdit = new JButton("Edit");
		
		JButton btnRemove = new JButton("Remove");
		
		JButton button = new JButton("Add");
		
		JButton button_1 = new JButton("Edit");
		
		JButton button_2 = new JButton("Remove");
		GroupLayout gl_levelTab = new GroupLayout(levelTab);
		gl_levelTab.setHorizontalGroup(
			gl_levelTab.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_levelTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_levelTab.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(nameField, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(authorField, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(websiteField, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(lblName, Alignment.LEADING)
						.addComponent(lblAuthor, Alignment.LEADING)
						.addComponent(lblWebsite, Alignment.LEADING)
						.addComponent(lblDescription, Alignment.LEADING)
						.addComponent(lblPlayers, Alignment.LEADING)
						.addComponent(scrollPane_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(lblRooms, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_levelTab.createSequentialGroup()
							.addComponent(btnAdd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEdit, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRemove))
						.addGroup(Alignment.LEADING, gl_levelTab.createSequentialGroup()
							.addGroup(gl_levelTab.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(textField, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addComponent(lblStartXY, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_levelTab.createSequentialGroup()
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_1, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_levelTab.setVerticalGroup(
			gl_levelTab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_levelTab.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAuthor)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(authorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblWebsite)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(websiteField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDescription)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPlayers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_levelTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnEdit)
						.addComponent(btnRemove))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblRooms)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_levelTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(button)
						.addComponent(button_1)
						.addComponent(button_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblStartXY)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_levelTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(44, Short.MAX_VALUE))
		);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Home"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane_2.setViewportView(list);
		
		JList playersList = new JList();
		scrollPane_1.setViewportView(playersList);
		playersList.setModel(new AbstractListModel() {
			
			final JLabel myLabel = new JLabel("Local [Human]", new ImageIcon("iconz/colourRed.png"), 0);
			final JLabel myLabel2 = new JLabel("Remote", new ImageIcon("iconz/colourRed.png"), 0);
			
			JLabel[] values = new JLabel[] {myLabel, myLabel2};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		playersList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel myLabel = (JLabel) value;
				JLabel label = (JLabel) super.getListCellRendererComponent(list, myLabel.getText(), index, isSelected, cellHasFocus);
				label.setIcon(myLabel.getIcon());
				return label;
			}
		});
		
		JTextArea aboutField = new JTextArea();
		scrollPane.setViewportView(aboutField);
		aboutField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		aboutField.setLineWrap(true);
		levelTab.setLayout(gl_levelTab);
		
		JPanel tileTab = new JPanel();
		tabbedPane.addTab("Tilez", null, tileTab, null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Foreground", "Active", "Landscape", "Background"}));
		
		JLabel lblTileset = new JLabel("Tile Set");
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Standard"}));
		
		JButton btnEdit_1 = new JButton("...");
		
		JLabel lblLayer = new JLabel("Layer");
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		
		JLabel lblBrush = new JLabel("Brush");
		
		JSlider slider = new JSlider();
		slider.setMaximum(10);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(1);
		GroupLayout gl_tileTab = new GroupLayout(tileTab);
		gl_tileTab.setHorizontalGroup(
			gl_tileTab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tileTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tileTab.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
						.addComponent(comboBox, Alignment.TRAILING, 0, 204, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_tileTab.createSequentialGroup()
							.addComponent(comboBox_1, 0, 153, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEdit_1))
						.addComponent(slider, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBrush)
						.addComponent(lblLayer)
						.addComponent(lblTileset))
					.addContainerGap())
		);
		gl_tileTab.setVerticalGroup(
			gl_tileTab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tileTab.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLayer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTileset)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tileTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEdit_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblBrush)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Paint", null, panel_1, null);
		
		JList list_1 = new JList();
		list_1.setModel(new AbstractListModel() {
			String[] values = new String[] {"Default", "Alternate", "Hill", "Invert Hill", "Water", "Death", "Stairs", "Fill Default", "Fill Water", "Fill Death", "Insert Row", "Insert Column", "Delete Row", "Delete Column"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		tabbedPane_1.addTab("Smart", null, list_1, null);
		tileTab.setLayout(gl_tileTab);
		
		JLabel lblLogic = new JLabel("Logic");
		JPanel objectTab = new JPanel();
		tabbedPane.addTab("Objectz", null, objectTab, null);
		
		JLabel lblProperties = new JLabel("Properties");
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		JLabel lblTemplate = new JLabel("Template");
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"Secret"}));
		
		JButton btnUse = new JButton("Use");
		
		JButton btnRemove_1 = new JButton("Remove");
		
		JButton btnUpdate = new JButton("Update");
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("E:\\Workspace\\Dizgruntled\\iconz\\colourRed.png"));
		
		JLabel lblTransform = new JLabel("Transform");
		
		JButton btnClone = new JButton("Clone");
		GroupLayout gl_objectTab = new GroupLayout(objectTab);
		gl_objectTab.setHorizontalGroup(
			gl_objectTab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_objectTab.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_objectTab.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
						.addGroup(gl_objectTab.createSequentialGroup()
							.addGroup(gl_objectTab.createParallelGroup(Alignment.LEADING)
								.addComponent(lblLogic)
								.addComponent(lblProperties))
							.addContainerGap(172, Short.MAX_VALUE))
						.addGroup(gl_objectTab.createSequentialGroup()
							.addGroup(gl_objectTab.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTemplate)
								.addComponent(comboBox_2, 0, 212, Short.MAX_VALUE)
								.addGroup(gl_objectTab.createSequentialGroup()
									.addComponent(btnUse, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnUpdate)
									.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
									.addComponent(btnRemove_1)))
							.addGap(9))
						.addGroup(gl_objectTab.createSequentialGroup()
							.addComponent(label)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_objectTab.createSequentialGroup()
							.addComponent(lblTransform)
							.addContainerGap(175, Short.MAX_VALUE))
						.addComponent(btnClone)))
		);
		gl_objectTab.setVerticalGroup(
			gl_objectTab.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_objectTab.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLogic)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_objectTab.createParallelGroup(Alignment.TRAILING)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblProperties)
					.addGap(8)
					.addComponent(scrollPane_3, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTemplate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_objectTab.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnUse)
						.addComponent(btnRemove_1)
						.addComponent(btnUpdate))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTransform)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnClone)
					.addContainerGap(173, Short.MAX_VALUE))
		);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Property", "Value"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane_3.setViewportView(table);
		objectTab.setLayout(gl_objectTab);
		getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnLevel = new JMenu("File");
		menuBar.add(mnLevel);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnLevel.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnLevel.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnLevel.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mnLevel.add(mntmSaveAs);
		
		JMenuItem mntmRename = new JMenuItem("Rename");
		mnLevel.add(mntmRename);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		mnEdit.add(mntmRedo);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JCheckBoxMenuItem chckbxmntmGrid = new JCheckBoxMenuItem("Grid");
		mnView.add(chckbxmntmGrid);
		
		JCheckBoxMenuItem chckbxmntmBackground = new JCheckBoxMenuItem("Background");
		mnView.add(chckbxmntmBackground);
		
		JCheckBoxMenuItem chckbxmntmShowLandscape = new JCheckBoxMenuItem("Landscape");
		mnView.add(chckbxmntmShowLandscape);
		
		JCheckBoxMenuItem chckbxmntmActive = new JCheckBoxMenuItem("Active");
		mnView.add(chckbxmntmActive);
		
		JCheckBoxMenuItem chckbxmntmForeground = new JCheckBoxMenuItem("Foreground");
		mnView.add(chckbxmntmForeground);
		
	}
}
