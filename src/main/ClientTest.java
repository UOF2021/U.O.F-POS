package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(Socket socket = new Socket("localhost",13)){
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// �������� ����
			System.out.println("�������� ���� ����");
			writer.write("YOU\n");
			writer.flush();
			System.out.println("�������� ���� ��");

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// �������� �б�
			System.out.println("�������� �б� ����");
			for(String line = reader.readLine(); line != null;line = reader.readLine()) {
				System.out.println(line);
			}
			System.out.println("�������� �б� ��");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
