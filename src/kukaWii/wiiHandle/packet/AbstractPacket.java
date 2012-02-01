package kukaWii.wiiHandle.packet;

import java.io.Serializable;

public abstract class AbstractPacket implements Comparable<AbstractPacket>, Serializable{

	private static final long serialVersionUID = -7679458224425565990L;
	
	private long ts;
	private long tsMillis;
	
	public AbstractPacket(){
		ts = System.nanoTime();
		tsMillis = System.currentTimeMillis();
	}
	
	public long getTimestamp(){
		return ts;
	}
	
	public long getTimestampMillis(){
		return tsMillis;
	}

	@Override
	public int compareTo(AbstractPacket o) {
		 return new Long(ts).compareTo(o.getTimestamp());
	}
	
	
	
	
}
