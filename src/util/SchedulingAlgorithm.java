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
	 * 先来先服务算法
	 */
	public boolean FCFS(Process nextProcess,int sAlgorithmID){
		//判断当前页是否在驻留集中
		if (!isExist(nextProcess,sAlgorithmID)) {
			//不存在取出最先来的页ID并从驻留集队列中删除
			int id = pQueue.processWaitQueue.pollFirst().getProcess_id();
			//将当前访问页添加到驻留集队列末尾
			pQueue.processWaitQueue.addLast(nextProcess);
			//判断当前若不是为演示belady现象调用该方法
			if (sAlgorithmID == 1) {
				//缺页数+1
				lack_num++;
				//访问数+1
				access_num++;
				System.out.println("当前访问所有页:");
				for (int i = 0; i < pQueue.processBeladyList.size(); i++) {
					System.out.print(pQueue.processBeladyList.get(i).toString(4));
				}
				System.out.println(" ");
				System.out.println("当前页是否命中:"+ "false" +"     " +"当前淘汰页号:"+id+"     " +"当前缺页次数:"+lack_num+"     "+"当前访问总次数:"+access_num+"     "+"当前缺页率:"+calculatePageFaultRate(lack_num,access_num));
			}
			return false;
		}else{
			if (sAlgorithmID == 1) {
				access_num++;
				System.out.println("当前访问所有页:");
				for (int i = 0; i < pQueue.processBeladyList.size(); i++) {
					System.out.print(pQueue.processBeladyList.get(i).toString(4));
				}
				System.out.println(" ");
				System.out.println("当前页是否命中:"+ "true" +"     " +"当前淘汰页号:null"+"     "+"当前缺页次数:"+lack_num+"     "+"当前访问总次数:"+access_num+"     "+"当前缺页率:"+calculatePageFaultRate(lack_num,access_num));
			}
			return true;
		}
	}

	
	public void belady(){
		int set_num = pQueue.resident_set_num +1;//驻留集数加1
		int lack = 0;
		int asscss = 0;
		pQueue.processWaitQueue.clear();//清空waitQueue元素，为演示belady现象做准备
		//输出前驻留集数的几页
		for (int i = 0; i < set_num; i++) {
			for (int j = 0; j <= i; j++) {
				System.out.print(pQueue.processBeladyList.get(j).toString(4));
			}
			pQueue.processWaitQueue.add(pQueue.processBeladyList.get(i));
			System.out.print("   当前页是否命中: false");
			System.out.print("   当前淘汰页号: null");
			System.out.print("   当前缺页次数:" + lack++);
			System.out.print("   当前访问次数:" + asscss++);
			System.out.print("   当前缺页率:"+calculatePageFaultRate(lack,asscss));
			System.out.println(" ");
		}
		//为驻留集数后添加的页调用FCFS方法,并判断输出
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
				System.out.print("   当前页是否命中: false");
				System.out.print("   当前淘汰页号: "+ id);
				System.out.print("   当前缺页次数:" + lack);
				System.out.print("   当前访问次数:" + asscss);
				System.out.print("   当前缺页率:"+calculatePageFaultRate(lack,asscss));
				System.out.println(" ");
			}else {
				asscss++;
				for (int j = 0; j < pQueue.processWaitQueue.size(); j++) {
					System.out.print(pQueue.processWaitQueue.get(j).toString(4));
				}
				System.out.print("   当前页是否命中: true");
				System.out.print("   当前淘汰页号: null");
				System.out.print("   当前缺页次数:" + lack);
				System.out.print("   当前访问次数:" + asscss);
				System.out.print("   当前缺页率:"+calculatePageFaultRate(lack,asscss));
				System.out.println(" ");
			}
		}
		System.out.println("驻留集数为"+ pQueue.resident_set_num +"时:   " +"  " +"缺页次数:"+lack_num+"     "+"访问总次数:"+access_num+"     "+"缺页率:"+calculatePageFaultRate(lack_num,access_num));
	}
	
	//LRU算法(计数器实现)
	public void LRU_COUNT(Process nextProcess, int sAlgorithmID){
		Process out_process;
		//判断当前页若不在驻留集中
		if (!isExist(nextProcess,sAlgorithmID)) {
			//取得一个计数器值最大的页
			out_process = getMaxCounter();
			int id = out_process.getProcess_id();
			int counter = out_process.getProcess_access_counter();
			//将淘汰页从驻留集中删除
			pQueue.processWaitQueue.remove(out_process);
			//缺页次数+1
			lack_num++;
			//访问次数+1
			access_num++;
			//在队列尾添加当前访问页
			pQueue.processWaitQueue.addLast(nextProcess);
			System.out.println("当前页是否命中:"+ "false" +"     " +"当前淘汰页号:"+id+"     " +"当前淘汰页计数器:" +counter+"     "+"当前缺页次数:"+lack_num+"     "+"当前访问总次数:"+access_num+"     "+"当前缺页率:"+calculatePageFaultRate(lack_num,access_num));
		}else {
			//否则访问次数+1，其它计数器值变化，访问顺序变化等操作已在isExist中完成
			access_num++;
			System.out.println("当前页是否命中:"+ "true" +"     " +"当前淘汰页号:null"+"     "+"当前缺页次数:"+lack_num+"     "+"当前页计数器:" +0+"     "+"当前访问总次数:"+access_num+"     "+"当前缺页率:"+calculatePageFaultRate(lack_num,access_num));

		}
	}
	
	
	//LRU算法(堆栈实现)命中则调至栈顶，缺则删除栈底页加至栈顶
	public void LRU_STACK(Process nextProcess,int sAlgorithmID){
		//第一次调用则将按堆栈的页面放置顺序排序
		if (isFirst) {
			sort();
		}
		//判断当前页是否在驻留集中
		if (!isExist(nextProcess,sAlgorithmID)) {
			//不存在取出驻留集中的队尾页ID并删除
			int id = pQueue.processWaitQueue.pollLast().getProcess_id();
			//将当前页添加到驻留集列头
			pQueue.processWaitQueue.addFirst(nextProcess);
			lack_num++;
			access_num++;
			System.out.println("当前页是否命中:"+ "false" +"     " +"当前淘汰页号:"+id+"     "+"当前缺页次数:"+lack_num+"     "+"当前访问总次数:"+access_num+"     "+"当前缺页率:"+calculatePageFaultRate(lack_num,access_num));
		}else {
			//存在则将其调至队列头
			pQueue.processWaitQueue.addFirst(nextProcess);
			access_num++;
			System.out.println("当前页是否命中:"+ "true" +"     " +"当前淘汰页号:null"+"     "+"当前缺页次数:"+lack_num+"     "+"当前访问总次数:"+access_num+"     "+"当前缺页率:"+calculatePageFaultRate(lack_num,access_num));
		}
	}
	
	//按堆栈的页面放置顺序排序
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
	
	
	//判断当前队列是否含有该页面	
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
	
	//计算缺页率
	public String calculatePageFaultRate(int lack_num,int access_num){
		DecimalFormat deFormat = new DecimalFormat();
		deFormat.setMaximumFractionDigits(2);
		deFormat.setMinimumFractionDigits(2);
		return deFormat.format(lack_num * 100.00/access_num) + "%";
	}
	
	//获取计数器值最大的页
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
