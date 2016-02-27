# bluetoothchat

Por enquanto está conectando e enviando arquivo para celular windows phone.

Mesmo identificando o serviço de outro bluetooth de um notebook, não foi possível conectar.

Ao tentar conectar ao bluetooth de outro notebook, a exceção IOException é lançada:
IOException: Can't connect [General fail]
	at com.intel.bluetooth.BluetoothStackBlueSoleil.connectionRfOpenImpl(Native Method)
	at com.intel.bluetooth.BluetoothStackBlueSoleil.connectionRfOpenClientConnection(BluetoothStackBlueSoleil.java:368)
	.
	.
	.
	at javax.microedition.io.Connector.open(Connector.java:83)
	at bchat.FileSender.createConnection(FileSender.java:29)
	at bchat.Loader.main(Loader.java:66)
