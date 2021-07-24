package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class POSServer {
	
	public final static int PORT=13;
	
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(PORT)){
			while(true) {
				System.out.println("�����...");
				try(Socket connection = server.accept()){
					System.out.println("���� �Ϸ�");
					/*
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					 
					// Ŭ���̾�Ʈ���� �б�
					System.out.println("Ŭ���̾�Ʈ���� �б� ����");
					System.out.println(reader.readLine());
					System.out.println("Ŭ���̾�Ʈ���� �б� ��");
					*/
					
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
					//OutputStream writer2 = new OutputStream(connection.getOutputStream());
					//Writer writer3 = new OutputStreamWriter(connection.getOutputStream());
					// Ŭ���̾�Ʈ���� ����
					System.out.println("Ŭ���̾�Ʈ���� ���� ����2");
					writer.write("To Client From Server\n");
					//writer.write("To Client From Server\n");
					writer.flush();
					System.out.println("Ŭ���̾�Ʈ���� ���� ��");
					
				} catch(IOException e) {
					System.err.println(e);
				}
			}
		} catch(IOException e) {
			System.err.println(e);
		}
	}
}
