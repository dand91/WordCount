package Counter;


class IndexEntry implements Comparable{
	
	public String key;
	public int value;
	
	public IndexEntry(String key, int value){
		
		this.key = key;
		this.value = value;
		
	}
	

	public String toString(){
		
		return value + " " + key;
	}

	@Override
	public int compareTo(Object o) {
		
			
			if(value < ((IndexEntry)o).value){
				
				return 1;
				
			}else if(value > ((IndexEntry)o).value){
				
				return - 1;
				
			}else{
		
		return 0;
		
		}
			
			
	}
}