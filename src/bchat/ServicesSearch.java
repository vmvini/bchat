package bchat;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

public class ServicesSearch {
	
	private UUID OBEX_OBJECT_PUSH_PROFILE = new UUID(0x1105);
    /* To find file transfer service */
    private UUID OBEX_FILE_TRANSFER_PROFILE = new UUID(0x1106);
    /* To find hands free service */
    private UUID HANDS_FREE = new UUID(0x111E);
    /* Get URL attribute from bluetooth service */
    private int URL_ATTRIBUTE = 0X0100;
    
    private DiscoveryListener discoveryListener;
    private Object serviceSearchCompletedEvent;
    private Map<String, List<String>> mapReturnResult;
    private RemoteDeviceDiscovery remoteDeviceDiscovery;
    
    public ServicesSearch(RemoteDeviceDiscovery remoteDeviceDiscovery, DiscoveryListener discoveryListener, Object serviceSearchCompletedEvent, Map<String, List<String>> mapReturnResult){
    	this.discoveryListener = discoveryListener;
    	this.serviceSearchCompletedEvent = serviceSearchCompletedEvent;
    	this.mapReturnResult = mapReturnResult;
    	this.remoteDeviceDiscovery = remoteDeviceDiscovery;
    }
    
    public Map<String, List<String>> getBluetoothDevices(){
    	/* Initialize UUID Array */
        UUID[] searchUuidSet = new UUID[]{OBEX_OBJECT_PUSH_PROFILE};
        int[] attrIDs = new int[]{URL_ATTRIBUTE}; //cria array com um valor ja definido {URL_ATTRIBUTE}
        
        /* Get list of bluetooth device from class RemoteDeviceDiscovery */
        try{
	        for (Enumeration en = remoteDeviceDiscovery.getDevices().elements(); en.hasMoreElements();) {
	            /* Get RemoteDevice object */
	            RemoteDevice btDevice = (RemoteDevice) en.nextElement();
	            /* Create list to return details */
	            List<String> listDeviceDetails = new ArrayList<String>();
	            
	            try {
	                /* Add bluetooth device name and address in list */
	                listDeviceDetails.add(btDevice.getFriendlyName(false));
	                listDeviceDetails.add(btDevice.getBluetoothAddress());
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            
	            /* Put bluetooth device details in map */
	            mapReturnResult.put(btDevice.getBluetoothAddress(), listDeviceDetails);
	            synchronized (serviceSearchCompletedEvent) {
	                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet, btDevice, discoveryListener);
	                
	                serviceSearchCompletedEvent.wait();
	            }
	        }
	        
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return mapReturnResult;
        
    }
	
}
