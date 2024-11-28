package run;

import logic.PTMS;
import visual.MainScreen;

public class Main{
	
	
	public static void main(String[] args) {

		PTMS.getInstance().loadPTMS();
		MainScreen.launch(MainScreen.class,args);
	}
}
