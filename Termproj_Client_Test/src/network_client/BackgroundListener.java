package network_client;

public interface BackgroundListener {
	public void start();
	public void callback(CallbackEvent<String> ev);
	public void stopListen();
}
