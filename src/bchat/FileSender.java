package bchat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;

public class FileSender {
	
	private File file;
	private OutputStream fileOutPutStream;
	private OutputStream operationOutputStream;
	private Operation putOperation;
	private HeaderSet hs;
	private Connection connection;
	private ClientSession cs;
	
	
	public void createConnection(String bturl)throws IOException{
		System.out.println("criando conexao para " + bturl);
		if(bturl != null){
			connection = Connector.open(bturl);
			System.out.println("criou conexao");
		}
		
	}
	public void createSession() throws IOException{
		if(connection != null){
			cs = (ClientSession)connection;
			hs = cs.createHeaderSet();
			cs.connect(hs);
			System.out.println("criou sessao");
		}
	}
	
	
	
	private void setFileMetaData() throws IOException{
		
		if(hs != null){
			hs.setHeader(HeaderSet.NAME, file.getName());
			hs.setHeader(HeaderSet.LENGTH, new Long(file.length()));
			hs.setHeader(HeaderSet.TYPE, "image/png"); 
			putOperation = cs.put(hs);
			System.out.println("definiu metadados do arquivo");
		}
	}
	
	
	
	public void sendFile(String filepath) throws FileNotFoundException, IOException{
			if(connection != null && cs != null && hs != null ){
			file=new File(filepath);
	        fileOutPutStream = new FileOutputStream(file);
	        byte[] buf = new byte[(int)file.length()];
	        
	        setFileMetaData();
	        
	        operationOutputStream = putOperation.openOutputStream();
	        operationOutputStream.write(buf);
	        
	        fileOutPutStream.close();
	        putOperation.close();
	        cs.disconnect(null);
	        connection.close();
	        System.out.println("enviou arquivo");
			}
		
	}
	
	

}
