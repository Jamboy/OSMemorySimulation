package model;

public class Process {
	public int process_id;//ҳ��ID
	public int process_access_order;//����˳��
	public int process_access_counter;//���ʴ���
	
	
	public int getProcess_id() {
		return process_id;
	}
	public void setProcess_id(int process_id) {
		this.process_id = process_id;
	}
	public int getProcess_access_order() {
		return process_access_order;
	}
	public void setProcess_access_order(int process_access_order) {
		this.process_access_order = process_access_order;
	}
	public int getProcess_access_counter() {
		return process_access_counter;
	}
	public void setProcess_access_counter(int process_access_counter) {
		this.process_access_counter = process_access_counter;
	}
	
	public String toString(int id){
		if (id == 1 || id == 3){
			return "ҳ��ID:" + "      "+process_id + "      "+"ҳ�����˳��:" + "      "+process_access_order;
		}
		else if (id == 4) {
			return process_id +  "  ";
//		}else if (id == 5) {
//			for (int  = 0;  < array.length; ++) {
//				
//			}
//			return process_id +  "  ";
		}else{
			return "ҳ��ID:" + "      "+process_id + "      "+"ҳ�����˳��:" + "      "+process_access_order +"      "+ "��ǰҳ���ʼ�����:" + process_access_counter;
		} 
	}
}
