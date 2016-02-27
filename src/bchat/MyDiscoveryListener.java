package bchat;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class MyDiscoveryListener implements DiscoveryListener {
	
	private Vector devicesDiscovered;
	private Object inquiryCompletedEvent;
	private Map<String, List<String>> mapReturnResult;
	private int URL_ATTRIBUTE = 0X0100;
	private Object serviceSearchCompletedEvent;
	
	public MyDiscoveryListener(Vector devicesDiscovered, Object inquiryCompletedEvent, Object serviceSearchCompletedEvent, Map<String, List<String>> mapReturnResult ){
		this.devicesDiscovered = devicesDiscovered;
		this.inquiryCompletedEvent = inquiryCompletedEvent;
		this.mapReturnResult = mapReturnResult;
		this.serviceSearchCompletedEvent = serviceSearchCompletedEvent;
	}
	
	
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        /* Get devices paired with system or in range(Without Pair) */
		
        devicesDiscovered.addElement(btDevice);
        
    }

    public void inquiryCompleted(int discType) {
        /* Notify thread when inquiry completed */
        synchronized (inquiryCompletedEvent) {
            inquiryCompletedEvent.notifyAll();
        }
    }

    /* To find service on bluetooth */
    public void serviceSearchCompleted(int transID, int respCode) {
    	//nao implementado no projeto eclipse
        /* Notify thread when search completed */
        synchronized (serviceSearchCompletedEvent) {
            serviceSearchCompletedEvent.notifyAll();
        }
    }

    /* To find service on bluetooth */
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    	System.out.println("serviços encontrados");
    	//nao implementado no projeto eclipse
        for (int i = 0; i < servRecord.length; i++) {
            /* Find URL of bluetooth device */
            //ServiceRecord.getConnectionURL(int requiredSecurity, boolean mustBeMaster) : 
                //Returns a String including optional parameters that can be used by a client to connect to the service described by this ServiceRecord.
            String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            
            if (url == null) {
                continue; //se url for null, passe para proxima iteração do laço for
            }
            String temporaryString = "";
            /* Get object of bluetooth device */
            RemoteDevice rd = servRecord[i].getHostDevice();
            /* Get attribute from ServiceRecord */
            DataElement serviceName = servRecord[i].getAttributeValue(URL_ATTRIBUTE);
            if (serviceName != null) {         
                temporaryString =  url;
                /* Put it in map */
                mapReturnResult.get(rd.getBluetoothAddress()).add(temporaryString);
            } else {
            	
                temporaryString = "Uknown service \n" + url;
                /* Put it in map */
                mapReturnResult.get(rd.getBluetoothAddress()).add(temporaryString);
            }
        }
    }

}
