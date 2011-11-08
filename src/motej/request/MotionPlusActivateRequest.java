package motej.request;

public class MotionPlusActivateRequest extends WriteRegisterRequest {

	public MotionPlusActivateRequest( ) {
		// Sending 0x04 to 0xa600fe to activate the MotionPlus
		super(new byte[]{ (byte) 0xa6 , 0x00, (byte) 0xfe}, new byte[]{0x04});
		
	}

	@Override
	public byte[] getBytes() {
		return super.getBytes();
	}

}
