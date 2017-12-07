package util;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;

import model.Process;
import model.ProcessQueue;


public class SchedulingAlgorithm {
	static ProcessQueue pQueue = ProcessQueue.getInstance();
	static LinkedList<Process> sortByOrderList = new LinkedList<>();
	static int lack_num = pQueue.resident_set_num;
	static int access_num = pQueue.resident_set_num;
	static boolean isFirst = true;

	/*
	 * �����ȷ����㷨
	 */
	public boolean FCFS(Process nextProcess,int sAlgorithmID){
		//�жϵ�ǰҳ�Ƿ���פ������
		if (!isExist(nextProcess,sAlgorithmID)) {
			//������ȡ����������ҳID����פ����������ɾ��
			int id = pQueue.processWaitQueue.pollFirst().getProcess_id();
			//����ǰ����ҳ��ӵ�פ��������ĩβ
			pQueue.processWaitQueue.addLast(nextProcess);
			//�жϵ�ǰ������Ϊ��ʾbelady������ø÷���
			if (sAlgorithmID == 1) {
				//ȱҳ��+1
				lack_num++;
				//������+1
				access_num++;
				System.out.println("��ǰ��������ҳ:");
				for (int i = 0; i < pQueue.processBeladyList.size(); i++) {
					System.out.print(pQueue.processBeladyList.get(i).toString(4));
				}
				System.out.println(" ");
				System.out.println("��ǰҳ�Ƿ�����:"+ "false" +"     " +"��ǰ��̭ҳ��:"+id+"     " +"��ǰȱҳ����:"+lack_num+"     "+"��ǰ�����ܴ���:"+access_num+"     "+"��ǰȱҳ��:"+calculatePageFaultRate(lack_num,access_num));
			}
			return false;
		}else{
			if (sAlgorithmID == 1) {
				access_num++;
				System.out.println("��ǰ��������ҳ:");
				for (int i = 0; i < pQueue.processBeladyList.size(); i++) {
					System.out.print(pQueue.processBeladyList.get(i).toString(4));
				}
				System.out.println(" ");
				System.out.println("��ǰҳ�Ƿ�����:"+ "true" +"     " +"��ǰ��̭ҳ��:null"+"     "+"��ǰȱҳ����:"+lack_num+"     "+"��ǰ�����ܴ���:"+access_num+"     "+"��ǰȱҳ��:"+calculatePageFaultRate(lack_num,access_num));
			}
			return true;
		}
	}

	
	public void belady(){
		int set_num = pQueue.resident_set_num +1;//פ��������1
		int lack = 0;
		int asscss = 0;
		pQueue.processWaitQueue.clear();//���waitQueueԪ�أ�Ϊ��ʾbelady������׼��
		//���ǰפ�������ļ�ҳ
		for (int i = 0; i < set_num; i++) {
			for (int j = 0; j <= i; j++) {
				System.out.print(pQueue.processBeladyList.get(j).toString(4));
			}
			pQueue.processWaitQueue.add(pQueue.processBeladyList.get(i));
			System.out.print("   ��ǰҳ�Ƿ�����: false");
			System.out.print("   ��ǰ��̭ҳ��: null");
			System.out.print("   ��ǰȱҳ����:" + lack++);
			System.out.print("   ��ǰ���ʴ���:" + asscss++);
			System.out.print("   ��ǰȱҳ��:"+calculatePageFaultRate(lack,asscss));
			System.out.println(" ");
		}
		//Ϊפ����������ӵ�ҳ����FCFS����,���ж����
		for (int i = set_num; i < pQueue.processBeladyList.size(); i++) {
			Process process = pQueue.processBeladyList.get(i);
			int id = pQueue.processWaitQueue.getFirst().getProcess_id();
			boolean isExist = FCFS(process, 2);
			if (!isExist) {
				lack++;
				asscss++;
				for (int j = 0; j < pQueue.processWaitQueue.size(); j++) {
					System.out.print(pQueue.processWaitQueue.get(j).toString(4));
				}
				System.out.print("   ��ǰҳ�Ƿ�����: false");
				System.out.print("   ��ǰ��̭ҳ��: "+ id);
				System.out.print("   ��ǰȱҳ����:" + lack);
				System.out.print("   ��ǰ���ʴ���:" + asscss);
				System.out.print("   ��ǰȱҳ��:"+calculatePageFaultRate(lack,asscss));
				System.out.println(" ");
			}else {
				asscss++;
				for (int j = 0; j < pQueue.processWaitQueue.size(); j++) {
					System.out.print(pQueue.processWaitQueue.get(j).toString(4));
				}
				System.out.print("   ��ǰҳ�Ƿ�����: true");
				System.out.print("   ��ǰ��̭ҳ��: null");
				System.out.print("   ��ǰȱҳ����:" + lack);
				System.out.print("   ��ǰ���ʴ���:" + asscss);
				System.out.print("   ��ǰȱҳ��:"+calculatePageFaultRate(lack,asscss));
				System.out.println(" ");
			}
		}
		System.out.println("פ������Ϊ"+ pQueue.resident_set_num +"ʱ:   " +"  " +"ȱҳ����:"+lack_num+"     "+"�����ܴ���:"+access_num+"     "+"ȱҳ��:"+calculatePageFaultRate(lack_num,access_num));
	}
	
