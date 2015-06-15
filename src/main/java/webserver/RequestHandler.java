package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.HttpRequestUtilsTest;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			DataOutputStream dos = new DataOutputStream(out);
			String loadFileName = "/WFADT.pdf";
			byte[] body = Files.readAllBytes(new File("./webapp" + loadFileName).toPath());
			//index.html file을 body에 넣어야 하는데...
//			InputStreamReader inputStreamReader = new InputStreamReader(in);
//			BufferedReader br = new BufferedReader(inputStreamReader);
//			//if POST 요청이 들어오
//			String test = br.readLine();
//			System.out.println(test);
//			String[] tokens = test.split(" "); 
//			String url = tokens[1];
//			
//			while(!"".equals(test)){				
//				System.out.println(test);
//				test=br.readLine();
//			}
//			
//			if(test == null){
//				return;
//			}
//			
//			IOUtils.readData(br, contentLength)
			//if GET요청이들어오
//			String test = br.readLine();
//			String[] tokens = test.split(" "); 
//			for( int i=0; i< tokens.length; i++){
//				System.out.println(tokens[i]);
//			}
//			String url = tokens[1];
//			int index = url.indexOf("?");
//			if( index > -1){	
//				String requestPath = url.substring(0, index); 
//				String params = url.substring(index+1);
//				//params의 value를 user의 값으로 넣어야 한다.
//				Map<String, String> testing = HttpRequestUtils.parseQueryString(params);
//				User user = new User(testing.get("userId"), testing.get("password"), testing.get("name"), testing.get("email"));
//			}
			
			
//			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
//			byte[] body = "".getBytes();
			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 Document Follows \r\n");
//			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Type: application/pdf;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
