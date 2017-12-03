package network_client;

public interface CallbackEvent<T> {
	/* *
	 * �� Ŭ������ ����Ͽ� run()�Լ��� �������� �� ���ڷ� �ѱ� �� ����.
	 * */
	public void run(T input);
}
