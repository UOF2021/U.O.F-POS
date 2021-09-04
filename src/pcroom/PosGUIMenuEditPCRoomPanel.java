package pcroom;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import restaurant.RestaurantMenu;

public class PosGUIMenuEditPCRoomPanel extends JPanel implements ActionListener{

	private CardLayout cards;
	private JFrame frame;
	private JTabbedPane tp;
	private PCRoomInformation info;
	private JButton backButton;
	
	// ī�װ� �޴� ����
	private JSONArray category_list;
	private JButton addCategoryButton;
	private JButton renameCategoryButton;
	private JTextField renameCategoryTF;
	private JButton deleteCategoryButton;
	
	// �޴� �߰� ����
	private JButton addMenuButton;
	private JDialog addMenuDialog;
	private JButton imgChooseButton;
	private JButton menuSaveButton;
	private JFileChooser fc;
	private JLabel chooseFoodLabel;
	private JLabel nameLabel;
	private JTextField nameTF;
	private JLabel priceLabel;
	private JTextField priceTF;
	private JLabel descLabel;
	private JTextField descriptionTF;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	public PosGUIMenuEditPCRoomPanel(CardLayout cards, JFrame frame) {
		this.cards = cards;
		this.frame = frame;

		initTabbedPane();
		insertCategoryIntoTabbedPane();
		
		// ī�װ� �̸� ���� �г�
		JPanel renameCategoryPanel = new JPanel();
		JLabel renameCategoryLabel = new JLabel("ī�װ� �̸�");
		renameCategoryTF = new JTextField(10);
		renameCategoryButton = new JButton("����");
		renameCategoryButton.addActionListener(this);
		renameCategoryPanel.add(renameCategoryLabel);
		renameCategoryPanel.add(renameCategoryTF);
		renameCategoryPanel.add(renameCategoryButton);
		
		// ī�װ� �߰� ��ư
		addCategoryButton = new JButton("ī�װ� �߰�");
		addCategoryButton.addActionListener(this);
		
		// ī�װ� ���� ��ư
		deleteCategoryButton = new JButton("ī�װ� ����");
		deleteCategoryButton.addActionListener(this);
		
		// �޴� �߰� ��ư
		addMenuButton = new JButton("�޴��߰�");
		addMenuButton.addActionListener(this);
		
		// backButton
		backButton = new JButton("back");
		backButton.addActionListener(this);
		
		this.add(tp);
		this.add(renameCategoryPanel);
		this.add(addCategoryButton);
		this.add(deleteCategoryButton);
		this.add(addMenuButton);
		this.add(backButton);
	}

	private void initTabbedPane() {
		info = new PCRoomInformation();
		tp = new JTabbedPane();
	}
	
	private void insertCategoryIntoTabbedPane() {
		// ī�װ� �߰�
		category_list = info.getCategoryList();
		for(int i=0; i<category_list.size(); i++) {
			JSONObject category = (JSONObject)category_list.get(i);
			PCRoomCategory pcRoomCategory = new PCRoomCategory(category);	
			JSONArray product_list = (JSONArray)category.get("product_list");
			
			// ī�װ� �ȿ� �޴� �߰�
			for(int j=0; j<product_list.size(); j++) {
				JSONObject menu = (JSONObject) product_list.get(j);
				PCRoomMenu menuPanel = new PCRoomMenu(menu, j);
				menuPanel.addMouseListener(new MouseAdapter() { // ���� Ŭ�� �� ����
					public void mouseClicked(MouseEvent e) {
						PCRoomMenu delete_target = (PCRoomMenu)e.getSource();
						int menuIndex = delete_target.getIndex();
						int categoryIndex = tp.getSelectedIndex();
						deleteMenu(categoryIndex, menuIndex);
					}
				});
				pcRoomCategory.add(menuPanel);
			}
			tp.add(pcRoomCategory, pcRoomCategory.getCategoryName());
		}
	}
	
