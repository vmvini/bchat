package bchat;

import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;

public class RemoteDeviceDiscovery {
	
	private Vector devicesDiscovered;
	
	private Object inquiryCompletedEvent;
	
	private DiscoveryListener listener;
	
	public RemoteDeviceDiscovery(DiscoveryListener listener, Object inquiryCompletedEvent, Vector devicesDiscovered ){
		this.listener = listener;
		this.inquiryCompletedEvent = inquiryCompletedEvent;
		this.devicesDiscovered = devicesDiscovered;
	}
	
	public Vector getDevices() throws BluetoothStateException, InterruptedException{
		 devicesDiscovered.clear();
		 
		 synchronized (inquiryCompletedEvent) {
			 /* Start device discovery */
             boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
             if (started) {
                 System.out.println("wait for device inquiry to complete...");
                 inquiryCompletedEvent.wait();
             }
		 }
		 
		 return devicesDiscovered;
		 
	}
	
	

}
