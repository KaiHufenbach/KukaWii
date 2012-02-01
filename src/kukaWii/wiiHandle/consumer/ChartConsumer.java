package kukaWii.wiiHandle.consumer;

import java.awt.GridLayout;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JFrame;

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

public class ChartConsumer extends AbstractPacketConsumer{

	private TimeSeries xTimeSeries;
	private TimeSeries yTimeSeries;
	private TimeSeries zTimeSeries;
	
	
	private JFrame frame;
	
	public ChartConsumer(){
		super();
		
		frame = new JFrame();
		
		GridLayout gridLayout = new GridLayout(2, 2);
		
		frame.setLayout(gridLayout);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		XYPlot plotX = new XYPlot();
		TimeSeriesCollection timeSeriesCollX = new TimeSeriesCollection();
		plotX.setDataset(timeSeriesCollX);
		xTimeSeries = new TimeSeries("Accel Werte X");
		timeSeriesCollX.addSeries(xTimeSeries);
		
		
		JFreeChart chartX = ChartFactory.createTimeSeriesChart("Aufzeichnung der Wii Accel Werte X", "Zeitpunkt", "Wert", timeSeriesCollX, false, false, false);
		final ChartPanel chartPanelX = new ChartPanel(chartX);
		
		
		XYPlot plotY = new XYPlot();
		TimeSeriesCollection timeSeriesCollY = new TimeSeriesCollection();
		plotY.setDataset(timeSeriesCollY);
		yTimeSeries = new TimeSeries("Accel Werte Y");
		timeSeriesCollY.addSeries(yTimeSeries);
		
		
		JFreeChart chartY = ChartFactory.createTimeSeriesChart("Aufzeichnung der Wii Accel Werte Y", "Zeitpunkt", "Wert", timeSeriesCollY, false, false, false);
		final ChartPanel chartPanelY = new ChartPanel(chartY);
		
		XYPlot plotZ = new XYPlot();
		TimeSeriesCollection timeSeriesCollZ = new TimeSeriesCollection();
		plotZ.setDataset(timeSeriesCollZ);
		zTimeSeries = new TimeSeries("Accel Werte Z");
		timeSeriesCollZ.addSeries(zTimeSeries);
		
		
		JFreeChart chartZ = ChartFactory.createTimeSeriesChart("Aufzeichnung der Wii Accel Werte Z", "Zeitpunkt", "Wert", timeSeriesCollZ, false, false, false);
		final ChartPanel chartPanelZ = new ChartPanel(chartZ);
		
		
		
		
		
		
		frame.add(chartPanelX);
		frame.add(chartPanelY);
		frame.add(chartPanelZ);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	protected void consume(AbstractPacket packet) {
		
		if(packet instanceof AccelerometerPacket){
			AccelerometerPacket accelPacket = (AccelerometerPacket) packet;
			xTimeSeries.addOrUpdate(new TimeSeriesDataItem(new Millisecond(new Date(accelPacket.getTimestampMillis())), accelPacket.getX()));
			yTimeSeries.addOrUpdate(new TimeSeriesDataItem(new Millisecond(new Date(accelPacket.getTimestampMillis())), accelPacket.getY()));
			zTimeSeries.addOrUpdate(new TimeSeriesDataItem(new Millisecond(new Date(accelPacket.getTimestampMillis())), accelPacket.getZ()));
		}
		
		
		
	}
	
	public static void main(String[] args) {
		new ChartConsumer();
	}

}
