package model;

import java.util.LinkedList;


public class ProcessQueue {
	 private static ProcessQueue processQueue;
	 public LinkedList<Process> processWaitQueue; //ҳ��Ĵ����ʶ���
	 public LinkedList<Process> processRunQueue; //ҳ������ж���
	 public LinkedList<Process> processBlockingQueue;//���̵���������
	 public LinkedList<Process> processBeladyList;//��ǰ���н�����Ϣ
	 public int resident_set_num;//פ������
	 private ProcessQueue(){
			processWaitQueue = new LinkedList<>();
			processRunQueue = new LinkedList<>();
			processBlockingQueue = new LinkedList<>();
			processBeladyList = new LinkedList<>();
	 }
	 
	 public static synchronized ProcessQueue getInstance(){
		 if (processQueue == null) {
			processQueue = new ProcessQueue();
		}
		 return processQueue;
	 }

	public static ProcessQueue getProcessQueue() {
		return processQueue;
	}

	public static void setProcessQueue(ProcessQueue processQueue) {
		ProcessQueue.processQueue = processQueue;
	}

	public LinkedList<Process> getprocessWaitQueue() {
		return processWaitQueue;
	}

	public void setprocessWaitQueue(LinkedList<Process> processWaitQueue) {
		this.processWaitQueue = processWaitQueue;
	}

	public LinkedList<Process> getprocessRunQueue() {
		return processRunQueue;
	}

	public void setprocessRunQueue(LinkedList<Process> processRunQueue) {
		this.processRunQueue = processRunQueue;
	}

	public LinkedList<Process> getprocessBlockingQueue() {
		return processBlockingQueue;
	}

	public void setprocessBlockingQueue(LinkedList<Process> processBlockingQueue) {
		this.processBlockingQueue = processBlockingQueue;
	}

	public LinkedList<Process> getprocessBeladyList() {
		return processBeladyList;
	}

	public void setprocessBeladyList(LinkedList<Process> processBeladyList) {
		this.processBeladyList = processBeladyList;
	}

	public int getResident_set_num() {
		return resident_set_num;
	}

	public void setResident_set_num(int resident_set_num) {
		this.resident_set_num = resident_set_num;
	}
}
