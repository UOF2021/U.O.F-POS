package theater;

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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PosGUIMenuEditTheaterPanel extends JPanel implements ActionListener{

	private CardLayout cards;
	private JFrame frame;
	private JTabbedPane tp;
	private File file;
	private JButton backButton;
	private TheaterInformation info;
	

	// ī�װ� �޴� ����
	private JButton addCategoryButton;
	private JButton deleteCategoryButton;
	private JButton saveCategoryButton;
	
	// ī�װ� ����
	private ArrayList<TheaterCategory> theaterCategory;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	public PosGUIMenuEditTheaterPanel(CardLayout cards, JFrame frame) {
		this.cards = cards;
		this.frame = frame;		
		this.file = new File("information_theater.json");
		initTabbedPane();
		
		// ī�װ� �߰� ��ư
		addCategoryButton = new JButton("ī�װ� �߰�");
		addCategoryButton.addActionListener(this);
		
		// ī�װ� ���� ��ư
		saveCategoryButton = new JButton("ī�װ� ����");
		saveCategoryButton.addActionListener(this);
		
		// ī�װ� ���� ��ư
		deleteCategoryButton = new JButton("ī�װ� ����");
		deleteCategoryButton.addActionListener(this);
				
		// backButton
		backButton = new JButton("back");
		backButton.addActionListener(this);
		
		this.add(tp);
		this.add(addCategoryButton);
		this.add(saveCategoryButton);
		this.add(deleteCategoryButton);
		this.add(backButton);
	}

	private void initTabbedPane() {
		tp = new JTabbedPane();
		info = new TheaterInformation();
		theaterCategory = new ArrayList<TheaterCategory>();
		
		// ��ȭ ������
		JSONArray movieList =  (JSONArray)info.getMovieList();
		for(int i=0; i<movieList.size(); i++) {
			JSONObject movie = (JSONObject)movieList.get(i);
			theaterCategory.add(new TheaterCategory(movie, i));	
			tp.add(theaterCategory.get(i), theaterCategory.get(i).getMovieName());
		}
	}
	
	private void updateCategory() {
		tp.removeAll();
		info.update();
		theaterCategory.clear();
		
		// ��ȭ �߰�
		JSONArray movieList =  (JSONArray)info.getMovieList();
		for(int i=0; i<movieList.size(); i++) {
			JSONObject movie = (JSONObject)movieList.get(i);
			theaterCategory.add(new TheaterCategory(movie, i));	
			tp.add(theaterCategory.get(i), theaterCategory.get(i).getMovieName());
		}
		
		// ��Ʈ ī�װ� �߰�
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e.getSource()==backButton) {
				cards.show(frame.getContentPane(),"mainPanel");
		} else if(e.getSource() == addCategoryButton) {
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader(file));
				JSONArray target_category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("movie_list");
				
				// ���� ������ ����
				JSONObject inputData = new JSONObject();
				JSONArray seat_list = new JSONArray();
				
				inputData.put("movie", "");
				inputData.put("time", "");
				inputData.put("theater", "");
				inputData.put("width", "");
				inputData.put("height", "");
				inputData.put("seat_list", seat_list);
				// ī�װ�����Ʈ, ��Ʈ����Ʈ �߰��ؾߵ�.
				
				
				
				// ����
				target_category_list2.add(inputData);
				FileWriter writer = new FileWriter(file);
				writer.write(whole.toJSONString());
				writer.flush();
				writer.close();
				updateCategory();
			} catch (Exception e1) {
				e1.printStackTrace();
			}		
		} else if(e.getSource() == deleteCategoryButton) {
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader(file));
				JSONArray category_list2 = (JSONArray)((JSONObject)whole.get("message")).get("movie_list");
				category_list2.remove(tp.getSelectedIndex());
				
				// ����
				FileWriter writer = new FileWriter(file);
				writer.write(whole.toJSONString());
				writer.flush();
				
				updateCategory();
				writer.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
		} else if(e.getSource() == saveCategoryButton) {
			try {
				JSONParser parser = new JSONParser();
				JSONObject whole = (JSONObject)parser.parse(new FileReader(file));
				
				JSONObject message = (JSONObject)whole.get("message");
				JSONArray movie_list = (JSONArray)message.get("movie_list");

				movie_list.set(tp.getSelectedIndex(),theaterCategory.get(tp.getSelectedIndex()).getMovie());				

				FileWriter writer = new FileWriter(file);
				writer.write(whole.toJSONString());
				writer.flush();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}	
}
