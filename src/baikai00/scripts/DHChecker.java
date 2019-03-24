package baikai00.scripts;

import java.io.IOException;

public class DHChecker extends Thread{
	private static DHChecker instance;
	
	private boolean isStart = false;
	private int state;
	private DHChecker(){
	}
	
	public static synchronized DHChecker getInstance(){
		if (instance == null){
			instance = new DHChecker();
		}
		return instance;
	}
	
	@Override
	public void run() {
		isStart = true;
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state = readState();
		}
	}
	
	public boolean isStart(){
		return isStart;
	}
	
	private int readState(){
		String dhValue = null;
		try {
			dhValue = ReadIni.getValue("200", "name");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (dhValue != null && "1".equals(dhValue)){
			return 200;
		}
		return -1;
	}
	
	public int getIniState(){
		return state;
	}
}
