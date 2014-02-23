package zc.datawash.util;

import java.util.ArrayList;

public class UniqueList<E> extends ArrayList<E>{
	public boolean add(E element){
		if(!this.contains(element)){
			return super.add(element);
		}
		return false;
	}
}
