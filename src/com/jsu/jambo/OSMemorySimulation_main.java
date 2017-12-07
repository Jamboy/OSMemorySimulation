package com.jsu.jambo;

import java.util.Scanner;

import util.SchedulingAlgorithm;
import model.Process;
import model.ProcessQueue;


public class OSMemorySimulation_main {
	static Scanner scanner = new Scanner(System.in);
	static ProcessQueue processQueue = ProcessQueue.getInstance();
	static SchedulingAlgorithm sAlgorithm;
	static String [] algorithmStrings = {"FIFS算法","LRU算法(计数器实现)","LRU算法(堆栈实现)"};
	static int sAlgorithmChoose;
	static Process process_access;//下一访问页面
	static int assess_order = 0;//当前添加页的访问序号
	static boolean isFirst = true;
	static int run_num = 0;
	public static void main (String args[]){
		init();
		sAlgorithm= new SchedulingAlgorithm();
		showMenu(sAlgorithmChoose);
	}
	
	
	//调度对应算法
		public static void setAlgorithm(int algorithmChose){
			switch (algorithmChose) {
				case 1:
					sAlgorithm.FCFS(process_access,sAlgorithmChoose);
					printCurrentSetInfo(sAlgorithmChoose);
					showMenu(sAlgorithmChoose);
					break;
				case 2:
					sAlgorithm.LRU_COUNT(process_access,sAlgorithmChoose);
					printCurrentSetInfo(sAlgorithmChoose);
					showMenu(sAlgorithmChoose);
					break;
				case 3:
					sAlgorithm.LRU_STACK(process_access,sAlgorithmChoose);
					printCurrentSetInfo(sAlgorithmChoose);
					showMenu(sAlgorithmChoose);
					break;
				case 4:
					sAlgorithm.belady();
					break;
				default:
					System.out.println("您的输入有误");
					showMenu(sAlgorithmChoose);
					break;
			}
		}
	//显示菜单
	public static void showMenu(int sAlgorithmChoose){
		System.out.println("------模拟内存管理程序-------");
		System.out.println("------请输入下一个要访问的页面页号-------");
		if (sAlgorithmChoose == 1 && run_num >= 10) {
			
			System.out.println("------0.为当前驻留集增加一页，观察belady现象,输入其它继续执行置换算法-------");
			int choose = scanner.nextInt();
			if (choose == 0) {
				int cureent_set_num = processQueue.resident_set_num + 1;
				System.out.println("当前驻留集数已添加一页为:"+cureent_set_num);
				System.out.println("当前访问所有页:");
				for (int i = 0; i < processQueue.processBeladyList.size(); i++) {
					System.out.print(processQueue.processBeladyList.get(i).toString(4));
				}
				System.out.println(" ");
				System.out.println("置换过程:");				
				setAlgorithm(4);
			}else {
				System.out.println("------请输入下一个要访问的页面页号-------");
			}
		}
		int process_assess_order = scanner.nextInt();
		process_access = new Process();
		process_access.setProcess_id(process_assess_order);//设置页面ID
		process_access.setProcess_access_order(++assess_order);//设置页面访问顺序
		process_access.setProcess_access_counter(0);//设置页面计数器
		processQueue.processBeladyList.add(process_access);
		run_num++;
		setAlgorithm(sAlgorithmChoose);
	}
	
	//显示当前驻留集的页面
	public static void printCurrentSetInfo(int id){
		//输出当前驻留集信息
		System.out.println("--当前驻留集信息:");
		if (isFirst && id == 3) {
			for (int i = processQueue.processWaitQueue.size()-1; i >= 0; i--) {
				System.out.println(processQueue.processWaitQueue.get(i).toString(id));
			}
		}else {
			for (int i = 0; i < processQueue.processWaitQueue.size(); i++) {
				System.out.println(processQueue.processWaitQueue.get(i).toString(id));
			}
		}
	}
	
	
	//初始化操作，
	public static void init(){
		System.out.println("------模拟内存管理程序-------");
		System.out.println("------请输入驻留集块-------");
		processQueue.resident_set_num = scanner.nextInt();
		int access_counter = processQueue.resident_set_num;
		System.out.println("------您输入的驻留集块数是" + processQueue.resident_set_num);
		for (int i = 1; i < processQueue.resident_set_num + 1; i++) {
			System.out.println("------请先输入第"+ i +"个页面的页号");
			Process process = new Process();
			process.setProcess_id(scanner.nextInt());//设置页面ID
			process.setProcess_access_order(i);//设置页面访问顺序
			process.setProcess_access_counter(--access_counter);//设置访问次数
			processQueue.processWaitQueue.add(process);//添加到待访问队列
			processQueue.processBeladyList.add(process);
			assess_order = i;
			run_num++;
		}
		System.out.println("------请选择置换算法");
		System.out.println("------1.FIFS算法");
		System.out.println("------2.LRU算法(计数器实现)");
		System.out.println("------3.LRU算法(堆栈实现)");
		sAlgorithmChoose = scanner.nextInt();
		System.out.println("------您当前选择的置换算法是" + algorithmStrings[sAlgorithmChoose-1]);
		printCurrentSetInfo(sAlgorithmChoose);
		isFirst = false;
	}
}
