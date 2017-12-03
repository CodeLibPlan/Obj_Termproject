package network_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Connection extends Thread {
	// instance variables
	private Socket sock = new Socket();
	private PrintWriter pw;
	private BufferedReader br;

	private String address;
	private int port;

	// Constant
	public Connection(String address, int port) {
		this.address = address;
		this.port = port;
		this.startConnection();
		this.start();
	}

	// public Getter
	public PrintWriter writer() {
		return this.pw;
	}

	public BufferedReader reader() {
		return this.br;
	}

	public String address() {
		return this.address;
	}

	public int port() {
		return this.port;
	}

	// public setter
	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}

	// public method
	public boolean isConnected() {/// �̱��� ����
		return true;
	}

	public boolean networkStatus() {/// �̱��� ����
		return true;
	}

	public void run() {
		while (this.status() == 1) {
			if (this.writer() != null) {
				this.writer().println(""+0x00);//���� ������ ����
				this.writer().flush();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String toString() {
		return this.address + ":" + this.port;
	}

	public boolean startConnection() {
		Connector c = new Connector();
		c.start();
		try {
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.status() == 1)
			return true;
		return false;
	}

	public boolean resetConnection() {// �� Ŭ������ ����ϴ� Ŭ������ Thread ���ᰡ ����Ǿ�� ��.

		Connector c = new Connector();
		c.start();
		try {
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.status() == 1)
			return true;
		return false;
	}

	public boolean stopConnection() {// �� Ŭ������ ����ϴ� Ŭ������ Thread ���ᰡ ����Ǿ�� ��.
		if (status() == 1) {
			try {
				this.pw.close();
				this.br.close();
				this.sock.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	// private method
	private int status() {
		// -1 : Error//0 : not connected//1 : connected//
		if (this.sock.isClosed())
			return 0;
		if (this.sock.isConnected())
			return 1;
		return -1;
	}

	// private class Connector
	private class Connector extends Thread {
		public void run() {
			try {
				if (status() == 1) {
					pw.close();
					br.close();
					sock.close();
				}
				// 1. ������ IP�� ������ ���� ��Ʈ ��(10001)�� ���ڷ� �־� socket ����

				InetAddress ipaddress = InetAddress.getByName(address);
				System.out.println(ipaddress + " �� ����");
				sock = new Socket(ipaddress, port);
				sock.setKeepAlive(true);

				// 2. ������ Socket���κ��� InputStream�� OutputStream�� ����
				OutputStream out = sock.getOutputStream();
				InputStream in = sock.getInputStream();

				// 3. InputStream�� BufferedReader �������� ��ȯ
				// OutputStream�� PrintWriter �������� ��ȯ
				pw = new PrintWriter(new OutputStreamWriter(out));

				// 4. Ű����κ��� �� �پ� �Է¹޴� BufferedReader ��ü ����
				br = new BufferedReader(new InputStreamReader(in));
			} catch(java.net.UnknownHostException e) {
				System.out.println("[ERR] Invalid address");
				e.printStackTrace();
			}catch (Exception e) {
				//e.printStackTrace();�� ���� Ȱ��ȭ�Ͽ� Network �����ϱ�
			}

		}
	}
}
