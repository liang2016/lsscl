package com.lsscl.app.bean;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;

/**
 * 点统计消息实体
 * 
 * @author yxx
 * 
 */
public class PointsStatisticsMsgBody extends MsgBody {
	private static final long serialVersionUID = 2205274686837328701L;
	private List<Map<String, Object>> points = new ArrayList<Map<String, Object>>();
	private String title;
	private String subTitle;
	private int dataType;
	private double maxValue, minValue;
	private String imageType;//统计图表类型：bar：柱形图，line（默认）：折线图

	public List<Map<String, Object>> getPoints() {
		return points;
	}

	public void setPoints(List<Map<String, Object>> points) {
		this.points = points;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	private IntervalXYDataset getDataSet() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		TimeSeries series = new TimeSeries(subTitle, Millisecond.class);

		for (Map<String, Object> p : points) {
			// 精确度
			Millisecond millisecond = new Millisecond(new Date(
					(Long) p.get("ts")));
			Double value = (Double) p.get("pointValue");
			if (maxValue < value)
				maxValue = value;
			if (minValue > value)
				minValue = value;
			series.addOrUpdate(millisecond, value);
		}
		dataset.addSeries(series);
		return dataset;
	}

	public void toImage(OutputStream out) {
		// 建立JFreeChart
		IntervalXYDataset dataSet = getDataSet();
		JFreeChart jfc = null;
		if ("line".equals(imageType)||imageType==null) {
			jfc = ChartFactory.createTimeSeriesChart("点统计", " ",
					"", dataSet, false, // 显示图例
					true, // 采用标准生成器
					false // 是否生成超链接
					);
		} else if ("bar".equals(imageType)) {
			jfc = ChartFactory.createXYBarChart("点统计", " ",true,
					"", dataSet, PlotOrientation.VERTICAL,
					false, // 显示图例
					true, // 采用标准生成器
					false // 是否生成超链接
					);
		}
		TextTitle textTitle = new TextTitle("                       " + title);
		textTitle.setFont(new Font("宋体", Font.BOLD, 10));
		textTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		jfc.setTitle(textTitle);
		// 设置图表子标题
		TextTitle subTitleText = new TextTitle("                  "+subTitle);
		subTitleText.setFont(new Font("宋体", Font.BOLD, 10));
		subTitleText.setHorizontalAlignment(HorizontalAlignment.LEFT);
		jfc.addSubtitle(subTitleText);
		XYPlot plot = (XYPlot) jfc.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		if(plot.getRenderer() instanceof XYBarRenderer){//去除柱形图的阴影
			XYBarRenderer xyBarRenderer = (XYBarRenderer) plot.getRenderer();
			xyBarRenderer.setShadowVisible(false);
			xyBarRenderer.setBarPainter(new StandardXYBarPainter());
		}
		// x轴
		DateAxis xAxis = (DateAxis) plot.getDomainAxis();
		Font font = new Font("courier new", Font.BOLD, 8);
		Font font2 = new Font("微软雅黑", Font.BOLD, 10);
		xAxis.setTickLabelFont(font);
		xAxis.setLabelFont(font2);
		DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
		domainAxis.setDateFormatOverride(new SimpleDateFormat("MM/dd HH:mm"));
		// y轴
		ValueAxis yAxis = plot.getRangeAxis();
		yAxis.setTickLabelFont(font);
		yAxis.setLabelFont(font2);
		if (points.size() != 0) {
			if (minValue != maxValue)
				yAxis.setRange(minValue - (maxValue - minValue) / 2, maxValue
						+ (maxValue - minValue) / 2);
		}
		try {
			int width = points.size() / 4;
			width = width < 500 ? 500 : width;
			ChartUtilities.writeChartAsPNG(out, jfc, 500, 240);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
