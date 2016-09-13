package com.lsscl.app.bean;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;

/**
 * 24小时点数据统计实体
 * 
 * @author yxx
 * 
 */
public class PointsWithin24MsgBody extends MsgBody {
	private static final long serialVersionUID = -3693167843596930549L;
	private List<Map<String,Object>> points;
	private String title;
	private String subTitle;
	private int dataType;// 数据类型
	private String imageType;// 统计图类型 bar：柱形图，line（默认）：折线图

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

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/**
	 * 生成统计图片
	 */
	public void toImage(OutputStream out) {
		System.out.println("count:"+points.size());
		// 建立JFreeChart
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		TimeSeries series = new TimeSeries(subTitle, Millisecond.class);
		double max = 0;
		double min = 0;
		int i = 0;
		for (Map<String,Object>point:points) {
			Double value = (Double) point.get("pointValue");
			if (i == 0) {
				max = value;
				min = value;
			}
			if (value > max)
				max = value;
			if (value < min)
				min = value;
			Millisecond millisecond = new Millisecond(new Date(
					(Long) point.get("ts")));
			series.addOrUpdate(millisecond, value);
			i++;
		}
		dataset.addSeries(series);
		JFreeChart jfc = null;
		if ("line".equals(imageType)||imageType==null) {
			jfc = ChartFactory.createTimeSeriesChart("点统计", " ",
					null, dataset, false, // 显示图例
					true, // 采用标准生成器
					false // 是否生成超链接
					);
		} else if ("bar".equals(imageType)) {
			jfc = ChartFactory.createXYBarChart("点统计", " ",true,
					"", dataset, PlotOrientation.VERTICAL,
					false, // 显示图例
					true, // 采用标准生成器
					false // 是否生成超链接
					);
		}
		// 设置标题并且设置其字体，防止中文乱码
		TextTitle textTitle = new TextTitle("                    " + title);
		textTitle.setFont(new Font("宋体", Font.BOLD, 10));
		textTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		jfc.setTitle(textTitle);
		// 设置图表子标题
		TextTitle subTitleText = new TextTitle("                   " + subTitle);
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
		if (plot.getRenderer() instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer render = (LineAndShapeRenderer) plot
					.getRenderer();
			render.setBaseShapesVisible(true);
			render.setBaseShapesFilled(true);
			render.setSeriesOutlineStroke(0, new BasicStroke(0.3F));// 设置折点的大小
		}
		if(plot.getRenderer() instanceof XYBarRenderer){//去除柱形图的阴影
			XYBarRenderer xyBarRenderer = (XYBarRenderer) plot.getRenderer();
			xyBarRenderer.setShadowVisible(false);
			xyBarRenderer.setBarPainter(new StandardXYBarPainter());
		}
		// x轴
		Axis xAxis = plot.getDomainAxis();
		Font font = new Font("courier new", Font.BOLD, 8);
		Font font2 = new Font("微软雅黑", Font.BOLD, 10);
		xAxis.setTickLabelFont(font);
		xAxis.setLabelFont(font2);

		// y轴
		ValueAxis yAxis = plot.getRangeAxis();
		yAxis.setRange(min - (max - min) / 2, max + (max - min) / 2);
		yAxis.setTickLabelFont(font);
		yAxis.setLabelFont(font2);

		try {
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
