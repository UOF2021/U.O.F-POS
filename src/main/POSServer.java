package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class POSServer extends Thread{
	
	public final static int PORT=10003;
	
	@Override
	public void run() {
		try(ServerSocket server = new ServerSocket(PORT)){
			while(true) {
				Socket connection = server.accept();
				new POSThread(connection).start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

class POSThread extends Thread{
	private Socket connection;
	private BufferedReader reader;
	private PrintWriter writer;
	private JSONParser parser;
	
	POSThread(Socket connection){
		this.connection = connection;
	}
	
	@Override
	public void run() {
		System.out.println("���� �Ϸ�");	
		try {
			parser = new JSONParser();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"EUC-KR"));
			writer = new PrintWriter(connection.getOutputStream(), true);	

			System.out.println("���� �б� ����");
			String line;
			while(true) {
				line = reader.readLine();
				if(line==null)
					continue;
				JSONObject obj = (JSONObject)parser.parse(line);
				String code = (String) obj.get("request_code");
				System.out.println(code);
				System.out.println("���� �б� ��");
				
				switch(code) {
				case "0000":
					code0000();
					break;
				case "0011":
					code0011(obj); // �ֹ� ��û
					break;
				case "0012":
					code0012(); // �ֹ� ���
					break;
				case "0014": // �������� �� �ֹ����ɸ��
					code0014();
					break;
				case "0015": // QR�ڵ� �̹��� ��û
					code0015();
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void code0000() {}
	
	private void code0011(JSONObject obj) { // �ֹ� ��û
		JSONObject message = (JSONObject)obj.get("message");
		JSONObject cardJSON = (JSONObject) message.get("card");
		String card_num = (String) cardJSON.get("num");
		String card_cvc = (String) cardJSON.get("cvc");
		String card_pw = (String) cardJSON.get("pw");
		String card_due_date = (String) cardJSON.get("due_date");
		JSONObject orderJSON = (JSONObject) message.get("order");
		String order_name = (String) orderJSON.get("name");
		String order_count = (String) orderJSON.get("count");
		
		// �ʿ��� �͵�
		String id = (String) message.get("id");
		String order_number = (String) message.get("android_key_value");
		Card card = new Card(card_num, card_pw, card_cvc, card_due_date);
	}

	private void code0012() { // �ֹ� ���
		
	}

	
	public void code0014() throws FileNotFoundException, IOException, ParseException { // �������� �� �ֹ����ɸ��
		JSONObject whole = (JSONObject)(parser.parse(new FileReader("information_theater.json")));
		JSONArray categroy_list = ((JSONArray)((JSONObject)whole.get("message")).get("category_list"));
		
		// �̹��� �ּ� -> �̹��� ������ ����
		for(int i=0;i<categroy_list.size();i++) {
			JSONObject category = (JSONObject)categroy_list.get(i);
			JSONArray product_list = (JSONArray)category.get("product_list");
			for(int j=0; j<product_list.size(); j++) {
				JSONObject product = (JSONObject)product_list.get(j);
				String image = (String)product.get("image");
				File QRImage = new File(image);
				byte[] imageBytes = ImgEncoder.extractBytes("QRCODE.jpg");
				byte[] baseEncodingBytes = ImgEncoder.encodingBase64(imageBytes);
				String  encodedImg = new String(baseEncodingBytes);
				product.replace("image", encodedImg);
			}
		}
		writer.println(whole.toJSONString());
	}
	
	private void code0015() throws IOException {
		JSONObject data = new JSONObject();		
		JSONObject message = new JSONObject();
		File QRImage = new File("QRCODE.jpg");
		
		byte[] imageBytes = ImgEncoder.extractBytes("QRCODE.jpg");
		byte[] baseEncodingBytes = ImgEncoder.encodingBase64(imageBytes);
		
		message.put("image", new String(baseEncodingBytes));
		
		data.put("response_code", "0028");
		data.put("message",message);
		
		System.out.println(data.toJSONString());
		writer.println(data.toJSONString());
	}	
}