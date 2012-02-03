package kukaWii.wiiHandle.consumer;

import java.awt.GridLayout;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import kukaWii.wiiHandle.packet.AbstractPacket;
import kukaWii.wiiHandle.packet.AccelerometerPacket;
import kukaWii.wiiHandle.packet.MotionPlusPacket;

public class ChartConsumer extends AbstractPacketConsumer{

	private TimeSeries xAccelSeries;
	private TimeSeries yAccelSeries;
	private TimeSeries zAccelSeries;
	
	private TimeSeries mPlusPitchSeries;
	private TimeSeries mPlusPitchSpeedSeries;
	private TimeSeries mPlusRollSeries;
	private TimeSeries mPlusRollSpeedSeries;
	private TimeSeries mPlusYawSeries;
	private TimeSeries mPlusYawSpeedSeries;
	
	
	private JFrame frame;
	
	public ChartConsumer(){
		super();
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel accelPanel = new JPanel();
		JPanel mpPanel = new JPanel();
		
		tabbedPane.addTab("Accelerometer", accelPanel);
		tabbedPane.addTab("MotionPlus", mpPanel);
		accelPanel.setLayout(new GridLayout(2,2));
		mpPanel.setLayout(new GridLayout(2,3));
		
		xAccelSeries = createChart(accelPanel, "Accelerometer X");
		yAccelSeries = createChart(accelPanel, "Accelerometer Y");
		zAccelSeries = createChart(accelPanel, "Accelerometer Z");
		
		mPlusPitchSeries = createChart(mpPanel, "Pitch");
		mPlusRollSeries = createChart(mpPanel, "Roll");
		mPlusYawSeries = createChart(mpPanel, "Yaw");
		
		mPlusPitchSpeedSeries = createChart(mpPanel, "Pitch Speed");
		mPlusRollSpeedSeries = createChart(mpPanel, "Roll Speed");
		mPlusYawSpeedSeries = createChart(mpPanel, "Yaw Speed");
		
		frame.add(tabbedPane);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	protected void consume(AbstractPacket packet) {
		
		if(packet instanceof AccelerometerPacket){
			AccelerometerPacket accelPacket = (AccelerometerPacket) packet;
			addPacketToSeries(xAccelSeries, accelPacket, accelPacket.getX());
			addPacketToSeries(yAccelSeries, accelPacket, accelPacket.getY());
			addPacketToSeries(zAccelSeries, accelPacket, accelPacket.getZ());
		}else if(packet instanceof MotionPlusPacket){
			MotionPlusPacket motionPacket = (MotionPlusPacket) packet;
			addPacketToSeries(mPlusPitchSeries, motionPacket, motionPacket.getPitch());
			addPacketToSeries(mPlusPitchSpeedSeries, motionPacket, motionPacket.getPitchDownSpeed());
			addPacketToSeries(mPlusRollSeries, motionPacket, motionPacket.getRoll());
			addPacketToSeries(mPlusRollSpeedSeries, motionPacket, motionPacket.getRollLeftSpeed());
			addPacketToSeries(mPlusYawSeries, motionPacket, motionPacket.getYaw());
			addPacketToSeries(mPlusYawSpeedSeries, motionPacket, motionPacket.getYawLeftSpeed());
		}
		
		
		
	}
	
	private void addPacketToSeries(TimeSeries series, AbstractPacket packet, double value){
		series.addOrUpdate(new TimeSeriesDataItem(new Millisecond(new Date(packet.getTimestampMillis())), value));
	}
	
	private TimeSeries createChart(JPanel panel, String heading){
		XYPlot plot = new XYPlot();
		TimeSeriesCollection timeSeriesColl = new TimeSeriesCollection();
		plot.setDataset(timeSeriesColl);
		TimeSeries timeSeries = new TimeSeries("Werte");
		timeSeriesColl.addSeries(timeSeries);
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(heading, "Zeitpunkt", "Wert", timeSeriesColl, false, false, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		
		panel.add(chartPanel);
		
		return timeSeries;
	}
	
	public static void main(String[] args) {
		new ChartConsumer();
	}

}
