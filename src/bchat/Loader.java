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
	 * Retorna a url de conexão de um dispositivo bluetooth correspondente ao endereço btkey
	 * retorna null caso ele não possua serviço
	 */
	public static String getBluetoothUrl(Map<String, List<String>> map, String btkey){
		List<String> services = map.get(btkey);
		if(services.size() < 3) //se nao tiver 3 itens, quer dizer que não tem serviço
			return null;
		return services.get(2); //a url do bluetooth ta na ultima posicao 
	}
	
	/*
	 * Retorna a url de conexao do primeiro dispositivo bluetooth que possuir serviços encontrados 
	 * retorna null caso não exista bluetooth com serviço
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
		final Object serviceSearchCompletedEvent = new Object();//para sincronizar os eventos da busca por serviços concluida
		//map contendo codigo do dispositivo e seus dados em lista de string
		Map<String, List<String>> mapReturnResult = new HashMap<String, List<String>>(); 
		
		//implementação do DiscoveryListener
		DiscoveryListener listener = new MyDiscoveryListener(devicesDiscovered, inquiryCompletedEvent, serviceSearchCompletedEvent, mapReturnResult );
		
		//Objeto para busca por dispositivos
		RemoteDeviceDiscovery remoteDeviceDiscovery = new RemoteDeviceDiscovery(listener, inquiryCompletedEvent, devicesDiscovered );
		
		//Objeto para busca por serviços
		ServicesSearch servicesSearch = new ServicesSearch(remoteDeviceDiscovery, listener, serviceSearchCompletedEvent, mapReturnResult);
		
		//adiciona os serviços encontrados a cada um dos dispositivos encontrados
		servicesSearch.getBluetoothDevices();
		//exibir dispositivos e seus serviços
		System.out.println(mapReturnResult);
		
		//Objeto que trata da conexão e do envio do arquivo
		FileSender fs = new FileSender();
		try{
			//criar conexao com a url do primeiro dispositivo encontrado que possua serviço push object
			fs.createConnection(getFirstBluetoothWithService(mapReturnResult));
			fs.createSession();
			
			fs.sendFile("C:\\Users\\marcusviniv\\Desktop\\iOS-Testing-Mind-Map-1.2.png");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	}
}
		
	
		

		
		

