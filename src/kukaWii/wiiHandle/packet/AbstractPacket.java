package kukaWii.wiiHandle.packet;

import java.io.Serializable;

public abstract class AbstractPacket implements Comparable<AbstractPacket>, Serializable{

	private static final long serialVersionUID = -7679458224425565990L;
	
	private long ts;
	
	public AbstractPacket(){
		ts = System.currentTimeMillis();
	}
	
	public long getTimestamp(){
		return ts;
	}

	@Override
	public int compareTo(AbstractPacket o) {
		 return new Long(ts).compareTo(o.getTimestamp());
	}
	
	
	
	
}