	//LRU�㷨(������ʵ��)
	public void LRU_COUNT(Process nextProcess, int sAlgorithmID){
		Process out_process;
		//�жϵ�ǰҳ������פ������
		if (!isExist(nextProcess,sAlgorithmID)) {
			//ȡ��һ��������ֵ����ҳ
			out_process = getMaxCounter();
			int id = out_process.getProcess_id();
			int counter = out_process.getProcess_access_counter();
			//����̭ҳ��פ������ɾ��
			pQueue.processWaitQueue.remove(out_process);
			//ȱҳ����+1
			lack_num++;
			//���ʴ���+1
			access_num++;
			//�ڶ���β��ӵ�ǰ����ҳ
			pQueue.processWaitQueue.addLast(nextProcess);
			System.out.println("��ǰҳ�Ƿ�����:"+ "false" +"     " +"��ǰ��̭ҳ��:"+id+"     " +"��ǰ��̭ҳ������:" +counter+"     "+"��ǰȱҳ����:"+lack_num+"     "+"��ǰ�����ܴ���:"+access_num+"     "+"��ǰȱҳ��:"+calculatePageFaultRate(lack_num,access_num));
		}else {
			//������ʴ���+1������������ֵ�仯������˳��仯�Ȳ�������isExist�����
			access_num++;
			System.out.println("��ǰҳ�Ƿ�����:"+ "true" +"     " +"��ǰ��̭ҳ��:null"+"     "+"��ǰȱҳ����:"+lack_num+"     "+"��ǰҳ������:" +0+"     "+"��ǰ�����ܴ���:"+access_num+"     "+"��ǰȱҳ��:"+calculatePageFaultRate(lack_num,access_num));

		}
	}
	
	
	//LRU�㷨(��ջʵ��)���������ջ����ȱ��ɾ��ջ��ҳ����ջ��
	public void LRU_STACK(Process nextProcess,int sAlgorithmID){
		//��һ�ε����򽫰���ջ��ҳ�����˳������
		if (isFirst) {
			sort();
		}
		//�жϵ�ǰҳ�Ƿ���פ������
		if (!isExist(nextProcess,sAlgorithmID)) {
			//������ȡ��פ�����еĶ�βҳID��ɾ��
			int id = pQueue.processWaitQueue.pollLast().getProcess_id();
			//����ǰҳ��ӵ�פ������ͷ
			pQueue.processWaitQueue.addFirst(nextProcess);
			lack_num++;
			access_num++;
			System.out.println("��ǰҳ�Ƿ�����:"+ "false" +"     " +"��ǰ��̭ҳ��:"+id+"     "+"��ǰȱҳ����:"+lack_num+"     "+"��ǰ�����ܴ���:"+access_num+"     "+"��ǰȱҳ��:"+calculatePageFaultRate(lack_num,access_num));
		}else {
			//���������������ͷ
			pQueue.processWaitQueue.addFirst(nextProcess);
			access_num++;
			System.out.println("��ǰҳ�Ƿ�����:"+ "true" +"     " +"��ǰ��̭ҳ��:null"+"     "+"��ǰȱҳ����:"+lack_num+"     "+"��ǰ�����ܴ���:"+access_num+"     "+"��ǰȱҳ��:"+calculatePageFaultRate(lack_num,access_num));
		}
	}
	
	//����ջ��ҳ�����˳������
	public void sort(){
		LinkedList<Process> tempList = new LinkedList<Process>();
		int size = pQueue.processWaitQueue.size();
		Iterator<Process> iterator  = pQueue.processWaitQueue.iterator();
		while (iterator.hasNext() && size != 0) {
			tempList.addFirst(iterator.next());
			size--;
		}
		pQueue.processWaitQueue = tempList;	
		isFirst = false;
	}
	
	
	//�жϵ�ǰ�����Ƿ��и�ҳ��	
	public boolean isExist(Process nextProcess, int sAlgorithmID){
		boolean isExist = false;
		for (int i = 0; i < pQueue.processWaitQueue.size(); i++) {
			if(pQueue.processWaitQueue.get(i).process_id == nextProcess.process_id){
				isExist = true;
				pQueue.processWaitQueue.get(i).setProcess_access_order(nextProcess.getProcess_access_order());
				pQueue.processWaitQueue.get(i).setProcess_access_counter(0);
				if (sAlgorithmID == 3) {
					pQueue.processWaitQueue.remove(i);
				}
			}else {
				pQueue.processWaitQueue.get(i).setProcess_access_counter(pQueue.processWaitQueue.get(i).getProcess_access_counter()+1);
			}
		}
		return isExist;
	}
	
	//����ȱҳ��
	public String calculatePageFaultRate(int lack_num,int access_num){
		DecimalFormat deFormat = new DecimalFormat();
		deFormat.setMaximumFractionDigits(2);
		deFormat.setMinimumFractionDigits(2);
		return deFormat.format(lack_num * 100.00/access_num) + "%";
	}
	
	//��ȡ������ֵ����ҳ
	public Process getMaxCounter(){
		int max = 0;
		Process out_process = null;
		for (int i = 0; i < pQueue.processWaitQueue.size(); i++) {
			if (pQueue.processWaitQueue.get(i).process_access_counter > max) {
				max = pQueue.processWaitQueue.get(i).process_access_counter;
				out_process = pQueue.processWaitQueue.get(i);
			}
		}	
		return out_process;
	}
	
//	public Process getEarliest(){
//		int min = pQueue.resident_set_num;
//		Process earliest_process = null;
//		for (int i = 0; i < pQueue.processWaitQueue.size(); i++) {
//			if (pQueue.processWaitQueue.get(i).getProcess_access_order() < min) {
//				min = pQueue.processWaitQueue.get(i).getProcess_access_order();
//				earliest_process = pQueue.processWaitQueue.get(i);
//			}
//		}	
//		return earliest_process;
//	}
}
