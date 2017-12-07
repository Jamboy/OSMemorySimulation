package com.jsu.jambo;

import java.util.Scanner;

import util.SchedulingAlgorithm;
import model.Process;
import model.ProcessQueue;


public class OSMemorySimulation_main {
	static Scanner scanner = new Scanner(System.in);
	static ProcessQueue processQueue = ProcessQueue.getInstance();
	static SchedulingAlgorithm sAlgorithm;
	static String [] algorithmStrings = {"FIFS�㷨","LRU�㷨(������ʵ��)","LRU�㷨(��ջʵ��)"};
	static int sAlgorithmChoose;
	static Process process_access;//��һ����ҳ��
	static int assess_order = 0;//��ǰ���ҳ�ķ������
	static boolean isFirst = true;
	static int run_num = 0;
	public static void main (String args[]){
		init();
		sAlgorithm= new SchedulingAlgorithm();
		showMenu(sAlgorithmChoose);
	}
	
	
	//���ȶ�Ӧ�㷨
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
					System.out.println("������������");
					showMenu(sAlgorithmChoose);
					break;
			}
		}
	//��ʾ�˵�
	public static void showMenu(int sAlgorithmChoose){
		System.out.println("------ģ���ڴ�������-------");
		System.out.println("------��������һ��Ҫ���ʵ�ҳ��ҳ��-------");
		if (sAlgorithmChoose == 1 && run_num >= 10) {
			
			System.out.println("------0.Ϊ��ǰפ��������һҳ���۲�belady����,������������ִ���û��㷨-------");
			int choose = scanner.nextInt();
			if (choose == 0) {
				int cureent_set_num = processQueue.resident_set_num + 1;
				System.out.println("��ǰפ�����������һҳΪ:"+cureent_set_num);
				System.out.println("��ǰ��������ҳ:");
				for (int i = 0; i < processQueue.processBeladyList.size(); i++) {
					System.out.print(processQueue.processBeladyList.get(i).toString(4));
				}
				System.out.println(" ");
				System.out.println("�û�����:");				
				setAlgorithm(4);
			}else {
				System.out.println("------��������һ��Ҫ���ʵ�ҳ��ҳ��-------");
			}
		}
		int process_assess_order = scanner.nextInt();
		process_access = new Process();
		process_access.setProcess_id(process_assess_order);//����ҳ��ID
		process_access.setProcess_access_order(++assess_order);//����ҳ�����˳��
		process_access.setProcess_access_counter(0);//����ҳ�������
		processQueue.processBeladyList.add(process_access);
		run_num++;
		setAlgorithm(sAlgorithmChoose);
	}
	
	//��ʾ��ǰפ������ҳ��
	public static void printCurrentSetInfo(int id){
		//�����ǰפ������Ϣ
		System.out.println("--��ǰפ������Ϣ:");
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
	
	
	//��ʼ��������
	public static void init(){
		System.out.println("------ģ���ڴ�������-------");
		System.out.println("------������פ������-------");
		processQueue.resident_set_num = scanner.nextInt();
		int access_counter = processQueue.resident_set_num;
		System.out.println("------�������פ����������" + processQueue.resident_set_num);
		for (int i = 1; i < processQueue.resident_set_num + 1; i++) {
			System.out.println("------���������"+ i +"��ҳ���ҳ��");
			Process process = new Process();
			process.setProcess_id(scanner.nextInt());//����ҳ��ID
			process.setProcess_access_order(i);//����ҳ�����˳��
			process.setProcess_access_counter(--access_counter);//���÷��ʴ���
			processQueue.processWaitQueue.add(process);//��ӵ������ʶ���
			processQueue.processBeladyList.add(process);
			assess_order = i;
			run_num++;
		}
		System.out.println("------��ѡ���û��㷨");
		System.out.println("------1.FIFS�㷨");
		System.out.println("------2.LRU�㷨(������ʵ��)");
		System.out.println("------3.LRU�㷨(��ջʵ��)");
		sAlgorithmChoose = scanner.nextInt();
		System.out.println("------����ǰѡ����û��㷨��" + algorithmStrings[sAlgorithmChoose-1]);
		printCurrentSetInfo(sAlgorithmChoose);
		isFirst = false;
	}
}
