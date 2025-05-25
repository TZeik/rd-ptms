package com.zeik.ptms;
import com.zeik.ptms.logic.PTMS;
import com.zeik.ptms.visual.MainScreen;

public class Main{
	
	
	public static void main(String[] args) {
		
		PTMS.getInstance().loadPTMS();
		MainScreen.launch(MainScreen.class,args);
	}
}
