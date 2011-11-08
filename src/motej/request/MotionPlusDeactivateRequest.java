package motej.request;

public class MotionPlusDeactivateRequest extends WriteRegisterRequest{

	public MotionPlusDeactivateRequest() {
		super(new byte[]{ (byte) 0xa6 , 0x00, (byte) 0xf0}, new byte[]{0x55});
	}

	@Override
	public byte[] getBytes() {
		return super.getBytes();
	}

}