	private void deleteMenu(int categoryIndex, int menuIndex) { // �޴� ����
		try {
			JSONParser parser = new JSONParser();
			JSONObject whole = (JSONObject)parser.parse(new FileReader("information_pcroom.json"));
			JSONArray category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("category_list");
			JSONObject category = (JSONObject)category_list2.get(categoryIndex);
			JSONArray product_list = (JSONArray)category.get("product_list");
			
			// ����
			product_list.remove(menuIndex);
			FileWriter writer = new FileWriter("information_pcroom.json");
			writer.write(whole.toJSONString());
			writer.flush();
			
			updateCategory();
			writer.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void updateCategory() {
		tp.removeAll();
		
		info.update();
		// ī�װ� �߰�
		category_list = info.getCategoryList();
		for(int i=0; i<category_list.size(); i++) {
			JSONObject category = (JSONObject)category_list.get(i);
			PCRoomCategory pcRoomCategory = new PCRoomCategory(category);	
			JSONArray product_list = (JSONArray)category.get("product_list");
			
			// ī�װ� �ȿ� �޴� �߰�
			for(int j=0; j<product_list.size(); j++) {
				JSONObject menu = (JSONObject) product_list.get(j);
				PCRoomMenu menuPanel = new PCRoomMenu(menu, j);
				menuPanel.addMouseListener(new MouseAdapter() { // ���� Ŭ�� �� ����
					public void mouseClicked(MouseEvent e) {
						RestaurantMenu delete_target = (RestaurantMenu)e.getSource();
						int menuIndex = delete_target.getIndex();
						int categoryIndex = tp.getSelectedIndex();
						deleteMenu(categoryIndex, menuIndex);
					}
				});
				pcRoomCategory.add(menuPanel);
			}
			tp.add(pcRoomCategory, pcRoomCategory.getCategoryName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource()==backButton) { // Back ��ư Ŭ��
				cards.show(frame.getContentPane(),"mainPanel");
		} else if(e.getSource() == addMenuButton) { // �޴� �߰� ��ư Ŭ��
			addMenuDialog = new JDialog(frame, "�޴� �߰�", true);
			addMenuDialog.setSize(300,500);
			addMenuDialog.setLayout(new GridLayout(9,1));
			
			chooseFoodLabel = new JLabel();
			imgChooseButton = new JButton("Open");
			menuSaveButton = new JButton("Save");
			nameLabel = new JLabel("�̸�");
			nameTF = new JTextField(10);
			priceLabel = new JLabel("����");
			priceTF = new JTextField(10);
			descLabel = new JLabel("����");
			descriptionTF = new JTextField(10);
			imgChooseButton.addActionListener(this);
			menuSaveButton.addActionListener(this);
			
			addMenuDialog.add(chooseFoodLabel);
			addMenuDialog.add(imgChooseButton);
			addMenuDialog.add(nameLabel);
			addMenuDialog.add(nameTF);
			addMenuDialog.add(priceLabel);
			addMenuDialog.add(priceTF);
			addMenuDialog.add(descLabel);
			addMenuDialog.add(descriptionTF);
			addMenuDialog.add(menuSaveButton);
			
			addMenuDialog.setVisible(true);				
		} else if(e.getSource() == imgChooseButton) { // �޴� �߰� ���̾�α׿��� ���� ���ù�ư
			fc = new JFileChooser();
			int i= fc.showOpenDialog(this);
			if(i==JFileChooser.APPROVE_OPTION) { 
				File f = fc.getSelectedFile();
				String filepath = f.getPath();				
				ImageIcon icon = new ImageIcon(filepath);
				chooseFoodLabel.setIcon(icon);
			}
		} else if(e.getSource() == menuSaveButton) { // �޴� �߰� ���̾�α׿��� �����ư
			String name = nameTF.getText();
			String price = priceTF.getText();
			String description = descriptionTF.getText();
//			String[] arg = fc.getSelectedFile().toString().split("\\\\");
			String img_url = fc.getSelectedFile().toString();
			// json�� ����
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader("information_pcroom.json"));
				JSONArray category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("category_list");
				JSONObject category = (JSONObject)category_list2.get(tp.getSelectedIndex());
				JSONArray target_product_list = (JSONArray)category.get("product_list");
				
				// ���� ������ ����
				JSONObject inputData = new JSONObject();
				inputData.put("name", name);
				inputData.put("price", Long.parseLong(price));
				inputData.put("desc", description);
				inputData.put("image", img_url);
				
				// ����
				target_product_list.add(inputData);
				FileWriter writer = new FileWriter("information_pcroom.json");
				writer.write(whole.toJSONString());
				writer.flush();
				writer.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateCategory();
			addMenuDialog.dispose();
		} else if(e.getSource() == addCategoryButton) { // ī�װ� + ��ư
			// json ���Ͽ� �� �ڸ� ����
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader("information_pcroom.json"));
				JSONArray target_category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("category_list");
				
				// ���� ������ ����
				JSONObject inputData = new JSONObject();
				JSONArray product_list = new JSONArray();
				inputData.put("category", "�߰�");
				inputData.put("product_list", product_list);
				
				// ����
				target_category_list2.add(inputData);
				FileWriter writer = new FileWriter("information_pcroom.json");
				writer.write(whole.toJSONString());
				writer.flush();
				writer.close();
				updateCategory();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == renameCategoryButton) { // ī�װ� �̸� ����
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader("information_pcroom.json"));
				JSONArray category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("category_list");
				JSONObject target_category = (JSONObject)category_list2.get(tp.getSelectedIndex());
				
				// ���� ������ ����
				String inputData = (String)renameCategoryTF.getText();
				
				// ����
				target_category.replace("category", inputData);
				FileWriter writer = new FileWriter("information_pcroom.json");
				writer.write(whole.toJSONString());
				writer.flush();
				
				updateCategory();
				writer.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == deleteCategoryButton) {
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader("information_pcroom.json"));
				JSONArray category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("category_list");
				category_list2.remove(tp.getSelectedIndex());
				
				// ����
				FileWriter writer = new FileWriter("information_pcroom.json");
				writer.write(whole.toJSONString());
				writer.flush();
				
				updateCategory();
				writer.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}	
}
