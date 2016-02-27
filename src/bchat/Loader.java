package bchat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.bluetooth.DiscoveryListener;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;

public class Loader {
	
	/*
	 * Retorna a url de conex�o de um dispositivo bluetooth correspondente ao endere�o btkey
	 * retorna null caso ele n�o possua servi�o
	 */
	public static String getBluetoothUrl(Map<String, List<String>> map, String btkey){
		List<String> services = map.get(btkey);
		if(services.size() < 3) //se nao tiver 3 itens, quer dizer que n�o tem servi�o
			return null;
		return services.get(2); //a url do bluetooth ta na ultima posicao 
	}
	
	/*
	 * Retorna a url de conexao do primeiro dispositivo bluetooth que possuir servi�os encontrados 
	 * retorna null caso n�o exista bluetooth com servi�o
	 */
	public static String getFirstBluetoothWithService(Map<String, List<String>> map){
		Set<String> keyset = map.keySet();
		for(String s : keyset){
			String url = getBluetoothUrl(map, s);
			if( url != null  )
				return url;
		}
		return null;
	}
	
	public static void main(String[] args) throws InterruptedException {
		final Vector devicesDiscovered = new Vector();
		final Object inquiryCompletedEvent = new Object(); //para sincronizar os eventos da busca por dispositivos concluida
		final Object serviceSearchCompletedEvent = new Object();//para sincronizar os eventos da busca por servi�os concluida
		//map contendo codigo do dispositivo e seus dados em lista de string
		Map<String, List<String>> mapReturnResult = new HashMap<String, List<String>>(); 
		
		//implementa��o do DiscoveryListener
		DiscoveryListener listener = new MyDiscoveryListener(devicesDiscovered, inquiryCompletedEvent, serviceSearchCompletedEvent, mapReturnResult );
		
		//Objeto para busca por dispositivos
		RemoteDeviceDiscovery remoteDeviceDiscovery = new RemoteDeviceDiscovery(listener, inquiryCompletedEvent, devicesDiscovered );
		
		//Objeto para busca por servi�os
		ServicesSearch servicesSearch = new ServicesSearch(remoteDeviceDiscovery, listener, serviceSearchCompletedEvent, mapReturnResult);
		
		//adiciona os servi�os encontrados a cada um dos dispositivos encontrados
		servicesSearch.getBluetoothDevices();
		//exibir dispositivos e seus servi�os
		System.out.println(mapReturnResult);
		
		//Objeto que trata da conex�o e do envio do arquivo
		FileSender fs = new FileSender();
		try{
			//criar conexao com a url do primeiro dispositivo encontrado que possua servi�o push object
			fs.createConnection(getFirstBluetoothWithService(mapReturnResult));
			fs.createSession();
			
			fs.sendFile("C:\\Users\\marcusviniv\\Desktop\\iOS-Testing-Mind-Map-1.2.png");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	}
}
		
	
		

		
		

